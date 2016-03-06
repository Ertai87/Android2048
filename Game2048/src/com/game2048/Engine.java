package com.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.util.*;

class Engine extends Activity{
	private static final String DEBUG_TAG = "Push";
	
	int[][] board = new int[4][4]; //model of the game board
	int clear_value; //value to denote an empty space, will be defined by child classes
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null){
			for (int i=0; i < 4; i++){
				for (int j=0; j < 4; j++){
					board[i][j] = clear_value;
				}
			}
		}
	}
	
	/* Returns whether or not the game is over.
	 * 
	 * true = there are no valid moves, the game is over
	 * false = there is a valid move, the game continues
	 */
	boolean isEndGame(){
		for (int i=0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				if (board[i][j] == clear_value || 
						(i > 0 && board[i][j] == board[i-1][j]) ||
						(i < 3 && board[i][j] == board[i+1][j]) ||
						(j > 0 && board[i][j] == board[i][j-1]) ||
						(j < 3 && board[i][j] == board[i][j+1])){
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
				if (board[i][j] == 2048){
					return true;
				}
			}
		}
		return false;
	}
	
	boolean pushRight(){
		Log.d(DEBUG_TAG, "Right");
		/* Since we count from right to left, we shouldn't have any skipping errors*/
		boolean ret = false;
		boolean[] changed = new boolean[4];
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				changed[j] = false;
			}
			for (int j=3; j >= 0; j--){
				if (board[i][j] != clear_value){
					for (int k = 3; k > j; k--){
						if (board[i][k] == clear_value){
							if (k < 3 && board[i][k+1] == board[i][j] && !changed[k + 1]){
								//if we haven't yet combined into (i, k+1), combine into it
								board[i][k+1] *= 2;
								changed[k+1] = true;
							}else{
								//otherwise, just move the value
								board[i][k] = board[i][j];
							}
							board[i][j] = clear_value;
							ret = true;
							break;
						}else if (k == j + 1 && board[i][j] == board[i][k] && !changed[k]){
							board[i][k] *= 2;
							board[i][j] = clear_value;
							ret = true;
							changed[k] = true;
							break;
						}
					}
				}
			}
		}
		return ret;
	}
	
	boolean pushLeft(){
		Log.d(DEBUG_TAG, "Left");
		/* Since we count from left to right, we shouldn't have any skipping errors*/
		boolean ret = false;
		boolean[] changed = new boolean[4];
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				changed[j] = false;
			}
			for (int j=0; j < 4; j++){
				if (board[i][j] != clear_value){
					for (int k = 0; k < j; k++){
						if (board[i][k] == clear_value){
							if (k > 0 && board[i][k-1] == board[i][j] && !changed[k-1]){
								//if we haven't yet combined into (i, k-1), combine into it
								board[i][k-1] *= 2;
								changed[k-1] = true;
							}else{
								//otherwise, just move the value
								board[i][k] = board[i][j];
							}
							board[i][j] = clear_value;
							ret = true;
							break;
						}else if (k == j - 1 && board[i][j] == board[i][k] && !changed[k]){
							board[i][k] *= 2;
							changed[k] = true;
							board[i][j] = clear_value;
							ret = true;
							break;
						}
					}
				}
			}
		}
		return ret;
	}
	
	boolean pushUp(){
		Log.d(DEBUG_TAG, "Up");
		/* Since we count from top to bottom, we shouldn't have any skipping errors*/
		boolean ret = false;
		boolean[] changed = new boolean[4];
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				changed[j] = false;
			}
			for (int j=0; j < 4; j++){
				if (board[j][i] != clear_value){
					for (int k = 0; k < j; k++){
						if (board[k][i] == clear_value){
							if (k > 0 && board[k-1][i] == board[j][i] && !changed[k-1]){
								//if we haven't yet combined into (i, k-1), combine into it
								board[k-1][i] *= 2;
								changed[k-1] = true;
							}else{
								//otherwise, just move the value
								board[k][i] = board[j][i];
							}
							board[j][i] = clear_value;
							ret = true;
							break;
						}else if (k == j - 1 && board[k][i] == board[j][i] && !changed[k]){
							board[k][i] *= 2;
							board[j][i] = clear_value;
							ret = true;
							changed[k] = true;
							break;
						}
					}
				}
			}
		}
		return ret;
	}
	
	boolean pushDown(){
		Log.d(DEBUG_TAG, "Down");
		/* Since we count from bottom to top, we shouldn't have any skipping errors*/
		boolean ret = false;
		boolean[] changed = new boolean[4];
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				changed[j] = false;
			}
			for (int j=3; j >= 0; j--){
				if (board[j][i] != clear_value){
					for (int k = 3; k > j ; k--){
						if (board[k][i] == clear_value){
							if (k < 3 && board[k+1][i] == board[j][i] && !changed[k + 1]){
								//if we haven't yet combined into (i, k+1), combine into it
								board[k+1][i] *= 2;
								changed[k+1] = true;
							}else{
								//otherwise, just move the value
								board[k][i] = board[j][i];
							}
							board[j][i] = clear_value;
							ret = true;
							break;
						}else if (k == j + 1 && board[j][i] == board[k][i] && !changed[k]){
							board[k][i] *= 2;
							board[j][i] = clear_value;
							ret = true;
							changed[k] = true;
							break;
						}
					}
				}
			}
		}
		return ret;
	}
}