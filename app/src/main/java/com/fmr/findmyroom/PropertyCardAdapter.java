package com.fmr.findmyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by pvbgayashan on 4/20/18.
 */

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        // set property card layout
        view = layoutInflater.inflate(R.layout.list_prop_card, null);

        // update data for property card
        TextView propNameTxtView = view.findViewById(R.id.propName);
        TextView miniAddressTxtView = view.findViewById(R.id.miniAddress);
        ImageView propImgView = view.findViewById(R.id.propImageView);

        propNameTxtView.setText(propList.get(i).getPropName());
        miniAddressTxtView.setText(propList.get(i).getCity() + ", " + propList.get(i).getCountry());
        Picasso.with(context)
                .load(propList.get(i).getDownloadUrl())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerInside()
                .into(propImgView);

        return view;
    }
}
