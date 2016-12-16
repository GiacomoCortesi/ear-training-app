package com.cortesi.giacomo.eartrainingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*
To be done:
- Add the contiguous/simultaneous playing of the sounds during the level.
- Add the possibility to check for the current score (wrong and correct answers) in every moment.
-



Ideas:
- You have to allow the user to start the game fresh when clicking on the LET'S START button and to continue where he left
pressing the Continue Button
- Use a Registration and Login UI with Database. So that the user can keep track of the score and its ranking.
Simple Version: Using android's class for SQLite Databases. The scores can de saved in the internal memory of the device.
Complex Version: Making separately an SQL database connected to the Internet in order to share the ranking with all the users of the application.
 */


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startFirstLevelActivity(View view){
        Intent intent = new Intent(this, FirstLevelActivity.class);
        startActivity(intent);
    }
    public void startPracticeActivity(View view){
        Intent intent = new Intent(this, PracticeActivity.class);
        startActivity(intent);
    }
    public void startInfoActivity(View view){
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
}
