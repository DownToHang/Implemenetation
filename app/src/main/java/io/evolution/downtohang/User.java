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

    private String   id ;        // id# of user
    private String   username;   // self explanatory
    private String   hangoutStatus; //name for a hangout if they are in one
    private Location location;   // geolocation for now its a string
    private String   availability;// available, not available, in a hangout, busy etc.
    private boolean  isSelected;

    public User(){
        //constructor
    }

    //Constructor for Hangout Activity
    public User(String id, String username, String hangoutStatus){
        this.id             = id;
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
        this.id = id;
        this.username = username;
    }

    public User(String id, String username, String hangoutStatus,
                String availability) {
        this.id = id;
        this.username = username;
        this.hangoutStatus = hangoutStatus;
        this.availability = availability;
    }

    //setters
    public void setId(String id){
        this.id = id;
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
    public void setAvailability(String availability){
        this.availability = availability;
    }

    //getters
    public String getId(){
        return id;
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
    public String getAvailability(){
        return availability;
    }

}
