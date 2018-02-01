package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.activities.DetailActivity;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.activities.MainActivity;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.adapter.RouteAdapter;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.customview.FloatingActionButton;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.GetRouteListTask;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.JsonUtil;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnAddRouteListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteListFragment extends SwipeRefreshListFragment implements OnAddRouteListener, LoaderManager.LoaderCallbacks<List<Route>> {

    public static final String TAG = "RouteListFragment";
    public static final String EXTRA_USER = "CurrentUser";
    public static final String EXTRA_ROUTE = "CurrentRoute";
    private static final int REQUEST_CHANGE_ROUTE = 1;

    private RouteAdapter adapter;
    private UserInfo mCurrentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.ic_add))
                .withButtonColor(ContextCompat.getColor(getActivity(), R.color.floating_action_button_color))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(v -> {
            Intent routeIntent = new Intent(getActivity(), DetailActivity.class);
            routeIntent.putExtra(EXTRA_USER, mCurrentUser);
            startActivityForResult(routeIntent, REQUEST_CHANGE_ROUTE);
        });
        setEmptyText(getResources().getText(R.string.noDataInList));
        adapter = new RouteAdapter(getActivity());
        setListAdapter(adapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);

        setColorScheme(R.color.refres_status_color);
        setOnRefreshListener(() -> initRefresh());
    }

    private void initRefresh() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Route route = (Route) l.getItemAtPosition(position);
        if (route != null) {
            if (route.getUser().getUuid().equals(mCurrentUser.getUuid())) {
                Intent routeIntent = new Intent(getActivity(), DetailActivity.class);
                routeIntent.putExtra(EXTRA_ROUTE, route);
                startActivityForResult(routeIntent, REQUEST_CHANGE_ROUTE);
            } else {
                RoutePreviewDialogFragment fragment = new RoutePreviewDialogFragment();
                fragment.setRoute(route);
                fragment.show(getActivity().getFragmentManager(), TAG);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }


    public RouteListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAddRouteListener(Route route) {
        Log.i(TAG, "onAddRouteListener");
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader called.");
        return new GetRouteListTask(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        Log.i(TAG, "onLoadFinished called. data length: " + (data != null ? data.size() : "0"));
        adapter.setData(data);
        adapter.sort((o1, o2) -> Long.compare(o1.getStartDateTime(), o2.getStartDateTime()));
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        try {
            mCurrentUser = JsonUtil.getInstance().deserialization(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.USER_JSON, null), UserInfo.class);
        } catch (IOException e) {
            Log.d(TAG, "method onLoadFinished deserialization from property, exception is: " + e.getMessage());
        }
        setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {
        adapter.setData(null);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mCurrentUser = userInfo;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHANGE_ROUTE) {
            initRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initRefresh();
    }
}
