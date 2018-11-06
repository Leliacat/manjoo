package com.leliacat.restaurant_finder.activities;

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
import com.leliacat.restaurant_finder.R;
import com.leliacat.restaurant_finder.UI.CustomInfoWindow;
import com.leliacat.restaurant_finder.model.Restaurant;
import com.leliacat.restaurant_finder.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final LatLng ARCTIC = new LatLng(58.5010733,-52.6292835);
    public ArrayList<Restaurant> restosList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private RequestQueue queue;
    private static MapsActivity mInstance;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private Button btnShowList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnShowList = (Button) findViewById(R.id.map_btn_showlist);
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listIntent = new Intent(MapsActivity.this, RestaurantsListActivity.class);
                Bundle bundle = new Bundle;
                bundle.putSerializable("ARRAYLIST",restosList);
                listIntent.putExtra("restosList", bundle);
                startActivity(listIntent);
            }
        });

        mInstance = this;
        queue = Volley.newRequestQueue(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // pour rajouter fenêtre d'infos personnalisée sur les markers de restaurants
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
                // Called when a new location is found by the location provider.
                /*Log.d("GPS_Location", "onLocationChanged: " + location.toString());*/
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
                //we ask for permisssion
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            } else {
                //we have permission!

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
                    Marker here = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Here you are, you wild beast!")
                            /*.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))*/);
                    here.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mediumpandahead));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    // get the 30 best rated restaurants around the user in a radius of 20 km
                    // and add markers on the map where the restaurants are located
                    getRestaurants();

                    //add a TARDIS
                    // you think it's too big ? who cares ? this app is just an exercise, so take it easy, alright ?
                    Marker mTardis = mMap.addMarker(new MarkerOptions()
                            .position(ARCTIC)
                            .title("Run!"));
                    mTardis.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.tardis));
                }
            }
        }

    }


    // FROM ANDROID DOCUMENTATION :
    //When the user responds to your app's permission request,
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


//////////////////////////////////// OTHER METHOD TO HANDLE API REQUESTS ///////////////////////////////

    /////////found on this link : https://androidclarified.com/android-volley-example///////////

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


    ////////////////////////////////////// GET ALL RESTAURANTS OBJECTS  /////////////////////////////////////
    //////////// more info on this link :  https://developer.android.com/training/volley/simple  ///////////

    public ArrayList<Restaurant> getRestaurants() {
        final Restaurant resto = new Restaurant();

        String lat = String.valueOf(currentLocation.getLatitude());
        String lng = String.valueOf(currentLocation.getLongitude());

        // permet de passer dans l'URL les coordonnées de la position actuelle, pour chercher les restaurants autour de l'utilisateur
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
                                JSONObject restaurant = restaurants.getJSONObject(i).getJSONObject("restaurant");
                                Log.d("ZOMATO_restaurant ", "onResponse: " + restaurant);
                                Log.d("ZOMATO_name ", restaurant.getString("name"));
                                Log.d("ZOMATO_location ", restaurant.getJSONObject("location").toString());

                                int id = Integer.parseInt(restaurant.getJSONObject("R").getString("id"));
                                Log.d("RESTO_ID", "onResponse: ");
                                resto.setId(id);

                                // get restaurant coordinates
                                Double latitude = restaurant.getJSONObject("location").getDouble("latitude");
                                Double longitude = restaurant.getJSONObject("location").getDouble("longitude");
                                   //pour changer en float ou double : Float.parseFloat("25"); ou Double.value of("") ou location.getDouble("longitude")

                                // assign value to property coordinates of the Restaurant object
                                LatLng restoCoordinates = new LatLng(latitude,longitude);
                                resto.setCoordinates(restoCoordinates);
                                Log.d("RESTO_LATLNG", "onResponse: " + resto.getCoordinates().toString());

                                // assign value to property address of the Restaurant object
                                // the JSONobject location have the following properties (that we want to put in the List address) :
                                // "address", "locality", "city", "city_id", "latitude", "longitude", "zipcode", "country_id", "locality_verbose"
                                JSONObject location = restaurant.getJSONObject("location");
                                List<String> restoAddress = new ArrayList<>();
                                restoAddress.add(location.getString("address"));
                                restoAddress.add(location.getString("locality"));
                                restoAddress.add(location.getString("city"));
                                restoAddress.add(location.getString("city_id"));
                                restoAddress.add(location.getString("zipcode"));
                                restoAddress.add(location.getString("country_id"));
                                restoAddress.add(location.getString("locality_verbose"));
                                resto.setAddress(restoAddress);
                                Log.d("RESTO_LOCATION_Address",  resto.getAddress().get(0));

                                // assign value to category property of the Restaurant object
                                resto.setCategories(restaurant.getString("cuisines"));
                                Log.d("RESTO_CATEGORY",  resto.getCategories());

                                // assign value to name property of the Restaurant object
                                resto.setName(restaurant.getString("name"));
                                Log.d("RESTO_NAME",  resto.getName());

                                // assign value to rating property of the Restaurant object
                                Double rating = restaurant.getJSONObject("user_rating").getDouble("aggregate_rating");
                                Log.d("RESTO_RATING_", "number of stars " + rating.toString());
                                resto.setRating(rating);
                                Log.d("RESTO_RATING_", String.valueOf(resto.getRating()));

                                // assign value to average_cost_for_two property of the Restaurant object
                                resto.setAverage_cost_for_two(restaurant.getInt("average_cost_for_two"));
                                Log.d("RESTO_COST", String.valueOf(resto.getAverage_cost_for_two()));

                                // assign value to average_cost_for_two property of the Restaurant object
                                resto.setCurrency(restaurant.getString("currency"));
                                Log.d("RESTO_CURRENCY", resto.getCurrency());

                                //  assign value to average_cost_for_two property of the Restaurant object
                                resto.setDetail_link(restaurant.getString("url"));
                                Log.d("RESTO_URL", resto.getDetail_link());

                                //add Marker to map
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                markerOptions.title(resto.getName());
                                markerOptions.snippet( resto.getCategories() + "\n" +
                                                       "Rating: " + resto.getRating().toString());

                                markerOptions.position(resto.getCoordinates());
                                Marker marker = mMap.addMarker(markerOptions);
                                marker.setTag(resto);

                                restosList.add(resto);
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
            //to understand how to add a header to the request I used those links:
            // https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            // https://stackoverflow.com/questions/35218464/volley-jsonobjectrequest-send-headers-in-get-request

        };

        //Adding the request to the queue along with a unique string tag
        /*MapsActivity.getInstance().addToRequestQueue(jsonObjectRequest, "headerRequest" );*/
         queue.add(jsonObjectRequest);
         return restosList;
    }


    ////////////////////////////////////// OTHER METHODS WITH API REQUESTS //////////////////////////////////////////


    // allow to get more details about a specific restaurant
    // exemple d'URL https://developers.zomato.com/api/v2.1/restaurant?res_id=16774318
    public void getRestaurantDetails(Restaurant resto) {

                    String weblink = resto.getDetail_link();

                    dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                    View view = getLayoutInflater().inflate(R.layout.popup, null);

                    // we get the elements from the popup layout
                    Button dismissBtn = (Button) view.findViewById(R.id.popup_btn_dismiss);
                    Button dismissBtn2 = (Button) view.findViewById(R.id.popup_btn_dismiss2);
                    TextView specialties = (TextView) view.findViewById(R.id.popup_specialties);
                    WebView webView = (WebView) view.findViewById(R.id.popup_webview);

                    // adapt test of "specialties" and set onclicklisteners on dismiss buttons
                    specialties.setText( "Specialties: " + resto.getCategories());
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

                    // we'll display a web content inside our app through the webview element
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(weblink);

                    dialogBuilder.setView(view);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();

    }



    ////////////////////////////////////// METHODS IMPLEMENTED //////////////////////////////////////////
    //////////// FROM GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener //////////////

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("RESTO_TAG", "onInfoWindowClick: " + marker.getTag().toString());
        Restaurant resto = (Restaurant) marker.getTag();
        getRestaurantDetails(resto);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
