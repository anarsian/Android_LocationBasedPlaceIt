package com.example.place_its;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 *
 * @author Team 8
 *
 */
public class LocationMarkerInfo extends DialogFragment {
	EditText title;
	EditText description;
	Spinner spinner;
	MainActivity callingActivity;
	int duration;
	
	/**
	 * This method creates the popup dialog
	 */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.popup, null))
        	
        	// This method creates the button fuctionality that will accept title/description
           .setNegativeButton(R.string.Okay, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int ide) {
                   Dialog f = (Dialog) dialog;
                	   	title = (EditText) f.findViewById(R.id.TITLE);
                	   	description = (EditText) f.findViewById(R.id.DESCRIPTION);
                	   	dialog.dismiss();
                	   	callingActivity = (MainActivity) getActivity();
                	   	if(title.getText().toString().length() != 0) {
                        //callingActivity.onUserSelectValue();
                	   	  submitMarker();
                	   	}
                	   	else {
                	   	  Context context = getActivity();
                	   	  CharSequence text = "Must include title.";
                	   	  int duration = Toast.LENGTH_LONG;
                	   	  Toast toast = Toast.makeText(context, text, duration);
                	   	  toast.show(); 
                	   	}
                	  
                   }
               })
             // This method creates the button functionality when the user clicks cancel
        	.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int ide) {
         	   	dialog.dismiss();  
            }         
        });
        return builder.create();
    }
    
    @SuppressWarnings("static-access")
    private void submitMarker() {
    	System.out.println(" POSITION OF CLICK " + callingActivity.click_pos.latitude);
      callingActivity.activeList.add(new locationPlaceIt(title.getText().toString(), description.getText().toString(), 
          callingActivity.click_pos, callingActivity.mMap, callingActivity.pendingInt,callingActivity));
      ParseObject pq = new ParseObject("ActiveLocation");
		pq.put("title", title.getText().toString());
		pq.put("description", description.getText().toString());
		pq.put("latitude", callingActivity.click_pos.latitude);
		pq.put("longitude", callingActivity.click_pos.longitude);
		pq.put("user", ParseUser.getCurrentUser());
		pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
		pq.saveInBackground();
      //callingActivity.saveState();
    }
    /**
     * Get the title of the marker
     * @return String title of the marker
     *//*
    public String getTitle()
    {
    	return title.getText().toString();
    }

    public String getDescr()
    {
    	return description.getText().toString();
    }*/
}
