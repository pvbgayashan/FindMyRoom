package com.fmr.findmyroom.user_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.fmr.findmyroom.R;
import com.fmr.findmyroom.view_property.PropertyListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegularUserPreferenceActivity extends AppCompatActivity {

    private Spinner countrySpinner;
    private EditText cityTxt;
    private Spinner genderSpinner;
    private EditText ageTxt;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_user_preference);

        // init fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // init fire base database instance
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user_data");

        // set the toolbar
        Toolbar userPrefToolbar = findViewById(R.id.regUsrPrefToolbar);
        userPrefToolbar.setTitle("Regular User");
        setSupportActionBar(userPrefToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init input fields and progressbar
        countrySpinner = findViewById(R.id.regUsrCountries);
        cityTxt = findViewById(R.id.regUsrCityTxt);
        genderSpinner = findViewById(R.id.regUsrGenders);
        ageTxt = findViewById(R.id.regUsrAgeTxt);
        progressBar = findViewById(R.id.addRegUsrPrefProgressbar);

        // add user profile
        Button addUserPrefBtn = findViewById(R.id.addRegUsrPrefBtn);
        addUserPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserProfile();
            }
        });
    }

    // add user profile data for registered user
    private void addUserProfile() {
        // get name from intent
        String name = "";
        if (getIntent().getExtras() != null) {
            name = getIntent().getExtras().getString("name");
        }

        final String userType = "regular";
        String country = countrySpinner.getSelectedItem().toString();
        String city = cityTxt.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        String age = ageTxt.getText().toString();

        // show progressbar
        progressBar.setVisibility(View.VISIBLE);

        // save user profile data with registered user id
        if (mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().getUid().isEmpty()) {
            String uId = mAuth.getCurrentUser().getUid();
            User user = new User(userType, name, country, city, gender, age);
            mDatabaseRef.child(uId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // hide progressbar
                    progressBar.setVisibility(View.GONE);

                    // navigate to property list view
                    Intent propListIntent = new Intent(getApplicationContext(), PropertyListActivity.class);
                    startActivity(propListIntent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    Toast.makeText(RegularUserPreferenceActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
