package com.htoja.mifik.htoja;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.htoja.mifik.htoja.data.Dictionary;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;

    private static final int SENSOR_DELAY = 500 * 1000; // 500ms
    private static final int FROM_RADS_TO_DEGS = -57;
    private TextView textView;
    private List<String> words;
    private int counter = 0;

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
        try {
            mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        } catch (Exception e) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }

        words = Dictionary.getWords();
        long seed = System.nanoTime();
        Collections.shuffle(words, new Random(seed));

        setNextWord();
        startTimer();
    }

    public void clickYes(View view) {
        setNextWord();
    }

    public void clickNo(View view) {
        setNextWord();
    }

    private void setNextWord() {
        textView.setText(words.get(counter++));
        if (counter == words.size()) {
            counter = 0;
            long seed = System.nanoTime();
            Collections.shuffle(words, new Random(seed));
        }
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
        float roll = orientation[0] * FROM_RADS_TO_DEGS;
        float yaw = orientation[2] * FROM_RADS_TO_DEGS;

      /*  textView.setText("pitch : " + pitch + ",\nroll : " + roll + ",\nyaw : " + yaw);

        if (yaw > 0) {
            if (yaw < 98 && yaw > 82) {
                textView.setText("good pitch : " + pitch + ",\nroll : " + roll + ",\nyaw : " + yaw);


            }
        } else {
            if (yaw > -98 && yaw < -82) {
                textView.setText("good pitch : " + pitch + ",\nroll : " + roll + ",\nyaw : " + yaw);

            }
        }*/

    }

    private void startTimer() {
        final TextView timer = (TextView) findViewById(R.id.tvTimer);
        new CountDownTimer(1 * 60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                timer.setText("done!");
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
