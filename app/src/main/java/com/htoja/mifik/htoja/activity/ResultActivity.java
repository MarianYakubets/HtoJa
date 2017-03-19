package com.htoja.mifik.htoja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.fragment.NextTeamFragment;
import com.htoja.mifik.htoja.fragment.RoundResultFragment;

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
        showResultFragment();
    }

    private void showResultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new RoundResultFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickNext(View view) {
        TeamGameManager.getInstance().nextTeam();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new NextTeamFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickPlay(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
