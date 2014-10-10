package com.example.place_its;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
//import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This class creates the general framework for the rest of the application. 
 * This activity uses the google map API to be able to place markers on it
 * that will serve as notifications once the user gets near them.
 *
 * @author Team 8
 *
 */
public class MainActivity extends Activity implements OnMapClickListener, 
OnMapLongClickListener {
	protected static GoogleMap mMap;
	protected LatLng click_pos;
	protected static NotificationManager notifyManager;
	protected LocationMarkerInfo markerInfo;
	protected CategoryMarkerInfo categoryInfo;
	protected EditText editText;
	protected static Intent intent = new Intent();

	protected String innerTitle;
	protected String innerDescription;
	protected double innerLat;
	protected double innerLong;
	protected static PendingIntent pendingInt;
	protected Spinner spinner;
	protected View myView;
	protected ParseUser currentuser;
	protected List<ParseObject> ActList;
	protected static LocationFinder locationFinder;
	protected static activeList activeList;
	protected static triggeredList triggeredList;
	/**
	 * This method overrides the onCreate method of the activity. It creates
	 * the map and allows the user to set markers.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentuser = ParseUser.getCurrentUser();
		activeList = new activeList();
		triggeredList = new triggeredList();

		setUpMap();

		setIntents();
		
		setButtons();
		
		refresh();    
		
		editText = (EditText)findViewById(R.id.sendLocation);
		locationFinder = new LocationFinder(this);
		notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	/**
	 * Create the map if needed
	 */
	private void setUpMap() {
		setContentView(R.layout.activity_main);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setOnMapClickListener(this);
		mMap.setOnMapLongClickListener(this);
		mMap.setMyLocationEnabled(true);
	}

	private void setIntents() {
		// create intents for notifications
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("android.intent.action.VIEW");
		intent.setComponent(new ComponentName("com.example.place_its",
				"com.example.place_its.MainActivity"));
		pendingInt = PendingIntent.getActivity(MainActivity.this,0,intent,0);
	}

	private void setButtons() {
		Button btnUpdate = (Button) findViewById(R.id.sendLocationBtn);

		// Declare what happens when the user clicks in the textbox
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			/**
			 * This method allows the user to search for long/lat using the box
			 */
			@Override
			public void onClick(View v) throws NumberFormatException{
				double latitude;
				double longitude;
				EditText editText = (EditText) findViewById(R.id.sendLocation);
				String geoData = editText.getText().toString();
				String[] coordinate = geoData.split(",");

				// Make sure that the input are numbers to avoid runtime error
				if(coordinate.length == 2) {
					try {
						latitude = Double.valueOf(coordinate[0]).doubleValue();
					} catch (NumberFormatException e) { latitude = -1;}
					try {
						longitude = Double.valueOf(coordinate[1]).doubleValue();
					} catch (NumberFormatException e) { longitude = -1;}

					if(latitude != -1 && longitude != -1)
						mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new 
								LatLng(latitude,longitude), 12),2000, null);
				}
			}
		});

		Button menu = (Button) findViewById(R.id.menuButton);

		menu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) throws NumberFormatException{
				refresh();
				Intent openList = new Intent(MainActivity.this, ActivityListView.class);
				startActivity(openList);
			}
		});
	}

	/**
	 * This method defines the functionality when the user clicks on the map.
	 */
	public void onMapClick(LatLng position) {
		click_pos = position;
		System.out.println("SIZZE ONE TRI" +triggeredList.size());
		System.out.println("SIZZE TWO ACT " +activeList.size());
		markerInfo = new LocationMarkerInfo();
		markerInfo.show(getFragmentManager(), "");
	}


	/**
	 * This method defines what happens when the user long clicks. It brings 
	 * the user to the list activity.
	 */
	@Override
	public void onMapLongClick(LatLng arg0) {
		categoryInfo = new CategoryMarkerInfo();
		categoryInfo.show(getFragmentManager(), "");
	}

	/**
	 * This method creates the markers on the map once the title/description is
	 * given.
	 *//*
	public void onUserSelectValue() {
		activeList.add(new locationPlaceIt(markerInfo.getTitle(), markerInfo.getDescr(), 
				click_pos, mMap,pendingInt,MainActivity.this));
		saveState();
	}*/




	@Override
	public Object onRetainNonConfigurationInstance() { return activeList;}    


	@Override
	public void onSaveInstanceState(Bundle outstate) {
		super.onSaveInstanceState(outstate);
		System.out.println("SAVEINSTANCESTATE. ACTIVE: " + activeList.size() + "  TRIGGERED: " + triggeredList.size());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("ONDESTROY. ACTIVE: " + activeList.size() + "  TRIGGERED: " + triggeredList.size());
		//saveState();	
		}

	/**
	 * This method saves the state of the markers by writing to a text file.
	 * @throws ParseException 
	 */
	/*public void saveState()  
	{
		if(activeList == null || triggeredList == null) 
		{
			return;
		}
		placeIt p;
		List<ParseObject> found = new ArrayList<ParseObject>();
		for(int i=0; i<activeList.size(); i++) 
		{
			p = activeList.get(i);
			System.out.println(" SAVING STATE " + activeList.size());
			if( p instanceof locationPlaceIt) 
			{

				ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveLocation");
				query.whereEqualTo("latitude", ((locationPlaceIt) p).getLatitude());
				query.whereEqualTo("longitude", ((locationPlaceIt) p).getLongitude());
				query.whereEqualTo("title", p.getTitle());
				try 
				{
					found = query.find();
				} catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(found.size() == 0)
				{
					ParseObject pq = new ParseObject("ActiveLocation");
					pq.put("title", p.getTitle());
					pq.put("description", p.getDescription());
					pq.put("latitude", ((locationPlaceIt) p).getLatitude());
					pq.put("longitude", ((locationPlaceIt) p).getLongitude());
					pq.put("user", ParseUser.getCurrentUser());
					pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
					pq.saveInBackground();
				}

				System.out.println("IS THIS WORKING?");

			}

			else if ( p instanceof categoryPlaceIt) 
			{
				ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveCategory");
				query.whereEqualTo("title", ((categoryPlaceIt) p).getTitle());
				query.whereEqualTo("description", ((categoryPlaceIt) p).getDescription());
				query.whereEqualTo("cat1", ((categoryPlaceIt) p).getCat1());
				query.whereEqualTo("cat2", ((categoryPlaceIt) p).getCat2());
				query.whereEqualTo("cat3", ((categoryPlaceIt) p).getCat3());
				try 
				{
					found = query.find();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(found.size() == 0)
				{
					ParseObject pq = new ParseObject("ActiveCategory");
					pq.put("title", p.getTitle());
					pq.put("description", p.getDescription());
					pq.put("cat1", ((categoryPlaceIt) p).getCat1());
					pq.put("cat2", ((categoryPlaceIt) p).getCat2());
					pq.put("cat3", ((categoryPlaceIt) p).getCat3());
					pq.put("user", ParseUser.getCurrentUser());
					pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
					pq.saveInBackground();
				}
			}
		}
		for(int i=0; i<triggeredList.size(); i++) 
		{
			p = triggeredList.get(i);

			if( p instanceof locationPlaceIt)
			{
				ParseQuery<ParseObject> query = ParseQuery.getQuery("TriggeredPlaceIts");
				query.whereEqualTo("latitude", ((locationPlaceIt) p).getLatitude());
				query.whereEqualTo("longitude", ((locationPlaceIt) p).getLongitude());
				query.whereEqualTo("title", p.getTitle());
				try 
				{
					found = query.find();
				} catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(found.size() == 0)
				{
					ParseObject pq = new ParseObject("TriggeredPlaceIts");
					pq.put("title", p.getTitle());
					pq.put("description", p.getDescription());
					pq.put("latitude", ((locationPlaceIt) p).getLatitude());
					pq.put("longitude", ((locationPlaceIt) p).getLatitude());
					pq.put("user", ParseUser.getCurrentUser());
					pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
					pq.saveInBackground();
				}

			}
			else if ( p instanceof categoryPlaceIt) 
			{
				ParseQuery<ParseObject> query = ParseQuery.getQuery("TriggeredPlaceIts");
				query.whereEqualTo("title", ((categoryPlaceIt) p).getTitle());
				query.whereEqualTo("description", ((categoryPlaceIt) p).getDescription());
				query.whereEqualTo("cat1", ((categoryPlaceIt) p).getCat1());
				query.whereEqualTo("cat2", ((categoryPlaceIt) p).getCat2());
				query.whereEqualTo("cat3", ((categoryPlaceIt) p).getCat3());
				try 
				{
					found = query.find();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(found.size() == 0)
				{
					ParseObject pq = new ParseObject("TriggeredPlaceIts");
					pq.put("title", p.getTitle());
					pq.put("description", p.getDescription());
					pq.put("cat1", ((categoryPlaceIt) p).getCat1());
					pq.put("cat2", ((categoryPlaceIt) p).getCat2());
					pq.put("cat3", ((categoryPlaceIt) p).getCat3());
					pq.put("user", ParseUser.getCurrentUser());
					pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
					pq.saveInBackground();
				}

			}
		}
	}*/
	public void refresh() 
	{


		if(activeList == null || triggeredList == null) { 
			return;
		}
		String title, description, cat1, cat2, cat3;
		double latitude, longitude;
		System.out.println("AC SIZEEEEEEEE "+ activeList.size());
		activeList.removeAll();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveLocation");

		query.whereEqualTo("user", ParseUser.getCurrentUser());

		try 
		{
			List<ParseObject> ActiveLoc = query.find();
			for (int i = 0; i < ActiveLoc.size(); i++) 
			{
				title = (String) ActiveLoc.get(i).get("title");
				description = (String) ActiveLoc.get(i).get("description");
				latitude = (Double) ActiveLoc.get(i).get("latitude");
				longitude = (Double) ActiveLoc.get(i).get("longitude");
				activeList.add(new locationPlaceIt(title, description,
						new LatLng(latitude, longitude), mMap,
						pendingInt, MainActivity.this));
			}
		}
		catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
			
		ParseQuery<ParseObject> queryCat = ParseQuery.getQuery("ActiveCategory");

		queryCat.whereEqualTo("user", ParseUser.getCurrentUser());
		try 
		{
		List<ParseObject> activeCat = queryCat.find();
		for (int i = 0; i < activeCat.size(); i++) 
		{
			title = (String) activeCat.get(i).get("title");
			description = (String) activeCat.get(i).get("description");
			cat1 = (String) activeCat.get(i).get("cat1");
			cat2 = (String) activeCat.get(i).get("cat2");
			cat3 = (String) activeCat.get(i).get("cat3");
			activeList.add(new categoryPlaceIt(title, description, cat1, cat2, cat3, 
					mMap, pendingInt, MainActivity.this));
		}
		}
		catch(Exception e3)
		{
			e3.printStackTrace();
		}


		triggeredList =new triggeredList();;
		ParseQuery<ParseObject> TriggeredLocation = ParseQuery.getQuery("TriggeredPlaceIts");

		TriggeredLocation.whereEqualTo("user", ParseUser.getCurrentUser());
		try {
			List<ParseObject> trig = TriggeredLocation.find();
			System.out.println(" TRiggered list size on refresh " + trig.size());
			for (int i = 0; i < trig.size(); i++) 
			{
				if(trig.get(i).get("latitude") != null)
				{
					title = (String) trig.get(i).get("title");
					description = (String) trig.get(i).get("description");
					latitude = (Double) trig.get(i).get("latitude");
					longitude = (Double) trig.get(i).get("longitude");
					triggeredList.add(new locationPlaceIt(title, description,
							new LatLng(latitude, longitude)));
				}
					//System.out.println(" TRiggered list size on start " + triggeredList.size());						}
				else
				{
					title = (String) trig.get(i).get("title");
					description = (String) trig.get(i).get("description");
					cat1 = (String) trig.get(i).get("cat1");
					cat2 = (String) trig.get(i).get("cat2");
					cat3 = (String) trig.get(i).get("cat3");
					triggeredList.add(new categoryPlaceIt(title, description, cat1, cat2, cat3, 
							mMap, pendingInt, MainActivity.this));
				}
			}
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
