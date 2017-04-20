package com.htoja.mifik.htoja.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.adapter.CardAdapter;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.data.Vocabulary;
import com.htoja.mifik.htoja.utils.Storage;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements CardStackView.CardStackEventListener {
    public static final String CORRECT = "correct";
    public static final String SKIP = "skip";
    public static final String TEAM = "team";

    private CountDownTimer timer;
    private ArrayList<String> correct = new ArrayList<>();
    private ArrayList<String> skip = new ArrayList<>();
    private int seconds;
    private CardAdapter cardAdapter;
    private CardStackView cardStackView;
    private FloatingActionButton pauseBtn;
    private boolean paused = false;

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

        cardAdapter = new CardAdapter(this);
        TeamGameManager.getInstance().nextWord();
        cardAdapter.addAll(TeamGameManager.getInstance().getLeftWords());


        cardStackView = (CardStackView) findViewById(R.id.csvWord);
        cardStackView.setAdapter(cardAdapter);
        cardStackView.setCardStackEventListener(this);

        seconds = TeamGameManager.getInstance().getRoundTime();

        startTimer();

        pauseBtn = (FloatingActionButton) findViewById(R.id.fab_pause);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused) {
                    paused = false;
                    pauseBtn.setImageResource(R.drawable.ic_media_pause);
                    startTimer();
                } else {
                    paused = true;
                    pauseBtn.setImageResource(R.drawable.ic_media_play);
                    timer.cancel();
                }

                cardStackView.setSwipeEnabled(!paused);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
        pauseBtn.setImageResource(R.drawable.ic_media_play);
        timer.cancel();
    }

    public void clickYes(int index) {
        correct.add(cardAdapter.getItem(index));
        TeamGameManager.getInstance().nextWord();
    }

    public void clickNo(int index) {
        skip.add(cardAdapter.getItem(index));
        TeamGameManager.getInstance().nextWord();
    }

    private void startTimer() {
        final TextView timerView = (TextView) findViewById(R.id.tvTimer);
        timer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                Date date = new Date(millisUntilFinished);
                if (seconds == 6) {
                    timerView.setTextColor(Color.RED);
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                seconds = date.getMinutes() * 60 + date.getSeconds();
                timerView.setText(new SimpleDateFormat("mm:ss").format(date));
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
        Storage.saveCurrentTeamState(this);
    }

    @Override
    public void onBeginSwipe(int index, Direction direction) {
    }

    @Override
    public void onEndSwipe(Direction direction) {
        CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        card.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
    }

    @Override
    public void onSwiping(float positionX) {
        CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        if (positionX > 0) {
            card.setCardBackgroundColor(Color.parseColor("#38654B"));
        } else {
            card.setCardBackgroundColor(Color.parseColor("#a12323"));
        }
    }

    @Override
    public void onDiscarded(int index, Direction direction) {
        if (direction == Direction.TopRight || direction == Direction.BottomRight) {
            clickYes(index);
        } else {
            clickNo(index);
        }
        CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        card.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
    }

    @Override
    public void onTapUp(int index) {
        timer.cancel();
        timer.start();
    }

    public void clickYes(View view) {
        if (paused)
            return;

        CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        card.setCardBackgroundColor(Color.parseColor("#38654B"));

        cardStackView.discard(Direction.TopRight);
    }

    public void clickNo(View view) {
        if (paused)
            return;

        final CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        card.setCardBackgroundColor(Color.parseColor("#a12323"));

        cardStackView.discard(Direction.BottomLeft);
        card.post(new Runnable() {
            @Override
            public void run() {
                card.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
            }
        });
    }
}
