package com.android2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android2048.common.Constants2048;
import com.android2048.model.Model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Engine {
    Model board;

    public Engine(Class<? extends Model> c, int height, int width) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor ctors[] = c.getConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 2)
                break;
        }
        board = (Model)ctor.newInstance(height, width);
    }

    boolean isEndGame(){
        for (int i=0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (board.get(i, j) == Constants2048.CLEAR_VALUE ||
                        (i > 0 && board.get(i, j) == board.get(i-1, j)) ||
                        (i < 3 && board.get(i, j) == board.get(i+1, j)) ||
                        (j > 0 && board.get(i, j) == board.get(i, j-1)) ||
                        (j < 3 && board.get(i, j) == board.get(i, j+1))
                        ){
                    return false;
                }
            }
        }
        return true;
    }

    /* Checks whether a 2048 is on the board */
    boolean checkWin(){
        for (int i=0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (board.get(i, j) == Constants2048.WIN_VALUE){
                    return true;
                }
            }
        }
        return false;
    }

    boolean pushRight(){
        Log.d(Constants2048.DEBUG_TAG, "pushRight");
		/* Since we count from right to left, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i=0; i < board.getHeight(); i++){
            int[] row = board.getRow(i);
            for (int j = board.getWidth() - 1; j >= 0; j--){
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
            board.setRow(i, row);
        }
        return ret;
    }

    boolean pushLeft(){
        Log.d(Constants2048.DEBUG_TAG, "pushLeft");
		/* Since we count from left to right, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i=0; i < board.getHeight(); i++){
            int[] row = board.getRow(i);
            for (int j = 0; j < board.getWidth(); j++){
                if (row[j] == Constants2048.CLEAR_VALUE){
                    int k = j;
                    for (; k < board.getWidth() && row[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < board.getWidth()){
                        row[j] = row[k];
                        row[k] = Constants2048.CLEAR_VALUE;
                        j--;
                        ret = true;
                    }
                }else{
                    int k = j + 1;
                    for (; k < board.getWidth() && row[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < board.getWidth() && row[k] == row[j]){
                        row[j] *= 2;
                        row[k] = Constants2048.CLEAR_VALUE;
                        ret = true;
                    }
                }
            }
            board.setRow(i, row);
        }
        return ret;
    }

    boolean pushUp(){
        Log.d(Constants2048.DEBUG_TAG, "pushUp");
		/* Since we count from top to bottom, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i = 0; i < board.getWidth(); i++){
            int[] col = board.getCol(i);
            for (int j = 0; j < board.getHeight(); j++){
                if (col[j] == Constants2048.CLEAR_VALUE){
                    int k = j;
                    for (; k < board.getHeight() && col[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < board.getHeight()){
                        col[j] = col[k];
                        col[k] = Constants2048.CLEAR_VALUE;
                        j--;
                        ret = true;
                    }
                }else{
                    int k = j + 1;
                    for (; k < board.getHeight() && col[k] == Constants2048.CLEAR_VALUE; k++);
                    if (k < board.getHeight() && col[k] == col[j]){
                        col[j] *= 2;
                        col[k] = Constants2048.CLEAR_VALUE;
                        ret = true;
                    }
                }
            }
            board.setCol(i, col);
        }
        return ret;
    }

    boolean pushDown(){
        Log.d(Constants2048.DEBUG_TAG, "pushDown");
		/* Since we count from bottom to top, we shouldn't have any skipping errors*/
        boolean ret = false;
        for (int i = 0; i < board.getWidth(); i++){
            int[] col = board.getCol(i);
            for (int j = board.getHeight() - 1; j >= 0; j--){
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
            board.setCol(i, col);
        }
        return ret;
    }
}
