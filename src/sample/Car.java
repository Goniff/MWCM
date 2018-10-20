package sample;

import java.util.UUID;

public class Car {

    private String userID;
    private String make;
    private String model;
    private String year;
    private String comments = "";

    // Constructor
    public Car(String userID, String make, String model, String year, String comments) {
        this.userID = userID;
        this.make = make;
        this.model = model;
        this.year = year;
        this.comments = comments;
    }

    // Auto-generated getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    // Overridden toString method used for displaying the customer's name in a dropdown menu
    public String toString() {
        return make + " " + model + " " + year;
    }

}
