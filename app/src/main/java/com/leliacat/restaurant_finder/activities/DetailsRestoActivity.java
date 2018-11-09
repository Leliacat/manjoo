package com.leliacat.restaurant_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leliacat.restaurant_finder.R;
import com.leliacat.restaurant_finder.model.Restaurant;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailsRestoActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_resto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // put onclick listeners on different buttons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        Button dismissBtn = (Button) findViewById(R.id.details_btn_dismiss);
        Button dismissBtn2 = (Button) findViewById(R.id.details_btn_dismiss2);
        dismissBtn.setOnClickListener(this);
        dismissBtn2.setOnClickListener(this);


        // grab the extra from recyclerview
        extras = getIntent().getExtras();

        // we get the elements from the details layout
        TextView title = (TextView) findViewById(R.id.details_title);
        TextView specialties = (TextView) findViewById(R.id.details_specialties);
        TextView rating = (TextView) findViewById(R.id.details_rating);
        TextView address = (TextView) findViewById(R.id.details_address);
        TextView price = (TextView) findViewById(R.id.details_price);
        TextView link = (TextView) findViewById(R.id.details_textlink);




        // we fill layout elements with desired text with infos from extras
        if(extras != null) {
            /*extras.getSerializable("address");*/
            String currency =  extras.getString("currency");
            Log.d("DETAILS_CURRENCY", "onCreate: " + currency);

            title.setText(extras.getString("name"));
            specialties.setText(extras.getString("specialties"));
            address.setText(extras.getString("address"));
            rating.setText("Rating: " + extras.getString("rating"));
            price.setText("Average cost for two: " +  extras.getString("cost_for_two") + extras.getString("currency"));
            link.setText("More info on this link: " + "\n" +  extras.getString("link"));
        }

    }

    @Override
    public void onClick(View v) {
        Intent backToMapIntent = new Intent(DetailsRestoActivity.this, MapsActivity.class);
        startActivity(backToMapIntent);
    }
}
