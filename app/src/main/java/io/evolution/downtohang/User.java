package io.evolution.downtohang;

import android.location.Location;
import android.media.Image;

/**
 * Created by Yoonix on 4/2/2016.
 *
 * This is the User object, contains information on an individual user.
 * It will contain the following:
 *
 * ID number
 * Username
 * Hangout status
 * Location
 * Availability
 *
 * - Jyoon
 */
public class User {

    private String   uuid ;        // id# of user
    private String   username;   // self explanatory
    private String   hangoutStatus; //name for a hangout if they are in one
    private Location location;   // geolocation for now its a string
    private Integer   status;// available, not available, in a hangout, busy etc.
    private boolean  isSelected;

    private double Latitude;
    private double Longitude;

    public User(){
        //constructor
    }

    public User(String uuid, String username, Integer status, String hangoutStatus, double latitude,
                double longitude) {
        this.uuid = uuid;
        this.username = username;
        this.status = status;
        this.hangoutStatus = hangoutStatus;
        location = new Location("");
        Latitude = latitude;
        Longitude = longitude;
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    //returns latitude
    public double getLat() {
        return Latitude;
    }

    //returns longitude
    public double getLong() {
        return Longitude;
    }

    //Constructor for Hangout Activity
    public User(String id, String username, String hangoutStatus){
        this.uuid             = id;
        this.username       = username;
        this.hangoutStatus  = hangoutStatus;
        isSelected = false;
    }

    //check if the user is selected
    public boolean isSelected() {
        return isSelected;
    }

    //setting if the user is selected or not
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    //Constuctor for Create Hangout Activity
    public User(String username){
        this.username = username;
    }

    //Contructor for Manage Contacts
    public User(String id, String username, Image profilePic){
        this.uuid = id;
        this.username = username;
    }

    public User(String id, String username, String hangoutStatus,
                Integer status) {
        this.uuid = id;
        this.username = username;
        this.hangoutStatus = hangoutStatus;
        this.status = status;
    }

    //setters
    public void setId(String id){
        this.uuid = id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setHangStatus(String hangStatus){
        this.hangoutStatus = hangStatus;
    }
    public void setLocation(Location location){
        this.location = location;
    }
    public void setStatus(Integer availability){
        this.status = availability;
    }

    //getters
    public String getUUID(){
        return uuid;
    }
    public String getUsername(){
        return username;
    }
    public String getHangStatus(){
        return hangoutStatus;
    }
    public Location getLocation(){
        return location;
    }
    public Integer getStatus(){
        return status;
    }

}
