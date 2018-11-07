package com.leliacat.restaurant_finder.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    private String id;
    private String name;
    private String categories;
    private Double rating;
    private int average_cost_for_two;
    private String currency;
    private List<String> address;
    private Double latitude;
    private Double longitude;
    private String detail_link;

    //JSON OBJECT FROM API __ request URL : https://developers.zomato.com/api/v2.1/search
    //here are the infos we want to use, and that will serve as model for our Restaurant class'properties:
    //name _ location (address and coordinates) _ average cost for two _ user rating (aggregate_rating) _

   /* Model Schema

"restaurant": {
				"R": {
					"res_id": 18766126
				},
				"apikey": "deb242b665c4d425be528ddd7cc969e8",
				"id": "18766126",
				"name": "Barbeque Nation",
				"url": "https:\/\/www.zomato.com\/aurangabad\/barbeque-nation-chikalthana?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1",
				"location": {
					"address": "First Floor, Prozone Mall Food Court API Road, MIDC Industrial Aream Chikalthana, Aurangabad",
					"locality": "Prozone Mall, Chicalthana",
					"city": "Aurangabad",
					"city_id": 25,
					"latitude": "19.8768941596",
					"longitude": "75.3726713732",
					"zipcode": "",
					"country_id": 1,
					"locality_verbose": "Prozone Mall, Chicalthana, Aurangabad"
				},
				"switch_to_order_menu": 0,
				"cuisines": "North Indian, Seafood, Chinese, Mediterranean",
				"average_cost_for_two": 1200,
				"price_range": 3,
				"currency": "Rs.",
				"offers": [],
				"opentable_support": 0,
				"is_zomato_book_res": 0,
				"mezzo_provider": "OTHER",
				"is_book_form_web_view": 0,
				"book_form_web_view_url": "",
				"book_again_url": "",
				"thumb": "https:\/\/b.zmtcdn.com\/data\/pictures\/chains\/6\/18766126\/b6429ddad24625e65344caabb921bd57.jpg?fit=around%7C200%3A200&crop=200%3A200%3B%2A%2C%2A",
				"user_rating": {
					"aggregate_rating": "4.9",
					"rating_text": "Excellent",
					"rating_color": "3F7E00",
					"votes": "447"
				},
				"photos_url": "https:\/\/www.zomato.com\/aurangabad\/barbeque-nation-chikalthana\/photos?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1#tabtop",
				"menu_url": "https:\/\/www.zomato.com\/aurangabad\/barbeque-nation-chikalthana\/menu?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1&openSwipeBox=menu&showMinimal=1#tabtop",
				"featured_image": "https:\/\/b.zmtcdn.com\/data\/pictures\/chains\/6\/18766126\/b6429ddad24625e65344caabb921bd57.jpg?output-format=webp",
				"medio_provider": 1,
				"has_online_delivery": 0,
				"is_delivering_now": 0,
				"include_bogo_offers": true,
				"deeplink": "zomato:\/\/restaurant\/18766126",
				"is_table_reservation_supported": 1,
				"has_table_booking": 1,
				"book_url": "https:\/\/www.zomato.com\/aurangabad\/barbeque-nation-chikalthana\/book?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1",
				"events_url": "https:\/\/www.zomato.com\/aurangabad\/barbeque-nation-chikalthana\/events#tabtop?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1",
				"establishment_types": []
			}
		}*/


    ////////////////////////////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////////////////


    public Restaurant() {
    }



    /////////////////////////////////////////////////////////// GETTERS & SETTERS ///////////////////////////////////////////////////


    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getAverage_cost_for_two() {
        return average_cost_for_two;
    }

    public void setAverage_cost_for_two(int average_cost_for_two) { this.average_cost_for_two = average_cost_for_two; }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDetail_link() { return detail_link; }

    public void setDetail_link(String detail_link) { this.detail_link = detail_link; }

    public Double getLatitude() { return latitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
