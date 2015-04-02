package com.ghostflying.sleepbattery.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by ghostflying on 3/24/15.
 */
public abstract class BaseAlertDialogFragment extends DialogFragment {

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onPositiveButtonClick(Bundle value, int title);
        public void onNegativeButtonClick(Bundle value, int title);
    }

}
