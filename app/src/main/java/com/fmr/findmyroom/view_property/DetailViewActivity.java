package com.fmr.findmyroom.view_property;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fmr.findmyroom.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DetailViewActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        // set database reference
        final String DATA_REF = "property_data";
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);

        TextView petsStatusTxt = findViewById(R.id.petsStatusTxt);
        TextView smokingStatusTxt = findViewById(R.id.smokingStatusTxt);
        TextView childCareStatusTxt = findViewById(R.id.childCareStatusTxt);
        final RatingBar propRatingBar = findViewById(R.id.propRatingBar);

        ImageView mainImage = findViewById(R.id.mainImage);
        TextView propPaxTxt = findViewById(R.id.paxTxt);
        TextView propNameTxt = findViewById(R.id.propNameTxt);
        TextView propAddressLineTxt = findViewById(R.id.propAddressLineTxt);
        TextView propCountryTxt = findViewById(R.id.propCountryTxt);
        TextView propPhone = findViewById(R.id.propPhone);
        Button callNowBtn = findViewById(R.id.callNowBtn);

        // get data from intent
        if (getIntent().getExtras() != null) {
            final String id = getIntent().getExtras().getString("propId");
            boolean[] amensAndRules = getIntent().getExtras().getBooleanArray("propAmensAndRules");
            String imgDownloadUrl = getIntent().getExtras().getString("imgDownloadUrl");
            String pax = getIntent().getExtras().getString("propPax");
            String propName = getIntent().getExtras().getString("propName");
            String addressLine = getIntent().getExtras().getString("propAddressLine");
            String country = getIntent().getExtras().getString("propCountry");
            final String phone = getIntent().getExtras().getString("propPhone");
            final float ratingValue = getIntent().getExtras().getFloat("propRatingValue");
            final int rateCount = getIntent().getExtras().getInt("propRateCount");

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
            String maximumPax = "Maximum PAX: " + pax;
            propPaxTxt.setText(maximumPax);
            float rv = rateCount == 0 ? ratingValue : ratingValue / rateCount;
            propRatingBar.setRating(rv);
            propNameTxt.setText(propName);
            propAddressLineTxt.setText(addressLine);
            propCountryTxt.setText(country);
            if (phone != null && phone.length() >= 10) {
                String phoneNumber = "Tel: +94 (" + phone.substring(0, 1) + ") " + phone.substring(1, 3)
                        + " " + phone.substring(3, phone.length());
                propPhone.setText(phoneNumber);
            }

            // rating bar function
            propRatingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    // rating dialog
                    final Dialog ratingDialog = new Dialog(view.getContext());
                    ratingDialog.setContentView(R.layout.prop_rating_pop_up_dialog);

                    ratingDialog.setCanceledOnTouchOutside(false);
                    ratingDialog.show();

                    // handle rating submit button clicks
                    Button ratingSubmitBtn = ratingDialog.findViewById(R.id.ratingSubmitBtn);
                    ratingSubmitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RatingBar editableRatingBar = ratingDialog.findViewById(R.id.editablePropRatingBar);
                            float selectedRatingValue = editableRatingBar.getRating();

                            // set updated rating value
                            float rv = rateCount == 0 ? selectedRatingValue : (ratingValue + selectedRatingValue) / (rateCount + 1);
                            propRatingBar.setRating(rv);

                            // update rating details
                            try {
                                if (rateCount == 0) {
                                    mDatabaseRef.child(id).child("rating").setValue(selectedRatingValue);
                                    mDatabaseRef.child(id).child("rateCount").setValue(1);
                                } else if (rateCount > 0) {
                                    mDatabaseRef.child(id).child("rating").setValue(ratingValue + selectedRatingValue);
                                    mDatabaseRef.child(id).child("rateCount").setValue(rateCount + 1);
                                }
                            } catch (Exception ex) {
                                Toast.makeText(DetailViewActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            ratingDialog.dismiss();
                        }
                    });

                    return false;
                }
            });

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
