package ru.alexandrkotovfrombutovo.destrictpassengerapp.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.UUID;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.RegistrationFragment;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.RouteListFragment;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.JsonUtil;


public class MainActivity extends Activity {

    public static final String IS_REGISTER_TAG = "IS_REGISTER_TAG";
    public static final String USER_JSON = "USER_JSON";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRegister = preferences.getBoolean(IS_REGISTER_TAG,false);
        if(isRegister){
            try {
                UserInfo userInfo = JsonUtil.getInstance().deserialization(preferences.getString(USER_JSON, null), UserInfo.class);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RouteListFragment top = new RouteListFragment();
                top.setUserInfo(userInfo);
                transaction.replace(R.id.content_frame, top);
                transaction.commit();
            } catch (IOException e) {
                Log.d(TAG, "read from property JSON userInfo entity, exception is: "+ e.fillInStackTrace());
            }
        }else{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            registrationFragment.setUserUuid(UUID.randomUUID().toString());
            transaction.replace(R.id.content_frame, registrationFragment);
            transaction.commit();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:{
                item.setIntent(new Intent(this, SettingsActivity.class));
                break;
            }
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
