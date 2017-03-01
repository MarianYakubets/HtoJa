package com.htoja.mifik.htoja.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> skip = bundle.getStringArrayList(GameActivity.SKIP);
        ArrayList<String> correct = bundle.getStringArrayList(GameActivity.CORRECT);

        StringBuilder sb = new StringBuilder();
        sb.append("Correct: ");
        if (correct != null) {
            for (String word : correct) {
                sb.append(word);
                sb.append(", ");
            }
        }
        sb.append("\nSkip: ");
        if (skip != null) {
            for (String word : skip) {
                sb.append(word);
                sb.append(", ");
            }
        }
        TextView resultView = (TextView) findViewById(R.id.tvResult);
        resultView.setText(sb.toString());
    }

    public void clickNext(View view) {

    }
}
