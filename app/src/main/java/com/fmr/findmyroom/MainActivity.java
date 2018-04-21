package com.fmr.findmyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent userConnIntent = new Intent(getApplicationContext(), UserConnectionActivity.class);
                startActivity(userConnIntent);
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
        final CardView topRatedPlaceCard = findViewById(R.id.topRatedPlaceCard);

        topRatedPlaceCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });
    }
}
