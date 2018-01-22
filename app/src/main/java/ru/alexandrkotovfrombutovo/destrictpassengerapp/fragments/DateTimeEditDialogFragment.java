package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.OnDateTimeChangeListener;

/**
 * Created by alexkotov on 18.01.18.
 */

@SuppressLint("ValidFragment")
public class DateTimeEditDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "DTEditDialogFragment";
    private OnDateTimeChangeListener mListener;
    private EditText mDateText;
    private EditText mTimeText;
    private Calendar mCalendar;


    @SuppressLint("ValidFragment")
    public DateTimeEditDialogFragment(Calendar calendar) {
        mCalendar = calendar;
    }

    public DateTimeEditDialogFragment (){

    }


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.editDateTimeDialogTitle);
        View view = inflater.inflate(R.layout.date_time_picker, container);
        Button saveButton = view.findViewById(R.id.saveDateTimeButton);
        saveButton.setOnClickListener(this);
        Button cancelButton = view.findViewById(R.id.cancelDateTimeButton);
        cancelButton.setOnClickListener(this);
        mDateText = view.findViewById(R.id.dateText);
        setDateInEditText();
        mDateText.setOnClickListener(this);
        mTimeText = view.findViewById(R.id.timeText);
        setTimeInEditText();
        mTimeText.setOnClickListener(this);
        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        try {
            mListener = (OnDateTimeChangeListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement OnDateTimeChangeListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveDateTimeButton:{
                mListener.onDateTimeChange(mCalendar);
                this.dismiss();
                break;
            }
            case R.id.cancelDateTimeButton:{
                this.dismiss();
                break;
            }
            case R.id.dateText:{
                new DatePickerDialog(getActivity(),dateSetListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            }
            case R.id.timeText:{
                new TimePickerDialog(getActivity(), timeSetListener,
                        mCalendar.get(Calendar.HOUR),
                        mCalendar.get(Calendar.MINUTE),
                        true)
                        .show();
                break;
            }
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
        mCalendar.set(year,month,dayOfMonth);
        setDateInEditText();
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
        mCalendar.set(Calendar.HOUR,hourOfDay);
        mCalendar.set(Calendar.MINUTE,minute);
        setTimeInEditText();
    };

    private void setDateInEditText() {
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        mDateText.setText(format.format(mCalendar.getTime()));
    }

    private void setTimeInEditText() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        mTimeText.setText(format.format(mCalendar.getTime()));
    }
}
