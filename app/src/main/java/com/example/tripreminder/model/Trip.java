package com.example.tripreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trips_table")
public class Trip implements Parcelable {
    @PrimaryKey(autoGenerate = true )
    public int tripID;
    String tripName;
    String userID;
    String source;
    public List<String> notes;

    public int getTripID() {
        return tripID;
    }


    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    String destination;
    String date;
    String time;
    public String status;
    public String type;
    public Trip() {

        
    }

    public Trip(String tripName, String userID, String source, String destination, String date, String time, String status, String type,List<String> notes) {
        this.tripName = tripName;
        this.userID = userID;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.status = status;
        this.type = type;
        this.notes=notes;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected Trip(Parcel in) {
        tripID = in.readInt();
        tripName = in.readString();
        userID = in.readString();
        source = in.readString();
        if (in.readByte() == 0x01) {
            notes = new ArrayList<String>();
            in.readList(notes, String.class.getClassLoader());
        } else {
            notes = null;
        }
        destination = in.readString();
        date = in.readString();
        time = in.readString();
        status = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tripID);
        dest.writeString(tripName);
        dest.writeString(userID);
        dest.writeString(source);
        if (notes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(notes);
        }
        dest.writeString(destination);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}