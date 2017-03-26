package com.htoja.mifik.htoja.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.data.TeamsSet;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mi on 3/2/2017.
 */
public class Storage {
    private static final String LAST_TEAM_SESSION = "lastTeamSession";
    private static final String TEAM_SET = "TEAM_SET";
    private static final String TEAM = "team";
    private static final String ROUND = "round";


    public static void saveCurrentTeamState(Context ctx) {
        TeamGameManager manager = TeamGameManager.getInstance();
        SharedPreferences preferences = ctx.getSharedPreferences(LAST_TEAM_SESSION, MODE_PRIVATE);
        if (manager.hasStarted()) {
            SharedPreferences.Editor edit = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(manager.getCurrentTeamSet());
            edit.putString(TEAM_SET, json);
            edit.putString(TEAM, manager.getCurrentTeam());
            edit.putInt(ROUND, manager.getRound());
            edit.apply();
        } else {
            preferences.edit().clear().apply();
        }
    }

    public static void restoreCurrentTeamState(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(LAST_TEAM_SESSION, MODE_PRIVATE);
        if (preferences != null && preferences.contains(TEAM_SET)) {
            Gson gson = new Gson();
            String json = preferences.getString(TEAM_SET, "");
            TeamsSet set = gson.fromJson(json, TeamsSet.class);
            String team = preferences.getString(TEAM, "");
            int round = preferences.getInt(ROUND, 1);
            TeamGameManager manager = TeamGameManager.getInstance();
            manager.startNewSet(set, team, round);
        }
    }
}
