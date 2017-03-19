package com.htoja.mifik.htoja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.fragment.NextTeamFragment;
import com.htoja.mifik.htoja.fragment.SetupSettingsFragment;
import com.htoja.mifik.htoja.fragment.SetupTeamsFragment;

import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private SetupTeamsFragment setupTeamsFragment;
    private SetupSettingsFragment setupSettingsFragment;
    private List<String> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setupTeamsFragment = new SetupTeamsFragment();
        setupSettingsFragment = new SetupSettingsFragment();
        showTeamsFragment();
    }


    private void showTeamsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, setupTeamsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickNext(View view) {
        teams = setupTeamsFragment.getTeams();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, setupSettingsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickPlay(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void clickAdd(View view) {
        setupTeamsFragment.clickAdd(view);
    }

    public void clickStart(View view) {
        TeamGameManager.getInstance().startNewSet(teams, setupSettingsFragment.getTargetWords(), setupSettingsFragment.getSeconds());
        TeamGameManager.getInstance().firstTeam();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new NextTeamFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
