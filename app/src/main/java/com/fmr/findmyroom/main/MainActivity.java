package com.fmr.findmyroom.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fmr.findmyroom.R;
import com.fmr.findmyroom.common.Property;
import com.fmr.findmyroom.view_property.PropertyListActivity;
import com.fmr.findmyroom.add_property.AddPropertyActivity;
import com.fmr.findmyroom.user_management.UserConnectionActivity;
import com.fmr.findmyroom.virtual_agent.ChatBotActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private static final String DATA_REF = "property_data";

    private ImageView topPropImage;
    private TextView topPropName, topMiniAddress, topPrice;
    private RatingBar topPropRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // init fire base database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);

        // init prop data fields
        topPropImage = findViewById(R.id.topPropImageView);
        topPropName = findViewById(R.id.topPropName);
        topMiniAddress = findViewById(R.id.topMiniAddress);
        topPrice = findViewById(R.id.topPrice);
        topPropRating = findViewById(R.id.topPropRatingBar);

        // set the toolbar
        Toolbar mainActToolbar = findViewById(R.id.mainActivityToolbar);
        mainActToolbar.setTitle("Home");
        setSupportActionBar(mainActToolbar);

        // go to chat bot activity
        final CardView chatBotCard = findViewById(R.id.chatBotCard);

        chatBotCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent chatBotIntent = new Intent(getApplicationContext(), ChatBotActivity.class);
                startActivity(chatBotIntent);
            }
        });

        // go to user connection activity
        final CardView userConnCard = findViewById(R.id.userConnCard);

        userConnCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() == null) {
                    Intent userConnIntent = new Intent(getApplicationContext(), UserConnectionActivity.class);
                    startActivity(userConnIntent);
                } else {
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // go to add property activity
        final CardView addPropCard = findViewById(R.id.addPropCard);

        addPropCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addPropIntent = new Intent(getApplicationContext(), AddPropertyActivity.class);
                startActivity(addPropIntent);
            }
        });

        // go to all list activity
        final CardView allListingCard = findViewById(R.id.allListingCard);

        allListingCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent allListingIntent = new Intent(getApplicationContext(), PropertyListActivity.class);
                startActivity(allListingIntent);
            }
        });

        // load top rated property


        // go to detail view activity
        /*final CardView topRatedPlaceCard = findViewById(R.id.topRatedPlaceCard);

        topRatedPlaceCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        // set listener
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Property maxRatedProp = null;
                float maxRate = 0.0f;
                for (DataSnapshot propertySnap : dataSnapshot.getChildren()) {
                    Property property = propertySnap.getValue(Property.class);

                    float currentRate = property.getRating() / property.getRateCount();
                    if (currentRate > maxRate) {
                        maxRate = currentRate;
                        maxRatedProp = property;
                    }
                }

                // set max rated property
                float rate = maxRatedProp.getRating() / maxRatedProp.getRateCount();
                Picasso.with(getApplicationContext())
                        .load(maxRatedProp.getDownloadUrl())
                        .placeholder(R.drawable.placeholder)
                        .fit()
                        .centerInside()
                        .into(topPropImage);
                topPropName.setText(maxRatedProp.getName());
                topMiniAddress.setText(maxRatedProp.getCity() + ", " + maxRatedProp.getCountry());
                topPrice.setText("$" + maxRatedProp.getPrice() + "/day");
                topPropRating.setRating(rate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutOption:
                mAuth.signOut();
                break;
        }
        return true;
    }
}
