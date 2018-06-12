package com.fmr.findmyroom.view_property;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fmr.findmyroom.common.Property;
import com.fmr.findmyroom.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class PropertyCardAdapter extends BaseAdapter {

    private List<Property> propList;
    private LayoutInflater layoutInflater;
    private Context context;

    public PropertyCardAdapter(List<Property> propList, Context context) {
        this.propList = propList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return propList.size();
    }

    @Override
    public Object getItem(int i) {
        return propList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // set property card layout
        view = layoutInflater.inflate(R.layout.list_prop_card, null);

        // update data for property card
        TextView propNameTxtView = view.findViewById(R.id.propName);
        TextView miniAddressTxtView = view.findViewById(R.id.miniAddress);
        TextView priceTxtView = view.findViewById(R.id.price);
        ImageView propImgView = view.findViewById(R.id.propImageView);
        RatingBar propRatingBar = view.findViewById(R.id.propRatingBar);

        // set click listener for property card
        CardView propCard = view.findViewById(R.id.propCard);
        propCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailViewIntent = new Intent(context, DetailViewActivity.class);

                // get mapper
                Map<String, Boolean> propPrefMapper = propList.get(i).getPreferences();
                boolean amensAndRules[] = new boolean[3];
                if (propPrefMapper != null) {
                    amensAndRules[0] = propPrefMapper.get("pets");
                    amensAndRules[1] = propPrefMapper.get("smoking");
                    amensAndRules[2] = propPrefMapper.get("child_care");
                }

                // add data to intent
                detailViewIntent.putExtra("propId", propList.get(i).getId());
                detailViewIntent.putExtra("imgDownloadUrl", propList.get(i).getDownloadUrl());
                detailViewIntent.putExtra("propPax", String.valueOf(propList.get(i).getPax()));
                detailViewIntent.putExtra("propName", propList.get(i).getName());
                detailViewIntent.putExtra("propAddressLine", propList.get(i).getAddress()
                        + ", " + propList.get(i).getCity() + " " + propList.get(i).getPostalCode());
                detailViewIntent.putExtra("propCountry", propList.get(i).getCountry());
                detailViewIntent.putExtra("propPhone", propList.get(i).getPhone());
                detailViewIntent.putExtra("propRatingValue", propList.get(i).getRating());
                detailViewIntent.putExtra("propRateCount", propList.get(i).getRateCount());
                detailViewIntent.putExtra("propAmensAndRules", amensAndRules);

                context.startActivity(detailViewIntent);
            }
        });

        propNameTxtView.setText(propList.get(i).getName());
        String miniAddress = propList.get(i).getCity() + ", " + propList.get(i).getCountry();
        miniAddressTxtView.setText(miniAddress);
        String price = "$" + propList.get(i).getPrice() + "/day";
        priceTxtView.setText(price);
        float rv = propList.get(i).getRateCount() == 0 ? propList.get(i).getRating()
                : propList.get(i).getRating() / propList.get(i).getRateCount();
        propRatingBar.setRating(rv);
        Picasso.with(context)
                .load(propList.get(i).getDownloadUrl())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerInside()
                .into(propImgView);

        return view;
    }
}
