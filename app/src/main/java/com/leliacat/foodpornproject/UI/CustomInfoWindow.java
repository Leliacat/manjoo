package com.leliacat.foodpornproject.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.leliacat.foodpornproject.R;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private View view;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomInfoWindow(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_info_window,null);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

       // allows to personnalize infowindows according to the marker,
        // whether it is the one locating the user, or those representing the restaurants
        // ot the one representing the Tardis
       if (marker.getTag().toString().equals("HERE")) {

          Button button = (Button) view.findViewById(R.id.custominfo_btn_moreinfo);
          button.setVisibility(View.GONE);

          ImageView img = (ImageView) view.findViewById(R.id.custominfo_iconInfo);
          Drawable drawable = context.getResources().getDrawable(R.drawable.kungfun_panda);
          img.setImageDrawable(drawable);


       }else if(marker.getTag().toString().equals("TARDIS")){

           Button button = (Button) view.findViewById(R.id.custominfo_btn_moreinfo);
           button.setVisibility(View.GONE);

           ImageView img = (ImageView) view.findViewById(R.id.custominfo_iconInfo);
           img.setVisibility(View.GONE);
       }
       else {
           Button button = (Button) view.findViewById(R.id.custominfo_btn_moreinfo);
           button.setVisibility(View.VISIBLE);

           ImageView img = (ImageView) view.findViewById(R.id.custominfo_iconInfo);
           Drawable drawable = context.getResources().getDrawable(R.drawable.kawaii_suitcase);
           img.setImageDrawable(drawable);
       }

        TextView title = (TextView) view.findViewById(R.id.custominfo_title);
        title.setText(marker.getTitle());

        TextView specialties = (TextView) view.findViewById(R.id.custominfo_specialties);
        specialties.setText(marker.getSnippet());
        return view;
    }
}
