package ru.alexandrkotovfrombutovo.destrictpassengerapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.core.JsonProcessingException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.SeekBarPreference;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.JsonUtil;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostUserInfoTask;

import java.io.IOException;

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

    private SettingsFragment mFragment;
    private UserInfo mUserInfo;
    private SharedPreferences mPreferences;
    private final static String TAG = "SettingsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        mFragment = new SettingsFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, mFragment)
                .commit();
        getFragmentManager().executePendingTransactions();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            mUserInfo = JsonUtil.getInstance().deserialization(mPreferences.getString(MainActivity.USER_JSON, null), UserInfo.class);
            if (mUserInfo != null) {
                Preference userName = mFragment.findPreference("user_name");
                if (mUserInfo.getName() != null) {
                    userName.setSummary(mUserInfo.getName());
                }
                userName.setOnPreferenceChangeListener((preference, newValue) -> {
                    userName.setSummary((CharSequence) newValue);
                    mUserInfo.setName(String.valueOf(newValue));
                    return true;
                });
                Preference userPhone = mFragment.findPreference("user_phone");
                userPhone.setSummary(mUserInfo.getPhone());
                userPhone.setSelectable(false);
            }
        } catch (IOException e) {
            Log.d(TAG, e.fillInStackTrace().toString());
        }

        Preference duration = mFragment.findPreference("minuteToHideRow");
        duration.setOnPreferenceChangeListener((preference, newValue) -> {
            duration.setSummary("Hide row after "+ newValue + " min");
            return true;
        });
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
        postUserInfo();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                postUserInfo();
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setupActionBar();
        return true;
    }

    private void postUserInfo() {
        SharedPreferences.Editor editor = mPreferences.edit();
        try {
            editor.putString(MainActivity.USER_JSON, JsonUtil.getInstance().serialization(mUserInfo));
        } catch (JsonProcessingException e) {
            Log.d(TAG, e.fillInStackTrace().toString());
        }
        editor.apply();
        //update entity on server
        new PostUserInfoTask(this).execute(mUserInfo);
    }
}
