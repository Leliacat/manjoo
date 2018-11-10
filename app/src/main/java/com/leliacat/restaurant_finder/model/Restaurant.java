package com.leliacat.restaurant_finder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Parcelable {

    private String id;
    private String name;
    private String categories;
    private Double rating;
    private int average_cost_for_two;
    private String currency;
    /*private String[] completeAddress;*/
    private String address;
    private String locality;
    private String city;
    private String city_id;
    private String zipcode;
    private String country_id;
    private String locality_verbose;
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
       /* completeAddress = new String[7];
        completeAddress[0]= address;
        completeAddress[1]= locality;
        completeAddress[2]= city;
        completeAddress[3]= city_id;
        completeAddress[4]= zipcode;
        completeAddress[5]= country_id;
        completeAddress[6]= locality_verbose;*/
    }

    //constructor used for parcel
    public Restaurant(Parcel parcel){
        //read and set saved values from parcel
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

   /* public String[] getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String[] completeAddress) {
        this.completeAddress = completeAddress;
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getLocality_verbose() {
        return locality_verbose;
    }

    public void setLocality_verbose(String locality_verbose) {
        this.locality_verbose = locality_verbose;
    }



    //**************************************************  METHODS FROM PARCELABLE ********************************************************

    @Override
    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }


    @Override
    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags) {
        //write all properties to the parcel
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(categories);
        dest.writeString(address);
        dest.writeString(locality);
        dest.writeString(city);
        dest.writeString(city_id);
        dest.writeString(zipcode);
        dest.writeString(country_id);
        dest.writeString(locality_verbose);
        /*dest.writeStringArray(completeAddress);*/
        dest.writeDouble(rating);
        dest.writeInt(average_cost_for_two);
        dest.writeValue(currency);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(detail_link);

    }


    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>(){

        @Override
        public Restaurant createFromParcel(Parcel parcel) {
            return new Restaurant(parcel);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[0];
        }
    };





}
