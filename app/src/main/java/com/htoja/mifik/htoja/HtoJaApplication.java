package com.htoja.mifik.htoja;

import android.support.multidex.MultiDexApplication;

import com.htoja.mifik.htoja.utils.Storage;

/**
 * Created by marian on 26.03.17.
 */

public class HtoJaApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Storage.restoreCurrentTeamState(getBaseContext());
    }
}
