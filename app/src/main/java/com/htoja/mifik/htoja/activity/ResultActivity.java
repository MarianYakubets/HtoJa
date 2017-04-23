package com.htoja.mifik.htoja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_menu);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("SHOW_NEXT_TEAM")) {
            showNextTeamFragment();
        } else {

            showResultFragment();
        }
    }

    private void showResultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new RoundResultFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickNext(View view) {
        showNextTeamFragment();
    }

    private void showNextTeamFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new NextTeamFragment());
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void clickPlay(View view) {
        Intent i = new Intent(this, GameLandscapeActivity.class);
        if (TeamGameManager.getInstance().hasEnded()) {
            i = new Intent(this, MainActivity.class);
        }
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
