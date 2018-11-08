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

import com.leliacat.restaurant_finder.activities.RestaurantsListActivity;
import com.leliacat.restaurant_finder.data.DatabaseHandler;
import com.leliacat.restaurant_finder.model.Restaurant;
import com.leliacat.restaurant_finder.R;

import java.util.List;

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

    @NonNull
    @Override
    public RecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new Viewholder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.Viewholder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.restaurantName.setText(restaurant.getName());
        holder.quantity.setText(restaurant.getQuantity());
        holder.dateAdded.setText(restaurant.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView restaurantName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public Viewholder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            restaurantName = (TextView) view.findViewById(R.id.listrow_name);
            quantity =  (TextView) view.findViewById(R.id.listrow_quantity);
            dateAdded =  (TextView) view.findViewById(R.id.listrow_date_added);
            editButton =  (Button) view.findViewById(R.id.listrow_editButton);
            deleteButton =  (Button) view.findViewById(R.id.listrow_deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen --> DetailsActivity
                    int position = getAdapterPosition(); // important to know which item is clicked

                    Restaurant restaurant = restaurants.get(position);
                    Intent intent = new Intent(context, RestaurantsListActivity.class);
                    intent.putExtra("name", restaurant.getName());
                    intent.putExtra("quantity", restaurant.getQuantity());
                    intent.putExtra("id", restaurant.getId());
                    intent.putExtra("date", restaurant.getDateItemAdded());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.listrow_editButton :
                    int position = getAdapterPosition();
                    Restaurant grocery = restaurants.get(position);
                    editItem(grocery);
                    break;
                case R.id.listrow_deleteButton :
                    position = getAdapterPosition();
                    grocery = restaurants.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }


        public void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.confirmation_button_no);
            Button yesButton = (Button) view.findViewById(R.id.confirmation_button_yes);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item.
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteRestaurant(id);
                    restaurants.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();


                }
            });

        }


        public void editItem(final Restaurant restaurant) {

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.popup_grocery_item);
            final EditText quantity = (EditText) view.findViewById(R.id.popup_grocery_quantity);
            final TextView title = (TextView) view.findViewById(R.id.popup_title);

            title.setText("Add text review");
            Button saveButton = (Button) view.findViewById(R.id.popup_save_button);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    //Update item
                    restaurant.setName(groceryItem.getText().toString());
                    restaurant.setQuantity(quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty()
                            && !quantity.getText().toString().isEmpty()) {
                        db.updateRestaurant(restaurant);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else {
                        Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();

                }
            });

        }

    }
}


