package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.DialogFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnAddRouteListener;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnDateTimeChangeListener;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostRouteTask;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class RouteFragment extends DialogFragment implements View.OnClickListener, OnDateTimeChangeListener {


    public RouteFragment() {
        // Required empty public constructor
    }

    public static final String TAG = "RouteDialogFragment";
    private EditText fromRoute;
    private EditText toRoute;
    private EditText dateTimeEditText;
    private CheckBox isDoneChB;
    private Switch switchDriver;
    private ImageView imageView;
    private Calendar mCalendar;

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
        mCalendar = Calendar.getInstance();
        return inflater.inflate(R.layout.fragment_route, container, false);

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
            dateTimeEditText = view.findViewById(R.id.setDateTimeText);
            isDoneChB = view.findViewById(R.id.isDone);
            switchDriver = view.findViewById(R.id.switchButton);
            imageView = view.findViewById(R.id.userTypeImage);
            if(route!=null){
                fromRoute.setText(route.getFromRoute());
                toRoute.setText(route.getToRoute());
                mCalendar.setTimeInMillis(route.getStartDateTime());
                dateTimeEditText.setText(new SimpleDateFormat("dd:MM:yyyy HH:mm").format(mCalendar.getTime()));
                isDoneChB.setChecked(!route.getActive());
                switchDriver.setChecked(route.getDriver());
                switchHelper(switchDriver.isChecked());
            }
            dateTimeEditText.setOnClickListener(this);
            switchDriver.setOnClickListener(this);
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
                route.setStartDateTime(mCalendar.getTimeInMillis());
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
                this.dismiss();
                break;
            }
            case R.id.cancelButton: {
                this.dismiss();
                break;
            }
            case R.id.switchButton:{
                switchHelper(switchDriver.isChecked());
                break;
            }
            case R.id.setDateTimeText:{
                DateTimeEditDialogFragment dateTimeEditDialogFragment = new DateTimeEditDialogFragment(Calendar.getInstance().getTimeInMillis());
                dateTimeEditDialogFragment.setTargetFragment(getFragmentManager().findFragmentByTag("newRouteFragment"), 0);
                dateTimeEditDialogFragment.show(getFragmentManager(),"dateTimeDialogFragment");
                break;
            }
        }

    }

    @Override
    public void onDateTimeChange(Calendar calendar) {
        mCalendar = calendar;
        dateTimeEditText.setText(new SimpleDateFormat("dd:MM:yyyy HH:mm").format(mCalendar.getTime()));
    }

    private void switchHelper(boolean selected){
        if(selected){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_drive_eta, getActivity().getTheme()));
            switchDriver.setText(getResources().getText(R.string.switchLabelCarDriver));
        }else{
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_directions_run, getActivity().getTheme()));
            switchDriver.setText(getResources().getText(R.string.switchLabelPedestrian));
        }
    }
}
