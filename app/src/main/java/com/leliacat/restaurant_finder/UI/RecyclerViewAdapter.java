package com.leliacat.restaurant_finder.UI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leliacat.restaurant_finder.activities.ListRestoActivity;
import com.leliacat.restaurant_finder.data.DatabaseHandler;
import com.leliacat.restaurant_finder.model.Restaurant;
import com.leliacat.restaurant_finder.R;

import java.util.List;


//*************************************************************************************************************************************************
//*************************************************************************************************************************************************
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholder> {

    private Context context;
    private List<Restaurant> restaurants;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
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
        holder.average_cost_for_two.setText(restaurant.getAverage_cost_for_two());
    }

    //*****************************************************************************************************************************
    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    //*****************************************************************************************************************************
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            specialties =  (TextView) view.findViewById(R.id.listrow_specialties);
            average_cost_for_two =  (TextView) view.findViewById(R.id.listrow_average_price_for_two);
            infoButton =  (Button) view.findViewById(R.id.listrow_btn_moreinfo);

            infoButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen --> DetailsActivity
                    int position = getAdapterPosition(); // important to know which item is clicked

                    Restaurant restaurant = restaurants.get(position);
                    Intent intent = new Intent(context, ListRestoActivity.class);
                    intent.putExtra("name", restaurant.getName());
                    intent.putExtra("quantity", restaurant.getCategories());
                    intent.putExtra("id", restaurant.getId());
                    intent.putExtra("average_cost_for_two", restaurant.getAverage_cost_for_two());
                    context.startActivity(intent);
                }
            });
        }
        //**********************************************************
        @Override
        public void onClick(View v) {
                    int position = getAdapterPosition();
                    Restaurant grocery = restaurants.get(position);
                    // TODO : got to popup
        }


    }


}


