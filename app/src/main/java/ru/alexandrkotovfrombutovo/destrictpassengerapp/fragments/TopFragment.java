package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Collections;
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
public class TopFragment extends ListFragment implements OnAddRouteListener {

    private static final String TAG = "TopFragment";

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
        adapter = new RouteAdapter(getActivity(), routes);
        setListAdapter(adapter);
        fillList();
    }




    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG,"onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


    private void fillList(){
        GetRouteListTask task = new GetRouteListTask();
        task.execute();
        try {
            Route [] routesArr = task.get().getBody();
            routes.clear();
            adapter.notifyDataSetChanged();
            Collections.addAll(routes, routesArr);
            adapter.notifyDataSetChanged();
            getListView().requestLayout();
            //adapter.addAll(routes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddRouteListener(Route route) {
        Log.i(TAG,"onAddRouteListener");
        fillList();
    }
}
