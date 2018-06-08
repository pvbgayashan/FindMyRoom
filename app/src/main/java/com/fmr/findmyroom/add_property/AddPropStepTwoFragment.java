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
import android.widget.RadioButton;

import com.fmr.findmyroom.main.MainActivity;
import com.fmr.findmyroom.R;

public class AddPropStepTwoFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction fragmentTransaction;
    private Bundle addPropDataBundle;

    private RadioButton apartment, room;
    private CheckBox employee, student, other;
    private CheckBox male, female;
    private CheckBox urban, village, seaSide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prop_step_two, container, false);

        // init input checkboxes
        apartment = view.findViewById(R.id.radioBtnApartment);
        room = view.findViewById(R.id.radioBtnRoom);

        employee = view.findViewById(R.id.checkBoxEmployee);
        student = view.findViewById(R.id.checkBoxStudent);
        other = view.findViewById(R.id.checkBoxOther);

        male = view.findViewById(R.id.checkBoxMale);
        female = view.findViewById(R.id.checkBoxFemale);

        urban = view.findViewById(R.id.checkBoxUrban);
        village = view.findViewById(R.id.checkBoxVillage);
        seaSide = view.findViewById(R.id.checkBoxSeaSide);

        // init fragment transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // get data from fragment
        addPropDataBundle = getArguments();

        // handle button clicks
        Button stepTwoNextBtn, addPropCancelBtn;

        stepTwoNextBtn = view.findViewById(R.id.stepTwoNextBtn);
        addPropCancelBtn = view.findViewById(R.id.addPropCancelBtn);

        stepTwoNextBtn.setOnClickListener(this);
        addPropCancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stepTwoNextBtn:
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
        addPropDataBundle.putBoolean("apartment", apartment.isChecked());
        addPropDataBundle.putBoolean("room", room.isChecked());

        addPropDataBundle.putBoolean("employee", employee.isChecked());
        addPropDataBundle.putBoolean("student", student.isChecked());
        addPropDataBundle.putBoolean("other", other.isChecked());

        addPropDataBundle.putBoolean("male", male.isChecked());
        addPropDataBundle.putBoolean("female", female.isChecked());

        addPropDataBundle.putBoolean("urban", urban.isChecked());
        addPropDataBundle.putBoolean("village", village.isChecked());
        addPropDataBundle.putBoolean("sea_side", seaSide.isChecked());

        AddPropStepThreeFragment addPropStepThreeFragment = new AddPropStepThreeFragment();
        addPropStepThreeFragment.setArguments(addPropDataBundle);

        fragmentTransaction.replace(R.id.addPropStepContainer, addPropStepThreeFragment);
        fragmentTransaction.commit();
    }
}
