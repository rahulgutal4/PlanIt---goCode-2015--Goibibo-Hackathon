package com.planit.model;

/**
 * 
 * Class containing Itinerary detail
 * @author raj
 *
 */
public class ItenDetail {
    private int day;
    private String place;
    private String hotelName;
    private long hotelCharges;
    private String placesVisited;
    private String specialAtt;
    private String details;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public long getHotelCharges() {
        return hotelCharges;
    }

    public void setHotelCharges(long hotelCharges) {
        this.hotelCharges = hotelCharges;
    }

    public String getPlacesVisited() {
        if(placesVisited == null) return "";
        return placesVisited;
    }

    public void setPlacesVisited(String placesVisited) {
        this.placesVisited = placesVisited;
    }

    public String getSpecialAtt() {
        if(specialAtt == null) return "";
        return specialAtt;
    }

    public void setSpecialAtt(String specialAtt) {
        this.specialAtt = specialAtt;
    }

    public String getDetails() {
        if(details == null) return "";
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ItenDetail [day=" + day + ", place=" + place + ", hotelName=" + hotelName + ", hotelCharges="
                + hotelCharges + ", placesVisited=" + placesVisited + ", specialAtt=" + specialAtt + ", details=" + details + "]";
    }
    
    

}
