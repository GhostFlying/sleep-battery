package com.ghostflying.autobatterysaver.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import com.ghostflying.autobatterysaver.model.Time;


public abstract class BaseTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimePickerDialogInteraction mListener;

    public BaseTimePickerDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        try{
            mListener = (TimePickerDialogInteraction)getActivity();
        }
        catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()
                + " must implement TimePickerDialogInteraction");
        }
        Time setTime = getTimeFromSetting();
        return new TimePickerDialog(
                getActivity(),
                this,
                setTime.getHour(),
                setTime.getMinute(),
                false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Time time = new Time(hourOfDay, minute);
        setTimeToSetting(time);
        mListener.onTimeSet(time);
    }

    abstract Time getTimeFromSetting();

    abstract void setTimeToSetting(Time time);

    public interface TimePickerDialogInteraction{
        public void onTimeSet(Time time);
    }
}
