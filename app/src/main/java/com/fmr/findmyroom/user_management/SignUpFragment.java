package com.fmr.findmyroom.user_management;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fmr.findmyroom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpFragment extends Fragment {

    private EditText nameTxt, emailTxt, passwordTxt;
    private ProgressBar signUpProgressBar;

    private FirebaseAuth mAuth;

    private static final String NAME = "name";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // initialize fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // initialize input and action elements
        nameTxt = view.findViewById(R.id.signUpNameTxt);
        emailTxt = view.findViewById(R.id.emailTxt);
        passwordTxt = view.findViewById(R.id.passwordTxt);
        signUpProgressBar = view.findViewById(R.id.signUpProgressBar);

        // implement sign up function
        Button signUpBtn = view.findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(view);
            }
        });

        return view;
    }

    // user registration
    private void registerUser(final View view) {
        // get user input data
        String name = nameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        // validate name
        if (name.isEmpty()) {
            nameTxt.setError("Name is required");
            nameTxt.requestFocus();
            return;
        }

        // validate email
        if (email.isEmpty()) {
            emailTxt.setError("Email is required");
            emailTxt.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Invalid email");
            emailTxt.requestFocus();
            return;
        }

        // validate password
        if (password.isEmpty()) {
            passwordTxt.setError("Password is required");
            passwordTxt.requestFocus();
            return;
        } else if (password.length() < 6) {
            passwordTxt.setError("Password must be of minimum 6 characters");
            passwordTxt.requestFocus();
            return;
        }

        // show progressbar
        signUpProgressBar.setVisibility(View.VISIBLE);

        // create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // hide progressbar
                        signUpProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // get input name
                            final String name = nameTxt.getText().toString();

                            // get the user type and navigate to relevant preferences activity
                            Dialog userTypeSelector = new Dialog(view.getContext());
                            userTypeSelector.setContentView(R.layout.user_type_pop_up_dialog);

                            userTypeSelector.setCanceledOnTouchOutside(false);
                            userTypeSelector.show();

                            // handle button clicks of dialog box
                            // regular user button
                            Button regularUserBtn = userTypeSelector.findViewById(R.id.regularUserBtn);
                            regularUserBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent regularUserIntent = new Intent(view.getContext(), RegularUserPreferenceActivity.class);
                                    regularUserIntent.putExtra(NAME, name);
                                    startActivity(regularUserIntent);
                                }
                            });

                            // advanced user button
                            Button advancedUserBtn = userTypeSelector.findViewById(R.id.advancedUserBtn);
                            advancedUserBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent advancedUserIntent = new Intent(view.getContext(), AdvancedUserPreferenceActivity.class);
                                    advancedUserIntent.putExtra(NAME, name);
                                    startActivity(advancedUserIntent);
                                }
                            });
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(view.getContext(), "Username is not available!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() != null)
                                Toast.makeText(view.getContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}