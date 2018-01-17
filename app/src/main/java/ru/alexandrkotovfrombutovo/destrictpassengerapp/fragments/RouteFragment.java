package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnAddRouteListener;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostRouteTask;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class RouteFragment extends DialogFragment implements View.OnClickListener {


    public RouteFragment() {
        // Required empty public constructor
    }

    public static final String TAG = "RouteDialogFragment";
    private EditText fromRoute;
    private EditText toRoute;
    private EditText timePicker;
    private CheckBox isDoneChB;
    private Switch switchDriver;

    private Route route;

    private OnAddRouteListener targetListener;
    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"onCreateView");
        return inflater.inflate(R.layout.fragment_new_route, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        try {
            targetListener = (OnAddRouteListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
        View view = getView();
        if (view != null) {
            Button saveButton = view.findViewById(R.id.saveButton);
            Button cancelButton = view.findViewById(R.id.cancelButton);
            saveButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
            fromRoute = view.findViewById(R.id.fromRouteText);
            toRoute = view.findViewById(R.id.toRouteText);
            timePicker = view.findViewById(R.id.timePicker);
            isDoneChB = view.findViewById(R.id.isDone);
            switchDriver = view.findViewById(R.id.switchButton);
            if(route!=null){
                fromRoute.setText(route.getFromRoute());
                toRoute.setText(route.getToRoute());
                timePicker.setText(route.getStartDate());
                isDoneChB.setChecked(!route.getActive());
                switchDriver.setChecked(route.getDriver());
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.saveButton: {
                if(route==null) {
                    route = new Route();
                }
                route.setActive(!isDoneChB.isChecked());
                route.setFromRoute(String.valueOf(fromRoute.getText()));
                route.setToRoute(String.valueOf(toRoute.getText()));
                if (route.getUserUuid() == null ) route.setUserUuid(UUID.randomUUID().toString());
                route.setStartDate(String.valueOf(timePicker.getText()));
                route.setDriver(switchDriver.isChecked());
                PostRouteTask task = new PostRouteTask();
                task.execute(route);
                try {
                    ResponseEntity<Route> routeResponseEntity = task.get();
                    if (routeResponseEntity != null) {
                        Route result = routeResponseEntity.getBody();
                        if (result != null) {
                            targetListener.onAddRouteListener(result);
                            Toast.makeText(getActivity(), "Route added", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Network problem :(", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(), "Interrupted: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ExecutionException e) {
                    Toast.makeText(getActivity(), "Not added: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.cancelButton: {
                break;
            }
        }
        this.dismiss();
    }
}
