package com.fmr.findmyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init fire base auth instance
        mAuth = FirebaseAuth.getInstance();

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
                Intent allListingIntent = new Intent(getApplicationContext(), RoomListActivity.class);
                startActivity(allListingIntent);
            }
        });

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
