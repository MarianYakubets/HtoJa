package com.htoja.mifik.htoja.utils;

/**
 * Created by mi on 3/2/2017.
 */
public class Storage {
    private static final String LAST_TEAM_SESSION = "lastTeamSession";


    public static void saveCurrentTeamState(){
        AppStateHolder.getInstance().getCurrentSet();
    }

    public static void restoreCurrentTeamState(){
        AppStateHolder.getInstance().getCurrentSet();
    }
}
