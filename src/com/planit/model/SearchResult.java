package com.planit.model;

import java.util.List;

/**
 * Bean for returning search result.
 * 
 * @author raj
 *
 */
public class SearchResult {
    private Query query;
    private long count;
    private List<Itinerary> results;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Itinerary> getResults() {
        return results;
    }

    public void setResults(List<Itinerary> results) {
        this.results = results;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
