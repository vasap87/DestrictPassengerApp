package ru.alexandrkotovfrombutovo.destrictpassengerapp.customview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by alexkotov on 30.01.18.
 */

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private static final String androidns = "http://schemas.android.com/apk/res/android";

    private TextView mSeekLabel, mSplashText;
    private SeekBar mSeekBar;
    private Context mContext;

    private String mDialogMessage, mSuffix;
    private int mDefault, mMin, mMax, mCurrentValue = 0;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        mSuffix = attrs.getAttributeValue(androidns, "text");
        mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        mMin = attrs.getAttributeIntValue(androidns, "minLevel",0);
        mMax = attrs.getAttributeIntValue(androidns, "maxLevel", 60);
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);

        if(mDialogMessage!=null){
            mSplashText = new TextView(mContext);
            mSplashText.setText(mDialogMessage);
            layout.addView(mSplashText);
        }

        mSeekLabel = new TextView(mContext);
        mSeekLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        mSeekLabel.setTextSize(24);
        layout.addView(mSeekLabel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if(shouldPersist()){
            mCurrentValue = getPersistedInt(mDefault);
        }

        mSeekBar.setMax(mMax - mMin);
        mSeekBar.setProgress(mCurrentValue - mMin);

        mSeekLabel.setText(mSuffix == null?String.valueOf(mCurrentValue):String.valueOf(mCurrentValue).concat(mSuffix));

        return layout;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mSeekBar.setMax(mMax-mMin);
        mSeekBar.setProgress(mCurrentValue - mMin);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue,defaultValue);
        if(restorePersistedValue){
            mCurrentValue = shouldPersist()? getPersistedInt(mDefault): 0;
        }else{
            mCurrentValue = (int) defaultValue;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress+=mMin;
        mSeekLabel.setText(mSuffix == null?String.valueOf(progress):String.valueOf(progress).concat(mSuffix));
        if(shouldPersist()){
            persistInt(progress);
        }
        callChangeListener(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        this.mMin = min;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public void setProgress(int progress) {
        mCurrentValue = progress;
        if (mSeekBar != null)
            mSeekBar.setProgress(progress);
    }

    public int getProgress() {
        return mCurrentValue;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if(isPersistent()){
            return superState;
        }
        final SavedState myState = new SavedState(superState);
        myState.value = mCurrentValue;
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
        mCurrentValue = myState.value;
        mSeekLabel.setText(String.valueOf(mCurrentValue));
        mSeekBar.setProgress(mCurrentValue);
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
