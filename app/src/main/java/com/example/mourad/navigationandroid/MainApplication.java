package com.example.mourad.navigationandroid;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;

/**
 * Created by abdelle on 3/17/18.
 */

public class MainApplication extends MultiDexApplication {
    LocalizationApplicationDelegate localizationDelegate = new LocalizationApplicationDelegate(this);

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localizationDelegate.onConfigurationChanged(this);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }
}
