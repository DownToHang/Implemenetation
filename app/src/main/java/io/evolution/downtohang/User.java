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
    private Integer   status;// available, not available, in a hangout, busy etc.
    private boolean  isSelected;

    private double latitude;
    private double longitude;

    public User(){
        //constructor
    }

    public User(String uuid, String username, Integer status, String hangoutStatus, double latitude,
                double longitude) {
        this.uuid = uuid;
        this.username = username;
        this.status = status;
        this.hangoutStatus = hangoutStatus;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //returns latitude
    public double getLat() {
        return latitude;
    }

    //returns longitude
    public double getLong() {
        return longitude;
    }

    public void setLatitude(double l) {
        latitude = l;
    }

    public void setLongitude(double l) {
        longitude = l;
    }

    public Location getLocation() {
        Location l = new Location("");
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        return l;
    }

    public boolean equals(Object o) {
        if(o instanceof User) {
            User u = (User) o;
            if(u.getUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
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
    public Integer getStatus(){
        return status;
    }

}
