package com.example.place_its;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

public class LocationFinder   {
  private LocationManager service;
  private LocationListener locListener;
  static MainActivity main;
  Context context;

  public LocationFinder(MainActivity main) {
    LocationFinder.main = main;
    service = (LocationManager)main.getSystemService(Context.LOCATION_SERVICE);
    locListener = new MyLocationListener();
    service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
  }
  
  public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location arg0) {
      // TODO Auto-generated method stub
      //System.out.println("NOTIFICATION ARRAYSIZE CHECKING " + MainActivity.activeList.size());
      
      for(int i=0; i<MainActivity.activeList.size(); i++) {
        final placeIt p = MainActivity.activeList.get(i);
        
        if( p instanceof categoryPlaceIt) {
          ((categoryPlaceIt)p).changeLocation(arg0.getLatitude(), arg0.getLongitude());
        }
        
        for(int j=0; j<p.getMarkers().size(); j++) {
          
        // Get information of the marker
          
          LatLng position = p.getMarkers().get(j).getPosition();
          
          double lat = position.latitude;
          double lon = position.longitude;

          float distance[] = new float[1];
          Location.distanceBetween(arg0.getLatitude(), arg0.getLongitude(), 
              lat, lon, distance);

          // Get the user inputted information
          final int index = i;
          final String title = p.getTitle();
          final String descr = p.getDescription();
          

          
          // Check if the user is within half a mile of any marker
          if(distance[0]< 805 && p.getFlag() == false && !main.isFinishing()) {
 
            MainActivity.notifyManager.notify(p.getUniqueID(),p.getNotification());
            p.setFlag(true);
            AlertDialog.Builder dialog = new AlertDialog.Builder(main);
            dialog.setTitle(p.getTitle());

            if(p instanceof categoryPlaceIt) {
              dialog.setMessage(p.getDescription() + "\nLocation: " + p.getMarkers().get(j).getTitle()
                  + "\nAddress: " + p.getMarkers().get(j).getSnippet());
            }
            else {
              dialog.setMessage(p.getDescription());
            }
            // Pull button to remove the marker from the map
            dialog.setPositiveButton("Pull", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                MainActivity.notifyManager.cancel(MainActivity.activeList.get(index).getUniqueID());
                MainActivity.activeList.get(index).removeMarkers();
                placeIt pI =  MainActivity.activeList.get(index);
                MainActivity.triggeredList.add(pI);
                System.out.println(" WOOHOOO " +MainActivity.triggeredList.size() + " PULLLL");
                MainActivity.activeList.delete(index);
                ArrayList<ParseObject> found;
                if(pI instanceof locationPlaceIt)
                {
                	ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveLocation");
                	query.whereEqualTo("title", pI.getTitle());
                	//query.whereEqualTo("description", pI.getDescription());
                	System.out.println(" LOCATION PULLLLLLLLLL ");
                	try
                	{
                		found = (ArrayList<ParseObject>) query.find();
                		System.out.println("Active found sizee " +found.size());
                		ParseObject pq = new ParseObject("TriggeredPlaceIts");
                		pq.put("title", found.get(0).get("title"));
                		pq.put("description", found.get(0).get("description"));
                		pq.put("latitude", found.get(0).get("latitude"));
                		pq.put("longitude", found.get(0).get("longitude"));
                		pq.put("user", ParseUser.getCurrentUser());
                		pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
                		pq.save();
                		found.get(0).delete();
                	}
                	catch(Exception e)
                	{
                		e.printStackTrace();
                	}
                }
                else
                {
                	System.out.println(" CATEGORYYYY PULLLLL");
                	ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveCategory");
                	query.whereEqualTo("title", pI.getTitle());
                	query.whereEqualTo("description", pI.getDescription());

                	try
                	{
                		found = (ArrayList<ParseObject>) query.find();
                		System.out.println("QQWQDSCSCSCSC "+found.size());
                		
                		ParseObject pq = new ParseObject("TriggeredPlaceIts");
                		pq.put("title", found.get(0).get("title"));
                		pq.put("description", found.get(0).get("description"));
                		pq.put("cat1", found.get(0).get("cat1"));
                		pq.put("cat2", found.get(0).get("cat2"));
                		pq.put("cat3", found.get(0).get("cat3"));
                		pq.put("user", ParseUser.getCurrentUser());
                		pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
                		pq.save();
                		found.get(0).delete();
                	}
                	catch(Exception e)
                	{
                		e.printStackTrace();
                	}
                }

               // main.saveState();
              }
            });

            // Snooze button to notified again in 45 seconds
            dialog.setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) { 
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    if(!main.isFinishing()) {
                      
                      if( p instanceof locationPlaceIt ) {
                        MainActivity.activeList.add(new locationPlaceIt(title, descr, 
                          ((locationPlaceIt) p).getPosition(), 
                          MainActivity.mMap, MainActivity.pendingInt, main));
                      }
                      else {

                        final String cat1 = ((categoryPlaceIt)p).getCat1();
                        final String cat2 = ((categoryPlaceIt)p).getCat2();
                        final String cat3 = ((categoryPlaceIt)p).getCat3();
                        
                        MainActivity.activeList.add(new categoryPlaceIt(title, descr, cat1, cat2, cat3,
                            MainActivity.mMap, MainActivity.pendingInt, main));
                      } 
                   //   main.saveState();
                    }
                  }
                }, 45000);
                // Cancel the notification
                System.out.println("PULL NOW AFTER SNOOZE");
                MainActivity.notifyManager.cancel(MainActivity.activeList.get(index).getUniqueID());
                MainActivity.activeList.pull(index);         
            //    main.saveState();
              }
            });
            AlertDialog popup = dialog.create();
            popup.show();
          }
        }
      }
    }
    @Override
    public void onProviderDisabled(String arg0) {
      // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String arg0) {
      // TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
      // TODO Auto-generated method stub  
    }
  }
  
  public class createDialog {
    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
  }
}
