package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.adapter.RouteAdapter;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.customview.FloatingActionButton;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.GetRouteListTask;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnAddRouteListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteListFragment extends ListFragment implements OnAddRouteListener, LoaderManager.LoaderCallbacks<List<Route>> {

    private static final String TAG = "RouteListFragment";

    private ProgressBar progressBar;
    private ArrayList<Route> routes;
    private RouteAdapter adapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.ic_add))
                .withButtonColor(Color.GREEN)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(v -> {
            DialogFragment newRouteFragment = new RouteFragment();
            newRouteFragment.setTargetFragment(this, 1);
            newRouteFragment.show(getFragmentManager(), "newRouteFragment");
        });



    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Route route = (Route) l.getItemAtPosition(position);
        if(route!=null){
            RouteFragment fragment = new RouteFragment();
            fragment.setRoute(route);
            fragment.setTargetFragment(this, 1);
            fragment.show(getFragmentManager(), "editRoute");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        routes = new ArrayList<Route>();
        setEmptyText(getResources().getText(R.string.noDataInList));
        adapter = new RouteAdapter(getActivity());
        setListAdapter(adapter);
        setListShown(false);
        getLoaderManager().initLoader(0,null, this);
    }




    public RouteListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAddRouteListener(Route route) {
        Log.i(TAG,"onAddRouteListener");
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader called.");
        return new GetRouteListTask(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        Log.i(TAG, "onLoadFinished called. data length: " + (data!=null?data.size():"0"));
        adapter.setData(data);
        if(isResumed()){
            setListShown(true);
        }else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {
        adapter.setData(null);
    }
}
