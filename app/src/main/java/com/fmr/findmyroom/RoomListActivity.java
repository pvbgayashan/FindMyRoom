package com.fmr.findmyroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    private List<Property> propList;
    private ListView propListView;
    private Context thisContext = this;

    // constant
    private static final String DATA_FETCHING_STATUS = "Data Fetching Status";

    // database reference
    private static final String DATA_REF = "property_data";
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        // set the toolbar
        Toolbar roomListToolbar = findViewById(R.id.roomListToolbar);
        roomListToolbar.setTitle("All Listing");
        setSupportActionBar(roomListToolbar);

        // init property list and property list view
        propList = new ArrayList<>();
        propListView = findViewById(R.id.propListView);

        // set database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // show data fetching progress
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // set listener
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // reset the property list
                propList.clear();

                // hide progress dialog
                progressDialog.dismiss();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot propertySnap : dataSnapshot.getChildren()) {
                        Property property = propertySnap.getValue(Property.class);
                        propList.add(property);
                    }

                    // setup list view
                    PropertyCardAdapter propertyCardAdapter = new PropertyCardAdapter(propList, thisContext);
                    propListView.setAdapter(propertyCardAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATA_FETCHING_STATUS, databaseError.toString());

                // hide progress dialog
                progressDialog.dismiss();
            }
        });
    }
}