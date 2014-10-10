package com.example.place_its;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Color;

public class locationPlaceIt extends placeIt {
  private LatLng position;
  //private Marker marker;
  
  @SuppressWarnings("deprecation")
  public locationPlaceIt(String title, String description, LatLng position, 
      GoogleMap mMap, PendingIntent intent, Activity activity) {
    
    if(title.length() == 0) { this.title = "Title"; }
    else { this.title = title; }
    if(description.length() == 0) { this.description = "Description"; }
    else { this.description = description; }
    
    this.position = position;
    this.intent = intent;
    
    markerList = new ArrayList<Marker>();
    markerList.add(mMap.addMarker(new MarkerOptions().position(position).
        title(this.title).snippet(this.description)));
    
    createID();
    
    notification = new Notification(R.drawable.notification_icon, description, System.currentTimeMillis());
    notification.setLatestEventInfo(activity,title,description,intent);
    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    notification.defaults = Notification.DEFAULT_SOUND;
    notification.vibrate = new long[] {100,200,300};
    notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    notification.ledARGB = Color.CYAN;
    notification.ledOnMS = 500;
    notification.ledOffMS = 500;  }
  
  public locationPlaceIt(String title, String description, LatLng position) {
    this.position = position;
    this.description = description;
    this.title = title;
  }

  public void createID() {
    for(int i=0; i<this.title.length(); i++) {
      uniqueID += title.charAt(i);
    }
    for(int i=0; i<this.description.length(); i++){
      uniqueID += description.charAt(i);
    }
    uniqueID = (int) (uniqueID + position.longitude + position.latitude);
  }
  
  public double getLongitude() { return position.longitude; }
  public double getLatitude() { return position.latitude; }
  public LatLng getPosition() { return position; }
  
  
}
