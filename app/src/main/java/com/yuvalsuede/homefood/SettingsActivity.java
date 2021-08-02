package com.yuvalsuede.homefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.yuvalsuede.homefood.fragment.HomeFoodPreferenceFragment;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getName();
    final public static int LOGOUT_CODE_DONE = 909;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App app = (App) getApplication();
        if (app.needRestart()) {
            finish();
            startActivity(new Intent(this, SplashActivity.class));
        }
        setTitle(getString(R.string.settings));
        getFragmentManager().beginTransaction().replace(android.R.id.content, new HomeFoodPreferenceFragment()).commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void logoutActions() {
        LoginManager.getInstance().logOut();
        SharedPreferences preferences = getSharedPreferences();
        preferences.edit()
                .remove("userModel")
                .apply();
        setResult(RESULT_OK);
        finish();
    }
}
