package com.fmr.findmyroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class RoomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        // set the toolbar
        Toolbar roomListToolbar = findViewById(R.id.roomListToolbar);
        roomListToolbar.setTitle("All Listing");
        setSupportActionBar(roomListToolbar);

        // go to detail view activity
        final CardView placeCard1 = findViewById(R.id.placeCard1);

        placeCard1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });

        final CardView placeCard2 = findViewById(R.id.placeCard2);

        placeCard2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });

        final CardView placeCard3 = findViewById(R.id.placeCard3);

        placeCard3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });

        final CardView placeCard4 = findViewById(R.id.placeCard4);

        placeCard4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });

        final CardView placeCard5 = findViewById(R.id.placeCard5);

        placeCard5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(detailViewIntent);
            }
        });
    }
}
