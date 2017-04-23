package com.htoja.mifik.htoja.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.data.Vocabulary;
import com.htoja.mifik.htoja.fragment.NextTeamFragment;
import com.htoja.mifik.htoja.fragment.SetupCategoriesFragment;
import com.htoja.mifik.htoja.fragment.SetupSettingsFragment;
import com.htoja.mifik.htoja.fragment.SetupTeamsFragment;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetupActivity extends AppCompatActivity {

    private SetupTeamsFragment setupTeamsFragment;
    private SetupSettingsFragment setupSettingsFragment;
    private NextTeamFragment nextTeamFragment;
    private SetupCategoriesFragment setupCategoriesFragment;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_menu);

        setupTeamsFragment = new SetupTeamsFragment();
        setupSettingsFragment = new SetupSettingsFragment();
        nextTeamFragment = new NextTeamFragment();
        setupCategoriesFragment = new SetupCategoriesFragment();

        showTeamsFragment();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (setupTeamsFragment.isVisible()) {
                    getSupportActionBar().setTitle(R.string.title_menu);
                } else if (setupSettingsFragment.isVisible()) {
                    getSupportActionBar().setTitle(R.string.title_teams);
                } else if (nextTeamFragment.isVisible()) {
                    getSupportActionBar().setTitle(R.string.title_menu);
                } else if (setupCategoriesFragment.isVisible()) {
                    getSupportActionBar().setTitle(R.string.title_options);
                }
            }
        });
    }

    private void showTeamsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, setupTeamsFragment);
        transaction.addToBackStack("Команди");
        transaction.commit();
    }

    public void clickOptions(View view) {
        teams = setupTeamsFragment.getTeams();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, setupSettingsFragment);
        transaction.addToBackStack("Налаштування");
        transaction.commit();
    }

    public void clickGroups(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, setupCategoriesFragment);
        transaction.addToBackStack("Групи");
        transaction.commit();
    }

    public void clickPlay(View view) {
        Intent i = new Intent(this, GameLandscapeActivity.class);
        startActivityForResult(i, RESULT_OK);
    }

    public void clickAdd(View view) {
        setupTeamsFragment.clickAdd(view);
    }

    public void clickStart(View view) {
        List<String> categories = setupCategoriesFragment.getCategories();
        String[] arr = new String[categories.size()];
        arr = categories.toArray(arr);
        List<String> words = Vocabulary.getInstance().getWordsForCategories(arr);
        Collections.shuffle(words, new Random(System.nanoTime()));

        TeamGameManager.getInstance().startNewSet(teams, setupSettingsFragment.getTargetWords(),
                setupSettingsFragment.getSeconds(), setupSettingsFragment.getFine(),
                setupCategoriesFragment.getCategories(), words);
        TeamGameManager.getInstance().firstTeam();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, nextTeamFragment);
        transaction.addToBackStack("Наступна команда");
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (setupTeamsFragment.isVisible() || nextTeamFragment.isVisible()) {
            startMainActivity();
        } else {
            super.onBackPressed();
        }
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

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
