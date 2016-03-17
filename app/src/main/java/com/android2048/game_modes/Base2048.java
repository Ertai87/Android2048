package com.android2048.game_modes;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.android2048.R;
import com.android2048.common.Constants2048;
import com.android2048.common.Engine;
import com.android2048.main.Loader2048;
import com.android2048.model.ArrayModel;

import java.util.Comparator;

import static com.android2048.R.menu.base2048;

public class Base2048 extends AppCompatActivity {

	TextView[][] boardView = new TextView[4][4];
    Engine engine;
	GestureDetectorCompat detector;
	boolean newgameplus; //If game is in "New Game+" state: If 2048 has been reached and "Continue" was chosen

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Create a 4x4 game grid
        try {
			Integer[] genprobs = new Integer[100];
			for (int i=0; i < 10; i++){
				genprobs[i] = 4;
			}
			for (int i=10; i < 100; i++){
				genprobs[i] = 2;
			}
            engine = new Engine<>(ArrayModel.class, 4, 4, Constants2048.CLEAR_VALUE, Constants2048.WIN_VALUE, new Comparator<Integer>() {
				@Override
				public int compare(Integer lhs, Integer rhs) {
					return lhs - rhs;
				}
			}, genprobs);
        } catch (Exception e){
            Log.d(Constants2048.FATAL_TAG, e.getMessage());
            System.exit(1);
        }

        //Set up the GUI.  TextViews are named "box" + x-coord + y-coord where (0, 0) is the top-left and (3, 3) is the bottom right.
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

        newGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(base2048, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case R.id.newgame:
				newGame();
				return true;
            case R.id.main:
                Intent myIntent = new Intent(Base2048.this, Loader2048.class);
                Base2048.this.startActivity(myIntent);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

    /*
    Build and display a "You win!" dialog which gives the user the option between starting
    a new game or proceeding to New Game+ mode
     */
	public void win(){
		Builder builder = new Builder(this);
		builder.setMessage("Congratulations on getting to 2048!  You win!");
		builder.setTitle("You Win!");
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newgameplus = true;			}
		});
		builder.setNegativeButton("New Game", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				newGame();
			}
		});
		builder.create().show();
	}

    /*
    Build and display a "Game Over!" dialog which gives the user the option of starting a new game
    or leaving their current dead game on-screen, requiring them to start a new game manually.
     */
	public void endGame(){
		Builder builder = new Builder(this);
		builder.setMessage("Game Over!  Would you like to start a new game?");
		builder.setTitle("Game Over!");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newGame();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}

    /*
    Start a new game, create 2 new values
     */
	public void newGame(){
		engine.clearBoard();
		newgameplus = false;
		engine.createValue();
		engine.createValue();
		updateBoard();
	}

    /*
    Update the GUI based on the model
     */
	void updateBoard(){
		for (int i=0; i < 4; i++){
			for (int j=0; j < 4; j++){
				if (engine.getBoardModel().get(i, j).equals(Constants2048.CLEAR_VALUE)){
					boardView[i][j].setText("");
				}else{
					boardView[i][j].setText("" + engine.getBoardModel().get(i, j));
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

		public boolean onDown(MotionEvent e){
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
			Log.d(Constants2048.DEBUG_TAG, "Fling");
			float xdist, ydist;
			boolean endgame = false;
			xdist = e2.getX() - e1.getX();
			ydist = e2.getY() - e1.getY();
			if (Math.abs(xdist) > Math.abs(ydist)){
				if (Math.abs(xdist) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
					if (xdist > 0){
						if(engine.pushRight()){
							endgame = engine.createValue();
							updateBoard();
						}
					}else{
						if (engine.pushLeft()){
							endgame = engine.createValue();
							updateBoard();
						}
					}
				}
			}else{
				if (Math.abs(ydist) > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY){
					if (ydist > 0){
						if(engine.pushDown()){
							endgame = engine.createValue();
							updateBoard();
						}
					}else{
						if (engine.pushUp()){
							endgame = engine.createValue();
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
