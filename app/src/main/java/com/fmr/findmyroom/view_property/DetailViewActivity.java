package com.fmr.findmyroom.view_property;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmr.findmyroom.R;
import com.squareup.picasso.Picasso;

public class DetailViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        TextView petsStatusTxt = findViewById(R.id.petsStatusTxt);
        TextView smokingStatusTxt = findViewById(R.id.smokingStatusTxt);
        TextView childCareStatusTxt = findViewById(R.id.childCareStatusTxt);

        ImageView mainImage = findViewById(R.id.mainImage);
        TextView propPaxTxt = findViewById(R.id.paxTxt);
        TextView propNameTxt = findViewById(R.id.propNameTxt);
        TextView propAddressLineTxt = findViewById(R.id.propAddressLineTxt);
        TextView propCountryTxt = findViewById(R.id.propCountryTxt);
        TextView propPhone = findViewById(R.id.propPhone);
        Button callNowBtn = findViewById(R.id.callNowBtn);

        // get data from intent
        if (getIntent().getExtras() != null) {
            boolean[] amensAndRules = getIntent().getExtras().getBooleanArray("propAmensAndRules");
            String imgDownloadUrl = getIntent().getExtras().getString("imgDownloadUrl");
            String pax = getIntent().getExtras().getString("propPax");
            String propName = getIntent().getExtras().getString("propName");
            String addressLine = getIntent().getExtras().getString("propAddressLine");
            String country = getIntent().getExtras().getString("propCountry");
            final String phone = getIntent().getExtras().getString("propPhone");

            // set image view
            Picasso.with(getApplicationContext())
                    .load(imgDownloadUrl)
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .centerInside()
                    .into(mainImage);

            // set amens and rules status
            String txt0 = amensAndRules[0] ? "Yes" : "No";
            int color0 = amensAndRules[0] ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red);
            petsStatusTxt.setText(txt0);
            petsStatusTxt.setTextColor(color0);

            String txt1 = amensAndRules[1] ? "Yes" : "No";
            int color1 = amensAndRules[1] ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red);
            smokingStatusTxt.setText(txt1);
            smokingStatusTxt.setTextColor(color1);

            String txt2 = amensAndRules[2] ? "Yes" : "No";
            int color2 = amensAndRules[2] ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red);
            childCareStatusTxt.setText(txt2);
            childCareStatusTxt.setTextColor(color2);

            // set data
            propPaxTxt.setText("Maximum PAX: " + pax);
            propNameTxt.setText(propName);
            propAddressLineTxt.setText(addressLine);
            propCountryTxt.setText(country);
            if (phone != null && phone.length() >= 10)
                propPhone.setText("Tel: +94 (" + phone.substring(0, 1) + ") " + phone.substring(1, 3)
                        + " " + phone.substring(3, phone.length()));

            // create calling action
            callNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "tel: " + phone;
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                }
            });
        }
    }
}
