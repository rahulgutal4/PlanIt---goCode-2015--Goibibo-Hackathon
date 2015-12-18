package com.planit.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.planit.dao.ItineraryDAO;
import com.planit.model.ItenDetail;
import com.planit.model.Itinerary;
import com.planit.util.ItineraryUtil;

@Path("/")
public class ItineraryService {

    private static String folder = "F:/projects/PlanIT/WebContent/";

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello() {
        return "hello world!!";
    }

    @GET
    @Path("/search")
    @Produces(MediaType.TEXT_HTML)
    public String search(@QueryParam("dest") String dest, @QueryParam("numDays") int numDays, @QueryParam("minB") long minB,
            @QueryParam("maxB") long maxB) throws Exception {

        StringBuilder htmltext = getHtmlString(numDays, dest, minB, maxB);
        return htmltext.toString();

    }

    @POST
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response add(String input) {
        Itinerary itinerary = new Itinerary();
        try {
            Gson gson = new Gson();
            itinerary = gson.fromJson(input, Itinerary.class);
            String errMsg = validate(itinerary);
            if (errMsg == null) {
                ItineraryDAO dao = new ItineraryDAO();
                dao.add(itinerary);
                dao.cleanup();
            } else {
                return Response.status(200).entity(errMsg).build();
            }
        } catch (Exception e) {
            Response.status(200).entity(e.getMessage()).build();
        }
        return Response.status(200).entity(itinerary.toString()).build();
    }

    @POST
    @Path("/add1")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add1(Itinerary itinerary) {
        return Response.status(200).entity(itinerary.toString()).build();
    }

    private String validate(Itinerary itinerary) {
        String msg = null;
        if (ItineraryUtil.isEmpty(itinerary.getCountry())) {
            msg = "Country can not be null";
        } else if (ItineraryUtil.isEmpty(itinerary.getState())) {
            msg = "State can not be null";
        } else if (itinerary.getDetail() == null || itinerary.getDetail().size() == 0) {
            msg = "Detail can not be null";
        }
        return msg;
    }

    public static StringBuilder getHtmlString(int numDays, String dest, long minB, long maxB) throws Exception {

        ItineraryDAO dao = new ItineraryDAO();
        List<Itinerary> res = dao.search(numDays, dest, minB, maxB);

        int itenaries = res.size();

        StringBuilder htmlString = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(folder + "css.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            htmlString.append(line);

        }

        htmlString.append("<body>");

        br = new BufferedReader(new FileReader(folder + "Title.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            htmlString.append(line);

        }

        int i1 = 274, i2 = 282, i3 = 277;
        int i1_1 = 314, i1_2 = 314, i1_3 = 318, i1_4 = 318, i1_5 = 318;
        int d1 = 81, d2 = 343;
        int d1_1 = 143, d1_2 = 351;
        int p1 = 94, p2 = 377;
        int h1 = 95, h2 = 395;
        int hc1 = 95, hc2 = 414;
        int pv1 = 95, pv2 = 431;
        int sa1 = 97, sa2 = 509;
        int ac1 = 99, ac2 = 586;
        int pb1 = 96, pb2 = 452;
        int ab1 = 97, ab2 = 529;
        int cb1 = 98, cb2 = 608;
        int sb1 = 218, sb2 = 670;
        for (int i = 1; i <= itenaries; i++) {

            Itinerary itinerary = res.get(i - 1);
            List<ItenDetail> daysDetails = itinerary.getDetail();
            int days = itinerary.getDetail().size();

            StringBuilder itineraryHead = new StringBuilder();
            br = new BufferedReader(new FileReader(folder + "itineraryHead.txt"));
            while (true) {

                String line = br.readLine();
                if (line == null)
                    break;

                itineraryHead.append(line);

            }

            htmlString.append(String.format(itineraryHead.toString(), (i - 1) * 430 + i1, (i - 1) * 430 + i2, i, itinerary.getIt_id(),
                    (i - 1) * 430 + i3));

            itineraryHead.setLength(0);
            br = new BufferedReader(new FileReader(folder + "itinerary1.txt"));
            while (true) {

                String line = br.readLine();
                if (line == null)
                    break;

                itineraryHead.append(line);

            }
            int displayDays = 0;
            for (ItenDetail ddd : itinerary.getDetail()) {
                displayDays += ddd.getDay();
            }

            htmlString.append(String.format(itineraryHead.toString(), (i - 1) * 430 + i1_1, (i - 1) * 430 + i1_2, 180 * days, 180 * days,
                    (i - 1) * 430 + i1_3, displayDays, (i - 1) * 430 + i1_4, itinerary.getBudget()));
            int dayCount = 1;
            for (int j = 1; j <= days; j++) {

                ItenDetail day = daysDetails.get(j - 1);
                itineraryHead.setLength(0);
                br = new BufferedReader(new FileReader(folder + "day.txt"));
                while (true) {

                    String line = br.readLine();
                    if (line == null)
                        break;

                    itineraryHead.append(line);

                }

                if (j != days) {

                    StringBuilder button = new StringBuilder();
                    br = new BufferedReader(new FileReader(folder + "submitButton.txt"));
                    while (true) {

                        String line = br.readLine();
                        if (line == null)
                            break;

                        button.append(line);

                    }

                    htmlString.append(String.format(button.toString(), day.getPlace(), daysDetails.get(j).getPlace(), "distance" + i, "map"
                            + i, (j - 1) * 180 + sb1, (i - 1) * 430 + sb2));

                }

                String dayRange = "";
                if (day.getDay() > 1) {
                    dayRange = dayCount + "-" + (dayCount + day.getDay() - 1);
                } else {
                    dayRange = dayCount + "";
                }
                dayCount += day.getDay();

                htmlString.append(String.format(itineraryHead.toString(), (j - 1) * 180 + d1, (i - 1) * 430 + d2, (j - 1) * 180 + d1_1,
                        (i - 1) * 430 + d1_2, dayRange, (j - 1) * 180 + p1, (i - 1) * 430 + p2, day.getPlace(), (j - 1) * 180 + h1, (i - 1)
                                * 430 + h2, day.getHotelName().substring(0, Math.min(day.getHotelName().length(), 14)),
                        (j - 1) * 180 + hc1, (i - 1) * 430 + hc2, day.getHotelCharges(), (j - 1) * 180 + pv1, (i - 1) * 430 + pv2, (j - 1)
                                * 180 + sa1, (i - 1) * 430 + sa2, (j - 1) * 180 + ac1, (i - 1) * 430 + ac2, (j - 1) * 180 + pb1, (i - 1)
                                * 430 + pb2, day.getPlacesVisited(), (j - 1) * 180 + ab1, (i - 1) * 430 + ab2, day.getSpecialAtt(), (j - 1)
                                * 180 + cb1, (i - 1) * 430 + cb2, day.getDetails(), i, 180 * days + 80, (i - 1) * 430 + 327, i, i, i, i));

            }

        }

        StringBuilder recomm = new StringBuilder();
        br = new BufferedReader(new FileReader(folder + "recommhead.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            recomm.append(line);

        }

        // , factor + 519, factor + 533, factor + 519, factor + 533
        int top = itenaries * 430 + 314;
        int rb1 = 64, rb2 = top + 109;
        int rt1 = 111, rt2 = top + 123;
        int rd1 = 75, rd2 = top + 156;
        int rs1 = 228, rs2 = top + 189;
        int rm1 = 40, rm2 = top + 20;
        int rl2 = top + 295;

        htmlString.append(String.format(recomm.toString(), top, numDays, dest));

        recomm.setLength(0);
        br = new BufferedReader(new FileReader(folder + "recomm.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            recomm.append(line);

        }

        StringBuilder map = new StringBuilder();
        br = new BufferedReader(new FileReader(folder + "map.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            map.append(line);

        }

        List<List<String>> recommendations = dao.getRecommendations(dest.toLowerCase(), numDays);
        for (int i = 1; i <= recommendations.size(); i++) {

            List<String> recommendation = recommendations.get(i - 1);
            rb1 = 64;
            rt1 = 111;
            rd1 = 75;
            rs1 = 228;
            htmlString.append(String.format(map.toString(), i, rb1 + (recommendation.size()) * 234, rm2, i, i, i, i));

            for (int j = 1; j <= recommendation.size(); j++) {

                String place = recommendation.get(j - 1);
                htmlString.append(String.format(recomm.toString(), rb1, rb2, rt1, rt2, place, rd1, rd2, dao.getDetails(place)));

                if (j != recommendation.size()) {

                    StringBuilder button = new StringBuilder();
                    br = new BufferedReader(new FileReader(folder + "submitButton.txt"));
                    while (true) {

                        String line = br.readLine();
                        if (line == null)
                            break;

                        button.append(line);

                    }

                    htmlString
                            .append(String.format(button.toString(), place, recommendation.get(j), "distanceR" + i, "mapR" + i, rs1, rs2));

                }

                rb1 += 234;
                rt1 += 234;
                rd1 += 234;
                rs1 += 234;

            }

            StringBuilder line1 = new StringBuilder();
            br = new BufferedReader(new FileReader(folder + "line.txt"));
            while (true) {

                String line = br.readLine();
                if (line == null)
                    break;

                line1.append(line);

            }
            htmlString.append(String.format(line1.toString(), rl2, rb1 - 234));

            rb2 += 232;
            rt2 += 232;
            rd2 += 232;
            rs2 += 232;
            rl2 += 232;
            rm2 += 240;
        }

        dao.cleanup();
        return htmlString;

    }

    @GET
    @Path("/build/{it_id}")
    @Produces(MediaType.TEXT_HTML)
    public String build(@PathParam("it_id") long it_id) throws Exception {

        ItineraryDAO dao = new ItineraryDAO();
        Itinerary itinerary = dao.getItineraryById(it_id);

        dao.updateCount(it_id, itinerary.getUseCount() +1);
        String htmltext = getHtmlStringBuild(itinerary);
        dao.cleanup();
        return htmltext.toString();

    }

    public static String getHtmlStringBuild(Itinerary itinerary) throws Exception {

        StringBuilder htmlString = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(folder + "buildcss.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            htmlString.append(line);

        }

        br = new BufferedReader(new FileReader(folder + "build_page_head.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            htmlString.append(line);

        }

        br = new BufferedReader(new FileReader(folder + "build_head.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            htmlString.append(line);

        }

        StringBuilder ticket = new StringBuilder();
        br = new BufferedReader(new FileReader(folder + "build_ticket.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            ticket.append(line);

        }

        StringBuilder day = new StringBuilder();
        br = new BufferedReader(new FileReader(folder + "build_day.txt"));
        while (true) {

            String line = br.readLine();
            if (line == null)
                break;

            day.append(line);

        }

        int factor = 230;
        int t1 = 49, t2 = 80, t3 = 64;
        int p1 = 136, p2 = 150, p3 = 199, p4 = 150, p5 = 217, p6 = 181;
        for (int i = 1; i <= itinerary.getDetail().size(); i++) {

            if (i == 1) {
                htmlString.append(String.format(ticket.toString(), t1, t2, "Arrival", t3));
            } else {
                htmlString.append(String.format(ticket.toString(), t1, t2, "Travel", t3));
            }

            htmlString.append(String.format(day.toString(), p1, p2, p3, itinerary.getDetail().get(i - 1).getPlace(), p4, itinerary
                    .getDetail().get(i - 1).getHotelName(), p5, p6));

            t1 += factor;
            t2 += factor;
            t3 += factor;
            p1 += factor;
            p2 += factor;
            p3 += factor;
            p4 += factor;
            p5 += factor;
            p6 += factor;
        }
        htmlString.append(String.format(ticket.toString(), t1, t2, "Departure", t3));
        return htmlString.toString();

    }

    public static void main(String[] args) throws Exception {

        // BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "results.html"));

        ItineraryDAO dao = new ItineraryDAO();
        // Itinerary itinerary = dao.getItineraryById(1);

        // bw.write(getHtmlString(3, "munnar", 1, 50000).toString());
        // bw.close();

        Itinerary iti = new Itinerary();
        iti.setBudget(50000);
        iti.setCountry("India");
        iti.setDays(2);
        iti.setState("kerala");

        ItenDetail id = new ItenDetail();
        id.setDay(1);
        id.setPlace("munnar");
        id.setHotelName("inn");

        List<ItenDetail> ll = new ArrayList<ItenDetail>();
        ll.add(id);
        iti.setDetail(ll);
        System.out.println((new Gson()).toJson(iti));
    }

}
