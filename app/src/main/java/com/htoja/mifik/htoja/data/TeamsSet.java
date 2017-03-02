package com.htoja.mifik.htoja.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TeamsSet {
    private HashMap<String, Integer> teamResults = new LinkedHashMap<>();
    private int pointsToWin = 20;
    private int roundTime = 60;
    private boolean ended = false;
    private List<String> categories = new ArrayList<>();

    public TeamsSet() {
    }

    public TeamsSet(HashMap<String, Integer> teamResults, int pointsToWin, int roundTime) {
        this.teamResults = teamResults;
        this.pointsToWin = pointsToWin;
        this.roundTime = roundTime;
    }

    public TeamsSet(HashMap<String, Integer> teamResults) {
        this.teamResults = teamResults;
    }

    public HashMap<String, Integer> getTeamResults() {
        return teamResults;
    }

    public void setTeamResults(HashMap<String, Integer> teamResults) {
        this.teamResults = teamResults;
    }

    public int getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(int pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
