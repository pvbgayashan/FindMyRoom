package com.fmr.findmyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go to room list activity
        final Button roomListBtn = findViewById(R.id.roomListBtn);

        roomListBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent roomListIntent = new Intent(getApplicationContext(), RoomListActivity.class);
                startActivity(roomListIntent);
            }
        });
    }
}
