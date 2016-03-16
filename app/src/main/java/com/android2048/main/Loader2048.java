package com.android2048.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android2048.R;
import com.android2048.game_modes.Base2048;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Loader2048 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loader2048);
        Button basic = (Button)findViewById(R.id.BasicButton);
        Button zero = (Button)findViewById(R.id.ZeroButton);

        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Loader2048.this, Base2048.class);
                Loader2048.this.startActivity(myIntent);
            }
        });
    }
}
