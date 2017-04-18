package com.example.nguyenduchai.freakingmath;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    // time to play each level
    private final int TIME_PLAY_EACH_LEVEL = 1000;

    // define background to play screen
    private String[] arrColor = {"#FFFE0057", "#FF000000", "#FF8AFB00", "#FF002AFE", "#FF454546",
                                "#FFFEE000", "#FFFF7300", "#FFFB00FF", "#FF083AED", "#FF7F4343"};
    // view on screen
    private TextView lblFormular, lblResult, lblScore;
    private ImageView btnCorrect, btnWrong;
    private RelativeLayout relLayoutPlay;

    // timer
    private Timer timer;
    private TimerTask timerTask;

    // play score
    private int myScore = 0;

    // level model
    private LevelModel model;
    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove action bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play);

        // find view
        lblFormular = (TextView) findViewById(R.id.lbl_formular);
        lblResult = (TextView) findViewById(R.id.lbl_result);
        lblScore = (TextView) findViewById(R.id.lbl_score);
        relLayoutPlay = (RelativeLayout) findViewById(R.id.rel_layout_play);
        btnCorrect = (ImageView) findViewById(R.id.btn_correct);
        btnWrong = (ImageView) findViewById(R.id.btn_wrong);

        // handle click button
        btnCorrect.setOnClickListener(this);
        btnWrong.setOnClickListener(this);

        // create random
        rand = new Random();

        // generate to first level
        model = GenerateLevel.generateLevel(myScore);

        // show level information on screen
        displayNewLevel(model);
        // create time
        CreateTimeTask();

    }


    // display to information on screen
    private void displayNewLevel(LevelModel model){
        lblFormular.setText(model.strOperator);
        lblResult.setText(model.strResult);

        // set random background color in play
        int indexColor = rand.nextInt(arrColor.length);
        relLayoutPlay.setBackgroundColor(Color.parseColor(arrColor[indexColor]));
    }
    // start calculator time for play
    private void CreateTimeTask() {
        // create timer
        timer = new Timer();

        // create time task
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // show game over screen
                        showGameOver(myScore);
                    }
                });
            }
        };
        // set timer run time task
        timer.schedule(timerTask, TIME_PLAY_EACH_LEVEL);
    }

    // show pop game over
    private void showGameOver(final int score){
        // disable button on view
        btnCorrect.setEnabled(false);
        btnWrong.setEnabled(false);
        // lock screen touch ??

        // cancel timer
        cancelTimer();

        // show game over
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Your score: " + score)
                .setPositiveButton(R.string.relay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        btnCorrect.setEnabled(true);
                        btnWrong.setEnabled(true);

                        // play again
                        lblScore.setText("0");
                        PlayActivity.this.myScore = 0;
                        PlayActivity.this.nextLevel(myScore);
                    }
                })
                .setNegativeButton(R.string.home, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PlayActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    // cancel time while finish level
    private void cancelTimer(){
        timer.cancel();
        timerTask.cancel();
    }

    // new level
    private void nextLevel(int score){
        // update score
        lblScore.setText(String.valueOf(score));

        // cancer time
        cancelTimer();

        // update UI
        model = GenerateLevel.generateLevel(score);
        displayNewLevel(model);
        CreateTimeTask();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // player select correct button
        if(id == R.id.btn_correct){
            if(model.correctWrong == true){
                // increase score for player
                myScore += 1;

                // next level
                nextLevel(myScore);
            }else{
                // show game over
                showGameOver(myScore);
            }
        }
        // player select wrong button
        if(id == R.id.btn_wrong){
            if(model.correctWrong == true){
                // show game over
                showGameOver(myScore);
            }else{
                // increase score for player
                myScore += 1;

                // next level
                nextLevel(myScore);
            }
        }
    }
}
