package com.android2048.model;

import com.android2048.common.Constants2048;

/**
 * Created by lyle.waldman on 07/03/2016.
 */
public class ArrayModel implements Model {
    private int[][] board;
    private int height;
    private int width;

    public ArrayModel(int height, int width){
        this.height = height;
        this.width = width;
        this.board = new int[width][height];
        for (int i=0; i < height; i++){
            for (int j=0; j < width; j++){
                board[i][j] = Constants2048.CLEAR_VALUE;
            }
        }
    }

    @Override
    public int get(int x, int y) {
        return board[x][y];
    }

    @Override
    public int[] getRow(int row) {
        int[] ret = new int[width];
        for (int i=0; i < width; i++){
            ret[i] = board[i][row];
        }
        return ret;
    }

    @Override
    public int[] getCol(int col) {
        return board[col];
    }

    @Override
    public void set(int x, int y, int val) {
        board[x][y] = val;
    }

    @Override
    public void setRow(int row, int[] vals) {
        for (int i=0; i < width; i++){
            board[i][row] = vals[i];
        }
    }

    @Override
    public void setCol(int col, int[] vals) {
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
    public void clear() {
        for (int i=0; i < width; i++){
            for (int j=0; j < height; j++){
                board[i][j] = Constants2048.CLEAR_VALUE;
            }
        }
    }
}
