package com.example.place_its;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

import android.app.Notification;
import android.app.PendingIntent;

public abstract class placeIt {
	protected String title;
	protected String description;
	protected int uniqueID;
	protected Notification notification;
	protected PendingIntent intent;
	protected boolean notified = false;
  protected ArrayList<Marker> markerList;

	public void createID() {}
	public String getTitle() { return title; }
	public String getDescription() { return description; }
	public ArrayList<Marker> getMarkers() { return markerList; }
	public Notification getNotification() { return notification; }
	public boolean getFlag() { return notified; }
	public int getUniqueID() { return uniqueID; }
	public PendingIntent getIntent() { return intent; }
	public void setFlag(boolean set) { notified = set; }
	
	public void removeMarkers() {
	  for(int i=0; i<markerList.size(); i++) {
	    markerList.get(i).remove();
	  }  
	}
}
