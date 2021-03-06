package com.leliacat.foodpornproject.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leliacat.foodpornproject.R;
import com.leliacat.foodpornproject.UI.CustomInfoWindow;
import com.leliacat.foodpornproject.data.DatabaseHandler;
import com.leliacat.foodpornproject.model.Restaurant;
import com.leliacat.foodpornproject.util.Accelerometer;
import com.leliacat.foodpornproject.util.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, Serializable {

    private GoogleMap mMap;
    private static final LatLng ARCTIC = new LatLng(58.5010733,-52.6292835);
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private RequestQueue queue;
    private static MapsActivity mInstance;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private Button btnShowList;
    private Intent listIntent;
    private ArrayList<Restaurant> restosList;
    private Marker here;
    private DatabaseHandler db;
    private Accelerometer accelerometer;


    //****************************************************************  ON CREATE **********************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mInstance = this;
        queue = Volley.newRequestQueue(this);
        /* deleteDatabase(Constants.DB_NAME);*/
        db = new DatabaseHandler(this);

        btnShowList = (Button) findViewById(R.id.map_btn_showlist);
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listIntent = new Intent(MapsActivity.this, ListRestoActivity.class);

                startActivity(listIntent);
                finish();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onStop();

        if (restosList != null ){
            for(Restaurant rst : restosList){
                Log.d("RESTOLIST_NAMES",  rst.getName());
            }
        }

        // Get items from database - TEST
        /*List<Restaurant> restaurants = db.getAllRestaurants();
        String name = restaurants.get(2).getName();
        Log.d("DB_TEST2", "onStop: " + name );*/


        // stop the locationlistener when we leave the map activity
        if(locationManager !=null)
            locationManager.removeUpdates(locationListener);
        this.stopLockTask();
    }

    //***************************************************************  ON MAP READY *****************************************************************
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //to add personnalized infowindows on restaurants' markers
        mMap.setInfoWindowAdapter(new CustomInfoWindow((getApplicationContext())));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        // see this link :  https://developer.android.com/guide/topics/location/strategies#Flow
        // and this one : https://www.tutorialspoint.com/android/android_location_based_services.htm

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                here.setPosition(latLng);

                /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));*/
                /* mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));*/

                List results = db.getAllRestaurantsIDs();
                for (Object idt : results){
                    int key = Integer.parseInt(idt.toString());
                    db.deleteRestaurant(key);
                    // to check if database has been emptied
                    try {
                        Log.d("DB_VIDE?", "onLocationChanged: " + db.getAllRestaurants().get(0).getName());
                        Log.d("DB_VIDE?", "onLocationChanged: " + db.getAllRestaurants().get(18).getName());
                    } catch (Exception e){
                        Log.d("DB_VIDE?", "onLocationChanged: " + e );
                    }
                }
                // Called when a new location is found by the location provider.
                getRestaurants();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) { //less than Marshmallow
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // we ask for permisssion
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            } else {
                // we have permission!

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                // FROM ANDROID DOCUMENTATION :
                //The first parameter in requestLocationUpdates() is the type of location provider to use (in this case, the GPS).
                // You can control the frequency at which your listener receives updates with the second and third parameter
                // —the second is the minimum time interval between notifications
                // and the third is the minimum change in distance between notifications—
                // setting both to zero requests location notifications as frequently as possible.
                // The last parameter is your LocationListener, which receives callbacks for location updates.

                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //The time it takes for your location listener to receive the first location fix is often too long for users wait.
                // Until a more accurate location is provided to your location listener,
                // you should utilize a cached location by calling getLastKnownLocation(String).


                if (currentLocation != null){
                   /* Log.d("CURRENT_LOCATION", "onMapReady: " + String.valueOf(currentLocation.getLatitude())+ " " + currentLocation.getLongitude());*/
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    // define rendering type of the map
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    try {
                        // Customise map styling via JSON file
                        boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( this, R.raw.maps_style));
                        if (!success) {
                            Log.e("MAP_STYLE", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("MAP_STYLE", "Can't find style. Error: ", e);
                    }


                    //add marker on map at the coordinates where the phone is located
                    here = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("PANDA A FAIM")
                            .snippet("Here you are, you wild beast!")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    here.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mediumpandahead));
                    here.setTag("HERE");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));


                    // get the 30 best rated restaurants around the user in a radius of 20 km
                    // and add markers on the map where the restaurants are located
                    getRestaurants();

                    //add a TARDIS
                    // you think it's too big ? this app is just an exercise, so take it easy, alright ?
                    Marker mTardis = mMap.addMarker(new MarkerOptions()
                            .position(ARCTIC)
                            .title("Run!"));
                    mTardis.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.tardis));
                    mTardis.setDraggable(true);
                    mTardis.setTag("TARDIS");

                    accelerometer = new Accelerometer(this, new Accelerometer.MyCallBackInterface() {
                        @Override
                        public void onShakesDetected() {
                            Log.d("SHAKE_BABY", "onShakesDetected: I've just been shaked, love it!");
                            if (restosList != null){
                                // we will use it to get a random index in the list of restaurants
                                int randomIndex = new Random().nextInt(restosList.size());
                                Restaurant chosenResto = restosList.get(randomIndex);
                                Intent intentRandom = new Intent(MapsActivity.this, DetailsRestoActivity.class);
                                intentRandom.putExtra("name", chosenResto.getName());
                                intentRandom.putExtra("specialties", chosenResto.getCategories());
                                intentRandom.putExtra("address", chosenResto.getAddress());
                                intentRandom.putExtra("locality", chosenResto.getLocality());
                                intentRandom.putExtra("city", chosenResto.getCity());
                                intentRandom.putExtra("city_id", chosenResto.getCity_id());
                                intentRandom.putExtra("zipcode", chosenResto.getZipcode());
                                intentRandom.putExtra("country_id", chosenResto.getCountry_id());
                                intentRandom.putExtra("locality_verbose", chosenResto.getLocality_verbose());
                                intentRandom.putExtra("rating", chosenResto.getRating().toString());
                                intentRandom.putExtra("cost_for_two", String.valueOf(chosenResto.getAverage_cost_for_two()));
                                intentRandom.putExtra("currency", chosenResto.getCurrency());
                                intentRandom.putExtra("link", chosenResto.getDetail_link());
                                mInstance.startActivity(intentRandom);
                            }

                        }
                    });


                }
            }
        }

    }

    //**********************************************************  ON REQUEST PERMISSIONS RESULT ********************************************************
    // FROM ANDROID DOCUMENTATION :
    // When the user responds to your app's permission request,
    // the system invokes your app's onRequestPermissionsResult() method, passing it the user response.
    // Your app has to override that method to find out whether the permission was granted.
    // The callback is passed the same request code you passed to requestPermissions().
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Register the listener with the Location Manager to receive location updates
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
        }
    }


    /////////////////////////////////////////////////////////// OTHER METHOD TO HANDLE API REQUESTS ///////////////////////////////////////////////////
    /////////////////////////////////////////found on this link : https://androidclarified.com/android-volley-example//////////////////////////////////
    //*************************************************************************************************************************************************

    public static synchronized MapsActivity getInstance() {
        return mInstance;
    }

    // Ensures that the variable is instantiated only once
    // and the same instance is used throughout the application
    public RequestQueue getRequestQueue() {
        if (queue == null)
            queue = Volley.newRequestQueue(getApplicationContext());
        return queue;
    }

    // Setting a tag to every request helps in grouping them.
    // Tags act as identifier for requests and can be used while cancelling them
    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    // Cancel all the requests matching with the given tag
    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }


    ///////////////////////////////////////////////////////// GET ALL RESTAURANTS OBJECTS  ///////////////////////////////////////////////////////
    ///////////////////////////////// more info on this link :  https://developer.android.com/training/volley/simple  ////////////////////////////
    //*************************************************************************************************************************************************

    public void getRestaurants() {

        restosList = new ArrayList<>();
        String lat = String.valueOf(currentLocation.getLatitude());
        String lng = String.valueOf(currentLocation.getLongitude());

        // useful to pass coordinates of the user's current location into the URL
        // this way the response gives us the restaurants located around the user
        String CUSTOM_URL = Constants.URL
                + Constants.LIMIT
                + Constants.URL_COMPLEMENT_1 + lat
                + Constants.URL_COMPLEMENT_2 + lng
                + Constants.URL_COMPLEMENT_3;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, CUSTOM_URL, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray restaurants = response.getJSONArray("restaurants");
                            Log.d("ZOMATO_API", "onResponse: " + restaurants);

                            for (int i = 0; i < restaurants.length(); i ++) {

                                Restaurant resto = new Restaurant();

                                JSONObject restaurant = restaurants.getJSONObject(i).getJSONObject("restaurant");
                                Log.d("ZOMATO_restaurant ", "onResponse: " + restaurant);
                                Log.d("ZOMATO_name ", restaurant.getString("name"));
                                Log.d("ZOMATO_location ", restaurant.getJSONObject("location").toString());

                                String id = restaurant.getJSONObject("R").getString("res_id");
                                Log.d("RESTO_ID", "onResponse: ");
                                resto.setId(id);

                                // get restaurant coordinates
                                Double latitude = restaurant.getJSONObject("location").getDouble("latitude");
                                Double longitude = restaurant.getJSONObject("location").getDouble("longitude");
                                resto.setLatitude(latitude);
                                resto.setLongitude(longitude);
                                LatLng restoCoordinates = new LatLng(resto.getLatitude(),resto.getLongitude());
                                Log.d("RESTO_LATLNG", "onResponse: " + restoCoordinates.toString());


                                JSONObject location = restaurant.getJSONObject("location");
                                // assign value to properties related to address of the Restaurant object
                                resto.setAddress(location.getString("address"));
                                resto.setLocality(location.getString("locality"));
                                resto.setCity(location.getString("city"));
                                resto.setCity_id(location.getString("city_id"));
                                resto.setZipcode(location.getString("zipcode"));
                                resto.setCountry_id(location.getString("country_id"));
                                resto.setLocality_verbose(location.getString("locality_verbose"));
                                Log.d("RESTO_LOCATION_Address",  resto.getAddress());

                                // assign value to category property of the Restaurant object
                                resto.setCategories(restaurant.getString("cuisines"));
                                Log.d("RESTO_CATEGORY",  resto.getCategories());

                                // assign value to name property of the Restaurant object
                                resto.setName(restaurant.getString("name"));
                                Log.d("RESTO_NAME",  resto.getName());

                                // assign value to rating property of the Restaurant object
                                Double rating = restaurant.getJSONObject("user_rating").getDouble("aggregate_rating");
                                Log.d("RESTO_RATING", "number of stars " + rating.toString());
                                resto.setRating(rating);
                                Log.d("RESTO_RATING", String.valueOf(resto.getRating()));

                                // assign value to average_cost_for_two property of the Restaurant object
                                resto.setAverage_cost_for_two(restaurant.getInt("average_cost_for_two"));
                                Log.d("RESTO_COST", String.valueOf(resto.getAverage_cost_for_two()));

                                // assign value to average_cost_for_two property of the Restaurant object
                                resto.setCurrency(restaurant.getString("currency"));
                                Log.d("RESTO_CURRENCY", resto.getCurrency());

                                //  assign value to average_cost_for_two property of the Restaurant object
                                resto.setDetail_link(restaurant.getString("url"));
                                Log.d("RESTO_URL", resto.getDetail_link());

                                // add Marker to map
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                markerOptions.title(resto.getName());
                                markerOptions.snippet( resto.getCategories() + "\n" +
                                                       "Rating: " + resto.getRating().toString());

                                markerOptions.position(restoCoordinates);
                                Marker marker = mMap.addMarker(markerOptions);
                                marker.setTag(resto.getId());

                                restosList.add(resto);
                                db.addRestaurant(resto);

                            }

                            for (int i = 0; i< restosList.size(); i++) {
                                Log.d("RESTOLIST_RESULTS", restosList.get(i).getName() + " __at index " + String.valueOf(i));
                            }

                            ArrayList<Restaurant> restaurantList = (ArrayList) db.getAllRestaurants();
                            for ( Restaurant rst : restaurantList) {
                                Log.d("DB_TEST1", rst.getName());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            // Passing some request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                /*headers.put("Accept:", "application/json");*/
                headers.put("user-key", Constants.API_KEY);
                return headers;
            }
            // to understand how to add a header to the request I used those links:
            // https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            // https://stackoverflow.com/questions/35218464/volley-jsonobjectrequest-send-headers-in-get-request

        };

        // Adding the request to the queue along with a unique string tag
        MapsActivity.getInstance().addToRequestQueue(jsonObjectRequest, "Request_allRestaurants" );
        // it equals to :   queue.add(jsonObjectRequest);

    }


    //////////////////////////////////////////////////// OTHER METHODS WITH API REQUESTS ////////////////////////////////////////////////////////
    //*************************************************************************************************************************************************
    // allow to get more details about a specific restaurant
    // example URL  https://developers.zomato.com/api/v2.1/restaurant?res_id=16774318
    // alternative solution (better maybe) would be to pass the object resto into the marker tag
    // and then get back the informations from there
    public void getRestaurantDetails(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL_RESTO_DETAILS + id , new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                            View view = getLayoutInflater().inflate(R.layout.popup, null);

                            dialogBuilder.setView(view);
                            alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            // we get the elements from the popup layout
                            Button dismissBtn = (Button) view.findViewById(R.id.popup_btn_dismiss);
                            Button dismissBtn2 = (Button) view.findViewById(R.id.popup_btn_dismiss2);
                            TextView title = (TextView) view.findViewById(R.id.popup_title);
                            TextView specialties = (TextView) view.findViewById(R.id.popup_specialties);
                            TextView rating = (TextView) view.findViewById(R.id.popup_rating);
                            TextView address = (TextView) view.findViewById(R.id.popup_address);
                            TextView price = (TextView) view.findViewById(R.id.popup_price);
                            TextView link = (TextView) view.findViewById(R.id.popup_textlink);

                            JSONObject location = response.getJSONObject("location");

                            title.setText(response.getString("name"));
                            specialties.setText(response.getString("cuisines"));
                            /* I could/should have used a : StringBuilder completeAddress = new StringBuilder();
                            * Stringbuilder.append() */
                            String completeAddress =
                                    location.getString("address")+ "\n" +
                                    location.getString("locality")+ "\n" +
                                    location.getString("city")+ "\n" +
                                    location.getString("zipcode")+ "\n" +
                                    location.getString("country_id");
                            Log.d("RESTO_COMPLETE_ADDRESS", "onResponse: " + completeAddress);
                            address.setText(completeAddress);

                            String restoRating = response.getJSONObject("user_rating").getString("aggregate_rating");
                            rating.setText("Rating: " + restoRating);
                            price.setText("Average cost for two: " + response.getString("average_cost_for_two")
                                            + response.getString("currency"));
                            link.setText("More info on this link: " + "\n" + response.getString("url"));


                            /*// we'll display a web content inside our app through the webview element
                            WebView webView = (WebView) view.findViewById(R.id.popup_webview);
                            String weblink = response.getString("url");
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(weblink);*/
                            // I decided to remove it, because it was not an elegant solution,
                            // just keeping the code as a model if I want to use it later for something else

                            // buttons to close the popup
                            dismissBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            dismissBtn2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            // Passing some request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                /*headers.put("Accept:", "application/json");*/
                headers.put("user-key", Constants.API_KEY);
                return headers;
            }
        };

        // Adding the request to the queue along with a unique string tag
        MapsActivity.getInstance().addToRequestQueue(jsonObjectRequest, "Request_restaurantDetails" );
    }



    //////////////////////////////////////////////////// METHODS IMPLEMENTED ////////////////////////////////////////////////////////
    ////////////////////////// FROM GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener ////////////////////////////

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("RESTO_TAG", "onInfoWindowClick: " + marker.getTag().toString());
        String id = marker.getTag().toString();
        getRestaurantDetails(id);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
