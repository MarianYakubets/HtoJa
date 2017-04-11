package com.htoja.mifik.htoja.data;

import java.util.LinkedHashMap;
import java.util.List;

public class TeamsSet {
    private boolean fine;
    private final List<String> categories;
    private final List<String> words;
    private List<String> teams;
    private LinkedHashMap<String, Integer> teamResults;

    private int pointsToWin = 20;
    private int roundTime = 60;
    private boolean ended = false;
    private String victorian;

    public TeamsSet(LinkedHashMap<String, Integer> teamResults, List<String> teams, int pointsToWin,
                    int roundTime, boolean fine, List<String> categories, List<String> words) {
        this.teamResults = teamResults;
        this.teams = teams;
        this.pointsToWin = pointsToWin;
        this.roundTime = roundTime;
        this.fine = fine;
        this.categories = categories;
        this.words = words;
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

    public void setVictorian(String victorian) {
        this.victorian = victorian;
    }

    public String getVictorian() {
        return victorian;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    public boolean isFine() {
        return fine;
    }

    public void setFine(boolean fine) {
        this.fine = fine;
    }

    public LinkedHashMap<String, Integer> getTeamResults() {
        return teamResults;
    }

    public void setTeamResults(LinkedHashMap<String, Integer> teamResults) {
        this.teamResults = teamResults;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getWords() {
        return words;
    }
}
