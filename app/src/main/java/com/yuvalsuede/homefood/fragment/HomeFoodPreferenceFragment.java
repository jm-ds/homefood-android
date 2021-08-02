package com.yuvalsuede.homefood.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.yuvalsuede.homefood.App;
import com.yuvalsuede.homefood.R;
import com.yuvalsuede.homefood.SettingsActivity;
import com.yuvalsuede.homefood.TermsOfUseActivity;

public class HomeFoodPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private static final String TAG = HomeFoodPreferenceFragment.class.getName();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final App app = (App) getActivity().getApplication();
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences preferences = getSharedPreferences();
        Preference terms = findPreference("terms");
        terms.setOnPreferenceClickListener(this);
        Preference logoutPref = findPreference("logout");
        if (app.getUserModel() == null) {
            PreferenceScreen screen = getPreferenceScreen();
            Preference scLogoutPref = screen.findPreference("logout");
            screen.removePreference(scLogoutPref);
        }
        logoutPref.setOnPreferenceClickListener(this);
        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("currency") ||
                        key.equals("distance") ||
                        key.equals("push") ||
                        key.equals("weight") ||
                        key.equals("lang")) {
                    app.saveConfig();
                    Toast.makeText(getActivity(), R.string.restart, Toast.LENGTH_SHORT).show();
                }
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "terms":
                Intent intent = new Intent(getActivity(), TermsOfUseActivity.class);
                startActivity(intent);
                break;
            case "logout":
                ((SettingsActivity) getActivity()).logoutActions();
                break;
        }
        return false;
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
}
