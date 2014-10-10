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
import android.widget.Toast;

/**
 * TODO This method determines what happens when the user clicks on the map
 *
 * @author Team 8
 *
 */
public class CategoryMarkerInfo extends DialogFragment {
  EditText title;
  EditText description;
  EditText cat1;
  EditText cat2;
  EditText cat3;
  MainActivity main;
  int duration;
  
  /**
   * This method creates the popup dialog
   */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.categorypopup, null))
          
          // This method creates the button fuctionality that will accept title/description
           .setNegativeButton(R.string.Okay, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int ide) {
                   Dialog f = (Dialog) dialog;
                      title = (EditText) f.findViewById(R.id.catTITLE);
                      description = (EditText) f.findViewById(R.id.catDESCRIPTION);
                      cat1 = (EditText) f.findViewById(R.id.category1);
                      cat2 = (EditText) f.findViewById(R.id.category2);
                      cat3 = (EditText) f.findViewById(R.id.category3);
                      main = (MainActivity) getActivity();

                      
                      
                      if(title.getText().toString().length() != 0) {
                        submitMarker();
                      }
                      else {
                        Context context = getActivity();
                        CharSequence text = "Must include title.";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show(); 
                      }
                      dialog.dismiss();
                    
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
      String category1, category2, category3;
      System.out.println(" GOOOOOOOOOOOOOOOOOOOOOOO "+(cat1.getText().toString()));
      if(cat1.getText().toString().length() == 0) { category1 = " "; }
      else { category1 = cat1.getText().toString(); }
      if(cat2.getText().toString().length() == 0) { category2 = " "; }
      else { category2 = cat2.getText().toString(); }
      if(cat3.getText().toString().length() == 0) { category3 = " "; }
      else { category3 = cat3.getText().toString(); }
      StringFactory stringFactory = new StringFactory();
      category1 = stringFactory.createString(category1);
      category2 = stringFactory.createString(category2);
      category3 = stringFactory.createString(category3);
      main.activeList.add(new categoryPlaceIt(title.getText().toString(), description.getText().toString(), 
          category1, category2, category3, main.mMap, main.pendingInt,main));
      ParseObject pq = new ParseObject("ActiveCategory");
      pq.put("title", title.getText().toString());
      pq.put("description", description.getText().toString());
      pq.put("cat1", (String)category1);
      pq.put("cat2", (String)category2);
      pq.put("cat3", (String)category3);
	pq.put("user", ParseUser.getCurrentUser());
	pq.setACL(new ParseACL(ParseUser.getCurrentUser()));
      pq.saveInBackground();
      System.out.println(" CATEGORY SAVE IN BACKGROUND");
     // main.saveState();
    }

    /**
     * Get the title of the marker
     * @return String title of the marker
     */
    public String getTitle()
    {
      return title.getText().toString();
    }
    /**
     * Get the description of the marker
     * @return String of the description of the marker
     */
    public String getDescr()
    {
      return description.getText().toString();
    }
}