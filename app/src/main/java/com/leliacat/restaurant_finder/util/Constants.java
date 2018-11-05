package com.leliacat.restaurant_finder.util;

import java.util.Random;

public class Constants {
    public static final String API_KEY = "deb242b665c4d425be528ddd7cc969e8";
    public static final String URL = "https://developers.zomato.com/api/v2.1/search?";

    // defines the maximum of restaurnats we want in the request, here we have set it to 30
    public static final String LIMIT = "count=30";
    //to make custom requests according to user location
    public static final String URL_COMPLEMENT_1 = "&lat=";                                // permet d'ajouter une latitude spécifique
    public static final String URL_COMPLEMENT_2 = "&lon=";                               // pour ajouter une longitude spécifique
    public static final String URL_COMPLEMENT_3 = "&radius=20000&sort=rating&order=desc"; // limite le champ de recherche à rayon de 5000 mètres autour des coordonnées définies précédemment
                                                                                         // range les résultats en fonction des notes, du plus grand au plus petit

    /* "https://developers.zomato.com/api/v2.1/search?entity_type=city&count=30&lat=34.1381168&lon=-118.355567&radius=3000&sort=rating&order=desc";*/
    //https://developers.zomato.com/api/v2.1/search?count=30&lat=34.1381168&lon=-118.355567&radius=3000&sort=rating&order=desc

    // curl -X GET --header "Accept: application/json"
    // --header "user-key: deb242b665c4d425be528ddd7cc969e8"
    // "https://developers.zomato.com/api/v2.1/search?entity_type=city&count=30&lat=34.1381168&lon=-118.355567&radius=3000&sort=rating&order=desc"



    public static int randomInt(int max, int min) {
        return new Random().nextInt(max - min) + min;

    }
}
