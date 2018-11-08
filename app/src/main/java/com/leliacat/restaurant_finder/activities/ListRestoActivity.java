package com.leliacat.restaurant_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.leliacat.restaurant_finder.data.DatabaseHandler;
import com.leliacat.restaurant_finder.model.Restaurant;
import com.leliacat.restaurant_finder.R;
import com.leliacat.restaurant_finder.UI.RecyclerViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListRestoActivity extends AppCompatActivity implements Serializable {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Restaurant> restoList;
    private List<Restaurant> restaurants;
    private DatabaseHandler db;
    /*private Bundle extras;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMapIntent = new Intent(ListRestoActivity.this, MapsActivity.class);
                startActivity(backToMapIntent);
            }
        });


        db = new DatabaseHandler(this);
        restaurants =  new ArrayList<>();
        restoList = new ArrayList<>();
        // Get items from database
        restoList = db.getAllRestaurants();
        for (Restaurant place : restoList) {
            Log.d("DATABASE_TEST_LIST", place.getName());
        }
        if (restoList != null){
            for (Restaurant resto : restoList) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(resto.getName());
                restaurant.setCategories(resto.getCategories());
                restaurant.setId(resto.getId());
                restaurant.setAverage_cost_for_two(resto.getAverage_cost_for_two());
                restaurants.add(restaurant);
            }

            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewAdapter = new RecyclerViewAdapter(this, restoList);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

        } else {
            /*Toast.makeText("For some reasons, informations about restaurants can't be reached.",  )*/
        }



        // tests for Serializable
         Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Serializable serial = extras.getSerializable("restosList");
            restoList = (ArrayList<Restaurant>) serial;
            Log.d("RESTOLIST_NAMES_2",  restoList.get(0).getName());
            Log.d("RESTOLIST_NAMES_2",  restoList.get(19).getName());
        }


    }



}

