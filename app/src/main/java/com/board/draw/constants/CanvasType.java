package com.board.draw.constants;

/**
 * 画布类型实体类
 */
public class CanvasType {
    private int type;
    private int graphicsId;
    private String name;
    private boolean isSelected;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGraphicsId() {
        return graphicsId;
    }

    public void setGraphicsId(int graphicsId) {
        this.graphicsId = graphicsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
