package com.leliacat.foodpornproject.UI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leliacat.foodpornproject.activities.DetailsRestoActivity;
import com.leliacat.foodpornproject.model.Restaurant;
import com.leliacat.foodpornproject.R;

import java.util.List;


//*************************************************************************************************************************************************
//*************************************************************************************************************************************************
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholder> {

    private Context context;
    private List<Restaurant> restaurants;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    ///////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////

    public RecyclerViewAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }


    /////////////////////////////////////// METHODS //////////////////////////////////////////

    //*****************************************************************************************************************************
    @NonNull
    @Override
    public RecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new Viewholder(view, context);
    }

    //*****************************************************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.Viewholder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.restaurantName.setText(restaurant.getName());
        holder.specialties.setText(restaurant.getCategories());
        holder.average_cost_for_two.setText( "Average cost for two: " + Integer.toString(restaurant.getAverage_cost_for_two()) + restaurant.getCurrency());
    }

    //*****************************************************************************************************************************
    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    //*****************************************************************************************************************************
    public class Viewholder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView restaurantName;
        public TextView specialties;
        public TextView average_cost_for_two;
        public Button infoButton;

        public int id;

        //**********************************************************
        public Viewholder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            restaurantName = (TextView) view.findViewById(R.id.listrow_name);
            specialties = (TextView) view.findViewById(R.id.listrow_specialties);
            average_cost_for_two = (TextView) view.findViewById(R.id.listrow_average_price_for_two);
            infoButton = (Button) view.findViewById(R.id.listrow_btn_moreinfo);

            infoButton.setOnClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();// important to know which item is clicked
            Restaurant restaurant = restaurants.get(position);

            ////go to next screen --> DetailsRestoActivity
            Intent intent = new Intent(context, DetailsRestoActivity.class);
            intent.putExtra("name", restaurant.getName());
            intent.putExtra("specialties", restaurant.getCategories());
            intent.putExtra("address", restaurant.getAddress());
            intent.putExtra("locality", restaurant.getLocality());
            intent.putExtra("city", restaurant.getCity());
            intent.putExtra("city_id", restaurant.getCity_id());
            intent.putExtra("zipcode", restaurant.getZipcode());
            intent.putExtra("country_id", restaurant.getCountry_id());
            intent.putExtra("locality_verbose", restaurant.getLocality_verbose());
            intent.putExtra("rating", restaurant.getRating().toString());
            intent.putExtra("cost_for_two", String.valueOf(restaurant.getAverage_cost_for_two()));
            intent.putExtra("currency", restaurant.getCurrency());
            intent.putExtra("link", restaurant.getDetail_link());
            context.startActivity(intent);



        }
    }
}


