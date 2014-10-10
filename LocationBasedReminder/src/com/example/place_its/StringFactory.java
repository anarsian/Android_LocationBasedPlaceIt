package com.example.place_its;

import java.util.Locale;

public class StringFactory {
  public StringFactory() {}
  
  public String createString(String input) {
    if( input.equals(" ") ) { return "emptyinput"; }
    input = input.toLowerCase(Locale.getDefault());
    input = input.replaceAll("\\s","");
    
    if( input.equals("bookstore") ) { input = "book_store"; }
    else if( input.equals("busstation") ) { input = "bus_station"; }
    else if( input.equals("carwash") ) { input = "car_wash"; }
    else if( input.equals("clothingstore")) { input = "clothing_store"; }
    else if( input.equals("conveniencestore") ) { input = "convenience_store"; }
    else if( input.equals("departmentstore") ) { input = "department_store"; }
    else if( input.equals("electronicsstore") ) { input = "electronics_store"; }
    else if( input.equals("firestation") ) { input = "fire_station"; }
    else if( input.equals("gasstation") ) { input = "gas_station"; }
    else if( input.contains("grocery") || input.equals("supermarket") ) {
      input = "grocery_or_supermarket";
    }
    else if( input.equals("haircare") ) { input = "hair_care"; }
    else if( input.equals("hardwarestore") ) { input = "hardware_store"; }
    else if( input.equals("homegoodsstore") ) { input = "home_goods_store"; }
    else if( input.equals("liquorstore") ) { input = "liquor_store"; }
    else if( input.equals("mealdelivery") ) { input = "meal_delivery"; }
    else if( input.equals("mealtakeaway") ) { input = "meal_takeaway"; }
    else if( input.equals("movietheater") ) { input = "movie_theater"; }
    else if( input.equals("movierental") ) { input = "movie_rental"; }
    else if( input.equals("petstore") ) { input = "pet_store"; }
    else if( input.equals("postoffice") ) { input = "post_office"; }
    else if( input.equals("shoppingmall") ) { input = "shopping_mall"; }
    else if( input.equals("nightclub") ) { input = "night_club"; }
    
    return input;
  }
}
