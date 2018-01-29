package ru.alexandrkotovfrombutovo.destrictpassengerapp.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.RouteFragment;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments.RouteListFragment;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra(RouteListFragment.EXTRA_USER);
        Route route = (Route) getIntent().getSerializableExtra(RouteListFragment.EXTRA_ROUTE);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        RouteFragment fragment = new RouteFragment();
        fragment.setCurrentUser(userInfo);
        fragment.setRoute(route);
        transaction.replace(R.id.detail_content_frame, fragment).commit();
    }
}
