package com.leliacat.foodpornproject.util;

import com.leliacat.foodpornproject.model.Restaurant;

import java.util.ArrayList;
import java.util.Random;

public class Constants {



///////////////////////////////////////////// CONSTANTS for API CALLS //////////////////////////////////////////////////


    public static final String API_KEY = "deb242b665c4d425be528ddd7cc969e8";
    public static final String URL = "https://developers.zomato.com/api/v2.1/search?";

    // defines the maximum of restaurants we want in the request, here we have set it to 30
    public static final String LIMIT = "count=30";
    //to make custom requests according to user location
    public static final String URL_COMPLEMENT_1 = "&lat=";   // allows to add a specific latitude
    public static final String URL_COMPLEMENT_2 = "&lon=";   // allows to add a specific longitude
    public static final String URL_COMPLEMENT_3 = "&radius=10000&sort=rating&order=desc";
    // limits the radius of research to 10000 meters around the coordinates defined precedently
    // orders results according to rating, from the higher to the lower


   public static final String URL_RESTO_DETAILS = "https://developers.zomato.com/api/v2.1/restaurant?res_id=";
   public static final String URL_RESTO_MENU = "https://developers.zomato.com/api/v2.1/dailymenu?res_id=";


///////////////////////////////////////////// CONSTANTS for DATABASE HANDLER //////////////////////////////////////////////////

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "restaurantListDB";
    public static final String TABLE_NAME ="restaurantTable";

    // Table columns
    public static final String KEY_ID = "id";
    public static final String KEY_RESTO_NAME = "restaurant_name";
    public static final String KEY_RESTO_ADDRESS = "restaurant_address";
    public static final String KEY_RESTO_LOCALITY = "restaurant_locality";
    public static final String KEY_RESTO_CITY = "restaurant_city";
    public static final String KEY_RESTO_CITY_ID = "restaurant_city_id";
    public static final String KEY_RESTO_ZIPCODE = "restaurant_zipcode";
    public static final String KEY_RESTO_COUNTRY_ID = "restaurant_country_id";
    public static final String KEY_RESTO_LOCALITY_VERBOSE = "restaurant_locality_verbose";
    public static final String KEY_RESTO_SPECIALTIES = "cuisines";
    public static final String KEY_RESTO_RATING = "rating";
    public static final String KEY_RESTO_AVERAGE_COST_FOR_2 = "average_cost_for_two";
    public static final String KEY_RESTO_CURRENCY = "currency";
    public static final String KEY_RESTO_LATITUDE = "latitude";
    public static final String KEY_RESTO_LONGITUDE = "longitude";
    public static final String KEY_RESTO_DETAILS_LINK = "detail_link";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////// CONSTANTS for OTHER STUFF ////////////////////////////////////////////////////////////////////////////

    // we will use it to get a random index in the list of restaurants
    public static Restaurant getRandomRestaurant(ArrayList<Restaurant> arrayList) {
        int randomIndex = new Random().nextInt(arrayList.size());
        Restaurant chosenResto = arrayList.get(randomIndex);
        return chosenResto;
    }

    // model of response from Zomato API for the request "resto details".
      /*{
                                "R": {
                                "res_id": 16774318
                            },
                                "apikey": "deb242b665c4d425be528ddd7cc969e8",
                                    "id": "16774318",
                                    "name": "Otto Enoteca Pizzeria",
                                    "url": "https:\/\/www.zomato.com\/new-york-city\/otto-enoteca-pizzeria-greenwich-village?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1",
                                    "location": {
                                "address": "One Fifth Avenue at 8th Street, Greenwich Village 10003",
                                        "locality": "Greenwich Village",
                                        "city": "New York City",
                                        "city_id": 280,
                                        "latitude": "40.7318200000",
                                        "longitude": "-73.9965400000",
                                        "zipcode": "10003",
                                        "country_id": 216,
                                        "locality_verbose": "Greenwich Village"
                            },
                                "switch_to_order_menu": 0,
                                    "cuisines": "Pizza, Italian",
                                    "average_cost_for_two": 60,
                                    "price_range": 4,
                                    "currency": "$",
                                    "offers": [],
                                "opentable_support": 0,
                                    "is_zomato_book_res": 0,
                                    "mezzo_provider": "OTHER",
                                    "is_book_form_web_view": 0,
                                    "book_form_web_view_url": "",
                                    "book_again_url": "",
                                    "thumb": "https:\/\/b.zmtcdn.com\/data\/res_imagery\/16774318_RESTAURANT_fc526e8cfdc1cd8242c50298385d325c.JPG?fit=around%7C200%3A200&crop=200%3A200%3B%2A%2C%2A",
                                    "user_rating": {
                                "aggregate_rating": "4.4",
                                        "rating_text": "Very Good",
                                        "rating_color": "5BA829",
                                        "votes": "579"
                            },
                                "photos_url": "https:\/\/www.zomato.com\/new-york-city\/otto-enoteca-pizzeria-greenwich-village\/photos?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1#tabtop",
                                    "menu_url": "https:\/\/www.zomato.com\/new-york-city\/otto-enoteca-pizzeria-greenwich-village\/menu?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1&openSwipeBox=menu&showMinimal=1#tabtop",
                                    "featured_image": "https:\/\/b.zmtcdn.com\/data\/res_imagery\/16774318_RESTAURANT_fc526e8cfdc1cd8242c50298385d325c.JPG",
                                    "has_online_delivery": 0,
                                    "is_delivering_now": 0,
                                    "include_bogo_offers": true,
                                    "deeplink": "zomato:\/\/restaurant\/16774318",
                                    "is_table_reservation_supported": 0,
                                    "has_table_booking": 0,
                                    "events_url": "https:\/\/www.zomato.com\/new-york-city\/otto-enoteca-pizzeria-greenwich-village\/events#tabtop?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1"
                            }*/


}



