package com.example.place_its;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * Display Class creates the activity that displays the list. It is in charge 
 * of refreshing the lists as the user removes posts (both pulled and active),
 * as well as reposting them.
 *
 * @author Team 8
 *
 */
public class ActivityListView extends MainActivity {

  ArrayAdapter<String> adapter;

	/**
	 * Default code given. Added button functionalities
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display_list);
		
		final View contentView = findViewById(R.id.fullscreen_content);

		// add button functionalities
		activeButton();
		pulledButton();
		backButton();
		logOutButton();

		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});

		findViewById(R.id.activeButton).setOnTouchListener(
				mDelayHideTouchListener);
		
	}
	private void logOutButton() {
		Button btnUpdate = (Button) findViewById(R.id.logOutButton);
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent open = new Intent(ActivityListView.this, LoginActivity.class);
				startActivity(open);
			}
		});
	}
	/**
	 * This method is in charge of the back button in activity displaying the
	 * lists. More specifically, it brings the user back to the main screen.
	 */
	private void backButton() {
		Button btnUpdate = (Button) findViewById(R.id.backButton);
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent open = new Intent(ActivityListView.this, MainActivity.class);
				startActivity(open);
			}
		});
	}
	
	/**
	 * This method is in charge of the active button in activity displaying
	 * the lists. More specifically, it displays a list of active place-its
	 * in a textview.
	 */
	public void activeButton() {
		Button btnUpdate = (Button) findViewById(R.id.activeButton);
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create title on top of the screen
				setTitle("Active Place-Its");
				populateListView(activeList.getTitleArray());
				clickActive();
			}
		 });
	}
	
	/** 
	 * This method is in charge of the pulled button in activity displaying
	 * the lists. More specifically, it dispalys a list of pulled place-its
	 * in a textview.
	 */
	public void pulledButton() {
		Button btnUpdate = (Button) findViewById(R.id.pulledButton);
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create title on top of the screen
				setTitle("Pulled Place-Its");
				populateListView(triggeredList.getTitleArray());
				System.out.println("BUTTON ARRAY SIZE" + triggeredList.size());
				clickPulled();
			}
		 });
	}
	
	/**
	 * This method updates is used to update the textview.
	 * @param list, the updated list that will be displayed.
	 */
	public void populateListView(ArrayList<String> list) 
	{
		adapter = new ArrayAdapter<String>(this, R.layout.activelist, list);
		ListView List = (ListView) findViewById(R.id.listView1);
		List.setAdapter(adapter);
	}
	

	/**
	 * This method defines the functionality when the user clicks the active 
	 * button while in the list activity. 
	 */
	private void clickActive() {
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			/**
			 * This method defines what happens when the user clicks one of the
			 * items on the active lists. 
			 */
			@Override
			public void onItemClick(AdapterView<?> paret, View viewClicked, 
					final int position,long id) {
				TextView textview = (TextView) viewClicked;
				
				// Popup that appears when item is clicked
				new AlertDialog.Builder(ActivityListView.this)
			    .setTitle(textview.getText().toString())
			    .setMessage(activeList.getDesciptionArray().get(position))
			    
			    // Clicking this button will go back to the list screen
			    .setPositiveButton("Cancel",new DialogInterface.OnClickListener(){
			        public void onClick(DialogInterface dialog, int which) {
			        }
			     })
			     // Clicking this button will remove the place-it permanently
			    .setNegativeButton("Remove",new DialogInterface.OnClickListener(){
			        public void onClick(DialogInterface dialog, int which) { 

			        	List<ParseObject> found = new ArrayList<ParseObject>();
			        	if(activeList.get(position) instanceof locationPlaceIt)
			        	{
			        		ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveLocation");
			        		query.whereEqualTo("description", ((locationPlaceIt)activeList.get(position)).getDescription());
			        		query.whereEqualTo("title", activeList.get(position).getTitle());
			        		try 
			        		{
			        			found = query.find();
			        			System.out.println(" UMMMMMMMMMM " + found.get(0).get("title") + found.size());
			        			found.get(0).deleteInBackground();
			        		} catch (ParseException e) 
			        		{
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}

			        	}
			        	if(activeList.get(position) instanceof categoryPlaceIt)
			        	{
			        		ParseQuery<ParseObject> query = ParseQuery.getQuery("ActiveCategory");
			        		query.whereEqualTo("description", (activeList.get(position)).getDescription());
			        		query.whereEqualTo("title", activeList.get(position).getTitle());
			        		try 
			        		{
			        			found = query.find();
			        			System.out.println(" UMMMMMMMMMM " + found.get(0).get("title") + found.size());
			        			found.get(0).deleteInBackground();
			        		} catch (ParseException e) 
			        		{
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        	}
			        	activeList.delete(position);
			        	//saveState();
			        	populateListView(activeList.getTitleArray());
			    		  adapter.notifyDataSetChanged();
			        }
			     })
			     .show();		
			}
		});
	}
	
	/**
	 * This method defines what happens when the user clicks one of the
	 * items on the pulled lists. 
	 */
	private void clickPulled() {
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// Popup that appears when item is clicked
			@Override
			public void onItemClick(AdapterView<?> paret, View viewClicked, 
					final int position,long id) {
				TextView textview = (TextView) viewClicked;
				
				// This button simply goes back to the list screen
				new AlertDialog.Builder(ActivityListView.this)
			    .setTitle(textview.getText().toString())
			    .setMessage(triggeredList.getDesciptionArray().get(position))
			    .setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
			        public void onClick(DialogInterface dialog, int which) {

			        }
			     })
			     // This button removes the place-it forever
			     .setNeutralButton("Remove", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						
						
						List<ParseObject> found = new ArrayList<ParseObject>();
			        	ParseQuery<ParseObject> query = ParseQuery.getQuery("TriggeredPlaceIts");
			        	query.whereEqualTo("description", (triggeredList.get(position)).getDescription());
			        	query.whereEqualTo("title", triggeredList.get(position).getTitle());
			        	try 
			        	{
			        	found = query.find();
			        			System.out.println(" UMMMMMMMMMM " + found.get(0).get("title") + found.size());
			        	found.get(0).deleteInBackground();
			        	} catch (ParseException e) 
			        	{
			        		// TODO Auto-generated catch block
			        		e.printStackTrace();
			        	}
						
						
																	
																								
						
						triggeredList.delete(position);
					//	saveState();
						populateListView(triggeredList.getTitleArray());
						adapter.notifyDataSetChanged();
					}
				})
				// This button will repost the marker onto the map and activelist
			    .setNegativeButton("Repost", new DialogInterface.OnClickListener(){
			        public void onClick(DialogInterface dialog, int which) { 
			        	try 
			        	{
			        		ParseObject pq = new ParseObject("");

			        		List<ParseObject> found = new ArrayList<ParseObject>();
			        		ParseQuery<ParseObject> query = ParseQuery.getQuery("TriggeredPlaceIts");
			        		query.whereEqualTo("description", (triggeredList.get(position)).getDescription());
			        		query.whereEqualTo("title", triggeredList.get(position).getTitle());


			        		found = query.find();
			        		System.out.println(" UMMMMMMMMMM " + found.get(0).get("title") + found.size());
			        		if(found.get(0).get("latitude") != null)
			        		{
			        			pq = new ParseObject("ActiveLocation");
			        		}
			        		else
			        		{
			        			pq = new ParseObject("ActiveCategory");
			        		}
			        		pq.put("title", found.get(0).get("title"));
			        		pq.put("description", found.get(0).get("description"));



			        		placeIt repost = triggeredList.get(position);

			        		if( repost instanceof locationPlaceIt ) {
			        			pq.put("latitude", found.get(0).get("latitude"));
			        			pq.put("longitude", found.get(0).get("longitude"));
			        			activeList.add(new locationPlaceIt(repost.getTitle(), repost.getDescription(), 
			        					((locationPlaceIt) repost).getPosition(), mMap, pendingInt, ActivityListView.this));
			        		}
			        		else if( repost instanceof categoryPlaceIt ) {
			        			pq.put("cat1", found.get(0).get("cat1"));
			        			pq.put("cat2", found.get(0).get("cat2"));
			        			pq.put("cat3", found.get(0).get("cat3"));
			        			activeList.add(new categoryPlaceIt(repost.getTitle(), repost.getDescription(), 
			        					((categoryPlaceIt) repost).getCat1(), ((categoryPlaceIt) repost).getCat1(), 
			        					((categoryPlaceIt) repost).getCat1(), mMap, pendingInt, ActivityListView.this));
			        		}
			        		pq.save();
			        		found.get(0).delete();
			        		triggeredList.delete(position);
			        		//saveState();
			        		populateListView(triggeredList.getTitleArray());
			        		adapter.notifyDataSetChanged();
			        	}
			        	catch (ParseException e) 
			        	{
			        		// TODO Auto-generated catch block
			        		e.printStackTrace();
			        	}
			        }})
			        .show();		
			}
		});
	}
	
	/**
	 * Given Code
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}


	/**
	 * Given Code
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			return false;
		}
	};

	/**
	 * This method overrides the method that activities use. It saves the 
	 * current state of the application for later use.
	 */
	@Override
	public void onSaveInstanceState(Bundle outstate) {
    System.out.println("ALV SAVESTATE. ACTIVE: " + activeList.size() + "  TRIGGERED: " + triggeredList.size());
		super.onSaveInstanceState(outstate);
		activeButton();
		pulledButton();
	}
	/**
	 * This method overrides onResume from the activity. By doing so, it 
	 * refreshes the list.
	 */
	@Override
	public void onResume() {
    System.out.println("ALV ONRESUME. ACTIVE: " + activeList.size() + "  TRIGGERED: " + triggeredList.size());
		super.onResume();
		activeButton();
		pulledButton();
	}
	
	/**
	 * This method overrides onStart from the activity. By doing so, it 
	 * refreshes the list.
	 */
	@Override
	public void onStart() {
		System.out.println("ALV ONSTART. ACTIVE: " + activeList.size() + "  TRIGGERED: " + triggeredList.size());
		super.onStart();
		activeButton();
		pulledButton();
	}
	
	/**
	 * This method overrides onPause from the activity. By doing so, it
	 * saves the list.
	 */
	@Override
	public void onPause() { 
    System.out.println("ALV ONPAUSE. ACTIVE: " + activeList.size() + "  TRIGGERED: " + triggeredList.size());
		super.onPause();
		activeButton();
		pulledButton();
	}


}
