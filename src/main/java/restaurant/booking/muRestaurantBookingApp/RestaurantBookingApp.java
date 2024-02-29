package restaurant.booking.muRestaurantBookingApp;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import io.muserver.Method;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import restaurant.booking.model.BookingRequest;

/**
 * This application purpose is to allow customers to book tables at the
 * restaurant and owner of the restaurant to get all the bookings of particular
 * day.
 * 
 */
public class RestaurantBookingApp {

	// Store customer bookings
	private static Map<String, BookingRequest> bookings = new HashMap<>();
	private static Gson gson = new Gson();

	public static void main(String[] args) {

		MuServer server = MuServerBuilder.httpServer().withHttpPort(8080)
				.addHandler(Method.GET, "/health", (req, res, params) -> {
					healthCheckHandler(req, res);})
				.addHandler(Method.POST, "/book", (req, res, params) -> {
					handleBookingRequest(req, res);
				}).addHandler(Method.GET, "/bookings", (req, res, params) -> {
					fetchRestaurantBookedData(req, res);
				}).start();
	      
		System.out.println("Server started at " + server.uri());
	}

	/**
	 * This method checks application up/not.
	 * 
	 * @param req MuRequest
	 * @param res MuResponse
	 */
	private static void healthCheckHandler(MuRequest req, MuResponse res) {
		res.write("Server started Successfully...");
	}
	
	
	/**
	 * This method fetch customers Booked Data on specific date
	 * 
	 * @param req MuRequest
	 * @param res MuResponse
	 */
	private static void fetchRestaurantBookedData(MuRequest req, MuResponse res) {
		res.contentType("application/json");
		   String date = req.query().get("date");
		    List<BookingRequest> bookingsForDate = filterBookingsByDate(date);
		    if (bookingsForDate.isEmpty()) {
		        res.status(404);
		        res.write("No bookings found for the specified date");
		    }else{
		String json = gson.toJson(bookingsForDate);
		res.write(json);
		    }
	}

	/**
	 * This method handles the customer Booking request,validates the requested
	 * data and stores in data structure.
	 * 
	 * @param req
	 *            MuRequest
	 * @param res
	 *            MuResponse
	 * @throws IOException
	 */
	private static void handleBookingRequest(MuRequest req, MuResponse res) throws IOException {
		 try {
		// Set response content type
		res.contentType("application/json");

		// Read JSON request body as string
		String requestBody = req.readBodyAsString();
		if (requestBody == null || requestBody.isEmpty()) {
			res.status(400);
			res.write("Empty request body");
			return;
		}
		BookingRequest bookingRequest = gson.fromJson(requestBody, BookingRequest.class);

		String validationError = validateBookingRequest(bookingRequest);
        if (validationError != null) {
            res.status(400);
            res.write(validationError);
            return;
        }
		// Check if the time slot is already booked
		if (isTimeSlotBooked(bookingRequest.getDate(), bookingRequest.getTime())) {
			res.status(400);
			res.write("Requested time slot is not available");
			return;
		}


			if (!bookingRequest.getDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
				res.status(400);
				res.write("Invalid date format. Date must be in the format YYYY-MM-DD");
				return;
			}

			if (!bookingRequest.getTime().matches("\\d{2}:\\d{2}")) {
				res.status(400);
				res.write("Invalid time format. Time must be in the format HH:MM");
				return;
			}
		    
		String dateTimeSlot = bookingRequest.getDate() + " " +  bookingRequest.getTime();
		if (bookings.containsKey(dateTimeSlot)) {
			res.status(400);
			res.write("Requested time slot is not available");
			return;
		}
		
		bookings.put(dateTimeSlot, bookingRequest);
		res.status(201);
		res.write("Table booked successfully");
	   } catch (Exception e) {
	        res.status(500);
	        res.write("An unexpected error occurred");
	        e.printStackTrace(); 
	    }
	}

	/**
	 * This method validates booking request Data
	 * 
	 * @param bookingRequest
	 * @return String
	 */
	 private static String validateBookingRequest(BookingRequest bookingRequest) {
	        if (bookingRequest.getCustomerName() == null || bookingRequest.getDate() == null || bookingRequest.getTime() == null || bookingRequest.getTableSize() <=0 ) {
	            return "Incomplete booking information";
	        }

	        try {

	            if (bookingRequest.getTableSize() < 1 || bookingRequest.getTableSize() > 20) {
	                return "Table size must be between 1 and 20";
	            }
	        } catch (NumberFormatException e) {
	            return "Invalid table size format";
	        }

	        return null;
	        
	 }
	 
	 
	 
	/**
	 * This method checks the time slot for a specific date is already booked or
	 * not.
	 * 
	 * @param localDate
	 * @param time
	 * @return boolean
	 */
	private static boolean isTimeSlotBooked(String localDate, String time) {
		@SuppressWarnings("unchecked")
		Set<String> bookedTimes = (Set<String>) bookings.get(localDate);
		return bookedTimes != null && bookedTimes.contains(time);
	}
	

	/**
	 * This method filters Bookings By Date
	 * 
	 * @param date
	 * @return List of Bookings
	 */
	private static List<BookingRequest> filterBookingsByDate(String date) {
		return bookings.values().stream().filter(booking -> date.equals(booking.getDate()))
				.collect(Collectors.toList());
	}
}
