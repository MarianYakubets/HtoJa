package com.htoja.mifik.htoja.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.adapter.CardAdapter;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.gyroscope.ImprovedOrientationSensor2Provider;
import com.htoja.mifik.htoja.gyroscope.OrientationProvider;
import com.htoja.mifik.htoja.gyroscope.representation.Quaternion;
import com.htoja.mifik.htoja.utils.Storage;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameLandscapeActivity extends AppCompatActivity implements CardStackView.CardStackEventListener {
    public static final String CORRECT = "correct";
    public static final String SKIP = "skip";
    public static final String TEAM = "team";
    private final static int START_COUNT = 30;

    private CountDownTimer timer;
    private Timer sensorTimer = new Timer();

    private ArrayList<String> correct = new ArrayList<>();
    private ArrayList<String> skip = new ArrayList<>();
    private int seconds;
    private CardAdapter cardAdapter;
    private CardStackView cardStackView;
    private FloatingActionButton pauseBtn;
    private boolean paused = false;
    private OrientationProvider currentOrientationProvider;
    private Quaternion quaternion = new Quaternion();

    private int counter = 0;
    private Quaternion startQ = new Quaternion();
    private boolean inUse = false;
    private TextView timerView;
    private TextView tvStartRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_landscape);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        seconds = TeamGameManager.getInstance().getRoundTime();

        cardAdapter = new CardAdapter(this, R.layout.item_card_stack_landscape);
        TeamGameManager.getInstance().nextWord();
        cardAdapter.addAll(TeamGameManager.getInstance().getLeftWords());

        timerView = (TextView) findViewById(R.id.tvTimer);
        tvStartRound = (TextView) findViewById(R.id.tv_start_round);
        Date date = new Date(seconds * 1000);
        seconds = date.getMinutes() * 60 + date.getSeconds();
        timerView.setText(new SimpleDateFormat("mm:ss").format(date));

        cardStackView = (CardStackView) findViewById(R.id.csvWord);
        cardStackView.setAdapter(cardAdapter);
        cardStackView.setCardStackEventListener(this);

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
            }
        });

        currentOrientationProvider = new ImprovedOrientationSensor2Provider((SensorManager) getSystemService(GameLandscapeActivity.SENSOR_SERVICE));
    }

    private void runSensorTimer() {
        sensorTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (paused) {
                            return;
                        }

                        currentOrientationProvider.getQuaternion(quaternion);
                        if (counter >= START_COUNT) {
                            double[] angles = quaternion.toEulerAngles();
                            double yaw = Math.toDegrees(angles[0]);
                            double roll = Math.toDegrees(angles[1]);
                            double[] anglesStart = startQ.toEulerAngles();
                            double startYaw = Math.toDegrees(anglesStart[0]);
                            double startRoll = Math.toDegrees(anglesStart[1]);

                            double diff = startYaw - yaw;
                            double rollDiff = startRoll - roll;

                            if (Math.abs(rollDiff) < 15 && !inUse && Math.abs(diff) > 50) {
                                if (diff > 0) {
                                    clickNo();
                                } else {
                                    clickYes();
                                }
                                inUse = true;
                            } else if (Math.abs(diff) < 15) {
                                inUse = false;
                            }
                            return;
                        }

                        double[] angles = quaternion.toEulerAngles();
                        double yaw = Math.toDegrees(angles[0]);
                        double pitch = Math.toDegrees(angles[2]);
                        if (80 < Math.abs(yaw) && Math.abs(yaw) < 100) {
                            if (Math.abs(pitch) < 10 || 170 < Math.abs(pitch)) {
                                counter++;
                                if (counter == START_COUNT) {
                                    Toast.makeText(getBaseContext(), "Почали", Toast.LENGTH_SHORT).show();
                                    startTimer();
                                    startQ.set(quaternion);
                                    tvStartRound.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
            }
        }, 0, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentOrientationProvider.start();
        runSensorTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
        pauseBtn.setImageResource(R.drawable.ic_media_play);
        timer.cancel();
        currentOrientationProvider.stop();
        sensorTimer.cancel();
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

    public void clickYes() {
        if (paused)
            return;

        final CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        card.setCardBackgroundColor(Color.parseColor("#38654B"));

        cardStackView.discard(Direction.TopRight);

        cardStackView.postDelayed(new Runnable() {
            @Override
            public void run() {
                card.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
            }
        }, 100);
    }

    public void clickNo() {
        if (paused)
            return;

        final CardView card = (CardView) cardStackView.getTopView().findViewById(R.id.cvTop);
        card.setCardBackgroundColor(Color.parseColor("#a12323"));

        cardStackView.discard(Direction.BottomLeft);

        cardStackView.postDelayed(new Runnable() {
            @Override
            public void run() {
                card.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
            }
        }, 100);
    }
}
