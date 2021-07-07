package com.board.draw.util;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;

public class PathUtil {

    /**
     * 绘制五角星Path
     */
    public static Path buildStarPath() {
        RectF rectF = new RectF(0, 0, 50, 50);
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
}
