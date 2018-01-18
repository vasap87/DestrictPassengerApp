package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnDateTimeChangeListener;

/**
 * Created by alexkotov on 18.01.18.
 */

public class DateTimeEditDialogFragment extends DialogFragment implements View.OnClickListener{

    private OnDateTimeChangeListener listener;

    private EditText dateEdit;
    private EditText timeEdit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.editDateTimeDialogTitle);
        View view = inflater.inflate(R.layout.date_time_picker, container);
        dateEdit = view.findViewById(R.id.dateEditText);
        timeEdit = view.findViewById(R.id.timeEditView);
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
        Date date = new Date();
        dateEdit.setText(dateFormat.format(date));
        timeEdit.setText(timeFormat.format(date));
        Button saveButton = view.findViewById(R.id.saveDateTimeButton);
        saveButton.setOnClickListener(this);
        Button cancelButton = view.findViewById(R.id.cancelDateTimeButton);
        cancelButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (OnDateTimeChangeListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement OnDateTimeChangeListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveDateTimeButton:{

            }
            case R.id.cancelDateTimeButton:{

            }
        }
    }
}
