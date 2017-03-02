package com.htoja.mifik.htoja.utils;

import com.htoja.mifik.htoja.data.TeamsSet;

/**
 * Created by mi on 3/2/2017.
 */
public class AppStateHolder {

    private static AppStateHolder ourInstance = new AppStateHolder();

    public static AppStateHolder getInstance() {
        return ourInstance;
    }

    private AppStateHolder() {
    }

    private TeamsSet currentSet;

    public TeamsSet getCurrentSet() {
        return currentSet;
    }

    public void setCurrentSet(TeamsSet currentSet) {
        this.currentSet = currentSet;
    }
}
