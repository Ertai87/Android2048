package com.android2048.common;

import android.util.Log;
import com.android2048.model.Model;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Engine {
    Model boardModel;

    public Engine(Class<? extends Model> c, int height, int width) throws IllegalAccessException, InvocationTargetException, InstantiationException, NullPointerException {
        Constructor ctors[] = c.getConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 2)
                break;
        }
        boardModel = (Model)ctor.newInstance(height, width);
    }

    public boolean isEndGame(){
        for (int i=0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (boardModel.get(i, j) == Constants2048.CLEAR_VALUE ||
                        (i > 0 && boardModel.get(i, j) == boardModel.get(i-1, j)) ||
                        (i < 3 && boardModel.get(i, j) == boardModel.get(i+1, j)) ||
                        (j > 0 && boardModel.get(i, j) == boardModel.get(i, j-1)) ||
                        (j < 3 && boardModel.get(i, j) == boardModel.get(i, j+1))
                        ){
                    return false;
                }
            }
        }
        return true;
    }

    /* Checks whether a 2048 is on the board */
    public boolean checkWin(){
        for (int i=0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (boardModel.get(i, j) == Constants2048.WIN_VALUE){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pushRight(){
        Log.d(Constants2048.DEBUG_TAG, "pushRight");
		/* Since we count from right to left, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i=0; i < boardModel.getHeight(); i++){
            int[] row = boardModel.getRow(i);
            for (int j = boardModel.getWidth() - 1; j >= 0; j--){
                if (row[j] == Constants2048.CLEAR_VALUE){
                    int k = j;
                    for (; k >= 0 && row[k] == Constants2048.CLEAR_VALUE; k--);
                    if (k >= 0){
                        row[j] = row[k];
                        row[k] = Constants2048.CLEAR_VALUE;
                        j++;
                        ret = true;
                    }
                }else{
                    int k = j - 1;
                    for (; k >= 0 && row[k] == Constants2048.CLEAR_VALUE; k--);
                    if (k >= 0 && row[k] == row[j]){
                        row[j] *= 2;
                        row[k] = Constants2048.CLEAR_VALUE;
                        ret = true;
                    }
                }
            }
            boardModel.setRow(i, row);
        }
        return ret;
    }

    public boolean pushLeft(){
        Log.d(Constants2048.DEBUG_TAG, "pushLeft");
		/* Since we count from left to right, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i=0; i < boardModel.getHeight(); i++){
            int[] row = boardModel.getRow(i);
            for (int j = 0; j < boardModel.getWidth(); j++){
                if (row[j] == Constants2048.CLEAR_VALUE){
                    int k = j;
                    for (; k < boardModel.getWidth() && row[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < boardModel.getWidth()){
                        row[j] = row[k];
                        row[k] = Constants2048.CLEAR_VALUE;
                        j--;
                        ret = true;
                    }
                }else{
                    int k = j + 1;
                    for (; k < boardModel.getWidth() && row[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < boardModel.getWidth() && row[k] == row[j]){
                        row[j] *= 2;
                        row[k] = Constants2048.CLEAR_VALUE;
                        ret = true;
                    }
                }
            }
            boardModel.setRow(i, row);
        }
        return ret;
    }

    public boolean pushUp(){
        Log.d(Constants2048.DEBUG_TAG, "pushUp");
		/* Since we count from top to bottom, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i = 0; i < boardModel.getWidth(); i++){
            int[] col = boardModel.getCol(i);
            for (int j = 0; j < boardModel.getHeight(); j++){
                if (col[j] == Constants2048.CLEAR_VALUE){
                    int k = j;
                    for (; k < boardModel.getHeight() && col[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < boardModel.getHeight()){
                        col[j] = col[k];
                        col[k] = Constants2048.CLEAR_VALUE;
                        j--;
                        ret = true;
                    }
                }else{
                    int k = j + 1;
                    for (; k < boardModel.getHeight() && col[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < boardModel.getHeight() && col[k] == col[j]){
                        col[j] *= 2;
                        col[k] = Constants2048.CLEAR_VALUE;
                        ret = true;
                    }
                }
            }
            boardModel.setCol(i, col);
        }
        return ret;
    }

    public boolean pushDown(){
        Log.d(Constants2048.DEBUG_TAG, "pushDown");
		/* Since we count from bottom to top, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i = 0; i < boardModel.getWidth(); i++){
            int[] col = boardModel.getCol(i);
            for (int j = boardModel.getHeight() - 1; j >= 0; j--){
                if (col[j] == Constants2048.CLEAR_VALUE){
                    int k = j;
                    for (; k >= 0 && col[k] == Constants2048.CLEAR_VALUE; k--);
                    if (k >= 0){
                        col[j] = col[k];
                        col[k] = Constants2048.CLEAR_VALUE;
                        j++;
                        ret = true;
                    }
                }else{
                    int k = j - 1;
                    for (; k >= 0 && col[k] == Constants2048.CLEAR_VALUE; k--);
                    if (k >= 0 && col[k] == col[j]){
                        col[j] *= 2;
                        col[k] = Constants2048.CLEAR_VALUE;
                        ret = true;
                    }
                }
            }
            boardModel.setCol(i, col);
        }
        return ret;
    }

    public Model getBoardModel(){
        return boardModel;
    }

    public void clearBoard(){
        boardModel.clear();
    }
}
