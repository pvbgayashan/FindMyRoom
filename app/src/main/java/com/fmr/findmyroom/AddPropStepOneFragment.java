package com.fmr.findmyroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddPropStepOneFragment extends Fragment implements View.OnClickListener {

    private EditText propNameTxt, propPriceTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prop_step_one, container, false);

        // init input items
        propNameTxt = view.findViewById(R.id.addPropNameTxt);
        propPriceTxt = view.findViewById(R.id.addPropPriceTxt);

        // handle button clicks
        Button addPropCancelBtn, stepOneNextBtn;

        stepOneNextBtn = view.findViewById(R.id.stepOneNextBtn);
        addPropCancelBtn = view.findViewById(R.id.addPropCancelBtn);

        stepOneNextBtn.setOnClickListener(this);
        addPropCancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stepOneNextBtn:
                nextFragment();
                break;
            case R.id.addPropCancelBtn:
                getActivity().finish();
                break;
        }
    }

    // next fragment navigator
    public void nextFragment() {
        // get input data
        String propName = propNameTxt.getText().toString();
        String propPrice = propPriceTxt.getText().toString();

        // add data to bundle
        Bundle addPropDataBundle = new Bundle();
        addPropDataBundle.putString("prop_name", propName);
        addPropDataBundle.putString("prop_price", propPrice);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddPropStepTwoFragment addPropStepTwoFragment = new AddPropStepTwoFragment();
        addPropStepTwoFragment.setArguments(addPropDataBundle);

        fragmentTransaction.replace(R.id.addPropStepContainer, addPropStepTwoFragment);
        fragmentTransaction.commit();
    }
}
