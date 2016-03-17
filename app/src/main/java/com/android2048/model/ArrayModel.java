package com.android2048.model;

import com.android2048.common.Constants2048;

public class ArrayModel implements Model<Integer> {
    private Integer[][] board;
    private int height;
    private int width;

    public ArrayModel(int height, int width){
        this.height = height;
        this.width = width;
        this.board = new Integer[width][height];
        for (int i=0; i < height; i++){
            for (int j=0; j < width; j++){
                board[i][j] = Constants2048.CLEAR_VALUE;
            }
        }
    }

    @Override
    public Integer get(int x, int y) {
        return board[x][y];
    }

    @Override
    public Integer[] getRow(int row) {
        Integer[] ret = new Integer[width];
        for (int i=0; i < width; i++){
            ret[i] = board[i][row];
        }
        return ret;
    }

    @Override
    public Integer[] getCol(int col) {
        return board[col];
    }

    @Override
    public void set(int x, int y, Integer val) {
        board[x][y] = val;
    }

    @Override
    public void setRow(int row, Integer[] vals) {
        for (int i=0; i < width; i++){
            board[i][row] = vals[i];
        }
    }

    @Override
    public void setCol(int col, Integer[] vals) {
        board[col] = vals;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Integer combine(Integer val1, Integer val2) {
        return val1 + val2;
    }
}
