package com.planit.model;

import java.util.List;

/**
 * Class representing Itinerary
 * 
 * @author raj
 *
 */
public class Itinerary {
    private long it_id;
    private int days;
    private long budget;
    private String country;
    private String state;
    private String city;
    private long useCount;

    private List<ItenDetail> detail;

    public long getIt_id() {
        return it_id;
    }

    public void setIt_id(long it_id) {
        this.it_id = it_id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<ItenDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<ItenDetail> detail) {
        this.detail = detail;
    }

    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }

    @Override
    public String toString() {
        return "Itinerary [it_id=" + it_id + ", days=" + days + ", budget=" + budget + ", country=" + country + ", state=" + state
                + ", city=" + city + ", detail=" + detail + "]";
    }

}
