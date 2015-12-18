package com.planit.model;

public class Query {
    private String place;
    private int numDays;
    private long minBudget;
    private long maxBudget;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getNumDays() {
        return numDays;
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public long getMinBudget() {
        return minBudget;
    }

    public void setMinBudget(long minBudget) {
        this.minBudget = minBudget;
    }

    public long getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(long maxBudget) {
        this.maxBudget = maxBudget;
    }

}
