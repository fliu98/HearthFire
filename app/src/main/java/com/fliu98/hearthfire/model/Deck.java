package com.fliu98.hearthfire.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

import java.util.ArrayList;

/**
 * Deck model matching server json.
 */

public class Deck{
    public String deck_id;
    public String name;
    public String creator_id;
    public int hero_class;
    public String description;
    public ArrayList<Integer> deck_list;

    public SparseIntArray getDeckListMap() {
        SparseIntArray deckListMap = new SparseIntArray();
        for (Integer id : deck_list) {
            int copies = deckListMap.get(id);
            copies++;
            deckListMap.put(id, copies);
        }
        return deckListMap;
    }

    public static ArrayList<Integer> deckListToArray(SparseIntArray deckList) {
        ArrayList<Integer> deckArrayList = new ArrayList<>();

        for (int i = 0; i < deckList.size(); i++) {
            for (int k = 0; k < deckList.valueAt(i); k++) {
                deckArrayList.add(deckList.keyAt(i));
            }
        }
        return deckArrayList;
    }

}
