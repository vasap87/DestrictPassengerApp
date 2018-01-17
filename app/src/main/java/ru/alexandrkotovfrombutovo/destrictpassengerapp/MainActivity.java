package ru.alexandrkotovfrombutovo.destrictpassengerapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.TopFragment;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TopFragment top = new TopFragment();
        transaction.add(R.id.content_frame, top);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider)shareItem.getActionProvider();
        return true;
    }


    public void doShare(Intent intent){
        shareActionProvider.setShareIntent(intent);
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
