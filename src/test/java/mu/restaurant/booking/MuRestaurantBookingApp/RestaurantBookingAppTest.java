package mu.restaurant.booking.MuRestaurantBookingApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;

/**
 * 
 * Test class for Restaurant Booking App
 *
 */
public class RestaurantBookingAppTest {

    private static MuServer server;

    @BeforeClass
    public static void setUp() {
        server = MuServerBuilder.httpServer()
            .withHttpPort(9080)
            .addHandler(Method.POST, "/book", (req, res, params) -> {
                // Mock book API logic
                res.write("Table booked successfully");
            })
            .addHandler(Method.GET, "/bookings", (req, res, params) -> {
                // Mock bookings API logic
                res.write("List of bookings");
            })
            .start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }

    @Test
    public void testBookAPI() throws Exception {
        URL url = new URL("http://localhost:9080/book");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        int responseCode = con.getResponseCode();
        Assert.assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = in.readLine();
        in.close();

        Assert.assertEquals("Table booked successfully", response);
    }

    @Test
    public void testBookingsAPI() throws Exception {
        URL url = new URL("http://localhost:9080/bookings");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        Assert.assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = in.readLine();
        in.close();

        Assert.assertEquals("List of bookings", response);
    }
   
}
