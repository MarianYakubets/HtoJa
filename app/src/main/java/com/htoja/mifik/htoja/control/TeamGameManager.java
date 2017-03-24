package com.htoja.mifik.htoja.control;

import com.htoja.mifik.htoja.data.TeamsSet;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TeamGameManager {
    private static TeamGameManager ourInstance = new TeamGameManager();

    private TeamsSet currentSet;
    private String currentTeam;
    private List<String> teams;
    private int rounds = 1;

    public static TeamGameManager getInstance() {
        return ourInstance;
    }

    private TeamGameManager() {
    }

    public void startNewSet(List<String> teams, int targetWords, int seconds, boolean fine) {
        this.teams = teams;
        this.currentTeam = teams.get(0);

        HashMap<String, Integer> teamMap = new LinkedHashMap<>(teams.size());
        for (String team : teams) {
            teamMap.put(team, 0);
        }
        this.currentSet = new TeamsSet(teamMap, targetWords, seconds, fine);
    }

    public String getCurrentTeam() {
        return this.currentTeam;
    }

    public int getRoundTime() {
        return currentSet.getRoundTime();
    }

    public int getPointsToWin() {
        return currentSet.getPointsToWin();
    }

    public String firstTeam() {
        currentTeam = teams.get(0);
        return currentTeam;
    }

    public void nextTeam() {
        int i = teams.indexOf(currentTeam);
        i++;
        if (i >= teams.size()) {
            nextRound();
            i = 0;
        }
        currentTeam = teams.get(i);
    }

    private void nextRound() {
        for (Map.Entry<String, Integer> entry : getTeamResults().entrySet()) {
            if (entry.getValue() >= currentSet.getPointsToWin()) {
                currentSet.setEnded(true);
                currentSet.setVictorian(entry.getKey());
                return;
            }
        }
        rounds++;
    }

    public String getVictorian() {
        return currentSet.getVictorian();
    }

    public int getRound() {
        return this.rounds;
    }

    public void addCurrentTeamPoints(int points) {
        int currentPoints = currentSet.getTeamResults().get(currentTeam);
        currentSet.getTeamResults().put(currentTeam, currentPoints + points);
    }

    public boolean hasStarted() {
        return currentSet != null;
    }

    public boolean hasEnded() {
        return currentSet.isEnded();
    }

    public boolean hasFine() {
        return currentSet.hasFine();
    }

    public Map<String, Integer> getTeamResults() {
        return currentSet.getTeamResults();
    }
}
