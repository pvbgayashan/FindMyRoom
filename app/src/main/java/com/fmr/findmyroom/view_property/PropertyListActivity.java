package com.fmr.findmyroom.view_property;

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

import com.fmr.findmyroom.common.Property;
import com.fmr.findmyroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class PropertyListActivity extends AppCompatActivity {

    private List<Property> propList;
    private ListView propListView;
    private Context thisContext = this;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private MaterialSearchView propSearchView;
    private Bundle intentDataBundle;

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

        // set database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);

        // init property list and property list view
        propList = new ArrayList<>();
        propListView = findViewById(R.id.propListView);

        // init progressbar
        progressBar = findViewById(R.id.propListProgressbar);

        // init material search view
        propSearchView = findViewById(R.id.propSearch);

        // get data from intent
        if (getIntent().getExtras() != null) {
            intentDataBundle = getIntent().getExtras();
        }
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
                int propCounter = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot propertySnap : dataSnapshot.getChildren()) {
                        Property property = propertySnap.getValue(Property.class);

                        // add filter to data
                        if (intentDataBundle != null
                                && intentDataBundle.getString("city") != null
                                && intentDataBundle.getString("accommodation_type") != null
                                && intentDataBundle.getString("area_type") != null
                                && intentDataBundle.getString("pax") != null) { // add filtered data to list

                            String city = intentDataBundle.getString("city").replace("\"", "");
                            String accommodationType = intentDataBundle.getString("accommodation_type")
                                    .replace("\"", "");
                            String areaType = intentDataBundle.getString("area_type").replace("\"", "");
                            String pax = intentDataBundle.getString("pax").replace("\"", "");

                            if (property.getCity().toLowerCase().contains(city.toLowerCase())
                                    && property.getPax() >= Integer.parseInt(pax)
                                    && (property.getPreferences().get(accommodationType) != null && property.getPreferences().get(accommodationType))
                                    && (property.getPreferences().get(areaType) != null && property.getPreferences().get(areaType))) {
                                propList.add(property);
                                propCounter++;
                            }
                        } else { // add all property to list
                            propList.add(property);
                            propCounter++;
                        }
                    }

                    // setup list view
                    PropertyCardAdapter propertyCardAdapter = new PropertyCardAdapter(propList, thisContext);
                    propListView.setAdapter(propertyCardAdapter);

                    // set the toolbar and property description
                    setToolbarAndPropDesc(propCounter, intentDataBundle);
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
    private void setToolbarAndPropDesc(int propCount, Bundle intentDataBundle) {
        // extract data bundle
        String city = null, pax = null;
        if (intentDataBundle != null) {
            if (intentDataBundle.getString("city") != null)
                city = intentDataBundle.getString("city").replace("\"", "");

            if (intentDataBundle.getString("pax") != null)
                pax = intentDataBundle.getString("pax").replace("\"", "");
        }

        // set the toolbar
        Toolbar roomListToolbar = findViewById(R.id.roomListToolbar);
        if ((city != null && !city.isEmpty()) && (pax != null && !pax.isEmpty())) {
            roomListToolbar.setTitle(city);
            roomListToolbar.setSubtitle(pax + " person(s)");
        } else {
            roomListToolbar.setTitle(R.string.all_properties);
        }
        setSupportActionBar(roomListToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set property desc result text view
        TextView propResultDescTxt = findViewById(R.id.propResultDescTxt);
        propResultDescTxt.setBackgroundColor(Color.parseColor("#E0E0E0"));

        if (propCount == 0) {
            String message = "No results found";
            propResultDescTxt.setText(message);
        } else if (propCount == 1) {
            String message = "Showing " + propCount + " property";
            propResultDescTxt.setText(message);
        } else {
            String message = "Showing " + propCount + " properties";
            propResultDescTxt.setText(message);
        }
    }
}