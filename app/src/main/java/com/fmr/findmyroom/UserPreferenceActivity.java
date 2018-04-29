package com.fmr.findmyroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserPreferenceActivity extends AppCompatActivity {

    private EditText nameTxt;
    private EditText ageTxt;
    private EditText genderTxt;
    private EditText countryTxt;
    private EditText cityTxt;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);

        // set the toolbar
        Toolbar userPrefToolbar = findViewById(R.id.userPrefToolbar);
        userPrefToolbar.setTitle("User Preferences");
        setSupportActionBar(userPrefToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init input fields and progressbar
        nameTxt = findViewById(R.id.nameTxt);
        ageTxt = findViewById(R.id.ageTxt);
        genderTxt = findViewById(R.id.genderTxt);
        countryTxt = findViewById(R.id.countryTxt);
        cityTxt = findViewById(R.id.cityTxt);
        progressBar = findViewById(R.id.addUserProProgressbar);

        // init fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // init fire base database instance
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user_data");

        // add user profile
        Button addUserProBtn = findViewById(R.id.addUserProBtn);
        addUserProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserProfile();
            }
        });
    }

    // add user profile data for registered user
    private void addUserProfile() {
        String name = nameTxt.getText().toString();
        String age = ageTxt.getText().toString();
        String gender = genderTxt.getText().toString();
        String country = countryTxt.getText().toString();
        String city = cityTxt.getText().toString();

        // show progressbar
        progressBar.setVisibility(View.VISIBLE);

        // save user profile data with registered user id
        if (mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().getUid().isEmpty()) {
            String uId = mAuth.getCurrentUser().getUid();
            User user = new User(name, age, gender, country, city);
            mDatabaseRef.child(uId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // hide progressbar
                    progressBar.setVisibility(View.GONE);

                    // navigate to property list view
                    Intent propListIntent = new Intent(getApplicationContext(), RoomListActivity.class);
                    startActivity(propListIntent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    Toast.makeText(UserPreferenceActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
