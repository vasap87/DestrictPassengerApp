package ru.alexandrkotovfrombutovo.destrictpassengerapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by alexkotov on 30.01.18.
 */

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private TextView mSeekLabel;
    private SeekBar mSeekBar;
    private int mNewValue;
    private int mCurrentValue;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.durationpicker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        mSeekBar = view.findViewById(R.id.seekBar);
        mSeekLabel = view.findViewById(R.id.seekBarValue);
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            persistInt(mNewValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue){
            mCurrentValue = this.getPersistedInt(5);
        }else{
            mCurrentValue = (int) defaultValue;
        }
        persistInt(mCurrentValue);
//        mSeekLabel.setText(String.valueOf(mCurrentValue));
//        mSeekBar.setProgress(mCurrentValue);
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 5);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mNewValue = mSeekBar.getProgress();
        mSeekLabel.setText(String.valueOf(mNewValue));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if(isPersistent()){
            return superState;
        }
        final SavedState myState = new SavedState(superState);
        myState.value = mNewValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state == null || !state.getClass().equals(SavedState.class)){
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mNewValue = myState.value;
        mSeekLabel.setText(String.valueOf(mNewValue));
        mSeekBar.setProgress(mNewValue);
    }

    private static class SavedState extends BaseSavedState{

        int value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            this.value = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[0];
            }
        };
    }
}
