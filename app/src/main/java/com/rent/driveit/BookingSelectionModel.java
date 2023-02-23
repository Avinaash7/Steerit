package com.rent.driveit;

public class BookingSelectionModel {

long PickupDateInMillis;
long DropoffDateinMillis;
float PickupHour;
float DropoffHour;

    public long getPickupDateInMillis() {
        return PickupDateInMillis;
    }

    public void setPickupDateInMillis(long pickupDateInMillis) {
        PickupDateInMillis = pickupDateInMillis;
    }

    public long getDropoffDateinMillis() {
        return DropoffDateinMillis;
    }

    public void setDropoffDateinMillis(long dropoffDateinMillis) {
        DropoffDateinMillis = dropoffDateinMillis;
    }

    public float getPickupHour() {
        return PickupHour;
    }

    public void setPickupHour(float pickupHour) {
        PickupHour = pickupHour;
    }

    public float getDropoffHour() {
        return DropoffHour;
    }

    public void setDropoffHour(float dropoffHour) {
        DropoffHour = dropoffHour;
    }

    public BookingSelectionModel(){

}

    public BookingSelectionModel(long pickupDateInMillis, long dropoffDateinMillis, float pickupHour, float dropoffHour) {
        PickupDateInMillis = pickupDateInMillis;
        DropoffDateinMillis = dropoffDateinMillis;
        PickupHour = pickupHour;
        DropoffHour = dropoffHour;
    }
}
