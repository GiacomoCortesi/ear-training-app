package com.cortesi.giacomo.eartrainingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.R.attr.defaultValue;

public class SecondLevelActivity extends AppCompatActivity {

    private RadioGroup radioGroupTriads1;
    private RadioGroup radioGroupTriads2;
    private RadioButton radioButtonTriad;
    private boolean submittedAnswer;
    private int scoreCounter;
    private ProgressBar scoreBar;
    private TextView scoreText;
    private int wrongAnswersCounter;
    private int correctAnswersCounter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    final static int MAX_SCORE = 100;
    final static int POINTS_CORRECT_ANSWER = +50;
    final static int POINTS_WRONG_ANSWER = -1;
    final static int MIN_SECOND = 1;
    final static int MAJ_SECOND = 2;
    final static int MIN_THIRD = 3;
    final static int MAJ_THIRD = 4;
    final static int PERF_FOURTH = 5;
    final static int AUG_FOURTH = 6;
    final static int PERF_FIFTH = 7;
    final static int MIN_SIXTH = 8;
    final static int MAJ_SIXTH = 9;
    final static int MIN_SEVENTH = 10;
    final static int MAJ_SEVENTH = 11;
    final static int PERF_OCTAVE = 12;

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                radioGroupTriads2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception
                radioGroupTriads2.clearCheck(); // clear the second RadioGroup!
                radioGroupTriads2.setOnCheckedChangeListener(listener2); //reset the listener
                Log.e("XXX2", "do the work");
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                radioGroupTriads1.setOnCheckedChangeListener(null);
                radioGroupTriads1.clearCheck();
                radioGroupTriads1.setOnCheckedChangeListener(listener1);
                Log.e("XXX2", "do the work");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_level);

        radioGroupTriads1 = (RadioGroup) findViewById(R.id.radioGroupTriads1);
        radioGroupTriads2 = (RadioGroup) findViewById(R.id.radioGroupTriads2);

        // this is so we can start fresh, with no selection on both RadioGroups
        radioGroupTriads1.clearCheck();
        radioGroupTriads2.clearCheck();

        radioGroupTriads1.setOnCheckedChangeListener(listener1);
        radioGroupTriads2.setOnCheckedChangeListener(listener2);

        sharedPref = getSharedPreferences("savingsLevel2", MODE_PRIVATE);
        scoreCounter = sharedPref.getInt("score", 0);
        correctAnswersCounter = sharedPref.getInt("correctAnswers", 0);
        wrongAnswersCounter = sharedPref.getInt("wrongAnswers", 0);

        scoreBar = (ProgressBar)findViewById(R.id.scoreBar);
        scoreBar.setProgress(scoreCounter);
        scoreText = (TextView)findViewById(R.id.scoreText);
        scoreText.setText( scoreCounter + "/" + scoreBar.getMax());

        if(scoreCounter >= MAX_SCORE)
        {
            startActivity(new Intent(SecondLevelActivity.this, ThirdLevelActivity.class));
        }

    }

    //This is just to display the action bar stats button on the current activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();

        ImageButton button = (ImageButton) findViewById(R.id.PlayButton);
        submittedAnswer = true;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decideInterval();
            }
        });

        Button submitButton = (Button) findViewById(R.id.submitButton);

        //This is so that an error Toast is displayed when pressing the submit button without playing the interval
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondLevelActivity.this,
                        R.string.exception1, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onPause(){
        super.onPause();

        //We want to save the current Score Whenever the Application is Paused so that the user can start where he left
        sharedPref = getSharedPreferences("savingsLevel2", MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putInt("score", scoreCounter);
        editor.putInt("correctAnswers", correctAnswersCounter);
        editor.putInt("wrongAnswers", wrongAnswersCounter);
        editor.apply();
    }

    //The following method determines which interval will be played in a random way
    public void decideInterval() {

        String triad = "";
        int root = 0;
        int third = 0;
        int fifth = 0;

        SharedPreferences sharedPref = getSharedPreferences("savingsLevel2", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (submittedAnswer) {

            final int randomNum = randInt(1, 6);
            switch (randomNum) {
                //Minor triad
                case 1: {
                    triad = "Minor";
                    root = randInt(1, 5);
                    third = root + MIN_THIRD;
                    fifth = third + MAJ_THIRD;
                    break;
                }
                //Major triad
                case 2: {
                    triad = "Major";
                    root = randInt(1, 5);
                    third = root + MAJ_THIRD;
                    fifth = third + MIN_THIRD;
                    break;
                }
                //Diminished triad
                case 3: {
                    triad = "Diminished";
                    root = randInt(1, 6);
                    third = root + MIN_THIRD;
                    fifth = third + MIN_THIRD;
                    break;
                }
                //Augmented triad
                case 4: {
                    triad = "Augmented";
                    root = randInt(1, 4);
                    third = root + MAJ_THIRD;
                    fifth = third + MAJ_THIRD;
                    break;
                }
                //Suspended 2
                case 5: {
                    triad = "Sus 2";
                    root = randInt(1, 5);
                    third = root + MAJ_SECOND;
                    fifth = third + PERF_FOURTH;
                    break;
                }
                //Suspended 4
                case 6: {
                    triad = "Sus 4";
                    root = randInt(1, 5);
                    third = root + PERF_FOURTH;
                    fifth = third + MAJ_SECOND;
                    break;
                }
            }
            editor.putInt("root", root);
            editor.putInt("third", third);
            editor.putInt("fifth", fifth);
            editor.putString("triad", triad);
            editor.apply();
            playTriad(root, third, fifth);
            submittedAnswer = false;
            defineAnswer(triad);
        } else {
            int savedRoot = sharedPref.getInt("root", defaultValue);
            int savedThird = sharedPref.getInt("third", defaultValue);
            int savedFifth = sharedPref.getInt("fifth", defaultValue);
            String savedTriad = sharedPref.getString("triad", triad);
            playTriad(savedRoot, savedThird, savedFifth);
            defineAnswer(savedTriad);
        }
    }

    // The following method generates a random integer between min and max
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    protected void playTriad(int root, int third, int fifth) {
        String rootStr = Integer.toString(root);
        String thirdStr = Integer.toString(third);
        String fifthStr = Integer.toString(fifth);
        int resIdRoot = this.getResources().
                getIdentifier("sound_" + rootStr, "raw", this.getPackageName());
        int resIdThird = this.getResources().
                getIdentifier("sound_" + thirdStr, "raw", this.getPackageName());
        int resIdFifth = this.getResources().
                getIdentifier("sound_" + fifthStr, "raw", this.getPackageName());

        final MediaPlayer mediaPlayer1 = MediaPlayer.create(this, resIdRoot);
        final MediaPlayer mediaPlayer2 = MediaPlayer.create(this, resIdThird);
        final MediaPlayer mediaPlayer3 = MediaPlayer.create(this, resIdFifth);

        //This way the notes of the triad are played consecutively
        mediaPlayer1.start();
        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer2.start();
            }
        });
        mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer3.start();
            }
        });
       /*//This way the notes of the triad are played simultaneously
        mediaPlayer1.start();
        mediaPlayer2.start();
        mediaPlayer3.start();
        */

    }

    public void defineAnswer(final String playedTriad) {

        /*The problem of the Radio Group is that, being a subset of the Linear Layout, doesn't allow to have multiple columns.
        To solve that we have to create two different Radio Groups and try to use them as one. Set up of the RGs:*/
        radioGroupTriads1 = (RadioGroup) findViewById(R.id.radioGroupTriads1);
        radioGroupTriads2 = (RadioGroup) findViewById(R.id.radioGroupTriads2);
        radioGroupTriads1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        radioGroupTriads2.clearCheck();
        radioGroupTriads1.setOnCheckedChangeListener(listener1);
        radioGroupTriads2.setOnCheckedChangeListener(listener2);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId1 = radioGroupTriads1.getCheckedRadioButtonId();
                int selectedId2 = radioGroupTriads2.getCheckedRadioButtonId();
                int realCheck = selectedId1 == -1 ? selectedId2 : selectedId1;

                // find the radio button by returned id
                radioButtonTriad = (RadioButton) findViewById(realCheck);

                //The following if condition check if a Radio Button has been checked or not
                if (realCheck == -1) {
                    Toast.makeText(SecondLevelActivity.this,
                            R.string.exception2, Toast.LENGTH_SHORT).show();
                    submittedAnswer = false;
                } else {
                    if (radioButtonTriad.getText().toString().equals(playedTriad)) {
                        showDialogCorrectAnswer();
                        Toast.makeText(SecondLevelActivity.this, POINTS_CORRECT_ANSWER + " Point", Toast.LENGTH_SHORT).show();
                        scoreCounter = scoreCounter + POINTS_CORRECT_ANSWER;
                        correctAnswersCounter = correctAnswersCounter + 1;
                        scoreBar.setProgress(scoreCounter);
                        scoreText.setText(scoreBar.getProgress() + "/" + scoreBar.getMax());
                    } else {
                        wrongAnswersCounter = wrongAnswersCounter + 1;
                        showDialogWrongAnswer(playedTriad);
                        Toast.makeText(SecondLevelActivity.this,
                                POINTS_WRONG_ANSWER + " Point", Toast.LENGTH_SHORT).show();
                        if (scoreCounter < - POINTS_WRONG_ANSWER) {
                            scoreBar.setProgress(0);
                            scoreText.setText(0 + "/" + scoreBar.getMax());
                        } else {
                            scoreCounter = scoreCounter + POINTS_WRONG_ANSWER;
                            scoreBar.setProgress(scoreCounter);
                            scoreText.setText(scoreBar.getProgress() + "/" + scoreBar.getMax());
                        }
                    }
                    submittedAnswer = true;

                    //This is in order to clear the selection of the Radio Button after the answer has been given
                    radioGroupTriads1.clearCheck();
                    radioGroupTriads2.clearCheck();
                }
            }
        });
    }

    protected void showDialogCorrectAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.correct_answer)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //This is in order to jump to the next level activity when you reach the needed score
                        if(scoreCounter >= MAX_SCORE)
                        {
                            showDialogNextLevel();
                        }
                        onResume();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected String showDialogWrongAnswer(String playedTriad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.wrong_answer) + "\n" + playedTriad)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onResume();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return playedTriad;
    }

    protected void showDialogNextLevel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.next_level))
                .setCancelable(false)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SecondLevelActivity.this, ThirdLevelActivity.class));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //This is in order to display a Toast Message Containing the current stats when pressing on the Stats Action Button.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.statsAction:
                Toast.makeText(SecondLevelActivity.this,
                        "Correct Answers: " + correctAnswersCounter + "\nWrong Answers: " + wrongAnswersCounter, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //This is in order to Disable the Back Button (in the bottom of the device).
    @Override
    public void onBackPressed() {
    }
}