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
import android.widget.Spinner;

public class AddPropStepTwoFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction fragmentTransaction;

    private Spinner countrySpinner;
    private EditText cityTxt;
    private Bundle addPropDataBundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prop_step_two, container, false);

        // init input items
        countrySpinner = view.findViewById(R.id.addPropCountrySpinner);
        cityTxt = view.findViewById(R.id.addPropCityTxt);

        // init fragment transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // get data from fragment
        addPropDataBundle = getArguments();

        // handle button clicks
        Button stepTwoBackBtn, stepTwoNextBtn;

        stepTwoBackBtn = view.findViewById(R.id.stepTwoBackBtn);
        stepTwoNextBtn = view.findViewById(R.id.stepTwoNextBtn);

        stepTwoBackBtn.setOnClickListener(this);
        stepTwoNextBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stepTwoBackBtn:
                backFragment();
                break;
            case R.id.stepTwoNextBtn:
                nextFragment();
                break;
        }
    }

    // back fragment navigator
    private void backFragment() {
        fragmentTransaction.replace(R.id.addPropStepContainer, new AddPropStepOneFragment());
        fragmentTransaction.commit();
    }

    // next fragment navigator
    private void nextFragment() {
        // get input data
        String country = countrySpinner.getSelectedItem().toString();
        String city = cityTxt.getText().toString();

        // add input data to bundle
        addPropDataBundle.putString("country", country);
        addPropDataBundle.putString("city", city);

        AddPropStepThreeFragment addPropStepThreeFragment = new AddPropStepThreeFragment();
        addPropStepThreeFragment.setArguments(addPropDataBundle);

        fragmentTransaction.replace(R.id.addPropStepContainer, addPropStepThreeFragment);
        fragmentTransaction.commit();
    }
}
