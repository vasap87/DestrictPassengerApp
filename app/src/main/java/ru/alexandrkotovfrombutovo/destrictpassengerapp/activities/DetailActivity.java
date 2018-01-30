package ru.alexandrkotovfrombutovo.destrictpassengerapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.RouteFragment;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.RouteListFragment;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;

public class DetailActivity extends Activity {

    private RouteFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        setupActionBar();
        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra(RouteListFragment.EXTRA_USER);
        Route route = (Route) getIntent().getSerializableExtra(RouteListFragment.EXTRA_ROUTE);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment = new RouteFragment();
        fragment.setCurrentUser(userInfo);
        fragment.setRoute(route);
        transaction.replace(R.id.detail_content_frame, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setupActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
