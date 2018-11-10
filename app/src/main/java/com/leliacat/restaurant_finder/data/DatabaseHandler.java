package com.leliacat.restaurant_finder.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.leliacat.restaurant_finder.model.Restaurant;
import com.leliacat.restaurant_finder.util.Constants;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    private String TAG = "DATABASE_Handler";


    //////////////////////////////////////////////////////////////// CONSTRUCTOR /////////////////////////////////////////////////////////////

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }



    ///////////////////////////////////////////////////////////////// METHODS ////////////////////////////////////////////////////////////////



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESTAURANT_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INT PRIMARY KEY, " +
                Constants.KEY_RESTO_NAME + " TEXT, " +
                Constants.KEY_RESTO_ADDRESS + " TEXT, " +
                Constants.KEY_RESTO_LOCALITY + " TEXT, " +
                Constants.KEY_RESTO_CITY + " TEXT, " +
                Constants.KEY_RESTO_CITY_ID + " TEXT, " +
                Constants.KEY_RESTO_ZIPCODE + " TEXT, " +
                Constants.KEY_RESTO_COUNTRY_ID + " TEXT, " +
                Constants.KEY_RESTO_LOCALITY_VERBOSE + " TEXT, " +
                Constants.KEY_RESTO_SPECIALTIES + " TEXT, " +
                Constants.KEY_RESTO_RATING + " TEXT, " +
                Constants.KEY_RESTO_AVERAGE_COST_FOR_2  + " INT, " +
                Constants.KEY_RESTO_CURRENCY + " TEXT, " +
                Constants.KEY_RESTO_LATITUDE + " TEXT, " +
                Constants.KEY_RESTO_LONGITUDE + " TEXT, " +
                Constants.KEY_RESTO_DETAILS_LINK + " TEXT);";
        db.execSQL(CREATE_RESTAURANT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    //********************************************************************* CRUDS OPERATIONS **************************************************//
    //**************************************************************** Create Read Update Delete **********************************************//

    // Add Restaurant ************************************************************************************************************************
    public void addRestaurant(Restaurant resto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ID, Integer.parseInt(resto.getId()));
        values.put(Constants.KEY_RESTO_NAME, resto.getName());
        values.put(Constants.KEY_RESTO_ADDRESS, resto.getAddress());
        values.put(Constants.KEY_RESTO_LOCALITY, resto.getAddress());
        values.put(Constants.KEY_RESTO_CITY, resto.getAddress());
        values.put(Constants.KEY_RESTO_CITY_ID, resto.getAddress());
        values.put(Constants.KEY_RESTO_ZIPCODE, resto.getAddress());
        values.put(Constants.KEY_RESTO_COUNTRY_ID, resto.getAddress());
        values.put(Constants.KEY_RESTO_LOCALITY_VERBOSE, resto.getAddress());
        values.put(Constants.KEY_RESTO_SPECIALTIES, resto.getCategories());
        values.put(Constants.KEY_RESTO_RATING, resto.getRating().toString() );
        values.put(Constants.KEY_RESTO_AVERAGE_COST_FOR_2, resto.getAverage_cost_for_two());
        values.put(Constants.KEY_RESTO_CURRENCY, resto.getCurrency());
        values.put(Constants.KEY_RESTO_LATITUDE, resto.getLatitude().toString());
        values.put(Constants.KEY_RESTO_LONGITUDE, resto.getLongitude().toString());
        values.put(Constants.KEY_RESTO_DETAILS_LINK, resto.getDetail_link());


        //insert row in table
        db.insert(Constants.TABLE_NAME, null, values);
        Log.d(TAG, "addRestaurant: saved to DB");
    }

    // Get a resto item ************************************************************************************************************************
    public Restaurant getRestaurant(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Restaurant resto = new Restaurant();
        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[] {Constants.KEY_ID, Constants.KEY_RESTO_NAME,Constants.KEY_RESTO_ADDRESS, Constants.KEY_RESTO_LOCALITY,
                        Constants.KEY_RESTO_CITY, Constants.KEY_RESTO_CITY_ID,Constants.KEY_RESTO_ZIPCODE,
                        Constants.KEY_RESTO_COUNTRY_ID,Constants.KEY_RESTO_LOCALITY_VERBOSE, Constants.KEY_RESTO_SPECIALTIES,
                        Constants.KEY_RESTO_RATING, Constants.KEY_RESTO_AVERAGE_COST_FOR_2, Constants.KEY_RESTO_CURRENCY,
                        Constants.KEY_RESTO_LATITUDE, Constants.KEY_RESTO_LONGITUDE, Constants.KEY_RESTO_DETAILS_LINK},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)},
                null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();

            resto.setId(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)));
            resto.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_NAME)));

            resto.setAddress(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_ADDRESS)));
            resto.setLocality(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_LOCALITY)));
            resto.setCity(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_CITY)));
            resto.setCity_id(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_CITY_ID)));
            resto.setZipcode(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_ZIPCODE)));
            resto.setCountry_id(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_COUNTRY_ID)));
            resto.setLocality(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_LOCALITY_VERBOSE)));

            resto.setCategories(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_SPECIALTIES)));
            resto.setRating(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RESTO_RATING)));
            resto.setAverage_cost_for_two(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_AVERAGE_COST_FOR_2))));
            resto.setCurrency(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_CURRENCY)));
            resto.setLatitude(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RESTO_LATITUDE)));
            resto.setLongitude(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RESTO_LONGITUDE)));
            resto.setDetail_link(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_DETAILS_LINK)));
        }
        return resto;
    }

    // Get all restaurants ************************************************************************************************************************
    public List<Restaurant> getAllRestaurants(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Restaurant> restoList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                        Constants.KEY_ID, Constants.KEY_RESTO_NAME,Constants.KEY_RESTO_ADDRESS, Constants.KEY_RESTO_LOCALITY,
                        Constants.KEY_RESTO_CITY, Constants.KEY_RESTO_CITY_ID,Constants.KEY_RESTO_ZIPCODE,
                        Constants.KEY_RESTO_COUNTRY_ID,Constants.KEY_RESTO_LOCALITY_VERBOSE, Constants.KEY_RESTO_SPECIALTIES,
                        Constants.KEY_RESTO_RATING, Constants.KEY_RESTO_AVERAGE_COST_FOR_2, Constants.KEY_RESTO_CURRENCY,
                        Constants.KEY_RESTO_LATITUDE, Constants.KEY_RESTO_LONGITUDE, Constants.KEY_RESTO_DETAILS_LINK},
                null, null, null, null, Constants.KEY_RESTO_RATING + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Restaurant resto = new Restaurant();

                resto.setId(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)));
                resto.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_NAME)));
                resto.setAddress(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_ADDRESS)));
                resto.setLocality(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_LOCALITY)));
                resto.setCity(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_CITY)));
                resto.setCity_id(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_CITY_ID)));
                resto.setZipcode(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_ZIPCODE)));
                resto.setCountry_id(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_COUNTRY_ID)));
                resto.setLocality(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_LOCALITY_VERBOSE)));
                resto.setCategories(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_SPECIALTIES)));
                resto.setRating(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RESTO_RATING)));
                resto.setAverage_cost_for_two(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_AVERAGE_COST_FOR_2))));
                resto.setCurrency(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_CURRENCY)));
                resto.setLatitude(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RESTO_LATITUDE)));
                resto.setLongitude(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RESTO_LONGITUDE)));
                resto.setDetail_link(cursor.getString(cursor.getColumnIndex(Constants.KEY_RESTO_DETAILS_LINK)));

                // Add to the resto List
                restoList.add(resto);
            } while (cursor.moveToNext());
        }
        return restoList;
    }

    // Get all restaurants ID *******************************************************************************************************************
    public List<Integer> getAllRestaurantsIDs(){
        List<Integer> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columns = {Constants.KEY_ID};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        int id = cursor.getColumnIndex(Constants.KEY_ID);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Integer rowId = cursor.getInt(id);
            results.add(rowId);
        }
        cursor.close();
        db.close();

        return results;
    }



    // Update restaurant ************************************************************************************************************************
    public int updateRestaurant (Restaurant resto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_ID, Integer.parseInt(resto.getId()));
        values.put(Constants.KEY_RESTO_NAME, resto.getName());
        values.put(Constants.KEY_RESTO_ADDRESS, resto.getAddress());
        values.put(Constants.KEY_RESTO_LOCALITY, resto.getLocality());
        values.put(Constants.KEY_RESTO_CITY, resto.getCity());
        values.put(Constants.KEY_RESTO_CITY_ID, resto.getCity_id());
        values.put(Constants.KEY_RESTO_ZIPCODE, resto.getZipcode());
        values.put(Constants.KEY_RESTO_COUNTRY_ID, resto.getCountry_id());
        values.put(Constants.KEY_RESTO_LOCALITY_VERBOSE, resto.getLocality_verbose());
        values.put(Constants.KEY_RESTO_SPECIALTIES, resto.getCategories());
        values.put(Constants.KEY_RESTO_RATING, resto.getRating());
        values.put(Constants.KEY_RESTO_AVERAGE_COST_FOR_2, resto.getAverage_cost_for_two());
        values.put(Constants.KEY_RESTO_CURRENCY, resto.getCurrency());
        values.put(Constants.KEY_RESTO_LATITUDE, resto.getLatitude());
        values.put(Constants.KEY_RESTO_LONGITUDE, resto.getLongitude());
        values.put(Constants.KEY_RESTO_DETAILS_LINK, resto.getDetail_link());

        //updated row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + " =?", new String[] {String.valueOf(resto.getId())});
    }

    // Delete restaurant ************************************************************************************************************************
    public void deleteRestaurant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " =?",
                new String[] {String.valueOf(id)});
        db.close();
    }


    // Get count of restaurants ************************************************************************************************************************
    public int getRestaurantsCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }




}
