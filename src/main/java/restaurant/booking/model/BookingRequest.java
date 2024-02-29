package restaurant.booking.model;

import java.time.LocalDate;

/**
 * 
 * BookingRequest Model Class
 *
 */
public class BookingRequest {
    private String customerName;
    private int tableSize;
    //TODO: LocalDate
    private String date;
    //TODO: LocalDateTime
    private  String time;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getTableSize() {
		return tableSize;
	}
	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
