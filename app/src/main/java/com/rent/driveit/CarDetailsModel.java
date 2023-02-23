package com.rent.driveit;

public class CarDetailsModel {

    String Car_Url;
    String Car_Name;
    String Seater;
    String Car_Type;
    String Fuel_Type;
    String Car_Location;
    String Owner_Name;
    String Fuel_Limit;

    public CarDetailsModel(String car_Url, String car_Name, String seater, String car_type, String fuel_Type, String car_Location, String owner_Name, String fuel_Limit) {
        Car_Url = car_Url;
        Car_Name = car_Name;
        Seater = seater;
        Car_Type = car_type;
        Fuel_Type = fuel_Type;
        Car_Location = car_Location;
        Owner_Name = owner_Name;
        Fuel_Limit = fuel_Limit;
    }

    public String getFuel_Limit() {
        return Fuel_Limit;
    }

    public void setFuel_Limit(String fuel_Limit) {
        Fuel_Limit = fuel_Limit;
    }

    public String getCar_Url() {
        return Car_Url;
    }

    public void setCar_Url(String car_Url) {
        Car_Url = car_Url;
    }

    public String getCar_Name() {
        return Car_Name;
    }

    public void setCar_Name(String car_Name) {
        Car_Name = car_Name;
    }

    public String getSeater() {
        return Seater;
    }

    public void setSeater(String seater) {
        Seater = seater;
    }

    public String getCar_type() {
        return Car_Type;
    }

    public void setCar_type(String car_type) {
        Car_Type = car_type;
    }

    public String getFuel_Type() {
        return Fuel_Type;
    }

    public void setFuel_Type(String fuel_Type) {
        Fuel_Type = fuel_Type;
    }

    public String getCar_Location() {
        return Car_Location;
    }

    public void setCar_Location(String car_Location) {
        Car_Location = car_Location;
    }

    public String getOwner_Name() {
        return Owner_Name;
    }

    public void setOwner_Name(String owner_Name) {
        Owner_Name = owner_Name;
    }

    public CarDetailsModel(){

    }
}
