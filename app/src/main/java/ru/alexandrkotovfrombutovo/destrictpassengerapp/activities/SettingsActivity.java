package ru.alexandrkotovfrombutovo.destrictpassengerapp.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.JsonUtil;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostUserInfoTask;

import java.io.IOException;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    private SettingsFragment fragment;
    private SharedPreferences preferences;
    private final static String TAG = "SettingsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        fragment = new SettingsFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
        getFragmentManager().executePendingTransactions();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            UserInfo userInfo = JsonUtil.getInstance().deserialization(preferences.getString(MainActivity.USER_JSON, null), UserInfo.class);
            if (userInfo != null) {
                Preference userName = fragment.findPreference("user_name");
                userName.setSummary(userInfo.getName());
                Preference userPhone = fragment.findPreference("user_phone");
                userPhone.setSummary(userInfo.getPhone());
            }
        } catch (IOException e) {
            Log.d(TAG, e.fillInStackTrace().toString());
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_root);
            setHasOptionsMenu(true);

        }

    }

    @Override
    public void onBackPressed() {
        try {
            UserInfo userInfo = JsonUtil.getInstance().deserialization(preferences.getString(MainActivity.USER_JSON, null), UserInfo.class);
            if (userInfo != null) {
                userInfo.setName(preferences.getString("user_name", null));
            }
            //update entity on server
            PostUserInfoTask task = new PostUserInfoTask();
            task.execute(userInfo);
        } catch (IOException e) {
            Log.d(TAG, e.fillInStackTrace().toString());
        }
    }
}
