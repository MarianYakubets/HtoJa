package com.htoja.mifik.htoja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.data.Dictionary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    public static final String CORRECT = "correct";
    public static final String SKIP = "skip";
    public static final String TEAM = "team";


    private TextView textView;
    private List<String> words;
    private int counter = 0;
    private CountDownTimer timer;
    private ArrayList<String> correct = new ArrayList<>();
    private ArrayList<String> skip = new ArrayList<>();
    private int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        textView = (TextView) findViewById(R.id.tvWord);

        words = Dictionary.getWords();
        Collections.shuffle(words, new Random(System.nanoTime()));

        seconds = TeamGameManager.getInstance().getRoundTime();

        setNextWord();
        startTimer();
    }

    public void clickYes(View view) {
        correct.add(words.get(counter));
        setNextWord();
    }

    public void clickNo(View view) {
        skip.add(words.get(counter));
        setNextWord();
    }

    private void setNextWord() {
        textView.setText(words.get(counter++));
        if (counter == words.size()) {
            counter = 0;
            Collections.shuffle(words, new Random(System.nanoTime()));
        }
    }

    private void startTimer() {
        final TextView timerView = (TextView) findViewById(R.id.tvTimer);
        timer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerView.setText(new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                endRound();
            }
        };
        timer.start();
    }

    private void endRound() {
        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(CORRECT, correct);
        bundle.putStringArrayList(SKIP, skip);
        bundle.putString(TEAM, TeamGameManager.getInstance().getCurrentTeam());
        i.putExtra("SHOW_NEXT_TEAM", false);
        i.putExtras(bundle);
        saveResults();
        startActivity(i);
    }

    private void saveResults() {
        int result = correct.size();
        if (TeamGameManager.getInstance().hasFine()) {
            result -= skip.size();
        }
        TeamGameManager.getInstance().addCurrentTeamPoints(result);
        TeamGameManager.getInstance().nextTeam();
    }
}
