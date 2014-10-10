package com.example.place_its;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

public class activeList extends placeItList{
  protected ArrayList<Marker> markerList;
  
  public activeList() {
    adapter = new ArrayList<placeIt>();
  }
  
  public void add(placeIt p) {
    super.add(p);
  }
  
  @Override
  public void delete(int i) {
    adapter.get(i).removeMarkers();
    super.delete(i);
  }
  
  public placeIt pull(int i) {
    placeIt toReturn = adapter.get(i);
    adapter.get(i).removeMarkers();
    super.delete(i);
    return toReturn;
  }
}
