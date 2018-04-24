package com.fmr.findmyroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        ImageView mainImage = findViewById(R.id.mainImage);
        TextView propNameTxt = findViewById(R.id.propName);

        // get data from intent
        if (getIntent().getExtras() != null) {
            String imgDownloadUrl = getIntent().getExtras().getString("imgDownloadUrl");
            String propName = getIntent().getExtras().getString("propName");

            // set image view
            Picasso.with(getApplicationContext())
                    .load(imgDownloadUrl)
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .centerInside()
                    .into(mainImage);

            // set data
            propNameTxt.setText(propName);
        }
    }
}
