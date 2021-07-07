package com.board.draw.constants;

import android.graphics.Path;

import com.board.draw.util.PaintMode;

/**
 * 路径、画笔类
 */
public class DrawPath {
    private Path path;
    private int color;
    private PaintMode drawingMode;
    private boolean pathComplete;
    private float brushSize;

    public DrawPath(Path path, int color, PaintMode drawingMode, boolean pathComplete, float brushSize) {
        this.path = path;
        this.color = color;
        this.drawingMode = drawingMode;
        this.pathComplete = pathComplete;
        this.brushSize = brushSize;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PaintMode getDrawingMode() {
        return drawingMode;
    }

    public void setDrawingMode(PaintMode drawingMode) {
        this.drawingMode = drawingMode;
    }

    public boolean isPathComplete() {
        return pathComplete;
    }

    public void setPathComplete(boolean pathComplete) {
        this.pathComplete = pathComplete;
    }

    public float getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(float brushSize) {
        this.brushSize = brushSize;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
