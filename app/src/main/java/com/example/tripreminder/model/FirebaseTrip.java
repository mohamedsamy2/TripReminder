package com.example.tripreminder.model;

import java.util.List;

public class FirebaseTrip {
    public int tripID;
    String tripName;
    String userID;
    String source;
    public String notes;
    String destination;
    String date;
    String time;
    public String status;
    public String type;

    public FirebaseTrip() {
    }

    public FirebaseTrip(int tripID, String tripName, String userID, String source, String notes, String destination, String date, String time, String status, String type) {
        this.tripID = tripID;
        this.tripName = tripName;
        this.userID = userID;
        this.source = source;
        this.notes = notes;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.status = status;
        this.type = type;
    }

    public int getTripID() {
        return tripID;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}
