package com.leliacat.restaurant_finder.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.leliacat.restaurant_finder.R;
import com.leliacat.restaurant_finder.activities.MapsActivity;

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

       /* ImageView cutepic = (ImageView) view.findViewById(R.id.iconInfo);
        Context context = MapsActivity.this;
        Drawable drawable = context.getResources().getDrawable(R.drawable.kawaii_suitcase);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        cutepic.setImageBitmap(bitmap);*/

        TextView title = (TextView) view.findViewById(R.id.custominfo_title);
        title.setText(marker.getTitle());

        TextView specialties = (TextView) view.findViewById(R.id.custominfo_specialties);
        specialties.setText(marker.getSnippet());
        return view;
    }
}
