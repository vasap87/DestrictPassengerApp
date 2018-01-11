package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.DialogFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostRouteTask;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class RouteFragment extends DialogFragment implements View.OnClickListener {


    public RouteFragment() {
        // Required empty public constructor
    }

    private EditText fromRoute;
    private EditText toRoute;
    private EditText timePicker;
    private CheckBox isDoneChB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_route, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
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

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton: {
                Route route = new Route();
                route.setActive(!isDoneChB.isChecked());
                route.setFromRoute(String.valueOf(fromRoute.getText()));
                route.setToRoute(String.valueOf(toRoute.getText()));
                route.setUserUuid(UUID.randomUUID().toString());
                route.setStartDate(String.valueOf(timePicker.getText()));
                PostRouteTask task = new PostRouteTask();
                task.execute(route);
                try {
                    ResponseEntity<Route> routeResponseEntity = task.get();
                    if (routeResponseEntity != null) {
                        Route result = routeResponseEntity.getBody();
                        if (result != null) {
                            Toast.makeText(getActivity(), "Route added", Toast.LENGTH_LONG).show();
                            this.dismiss();
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
                this.dismiss();
                break;
            }
            default:
                break;
        }
    }
}
