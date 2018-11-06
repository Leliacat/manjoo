package com.leliacat.restaurant_finder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;
    private Bundle extras;

    private List<Restaurant> restosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);

        listView = findViewById(R.id.restolist_listview);


        extras = getIntent().getExtras();
        if(extras != null) {
            restosList = (ArrayList<Restaurant>) extras.getSerializable("restoList");
            arrayAdapter = new ArrayAdapter(RestaurantsListActivity.this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, restosList);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }


    }

}
