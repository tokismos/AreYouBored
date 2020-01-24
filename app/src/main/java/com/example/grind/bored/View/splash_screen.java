package com.example.grind.bored.View;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grind.bored.R;

// L'activité qui se lance en premier
public class splash_screen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Button searchButton = (Button) findViewById(R.id.startButton);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.buttonsound);

        // Pour basculer vers la deuxieme activité lors du clique sur le boutton Start
        searchButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        mp.start();
                        Intent intent = new Intent(splash_screen.this, genreselection.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
