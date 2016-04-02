package io.evolution.downtohang;

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

    private int     id ;        // id# of user - jyoon
    private String  username;   // self explanatory -jyoon
    private String  hangStatus; //name for a hangout if they are in one -jyoon
    private String  location;   // geolocation for now its a string -jyoon
    private int     availablity;// available, not available, in a hangout, busy etc. - jyoon

    public User(){
        //constructor
    }

    //setters
    public void setId(int idi){
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setHangStatus(String hangStatus){
        this.hangStatus = hangStatus;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public void setAvailablity(int availablity){
        this.availablity = availablity;
    }

    //getters
    public int getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getHangStatus(){
        return hangStatus;
    }
    public String getLocation(){
        return location;
    }
    public int getAvailablity(){
        return availablity;
    }

}
