package com.board.draw.util;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;

public class PathUtil {

    /**
     * 绘制五角星Path
     */
    public static Path drawStarPath(int size) {
        RectF rectF = new RectF(0, 0, size, size);
        Path ovalPath = new Path();
        ovalPath.addOval(rectF, Path.Direction.CW);
        // 星星形状
        PathMeasure pathMeasure = new PathMeasure(ovalPath, false);
        float length = pathMeasure.getLength();
        float split = length / 5;
        float[] starPos = new float[10];
        float[] pos = new float[2];
        float[] tan = new float[2];

        for (int i = 0; i < 5; i++) {
            pathMeasure.getPosTan(split * i, pos, tan);
            starPos[i * 2] = pos[0];
            starPos[i * 2 + 1] = pos[1];
        }

        Path starPath = new Path();
        starPath.moveTo(starPos[0], starPos[1]);
        starPath.lineTo(starPos[4], starPos[5]);
        starPath.lineTo(starPos[8], starPos[9]);
        starPath.lineTo(starPos[2], starPos[3]);
        starPath.lineTo(starPos[6], starPos[7]);
        starPath.lineTo(starPos[0], starPos[1]);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90, rectF.centerX(), rectF.centerY());
        starPath.transform(matrix);
        return starPath;
    }


    /**
     * 绘制圆圈Path
     */
    public static Path drawCirclePath(int size) {
        RectF rectF = new RectF(0, 0, size, size);
        Path ovalPath = new Path();
        ovalPath.addOval(rectF, Path.Direction.CW);
        return ovalPath;
    }

    /**
     * n角星路径
     *
     * @param starCount 几角星
     * @param R         外接圆半径
     * @param r         内接圆半径
     * @return n角星路径
     */
    public static Path nStarPath(int starCount, float R, float r) {
        Path path = new Path();
        float perDeg = 360f / starCount;
        float degA = perDeg / 2 / 2;
        float degB = 360f / (starCount - 1f) / 2f - degA / 2 + degA;
        path.moveTo(
                (float) (Math.cos(rad(degA + perDeg * 0)) * R + R * Math.cos(rad(degA))),
                (float) (-Math.sin(rad(degA + perDeg * 0)) * R + R));
        for (int i = 0; i < starCount; i++) {
            path.lineTo(
                    (float) (Math.cos(rad(degA + perDeg * i)) * R + R * Math.cos(rad(degA))),
                    (float) (-Math.sin(rad(degA + perDeg * i)) * R + R));
            path.lineTo(
                    (float) (Math.cos(rad(degB + perDeg * i)) * r + R * Math.cos(rad(degA))),
                    (float) (-Math.sin(rad(degB + perDeg * i)) * r + R));
        }
        path.close();
        return path;
    }

    /**
     * 画正n边形的路径
     *
     * @param count  边数
     * @param radius 外接圆半径
     * @return 画正n边形的路径
     */
    public static Path regularPolygonPath(int count, float radius) {
        //!!一点解决
        float r = (float) (radius * (Math.cos(rad(360f / count / 2f))));
        return nStarPath(count, radius, r);
    }

    /**
     * 角度制化为弧度制
     *
     * @param deg 角度
     * @return 弧度
     */
    public static float rad(float deg) {
        return (float) (deg * Math.PI / 180);
    }


}
