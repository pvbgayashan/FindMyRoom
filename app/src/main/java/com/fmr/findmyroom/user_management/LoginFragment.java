package com.fmr.findmyroom.user_management;

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

import com.fmr.findmyroom.main.MainActivity;
import com.fmr.findmyroom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText loginEmailTxt, loginPasswordTxt;
    private ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // initialize input and action elements
        loginEmailTxt = view.findViewById(R.id.loginEmailTxt);
        loginPasswordTxt = view.findViewById(R.id.loginPasswordTxt);
        loginProgressBar = view.findViewById(R.id.loginProgressBar);

        // initialize fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // implement sign up function
        Button loginBtn = view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(view);
            }
        });

        return view;
    }

    // user login
    private void loginUser(final View view) {
        // get user input data
        String loginEmail = loginEmailTxt.getText().toString();
        String loginPassword = loginPasswordTxt.getText().toString();

        // validate email
        if (loginEmail.isEmpty()) {
            loginEmailTxt.setError("Email is required");
            loginEmailTxt.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            loginEmailTxt.setError("Invalid email");
            loginEmailTxt.requestFocus();
            return;
        }

        // validate password
        if (loginPassword.isEmpty()) {
            loginPasswordTxt.setError("Password is required");
            loginPasswordTxt.requestFocus();
            return;
        } else if (loginPassword.length() < 6) {
            loginPasswordTxt.setError("Minimum character length must be 6");
            loginPasswordTxt.requestFocus();
            return;
        }

        // show progressbar
        loginProgressBar.setVisibility(View.VISIBLE);

        // create user
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // hide progressbar
                        loginProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // intent for main activity
                            Intent mainActivityIntent = new Intent(view.getContext(), MainActivity.class);
                            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainActivityIntent);
                        } else {
                            if (task.getException() != null)
                                Toast.makeText(view.getContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
