package com.example.place_its;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Color;
import android.os.AsyncTask;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class categoryPlaceIt extends placeIt {
  private String category1;
  private String category2;
  private String category3;
  private final String API_KEY = "AIzaSyACnMqCZhItU8yldGyYRf5-P2Xdo0UeIds";
  //"AIzaSyBaJnlC5RTjRMCII-TOQxm93jFCyn683UE";
  private double latitude = 0;
  private double longitude = 0;

  //private String[] places;
  private GoogleMap mMap;
  
  PlaceFinder find1 = null;
  PlaceFinder find2 = null;
  PlaceFinder find3 = null;
  
  private int numOfPlaces;
  
  @SuppressWarnings("deprecation")
  public categoryPlaceIt(String title, String description, String cat1, 
      String cat2, String cat3, GoogleMap mMap, PendingIntent intent,
      Activity activity) {
  
     // places = activity.getResources().getStringArray(R.array.places);
    
      this.category1 = cat1;
      this.category2 = cat2;
      this.category3 = cat3;
      this.title = title;
      this.description = description;
      this.mMap = mMap;
      
      markerList = new ArrayList<Marker>();
      changeLocation(latitude, longitude);

      notification = new Notification(R.drawable.notification_icon, description, System.currentTimeMillis());
      notification.setLatestEventInfo(activity,title,description,intent);
      notification.flags |= Notification.FLAG_AUTO_CANCEL;
      notification.defaults = Notification.DEFAULT_SOUND;
      notification.vibrate = new long[] {100,200,300};
      notification.flags |= Notification.FLAG_SHOW_LIGHTS;
      notification.ledARGB = Color.CYAN;
      notification.ledOnMS = 500;
      notification.ledOffMS = 500;  
      System.out.println("CREATE");

  }
  
  public categoryPlaceIt(String title, String description, String cat1, String cat2, String cat3) {
    this.category1 = cat1;
    this.category2 = cat2;
    this.category3 = cat3;
    this.title = title;
    this.description = description;
  }
  
  public int getNumOfPlaces() { return numOfPlaces; }
  
  public void changeLocation(double latitude, double longitude) {
    removeMarkers(); 
    if( !category1.equals(" ")) {
      find1 = new PlaceFinder(category1, latitude, longitude);
      find1.execute();
    }
    if( !category2.equals(" ")) {
      find2 = new PlaceFinder(category2, latitude, longitude);
      find2.execute();
    }
    if( !category3.equals(" ")) {
      find3 = new PlaceFinder(category3, latitude, longitude);
      find3.execute();
    }
  } 
  
  public String getCat1() { return category1; }
  public String getCat2() { return category2; }
  public String getCat3() { return category3; }
  
  
  private class PlaceFinder extends AsyncTask <Void, ArrayList<Places>, ArrayList<Places>> {
    private String category;
    private double lat;
    private double lon;
    
    public PlaceFinder(String category, double lat, double lon) {
      this.category = category;
      this.lat = lat;
      this.lon = lon;
    }
    
    @Override
    protected ArrayList<Places> doInBackground(Void... params) {
      PlacesService service = new PlacesService(API_KEY);
        ArrayList<Places> findPlaces = service.findPlaces(lat, 
          lon, category); 

        for (int i = 0; i < findPlaces.size(); i++) {

         Places placeDetail = findPlaces.get(i);
         System.out.println( "places : " + placeDetail.getName());
         numOfPlaces++;
        }
        return findPlaces;
    }
    
    @Override
    protected void onPostExecute(ArrayList<Places> result) {
      for(int i=0; i<result.size(); i++) {
        markerList.add(mMap.addMarker(new MarkerOptions().position(
            new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude())).
              title(result.get(i).getName()).snippet(result.get(i).getVicinity())));
        
              markerList.get(i).setVisible(false);
      }      
    }
    
  }
}
