package com.ghostflying.autobatterysaver.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ghostflying.autobatterysaver.R;

/**
 * Created by ghostflying on 3/24/15.
 */
public class ChooseDialogFragment extends BaseAlertDialogFragment {
    // the fragment initialization parameters
    private static final String ARG_TITLE = "title";
    private static final String ARG_ITEMS = "items";
    private static final String ARG_IS_MULTI = "isMulti";

    public static final String ARG_ITEM_CHECKED = "checked";

    private int mTitle;
    private int mItems;
    private int mChecked;
    private boolean[] mCheckedArray;
    private boolean isMulti;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title the resoucce id of the title of the dialog.
     * @param items the resource id of items to choose.
     * @return A new instance of fragment SingleChooseDialogFragment.
     */
    public static ChooseDialogFragment newInstance(int title, int items, int checked) {
        ChooseDialogFragment fragment = new ChooseDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_ITEMS, items);
        args.putInt(ARG_ITEM_CHECKED, checked);
        args.getBoolean(ARG_IS_MULTI, false);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChooseDialogFragment newInstance(int title, int items, boolean[] checked){
        ChooseDialogFragment fragment = new ChooseDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_ITEMS, items);
        args.putBooleanArray(ARG_ITEM_CHECKED, checked);
        args.putBoolean(ARG_IS_MULTI, true);
        fragment.setArguments(args);
        return fragment;
    }

    public ChooseDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getInt(ARG_TITLE);
            mItems = getArguments().getInt(ARG_ITEMS);
            isMulti = getArguments().getBoolean(ARG_IS_MULTI);
            if (isMulti){
                mCheckedArray = getArguments().getBooleanArray(ARG_ITEM_CHECKED);
            }
            else {
                mChecked = getArguments().getInt(ARG_ITEM_CHECKED);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle values = new Bundle();
                        if (isMulti){
                            values.putBooleanArray(ARG_ITEM_CHECKED, mCheckedArray);
                        }
                        else {
                            values.putInt(ARG_ITEM_CHECKED, mChecked);
                        }
                        mListener.onPositiveButtonClick(values, mTitle);
                    }
                })
                .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        if (isMulti){
            builder.setMultiChoiceItems(mItems, mCheckedArray, mOnMultiClickListener);
        }
        else {
            builder.setSingleChoiceItems(mItems, mChecked, onClickListener);
        }

        return builder.create();
    }

    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mChecked = which;
        }
    };

    DialogInterface.OnMultiChoiceClickListener mOnMultiClickListener = new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            mCheckedArray[which] = isChecked;
        }
    };
}
