package com.fliu98.hearthfire.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

/**
 * Parcelable deck model.
 */

public class DeckInfo implements Parcelable {
    public String deckId;
    public String name;
    public String creatorId;
    public int heroClass;
    public String description;
    public ParcelableSparseIntArray deckList;


    public DeckInfo(){}

    public static DeckInfo buildFromDeckModel(Deck deck) {
        DeckInfo deckInfo = new DeckInfo();
        deckInfo.deckId = deck.deck_id;
        deckInfo.name = deck.name;
        deckInfo.creatorId = deck.creator_id;
        deckInfo.heroClass = deck.hero_class;
        deckInfo.description = deck.description;
        deckInfo.deckList = new ParcelableSparseIntArray(deck.getDeckListMap());
        return deckInfo;
    }

    public void updateDeckList(SparseIntArray deckList) {
        this.deckList = new ParcelableSparseIntArray(deckList);
    }

    private DeckInfo(Parcel in) {
        deckId = in.readString();
        name = in.readString();
        creatorId = in.readString();
        heroClass = in.readInt();
        description = in.readString();
        deckList = in.readParcelable(ParcelableSparseIntArray.class.getClassLoader());
    }

    public static final Creator<DeckInfo> CREATOR = new Creator<DeckInfo>() {
        @Override
        public DeckInfo createFromParcel(Parcel in) {
            return new DeckInfo(in);
        }

        @Override
        public DeckInfo[] newArray(int size) {
            return new DeckInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(deckId);
        parcel.writeString(name);
        parcel.writeString(creatorId);
        parcel.writeInt(heroClass);
        parcel.writeString(description);
        parcel.writeParcelable(deckList, i);
    }
}
