package com.fliu98.hearthfire.model;

import android.os.Parcel;
import android.util.SparseIntArray;
import android.os.Parcelable;

/**
 * Parcelable implementation of SparseIntArray.
 */
public class ParcelableSparseIntArray extends SparseIntArray implements Parcelable {

    private ParcelableSparseIntArray(Parcel in) {
        ParcelableSparseIntArray read = new ParcelableSparseIntArray();
        int size = in.readInt();
        int[] keys = new int[size];
        int[] values = new int[size];

        in.readIntArray(keys);
        in.readIntArray(values);

        for (int i = 0; i < size; i++) {
            read.put(keys[i], values[i]);
        }
    }

    private ParcelableSparseIntArray() {

    }

    public ParcelableSparseIntArray(SparseIntArray sparseIntArray) {
        for (int i = 0; i < sparseIntArray.size(); i++) {
            this.put(sparseIntArray.keyAt(i), sparseIntArray.valueAt(i));
        }
    }

    public SparseIntArray getSparseIntArray() {
        SparseIntArray sparseIntArray = new SparseIntArray(size());
        for (int i = 0; i < size(); i++) {
            sparseIntArray.put(keyAt(i), valueAt(i));
        }
        return sparseIntArray;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int[] keys = new int[size()];
        int[] values = new int[size()];

        for (int i = 0; i < size(); i++) {
            keys[i] = keyAt(i);
            values[i] = valueAt(i);
        }

        dest.writeInt(size());
        dest.writeIntArray(keys);
        dest.writeIntArray(values);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableSparseIntArray> CREATOR = new Creator<ParcelableSparseIntArray>() {
        @Override
        public ParcelableSparseIntArray createFromParcel(Parcel in) {
            return new ParcelableSparseIntArray(in);
        }

        @Override
        public ParcelableSparseIntArray[] newArray(int size) {
            return new ParcelableSparseIntArray[size];
        }
    };
}
