package com.android2048.model;

/**
 * Created by lyle.waldman on 07/03/2016.
 */
public interface Model {
    public int get(int x, int y);
    public int[] getRow(int row);
    public int[] getCol(int col);
    public void set(int x, int y, int val);
    public void setRow(int row, int[] vals);
    public void setCol(int col, int[] vals);
    public int getWidth();
    public int getHeight();
    public void clear();
}
