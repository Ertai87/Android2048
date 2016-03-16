package com.android2048;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.android2048.common.Constants2048;
import com.android2048.model.ArrayModel;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static com.android2048.R.menu.base2048;

public class Base2048 extends AppCompatActivity {

	TextView[][] boardView = new TextView[4][4];
    Engine engine;
	Random gen = new Random();
	GestureDetectorCompat detector;
	boolean newgameplus; //If game is in "New Game+" state: If 2048 has been reached and "Continue" was chosen

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        try {
            engine = new Engine(ArrayModel.class, 4, 4);
        } catch (Exception e){
            Log.d(Constants2048.FATAL_TAG, e.getMessage());
            System.exit(1);
        }
        setContentView(R.layout.activity_base2048);
		boardView[0][0] = (TextView)findViewById(R.id.box00);
		boardView[0][1] = (TextView)findViewById(R.id.box01);
		boardView[0][2] = (TextView)findViewById(R.id.box02);
		boardView[0][3] = (TextView)findViewById(R.id.box03);
		boardView[1][0] = (TextView)findViewById(R.id.box10);
		boardView[1][1] = (TextView)findViewById(R.id.box11);
		boardView[1][2] = (TextView)findViewById(R.id.box12);
		boardView[1][3] = (TextView)findViewById(R.id.box13);
		boardView[2][0] = (TextView)findViewById(R.id.box20);
		boardView[2][1] = (TextView)findViewById(R.id.box21);
		boardView[2][2] = (TextView)findViewById(R.id.box22);
		boardView[2][3] = (TextView)findViewById(R.id.box23);
		boardView[3][0] = (TextView)findViewById(R.id.box30);
		boardView[3][1] = (TextView)findViewById(R.id.box31);
		boardView[3][2] = (TextView)findViewById(R.id.box32);
		boardView[3][3] = (TextView)findViewById(R.id.box33);
		detector = new GestureDetectorCompat(this, new Mover());
		newgameplus = false;
		createValue();
		createValue();
		updateBoard();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        Log.d(Constants2048.DEBUG_TAG, "Creating Options Menu");
		getMenuInflater().inflate(base2048, menu);
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
		Builder builder = new Builder(this);
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
		Builder builder = new Builder(this);
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
		engine.board.clear();
		newgameplus = false;
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
		while (engine.board.get(x, y) != Constants2048.CLEAR_VALUE){
			x = Math.abs(gen.nextInt()) % 4;
			y = Math.abs(gen.nextInt()) % 4;
		}
		flag = Math.abs(gen.nextInt()) % 10;
		if (flag == 0){
			engine.board.set(x, y, 4);
		}else{
			engine.board.set(x, y, 2);
		}
		return engine.isEndGame();
	}

	void updateBoard(){
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				if (engine.board.get(i, j) == Constants2048.CLEAR_VALUE){
					boardView[i][j].setText("");
				}else{
					boardView[i][j].setText("" + engine.board.get(i, j));
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
						if(engine.pushRight()){
							endgame = createValue();
							updateBoard();
						}
					}else{
						if (engine.pushLeft()){
							endgame = createValue();
							updateBoard();
						}
					}
				}
			}else{
				if (Math.abs(ydist) > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY){
					if (ydist < 0){
						if(engine.pushDown()){
							endgame = createValue();
							updateBoard();
						}
					}else{
						if (engine.pushUp()){
							endgame = createValue();
							updateBoard();
						}
					}
				}
			}
			if (!newgameplus && engine.checkWin()){
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
