package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
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
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnAddRouteListener;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnDateTimeChangeListener;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostRouteTask;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class RouteFragment extends Fragment implements View.OnClickListener, OnDateTimeChangeListener {


    public RouteFragment() {
        // Required empty public constructor
    }

    public static final String TAG = "RouteDialogFragment";
    private EditText mFromRoute;
    private EditText mToRoute;
    private EditText mDateTimeEditText;
    private CheckBox mIsDoneChB;
    private Switch mSwitchDriver;
    private ImageView mImageView;
    private Calendar mCalendar;
    private Route mRoute;
    private UserInfo mCurrentUser;
    private boolean isNewRoute = false;

    //private OnAddRouteListener targetListener;
    public void setRoute(Route mRoute) {
        this.mRoute = mRoute;
    }

    public void setCurrentUser(UserInfo mCurrentUser) {
        this.mCurrentUser = mCurrentUser;
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
            mFromRoute = view.findViewById(R.id.fromRouteText);
            mToRoute = view.findViewById(R.id.toRouteText);
            mDateTimeEditText = view.findViewById(R.id.setDateTimeText);
            mIsDoneChB = view.findViewById(R.id.isDoneChb);
            mSwitchDriver = view.findViewById(R.id.switchButton);
            mImageView = view.findViewById(R.id.userTypeImage);
            if(mRoute !=null){
                mFromRoute.setText(mRoute.getFromRoute());
                mToRoute.setText(mRoute.getToRoute());
                mCalendar.setTimeInMillis(mRoute.getStartDateTime());
                mDateTimeEditText.setText(DateFormat.getInstance().format(mCalendar.getTime()));
                mIsDoneChB.setChecked(!mRoute.getActive());
                mSwitchDriver.setChecked(mRoute.getDriver());
                switchHelper(mSwitchDriver.isChecked());
            }
            mDateTimeEditText.setOnClickListener(this);
            mSwitchDriver.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.saveButton: {
                postRoute();
                getActivity().setResult(RESULT_OK, new Intent());
                getActivity().finish();
                break;
            }
            case R.id.cancelButton: {
                getActivity().finish();
                break;
            }
            case R.id.switchButton:{
                switchHelper(mSwitchDriver.isChecked());
                break;
            }
            case R.id.setDateTimeText:{
                DateTimeEditDialogFragment dateTimeEditDialogFragment = new DateTimeEditDialogFragment(mCalendar);
                dateTimeEditDialogFragment.setTargetFragment(this, 0);
                dateTimeEditDialogFragment.show(getFragmentManager(),"dateTimeDialogFragment");
                break;
            }
        }

    }


    @Override
    public void onDateTimeChange(Calendar calendar) {
        mCalendar = calendar;
        mDateTimeEditText.setText(DateFormat.getInstance().format(mCalendar.getTime()));
    }

    private void switchHelper(boolean selected){
        if(selected){
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_drive_eta, getActivity().getTheme()));
            mSwitchDriver.setText(getResources().getText(R.string.switchLabelCarDriver));
        }else{
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_directions_run, getActivity().getTheme()));
            mSwitchDriver.setText(getResources().getText(R.string.switchLabelPedestrian));
        }
    }

    public void postRoute(){
        if(mRoute ==null) {
            isNewRoute = true;
            mRoute = new Route();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mRoute.setLocation(Integer.valueOf(preferences.getString("location", "0")));
        }
        mRoute.setActive(!mIsDoneChB.isChecked());
        mRoute.setFromRoute(String.valueOf(mFromRoute.getText()));
        mRoute.setToRoute(String.valueOf(mToRoute.getText()));
        if (mRoute.getUser() == null ) mRoute.setUser(mCurrentUser);
        mRoute.setStartDateTime(mCalendar.getTimeInMillis());
        mRoute.setDriver(mSwitchDriver.isChecked());
        PostRouteTask task = new PostRouteTask(getActivity());
        task.execute(mRoute);
        try {
            ResponseEntity<Route> routeResponseEntity = task.get();
            if (routeResponseEntity != null) {
                Route result = routeResponseEntity.getBody();
                if (result != null) {
                    Toast.makeText(getActivity(), isNewRoute?"Route added":"Route changed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Network problem :(", Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            Toast.makeText(getActivity(), "Interrupted: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(getActivity(), "Not added: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
