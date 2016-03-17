package com.android2048.model;

public interface Model<T> {
    T get(int x, int y);
    T[] getRow(int row);
    T[] getCol(int col);
    void set(int x, int y, T val);
    void setRow(int row, T[] vals);
    void setCol(int col, T[] vals);
    int getWidth();
    int getHeight();
    T combine(T val1, T val2);
}
