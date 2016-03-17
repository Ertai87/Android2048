package com.android2048.common;

import android.util.Log;
import com.android2048.model.Model;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Random;

public class Engine<T> {
    Model<T> boardModel;
    final T clear_value;
    final T win_value;
    Comparator comparator;
    Random gen;
    T[] valgen; //The probability distribution for generating values for createValue.

    public Engine(Class<? extends Model<T>> c, int height, int width, T clear, T win, Comparator comp, T[] genprobs) throws IllegalAccessException, InvocationTargetException, InstantiationException, NullPointerException {
        Constructor ctors[] = c.getConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 2)
                break;
        }
        boardModel = (Model<T>)ctor.newInstance(height, width);
        clear_value = clear;
        win_value = win;
        comparator = comp;
        gen = new Random();
        if (genprobs.length != 100){
            throw new RuntimeException("Invalid probability field");
        }else{
            valgen = genprobs;
        }
    }

    public boolean isEndGame(){
        for (int i=0; i < boardModel.getWidth(); i++){
            for (int j = 0; j < boardModel.getHeight(); j++){
                if (boardModel.get(i, j).equals(clear_value) ||
                        (i > 0 && boardModel.get(i, j).equals(boardModel.get(i-1, j))) ||
                        (i < 3 && boardModel.get(i, j).equals(boardModel.get(i+1, j))) ||
                        (j > 0 && boardModel.get(i, j).equals(boardModel.get(i, j-1))) ||
                        (j < 3 && boardModel.get(i, j).equals(boardModel.get(i, j+1)))
                        ){
                    return false;
                }
            }
        }
        return true;
    }

    /* Checks whether a 2048 is on the board */
    public boolean checkWin(){
        for (int i=0; i < boardModel.getWidth(); i++){
            for (int j = 0; j < boardModel.getHeight(); j++){
                if (boardModel.get(i, j).equals(win_value)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pushRight(){
		/* Since we count from right to left, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i=0; i < boardModel.getHeight(); i++){
            T[] row = boardModel.getRow(i);
            for (int j = boardModel.getWidth() - 1; j >= 0; j--){
                if (row[j].equals(clear_value)){
                    int k = j;
                    for (; k >= 0 && row[k].equals(clear_value); k--);
                    if (k >= 0){
                        row[j] = row[k];
                        row[k] = clear_value;
                        j++;
                        ret = true;
                    }
                }else{
                    int k = j - 1;
                    for (; k >= 0 && row[k].equals(clear_value); k--);
                    if (k >= 0 && comparator.compare(row[k], row[j]) == 0){
                        row[j] = boardModel.combine(row[j], row[k]);
                        row[k] = clear_value;
                        ret = true;
                    }
                }
            }
            boardModel.setRow(i, row);
        }
        return ret;
    }

    public boolean pushLeft(){
		/* Since we count from left to right, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i=0; i < boardModel.getHeight(); i++){
            T[] row = boardModel.getRow(i);
            for (int j = 0; j < boardModel.getWidth(); j++){
                if (row[j].equals(clear_value)){
                    int k = j;
                    for (; k < boardModel.getWidth() && row[k].equals(clear_value); k++);
                    if (k < boardModel.getWidth()){
                        row[j] = row[k];
                        row[k] = clear_value;
                        j--;
                        ret = true;
                    }
                }else{
                    int k = j + 1;
                    for (; k < boardModel.getWidth() && row[k].equals(clear_value); k++);
                    if (k < boardModel.getWidth() && comparator.compare(row[k], row[j]) == 0){
                        row[j] = boardModel.combine(row[j], row[k]);
                        row[k] = clear_value;
                        ret = true;
                    }
                }
            }
            boardModel.setRow(i, row);
        }
        return ret;
    }

    public boolean pushUp(){
		/* Since we count from top to bottom, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i = 0; i < boardModel.getWidth(); i++){
            T[] col = boardModel.getCol(i);
            for (int j = 0; j < boardModel.getHeight(); j++){
                if (col[j].equals(clear_value)){
                    int k = j;
                    for (; k < boardModel.getHeight() && col[k].equals(clear_value); k++);
                    if (k < boardModel.getHeight()){
                        col[j] = col[k];
                        col[k] = clear_value;
                        j--;
                        ret = true;
                    }
                }else{
                    int k = j + 1;
                    for (; k < boardModel.getHeight() && col[k].equals(clear_value); k++);
                    if (k < boardModel.getHeight() && comparator.compare(col[j], col[k]) == 0){
                        col[j] = boardModel.combine(col[j], col[k]);
                        col[k] = clear_value;
                        ret = true;
                    }
                }
            }
            boardModel.setCol(i, col);
        }
        return ret;
    }

    public boolean pushDown(){
		/* Since we count from bottom to top, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i = 0; i < boardModel.getWidth(); i++){
            T[] col = boardModel.getCol(i);
            for (int j = boardModel.getHeight() - 1; j >= 0; j--){
                if (col[j].equals(clear_value)){
                    int k = j;
                    for (; k >= 0 && col[k].equals(clear_value); k--);
                    if (k >= 0){
                        col[j] = col[k];
                        col[k] = clear_value;
                        j++;
                        ret = true;
                    }
                }else{
                    int k = j - 1;
                    for (; k >= 0 && col[k].equals(clear_value); k--);
                    if (k >= 0 && comparator.compare(col[j], col[k]) == 0){
                        col[j] = boardModel.combine(col[j], col[k]);
                        col[k] = clear_value;
                        ret = true;
                    }
                }
            }
            boardModel.setCol(i, col);
        }
        return ret;
    }

    public boolean createValue(){
        int x = Math.abs(gen.nextInt()) % 4, y = Math.abs(gen.nextInt()) % 4, flag;
        while (!boardModel.get(x, y).equals(clear_value)){
            x = Math.abs(gen.nextInt()) % 4;
            y = Math.abs(gen.nextInt()) % 4;
        }
        flag = Math.abs(gen.nextInt()) % 100;
        boardModel.set(x, y, valgen[flag]);
        return isEndGame();
    }

    public Model getBoardModel(){
        return boardModel;
    }

    public void clearBoard(){
        for (int i = 0; i < boardModel.getWidth(); i++){
            for (int j = 0; j < boardModel.getHeight(); j++){
                boardModel.set(i, j, clear_value);
            }
        }
    }
}
