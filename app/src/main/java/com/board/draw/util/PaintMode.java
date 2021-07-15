package com.board.draw.util;

/**
 * 画笔类型
 */
public enum PaintMode {
    /**
     * PENCIL                                                         铅笔
     * HIGHLIGHTER                                                    荧光笔
     * EQUAL_DASHED_LINE、UNEQUAL_DASHED_LINE、
     * CIRCLE_LINE_DASHED_LINE、ALL_CIRCLE_DASHED_LINE                虚线笔
     * FIVE_DASHED_LINE
     * PATTERN_PEN                                                    图案笔
     * ERASER                                                         橡皮擦
     * FLOWER_PEN                                                     图片笔
     */
    PENCIL,
    HIGHLIGHTER,
    EQUAL_DASHED_LINE,
    UNEQUAL_DASHED_LINE,
    CIRCLE_LINE_DASHED_LINE,
    ALL_CIRCLE_DASHED_LINE,
    FIVE_DASHED_LINE,
    PATTERN_PEN,
    ERASER,
    FLOWER_PEN
}
