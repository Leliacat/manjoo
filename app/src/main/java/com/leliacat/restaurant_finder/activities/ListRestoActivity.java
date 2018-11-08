package com.leliacat.restaurant_finder.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.leliacat.restaurant_finder.data.DatabaseHandler;
import com.leliacat.restaurant_finder.model.Restaurant;
import com.leliacat.restaurant_finder.R;
import com.leliacat.restaurant_finder.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListRestoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Restaurant> restoList;
    private List<Restaurant> restaurants;
    private DatabaseHandler db;

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
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/



            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restoList = new ArrayList<>();
        restaurants =  new ArrayList<>();

        // Get items from database
        restoList = db.getAllRestaurants();

        for (Restaurant resto : restoList) {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(resto.getName());
            restaurant.setCategories(resto.getCategories());
            restaurant.setId(resto.getId());
            restaurant.setAverage_cost_for_two(resto.getAverage_cost_for_two());

            restaurants.add(restaurant);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, restaurants);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}

