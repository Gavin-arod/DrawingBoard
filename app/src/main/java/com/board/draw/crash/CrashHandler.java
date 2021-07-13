package com.board.draw.crash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.board.draw.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 崩溃日志收集类
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler crashHandler;
    private Context mContext;
    //用于格式化日期
    private DateFormat format;
    //app版本信息
    private String versionName;
    private String versionCode;
    private String crashTime;
    private String crashHead;
    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".txt";

    /**
     * 构造方法私有，防止外部构造多个实例
     * 即采用单例模式
     */
    private CrashHandler() {
    }

    /**
     * 单例模式：获取实例
     */
    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            synchronized (CrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                }
            }
        }
        return crashHandler;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (!handleException(e)) {
            //未经过人为处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, e);
            }
        } else {
            //已处理,系统自己退出
            try {
                Thread.sleep(1500);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            //重启客户端
            reStartApp();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 处理：自定义错误处理、收集错误信息、发送错误报告等操作
     *
     * @return true:已处理该异常信息；false:没处理异常
     */
    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        //初始化设备相关信息
        initCrashHead();
        //Toast显示异常信息:主线程弹出提示
        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(mContext, "程序发生未知异常，即将重启。", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();
        //保存日志文件
        saveCrashMessages(throwable);
        return true;
    }

    /**
     * 收集并保存崩溃日志信息
     */
    private void saveCrashMessages(Throwable throwable) {
        File file;
        PrintWriter pw = null;
        try {
            //Log保存路径
            // SDCard/Android/data/<application package>/cache
            // data/data/<application package>/cache
            File dir = new File(FileUtil.getCrashLogPath(mContext));
            if (!dir.exists()) {
                boolean success = dir.mkdirs();
                if (!success) {
                    return;
                }
            }

            //Log文件的名字
            String fileName = "crashInfo_" + versionName + "_" + crashTime + FILE_NAME_SUFFIX;
            file = new File(dir, fileName);
            if (!file.exists()) {
                boolean createNewFileOk = file.createNewFile();
                if (!createNewFileOk) {
                    return;
                }
            }
            //开始写日志
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //写入设备信息
            pw.println(crashHead);
            //导出异常的调用栈信息
            throwable.printStackTrace(pw);
            //异常信息
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
        } catch (Exception e) {
            Log.e("Exception", "保存日志失败：" + e.toString());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        //上传崩溃信息到服务器
        uploadCrashInfo();
    }

    /**
     * 初始化相关信息
     */
    private void initCrashHead() {
        //崩溃时间
        crashTime = format.format(new Date(System.currentTimeMillis()));
        //版本信息
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            if (packageInfo != null) {
                versionName = packageInfo.versionName;
                versionCode = String.valueOf(packageInfo.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //整合Android相关信息
        crashHead =
                "\n崩溃的时间=" + crashTime +
                        "\n系统硬件商=" + Build.MANUFACTURER +
                        "\n设备的品牌=" + Build.BRAND +
                        "\n手机的型号=" + Build.MODEL +
                        "\n设备版本号=" + Build.ID +
                        "\nCPU的类型=" + Build.CPU_ABI +
                        "\n系统的版本=" + Build.VERSION.RELEASE +
                        "\n系统版本值=" + Build.VERSION.SDK_INT +
                        "\n当前的版本=" + versionName + " -- " + versionCode +
                        "\n\n";
    }

    /**
     * 上传崩溃日志
     */
    private void uploadCrashInfo() {
        Log.i("uploadCrashInfo", "");
    }

    /**
     * 重启客户端
     */
    private void reStartApp() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }
}
