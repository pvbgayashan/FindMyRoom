package com.fmr.findmyroom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    private List<Property> propList;
    private ListView propListView;
    private Context thisContext = this;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private MaterialSearchView propSearchView;

    // constant
    private static final String DATA_FETCHING_STATUS = "Data Fetching Status";

    // database reference
    private static final String DATA_REF = "property_data";
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        // init fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // init property list and property list view
        propList = new ArrayList<>();
        propListView = findViewById(R.id.propListView);

        // init progressbar
        progressBar = findViewById(R.id.propListProgressbar);

        // init material search view
        propSearchView = findViewById(R.id.propSearch);

        // set database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_prop_list, menu);

        // set menu item to search view
        MenuItem searchMenuItem = menu.findItem(R.id.searchOption);
        propSearchView.setMenuItem(searchMenuItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutOption:
                mAuth.signOut();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // show progressbar
        progressBar.setVisibility(View.VISIBLE);

        // set listener
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // reset the property list
                propList.clear();

                // hide progressbar
                progressBar.setVisibility(View.GONE);

                // add property to property list
                if (dataSnapshot.exists()) {
                    for (DataSnapshot propertySnap : dataSnapshot.getChildren()) {
                        Property property = propertySnap.getValue(Property.class);
                        propList.add(property);
                    }

                    // setup list view
                    PropertyCardAdapter propertyCardAdapter = new PropertyCardAdapter(propList, thisContext);
                    propListView.setAdapter(propertyCardAdapter);

                    // set the toolbar and property description
                    Bundle dataBundle = null;
                    if (getIntent().getExtras() != null) {
                        dataBundle = getIntent().getExtras();
                    }
                    setToolbarAndPropDesc(dataSnapshot, dataBundle);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATA_FETCHING_STATUS, databaseError.toString());

                // hide progressbar
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // create the toolbar and property result description
    private void setToolbarAndPropDesc(DataSnapshot dataSnapshot, Bundle dataBundle) {
        // extract data bundle
        String city = null, pax = null;
        if (dataBundle != null) {
            city = dataBundle.getString("city").toString();
            pax = dataBundle.getString("pax").toString();
        }

        // set the toolbar
        Toolbar roomListToolbar = findViewById(R.id.roomListToolbar);

        // set toolbar title and subtitle
        if ((city != null && !city.isEmpty()) && (pax != null && !pax.isEmpty())) {
            roomListToolbar.setTitle("Properties from " + city);
            roomListToolbar.setSubtitle(pax + " person(s)");
        } else {
            roomListToolbar.setTitle(R.string.all_properties);
        }
        setSupportActionBar(roomListToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set property desc result text view
        long propCount = dataSnapshot.getChildrenCount();
        TextView propResultDescTxt = findViewById(R.id.propResultDescTxt);
        propResultDescTxt.setText("Showing " + propCount + " properties");
        propResultDescTxt.setBackgroundColor(Color.parseColor("#E0E0E0"));
    }
}