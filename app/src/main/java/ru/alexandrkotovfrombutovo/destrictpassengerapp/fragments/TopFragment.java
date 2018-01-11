package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends Fragment implements View.OnClickListener {


    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view!=null){
            FloatingActionButton addRouteButton = (FloatingActionButton) view.findViewById(R.id.addRouteButton);
            addRouteButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addRouteButton:{
                DialogFragment newRouteFragment = new RouteFragment();
                newRouteFragment.show(getFragmentManager(), "newRouteFragment");
                break;
            }
            default:
                break;
        }
    }
}
