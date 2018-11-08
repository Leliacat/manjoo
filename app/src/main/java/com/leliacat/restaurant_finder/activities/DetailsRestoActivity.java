package com.leliacat.restaurant_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leliacat.restaurant_finder.R;

public class DetailsRestoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_resto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                //TODO: get different information to personnalize the info card
                /*// we get the elements from the popup layout
                Button dismissBtn = (Button) v.findViewById(R.id.popup_btn_dismiss);
                Button dismissBtn2 = (Button) v.findViewById(R.id.popup_btn_dismiss2);
                TextView title = (TextView) v.findViewById(R.id.popup_title);
                TextView specialties = (TextView) v.findViewById(R.id.popup_specialties);
                TextView rating = (TextView) v.findViewById(R.id.popup_rating);
                TextView address = (TextView) v.findViewById(R.id.popup_address);
                TextView price = (TextView) v.findViewById(R.id.popup_price);
                TextView link = (TextView) v.findViewById(R.id.popup_textlink);

                // we fill layout elements with desired text
                title.setText(restaurant.getName());
                specialties.setText(restaurant.getCategories());
                address.setText(restaurant.getAddress().toString());
                rating.setText("Rating: " + restaurant.getRating());
                price.setText("Average cost for two: " + String.valueOf(restaurant.getAverage_cost_for_two()) + restaurant.getCurrency());
                link.setText("More info on this link: " + "\n" + restaurant.getDetail_link()); */

            }
        });
    }



    /*@Override
    public void onClick(View v) {
        if (v.getId() == R.id.popup_btn_dismiss || v.getId() == R.id.popup_btn_dismiss2){
            //TODO: onclicklisteners for the dismiss btns
        }*/


}
