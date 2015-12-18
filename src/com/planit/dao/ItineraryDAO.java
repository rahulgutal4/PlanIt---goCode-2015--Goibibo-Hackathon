package com.planit.dao;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.planit.model.ItenDetail;
import com.planit.model.Itinerary;
import com.planit.util.ItineraryUtil;

public class ItineraryDAO {

    private Connection conn;
    private Map<String, String> placeDetails = new HashMap<String, String>();

    public ItineraryDAO() {
        String url = "jdbc:mysql://localhost:3306/planit";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "password";

        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Itinerary> search(int days, String place, long minB, long maxB) {

        List<Itinerary> result = new ArrayList<Itinerary>();

        String lookupQuery = "select it_id from lookup where days= " + days + " and place = '" + place.toLowerCase() + "' and budget>="
                + minB + " and budget <=" + maxB;

        StringBuilder itineraryQuery = new StringBuilder("select * from itinerary where it_id in(");

        Set<Long> itenIds = new HashSet<Long>();
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            ResultSet rs = stmt.executeQuery(lookupQuery);
            while (rs.next()) {
                itenIds.add(rs.getLong(1));
            }

            if (itenIds.size() > 0) {
                for (long id : itenIds) {
                    itineraryQuery.append(id);
                    itineraryQuery.append(",");
                }
                itineraryQuery.setCharAt(itineraryQuery.length() - 1, ')');
                itineraryQuery.append(" order by usecount desc");

                rs = stmt.executeQuery(itineraryQuery.toString());
                while (rs.next()) {
                    Itinerary itinerary = new Itinerary();
                    itinerary.setIt_id(rs.getLong(1));
                    itinerary.setDays(rs.getInt(2));
                    itinerary.setBudget(rs.getLong(3));
                    itinerary.setCountry(rs.getString(4));
                    itinerary.setState(rs.getString(5));
                    itinerary.setCity(rs.getString(6));
                    result.add(itinerary);

                    List<ItenDetail> details = new ArrayList<ItenDetail>();
                    itinerary.setDetail(details);
                    // Fill the details
                    String getDetailsQuery = "select * from iten_detail where it_id = " + itinerary.getIt_id() + " order by days";
                    ResultSet rs1 = stmt1.executeQuery(getDetailsQuery);
                    while (rs1.next()) {
                        ItenDetail detail = new ItenDetail();
                        details.add(detail);

                        detail.setDay(rs1.getInt(2));
                        detail.setPlace(rs1.getString(3));
                        detail.setHotelName(rs1.getString(4));
                        detail.setHotelCharges(rs1.getLong(5));
                        detail.setPlacesVisited(rs1.getString(6));
                        detail.setSpecialAtt(rs1.getString(7));
                        detail.setDetails(rs1.getString(8));
                    }

                    rs1.close();
                }
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (stmt1 != null && !stmt1.isClosed()) {
                    stmt1.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }
        }

        return result;
    }

    public Itinerary getItineraryById(long it_id) {
        String itineraryQuery = "select * from itinerary where it_id = " + it_id;

        Statement stmt = null;
        Statement stmt1 = null;
        Itinerary itinerary = null;
        try {
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();

            ResultSet rs = stmt.executeQuery(itineraryQuery);
            while (rs.next()) {
                itinerary = new Itinerary();
                itinerary.setIt_id(rs.getLong(1));
                itinerary.setDays(rs.getInt(2));
                itinerary.setBudget(rs.getLong(3));
                itinerary.setCountry(rs.getString(4));
                itinerary.setState(rs.getString(5));
                itinerary.setCity(rs.getString(6));
                itinerary.setUseCount(rs.getLong(7));

                List<ItenDetail> details = new ArrayList<ItenDetail>();
                itinerary.setDetail(details);
                // Fill the details
                String getDetailsQuery = "select * from iten_detail where it_id = " + itinerary.getIt_id() + " order by days";
                ResultSet rs1 = stmt1.executeQuery(getDetailsQuery);
                while (rs1.next()) {
                    ItenDetail detail = new ItenDetail();
                    details.add(detail);

                    detail.setDay(rs1.getInt(2));
                    detail.setPlace(rs1.getString(3));
                    detail.setHotelName(rs1.getString(4));
                    detail.setHotelCharges(rs1.getLong(5));
                    detail.setPlacesVisited(rs1.getString(6));
                    detail.setSpecialAtt(rs1.getString(7));
                    detail.setDetails(rs1.getString(8));
                }

                rs1.close();
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (stmt1 != null && !stmt1.isClosed()) {
                    stmt1.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return itinerary;
    }

    public void add(Itinerary itinerary) {
        long id = System.currentTimeMillis();
        Set<String> lookup = new HashSet<String>();

        String insertLookupQuery = "insert into lookup values(?, ?, ?, ?)";
        String insertItineraryQuery = "insert into itinerary values(?, ?, ?, ?, ?, ?, ?)";
        String insertDetailQuery = "insert into iten_detail values(?, ?, ?, ?, ?, ?, ?, ?)";
        String insertPlacesMappingsQuery = "insert into places_map values(?, ?, ?, ?)";
        String insertPlaceStatsQuery = "insert into placestats values(?,?,?)";
        String city = itinerary.getDetail().get(0).getPlace();
        if (!ItineraryUtil.isEmpty(city)) {
            lookup.add(itinerary.getCountry().trim().toLowerCase());
            lookup.add(itinerary.getState().trim().toLowerCase());

            PreparedStatement pstmt1 = null;
            PreparedStatement pstmt2 = null;
            PreparedStatement pstmt3 = null;
            try {

                int numDays = 0;
                for (ItenDetail detail : itinerary.getDetail()) {
                    numDays += detail.getDay();
                }

                pstmt1 = conn.prepareStatement(insertItineraryQuery);
                pstmt1.setLong(1, id);
                pstmt1.setInt(2, numDays);
                pstmt1.setLong(3, itinerary.getBudget());
                pstmt1.setString(4, itinerary.getCountry());
                pstmt1.setString(5, itinerary.getState());
                pstmt1.setString(6, itinerary.getCity());
                pstmt1.setLong(7, 0);

                pstmt1.execute();

                pstmt2 = conn.prepareStatement(insertDetailQuery);
                for (ItenDetail detail : itinerary.getDetail()) {
                    if (!ItineraryUtil.isEmpty(detail.getPlace())) {
                        lookup.add(detail.getPlace().trim().toLowerCase());

                        pstmt2.setLong(1, id);
                        pstmt2.setInt(2, detail.getDay());
                        pstmt2.setString(3, detail.getPlace());
                        pstmt2.setString(4, detail.getHotelName());
                        pstmt2.setLong(5, detail.getHotelCharges());
                        pstmt2.setString(6, detail.getPlacesVisited());
                        pstmt2.setString(7, detail.getSpecialAtt());
                        pstmt2.setString(8, detail.getDetails());

                        pstmt2.execute();
                    }

                }

                pstmt3 = conn.prepareStatement(insertLookupQuery);
                for (String s : lookup) {
                    pstmt3.setLong(1, id);
                    pstmt3.setInt(2, numDays);
                    pstmt3.setLong(3, itinerary.getBudget());
                    pstmt3.setString(4, s);

                    pstmt3.execute();
                }

                PreparedStatement pstmt4 = conn.prepareStatement(insertPlacesMappingsQuery);
                // Fill the mappings
                for (ItenDetail detail : itinerary.getDetail()) {
                    insertPlaceMapping(pstmt4, itinerary.getCountry().toLowerCase(), detail.getPlace().toLowerCase(), 1);
                    insertPlaceMapping(pstmt4, itinerary.getState().toLowerCase(), detail.getPlace().toLowerCase(), 1);
                }

                for (int i = 0; i < itinerary.getDetail().size() - 1; i++) {
                    ItenDetail detail1 = itinerary.getDetail().get(i);
                    ItenDetail detail2 = itinerary.getDetail().get(i + 1);
                    insertPlaceMapping(pstmt4, detail1.getPlace().toLowerCase(), detail2.getPlace().toLowerCase(), 0);
                    insertPlaceMapping(pstmt4, detail2.getPlace().toLowerCase(), detail1.getPlace().toLowerCase(), 0);
                }

                // Add place description
                PreparedStatement pstmt5 = conn.prepareStatement("insert into description values(?,?)");
                for (ItenDetail detail : itinerary.getDetail()) {
                    String desc = getDetails(detail.getPlace());
                    if (ItineraryUtil.isEmpty(desc)) {
                        desc = getDetailsFromSearch(detail.getPlace());
                        if (!ItineraryUtil.isEmpty(desc)) {
                            pstmt5.setString(1, detail.getPlace());
                            pstmt5.setString(2, desc);
                            pstmt5.execute();
                        }
                    }
                }

                PreparedStatement pstmt6 = conn.prepareStatement(insertPlaceStatsQuery);
                for (ItenDetail detail : itinerary.getDetail()) {
                    pstmt6.setString(1, detail.getPlace().toLowerCase());
                    pstmt6.setLong(2, 1);
                    pstmt6.setInt(3, detail.getDay());
                    pstmt6.execute();
                }

                pstmt4.close();
                pstmt5.close();
                pstmt6.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (pstmt1 != null && !pstmt1.isClosed()) {
                        pstmt1.close();
                    }
                    if (pstmt2 != null && !pstmt2.isClosed()) {
                        pstmt2.close();
                    }
                    if (pstmt3 != null && !pstmt3.isClosed()) {
                        pstmt3.close();
                    }

                } catch (SQLException e) {

                }
            }
        }

    }

    /**
     * Insert a single mapping between places
     * 
     * @param pstmt4
     * @param place1
     * @param place2
     * @param type
     */
    private void insertPlaceMapping(PreparedStatement pstmt4, String place1, String place2, int type) {
        try {
            pstmt4.setString(1, place1 + place2);
            pstmt4.setString(2, place1);
            pstmt4.setString(3, place2);
            pstmt4.setInt(4, type);
            pstmt4.execute();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public List<List<String>> getRecommendations(String dest, int numDays) {
        List<List<String>> recos = new ArrayList<List<String>>();
        String getPlacesQuery = "select type, place2 from places_map where place1 = ?";
        String getPlaceVisitedCountQuery = "select place, count(*) from placestats group by place";
        String getPlaceMaxDayQuery = "select place, max(days) from placestats group by place";
        Map<String, List<String>> graph = new HashMap<String, List<String>>();

        Map<String, Long> placeVisit = new HashMap<String, Long>();
        Map<String, Long> placeMaxDays = new HashMap<String, Long>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(getPlacesQuery);

            int type = -1;
            List<String> initPlaces = new ArrayList<String>();
            initPlaces.add(dest.toLowerCase());

            int initType = 0;
            List<String> startPoints = new ArrayList<String>();

            for (int i = 0; i <= numDays; i++) {
                List<String> newPlaces = new ArrayList<String>();
                for (int j = 0; j < initPlaces.size(); j++) {
                    String place = initPlaces.get(j);
                    List<String> places = new ArrayList<String>();
                    if (!graph.containsKey(place)) {
                        pstmt.setString(1, place);
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            type = rs.getInt(1);
                            places.add(rs.getString(2));
                        }
                        if (i == 0) {
                            if (type == 1) {
                                initType = 1;
                                startPoints = places;
                            }
                        }
                        if (type == 0) {
                            graph.put(place, places);
                        }
                    }
                    newPlaces.addAll(places);
                }
                initPlaces = newPlaces;
            }

            Statement stmt = conn.createStatement();
            Statement stmt1 = conn.createStatement();

            ResultSet rs = stmt.executeQuery(getPlaceVisitedCountQuery);
            while (rs.next()) {
                placeVisit.put(rs.getString(1), rs.getLong(2));
            }

            ResultSet rs1 = stmt1.executeQuery(getPlaceMaxDayQuery);
            while (rs1.next()) {
                placeMaxDays.put(rs1.getString(1), rs1.getLong(2));
            }

            if (initType == 0) {
                List<String> visited = new ArrayList<String>();
                if (placeMaxDays.get(dest) != null) {
                    recos = getPaths(dest, graph, numDays, visited, placeMaxDays.get(dest), placeMaxDays);
                }
            } else {
                for (int i = 0; i < startPoints.size(); i++) {
                    List<String> visited = new ArrayList<String>();
                    if (placeMaxDays.get(startPoints.get(i)) != null) {
                        recos.addAll(getPaths(startPoints.get(i), graph, numDays, visited, placeMaxDays.get(startPoints.get(i)),
                                placeMaxDays));
                    }
                }

            }

            stmt.close();
            stmt1.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<List<String>> finalRecos = new ArrayList<List<String>>();
        Map<Long, List<String>> sortedRecos = new TreeMap<Long, List<String>>();

        Set<String> done = new HashSet<String>();

        for (List<String> ll : recos) {
            String[] arr = ll.toArray(new String[ll.size()]);
            Arrays.sort(arr);
            String key = "";
            for (int i = 0; i < arr.length; i++) {
                key += arr[i];
            }

            if (!done.contains(key)) {
                done.add(key);
                long val = 0;
                for (String s1 : ll) {
                    val += placeVisit.get(s1);
                }
                sortedRecos.put(-1 * val, ll);
            }
        }

        for (List<String> ll : sortedRecos.values()) {
            finalRecos.add(ll);
        }

        return finalRecos;
    }

    private List<List<String>> getPaths(String dest, Map<String, List<String>> graph, int numDays, List<String> visited, Long currDay,
            Map<String, Long> placeMaxDays) {
        List<List<String>> res = new ArrayList<List<String>>();
        if (currDay >= numDays) {
            List<String> entry = new ArrayList<String>();
            entry.add(dest);
            res.add(entry);
            return res;
        }

        visited.add(dest);
        for (String place : graph.get(dest)) {
            if (!visited.contains(place)) {
                List<List<String>> result = getPaths(place, graph, numDays, visited, currDay + placeMaxDays.get(place), placeMaxDays);
                for (List<String> ll : result) {
                    ll.add(0, dest);
                    res.add(ll);
                }
            }
        }

        return res;
    }

    public void cleanup() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {

            }
        }

    }

    public String getDetails(String place) {
        String query = "select description from description where place ='" + place.toLowerCase() + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {

        }

        return "";
    }

    public String getDetailsFromSearch(String place) {
        if (placeDetails.containsKey(place)) {
            return placeDetails.get(place);
        }

        String res = "";

        String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String query = "" + place;
        String charset = "UTF-8";
        try {
            URL url = new URL(address + URLEncoder.encode(query, charset));
            Reader reader = new InputStreamReader(url.openStream(), charset);
            GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

            int total = results.getResponseData().getResults().size();

            for (int i = 0; i <= total - 1; i++) {

                String content = results.getResponseData().getResults().get(i).getContent();
                content = content.replace("<b>", "");
                content = content.replace("</b>", "");

                if (!content.toLowerCase().contains("book") && !content.toLowerCase().contains("hotel")
                        && !content.toLowerCase().contains("ticket")) {
                    // System.out.println(content);
                    res += content;

                }

            }
            if (!ItineraryUtil.isEmpty(res)) {
                placeDetails.put(place, res);
            }
        } catch (Exception e) {

        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        ItineraryDAO dao = new ItineraryDAO();
        System.out.println(dao.getRecommendations("rajasthan", 2));
        // System.out.println(dao.search(3, "india", 15000, 25000));
        // System.out.println(dao.getDetailsFromSearch("munnar"));
        // System.out.println(dao.getDetailsFromSearch("munnar"));
        System.out.println(dao.getDetails("munnar"));

    }

    public void updateCount(long it_id, long count) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("update itinerary set usecount=" + count + " where it_id=" + it_id);
        } catch (Exception e) {

        }
    }
}

class GoogleResults {

    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public String toString() {
        return "ResponseData[" + responseData + "]";
    }

    static class ResponseData {
        private List<Result> results;

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public String toString() {
            return "Results[" + results + "]";
        }
    }

    static class Result {
        private String url;
        private String title;
        private String content;

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String toString() {
            return "Result[url:" + url + ",title:" + title + "]";
        }
    }
}
