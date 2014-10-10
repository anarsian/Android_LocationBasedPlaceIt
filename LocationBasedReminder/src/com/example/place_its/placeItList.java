package com.example.place_its;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public abstract class placeItList {
  protected ArrayList<placeIt> adapter;
  
  public void add(placeIt p) { 
    adapter.add(p);
  }
  
  public placeIt get(int i) {
    return adapter.get(i);
  }
  
  public int size() {
    return adapter.size();
  }
  
  public void delete(int i) {
    adapter.remove(i);
  }
  
  public void removeAll() {
	  System.out.println("Adaptor sizeee" + adapter.size());
    for(int i=0; i<adapter.size(); i++) {
      adapter.get(i).removeMarkers();
      adapter.remove(i);
    }
  }
  
  public ArrayList<String> getTitleArray() {
	    ArrayList<String> toReturn = new ArrayList<String>();
	    for(int i=0; i<adapter.size(); i++) {
      toReturn.add(adapter.get(i).getTitle());
    }
    return toReturn;
  }
  
  public ArrayList<String> getDesciptionArray() {
    ArrayList<String> toReturn = new ArrayList<String>();
    for(int i=0; i<adapter.size(); i++) {
      toReturn.add(adapter.get(i).getDescription());
    }
    return toReturn;
  }  
}
