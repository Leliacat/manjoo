package com.leliacat.restaurant_finder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.leliacat.restaurant_finder.R;
import com.leliacat.restaurant_finder.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsListActivity extends AppCompatActivity {

/*
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*

            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems =  new ArrayList<>();

        // Get items from database
        groceryList = db.getAllGroceries();

        for (Grocery c : groceryList) {
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty: " + c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());

            listItems.add(grocery);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    */










    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;
    private Bundle extras;
    private List<String> infosRestoList;
    private List<Restaurant> restosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);

        listView = findViewById(R.id.restolist_listview);
        extras = getIntent().getExtras();

        if(extras != null) {
            restosList = (ArrayList<Restaurant>) extras.getSerializable("restoList");

            String name =  restosList.get(0).getName();
            Log.d("RESTOLIST_NAME2", name);

            infosRestoList = new ArrayList<>();

            if (restosList != null) {
                for ( Restaurant resto : restosList){
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(
                            resto.getName()+ "\n" + resto.getCategories()+ "\n" + resto.getAddress() +
                                    "\n" + "Rating" + resto.getRating() + " /5" + "\n" + "Average Price for 2 : " + resto.getAverage_cost_for_two() +
                                    resto.getCurrency() + "\n" + "More info on this link : " + resto.getDetail_link());
                    stringBuilder.append("\n\n");
                    Log.d("RESTOLIST_INFOS", stringBuilder.toString());
                    infosRestoList.add(stringBuilder.toString());
                }
            }

            arrayAdapter = new ArrayAdapter(RestaurantsListActivity.this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, infosRestoList);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }


    }

}
