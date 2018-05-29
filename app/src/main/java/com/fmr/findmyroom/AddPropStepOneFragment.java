package com.fmr.findmyroom;

import android.content.Intent;
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

public class AddPropStepOneFragment extends Fragment implements View.OnClickListener {

    private EditText propNameTxt, propPriceTxt, propCityTxt, propAddressTxt, propPostalCodeTxt;
    private Spinner propCountrySpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prop_step_one, container, false);

        // init input items
        propNameTxt = view.findViewById(R.id.addPropNameTxt);
        propPriceTxt = view.findViewById(R.id.addPropPriceTxt);
        propCountrySpinner = view.findViewById(R.id.addPropCountrySpinner);
        propCityTxt = view.findViewById(R.id.addPropCityTxt);
        propAddressTxt = view.findViewById(R.id.addPropAddressTxt);
        propPostalCodeTxt = view.findViewById(R.id.addPropPostalCodeTxt);

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
                // finish activity
                getActivity().finish();

                // navigate to home
                Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(homeIntent);

                break;
        }
    }

    // next fragment navigator
    public void nextFragment() {
        // get input data
        String propName = propNameTxt.getText().toString();
        String propPrice = propPriceTxt.getText().toString();
        String propCountry= propCountrySpinner.getSelectedItem().toString();
        String propCity = propCityTxt.getText().toString();
        String propAddress = propAddressTxt.getText().toString();
        String propPostalCode = propPostalCodeTxt.getText().toString();

        // add data to bundle
        Bundle addPropDataBundle = new Bundle();
        addPropDataBundle.putString("prop_name", propName);
        addPropDataBundle.putString("prop_price", propPrice);
        addPropDataBundle.putString("prop_country", propCountry);
        addPropDataBundle.putString("prop_city", propCity);
        addPropDataBundle.putString("prop_address", propAddress);
        addPropDataBundle.putString("prop_postal_code", propPostalCode);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddPropStepTwoFragment addPropStepTwoFragment = new AddPropStepTwoFragment();
        addPropStepTwoFragment.setArguments(addPropDataBundle);

        fragmentTransaction.replace(R.id.addPropStepContainer, addPropStepTwoFragment);
        fragmentTransaction.commit();
    }
}
