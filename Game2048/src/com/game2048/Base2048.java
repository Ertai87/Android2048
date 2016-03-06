package com.game2048;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Menu;
import android.widget.TextView;
import java.util.*;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.util.*;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class Base2048 extends Engine{

	TextView[][] board = new TextView[4][4];
	Random gen = new Random();
	GestureDetectorCompat detector;
	Base2048 app = this; //Used to pass "this" object into functions of subclasses
	boolean newgameplus; //If game is in "New Game+" state: If 2048 has been reached and "Continue" was chosen
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.clear_value = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base2048);
		board[0][0] = (TextView)findViewById(R.id.box00);
		board[0][1] = (TextView)findViewById(R.id.box01);
		board[0][2] = (TextView)findViewById(R.id.box02);
		board[0][3] = (TextView)findViewById(R.id.box03);
		board[1][0] = (TextView)findViewById(R.id.box10);
		board[1][1] = (TextView)findViewById(R.id.box11);
		board[1][2] = (TextView)findViewById(R.id.box12);
		board[1][3] = (TextView)findViewById(R.id.box13);
		board[2][0] = (TextView)findViewById(R.id.box20);
		board[2][1] = (TextView)findViewById(R.id.box21);
		board[2][2] = (TextView)findViewById(R.id.box22);
		board[2][3] = (TextView)findViewById(R.id.box23);
		board[3][0] = (TextView)findViewById(R.id.box30);
		board[3][1] = (TextView)findViewById(R.id.box31);
		board[3][2] = (TextView)findViewById(R.id.box32);
		board[3][3] = (TextView)findViewById(R.id.box33);
		detector = new GestureDetectorCompat(this, new Mover());
		newgameplus = false;
		createValue();
		createValue();
		updateBoard();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base2048, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case R.id.newgame:
				newGame();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void win(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Congratulations on getting to 2048!  You win!");
		builder.setTitle("You Win!");
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newgameplus = true;
				return;
			}
		});
		builder.setNegativeButton("New Game", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				newGame();
			}
		});
		builder.create().show();
	}

	public void endGame(){
		Log.d("Endgame", "Endgame");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Game Over!  Would you like to start a new game?");
		builder.setTitle("Game Over!");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newGame();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		builder.create().show();
	}
	
	public void newGame(){
		super.board = new int[4][4];
		newgameplus = false;
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				super.board[i][j] = clear_value;
			}
		}
		createValue();
		createValue();
		updateBoard();
	}
	
	/*Creates a new value on the board, and then returns whether or not there
	 * is a valid move on the board
	 * 
	 * true = there is no move remaining
	 * false = there is a valid move
	 */
	
	boolean createValue(){
		/* Add a new value to a random open square
		 * We have a 10% chance of adding a 4, 90% chance of adding a 2
		 */
		int x = Math.abs(gen.nextInt()) % 4, y = Math.abs(gen.nextInt()) % 4, flag;
		while (super.board[x][y] != super.clear_value){
			x = Math.abs(gen.nextInt()) % 4;
			y = Math.abs(gen.nextInt()) % 4;
		}
		flag = Math.abs(gen.nextInt()) % 10;
		if (flag == 0){
			super.board[x][y] = 4;
		}else{
			super.board[x][y] = 2;
		}
		return isEndGame();
	}
	
	void updateBoard(){
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				if (super.board[i][j] == super.clear_value){
					board[i][j].setText("");
				}else{
					board[i][j].setText("" + super.board[i][j]);
				}
			}
		}
	}
	
	public boolean onTouchEvent(MotionEvent event){
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	class Mover extends SimpleOnGestureListener{
		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
		
		private static final String DEBUG_TAG = "Gestures";
		
		public boolean onDown(MotionEvent e){
			Log.d(DEBUG_TAG, "onDown");
			return true;
		}
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
			Log.d(DEBUG_TAG, "Fling");
			float xdist, ydist;
			boolean endgame = false;;
			xdist = e1.getX() - e2.getX();
			ydist = e1.getY() - e2.getY();
			if (Math.abs(xdist) > Math.abs(ydist)){
				if (Math.abs(xdist) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
					if (xdist < 0){
						if(pushRight()){
							endgame = createValue();
							updateBoard();
						}
					}else{
						if (pushLeft()){
							endgame = createValue();
							updateBoard();
						}
					}
				}
			}else{
				if (Math.abs(ydist) > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY){
					if (ydist < 0){
						if(pushDown()){
							endgame = createValue();
							updateBoard();
						}
					}else{
						if (pushUp()){
							endgame = createValue();
							updateBoard();
						}
					}
				}
			}
			if (!newgameplus && checkWin()){
				win();
				return true;
			}
			if (endgame){
				endGame();				
			}
			return true;
		}
	}
}
