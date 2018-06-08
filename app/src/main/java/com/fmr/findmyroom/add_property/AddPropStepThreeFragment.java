package com.fmr.findmyroom.add_property;

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
import android.widget.CheckBox;

import com.fmr.findmyroom.main.MainActivity;
import com.fmr.findmyroom.R;

public class AddPropStepThreeFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction fragmentTransaction;
    private Bundle addPropDataBundle;

    private CheckBox parking, attachedBathroom, ac, hotWater, upStair, privateBalcony;
    private CheckBox superMarket, gym, hospital, trainStation, busStation;
    private CheckBox pets, smoking, childCare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prop_step_three, container, false);

        // init input checkboxes
        parking = view.findViewById(R.id.checkBoxParking);
        attachedBathroom = view.findViewById(R.id.checkBoxAttachedBathroom);
        ac = view.findViewById(R.id.checkBoxAC);
        hotWater = view.findViewById(R.id.checkBoxHotWater);
        upStair = view.findViewById(R.id.checkBoxUpStair);
        privateBalcony = view.findViewById(R.id.checkBoxPrivateBalcony);

        superMarket = view.findViewById(R.id.checkBoxSuperMarket);
        gym = view.findViewById(R.id.checkBoxGym);
        hospital = view.findViewById(R.id.checkBoxHospital);
        trainStation = view.findViewById(R.id.checkBoxTrainStation);
        busStation = view.findViewById(R.id.checkBoxBusStation);

        pets = view.findViewById(R.id.checkBoxPets);
        smoking = view.findViewById(R.id.checkBoxSmoking);
        childCare = view.findViewById(R.id.checkBoxChildCare);

        // init fragment transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // get data from fragment
        addPropDataBundle = getArguments();

        // handle button clicks
        Button stepThreeNextBtn, addPropCancelBtn;

        stepThreeNextBtn = view.findViewById(R.id.stepThreeNextBtn);
        addPropCancelBtn = view.findViewById(R.id.addPropCancelBtn);

        stepThreeNextBtn.setOnClickListener(this);
        addPropCancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stepThreeNextBtn:
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
    private void nextFragment() {
        // add input data to bundle
        addPropDataBundle.putBoolean("parking", parking.isChecked());
        addPropDataBundle.putBoolean("attached_bathroom", attachedBathroom.isChecked());
        addPropDataBundle.putBoolean("ac", ac.isChecked());
        addPropDataBundle.putBoolean("hot_water", hotWater.isChecked());
        addPropDataBundle.putBoolean("up_stair", upStair.isChecked());
        addPropDataBundle.putBoolean("private_balcony", privateBalcony.isChecked());

        addPropDataBundle.putBoolean("super_market", superMarket.isChecked());
        addPropDataBundle.putBoolean("gym", gym.isChecked());
        addPropDataBundle.putBoolean("hospital", hospital.isChecked());
        addPropDataBundle.putBoolean("train_station", trainStation.isChecked());
        addPropDataBundle.putBoolean("bus_station", busStation.isChecked());

        addPropDataBundle.putBoolean("pets", pets.isChecked());
        addPropDataBundle.putBoolean("smoking", smoking.isChecked());
        addPropDataBundle.putBoolean("child_care", childCare.isChecked());

        AddPropStepFourFragment addPropStepFourFragment = new AddPropStepFourFragment();
        addPropStepFourFragment.setArguments(addPropDataBundle);

        fragmentTransaction.replace(R.id.addPropStepContainer, addPropStepFourFragment);
        fragmentTransaction.commit();
    }
}
