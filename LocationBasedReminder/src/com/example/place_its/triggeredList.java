package com.example.place_its;

import java.util.ArrayList;

public class triggeredList extends placeItList{
  public triggeredList() {
    adapter = new ArrayList<placeIt>();
  }
  
  public placeIt repost(int i) {
    placeIt toReturn = adapter.get(i);
    adapter.remove(i);
    return toReturn;
  }
}
