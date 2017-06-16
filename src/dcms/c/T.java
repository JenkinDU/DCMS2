package dcms.c;

import java.io.Serializable;

public class T implements Serializable {

	private int recordID = 0;
	
	private String firstn = "";
	private String lastn = "";
	private String address = "";
	private String phone = "";
	private String tClass = "";
	
	private String departure = "";
	private String departureDate = "";
	private String destination = "";
	
	public T() {};
	
	public T(String firstName, String lastName, String address, String phone, String destination, String departureDate,
			String ticketClass, String departure) {
		super();
		this.firstn = firstName;
		this.lastn = lastName;
		this.address = address;
		this.phone = phone;
		this.tClass = ticketClass;
		this.departure = departure;
		this.departureDate = departureDate;
		this.destination = destination;
	}
	public String getFirstName() {
		return firstn;
	}
	public void setFirstName(String firstName) {
		this.firstn = firstName;
	}
	public String getLastName() {
		return lastn;
	}
	public void setLastName(String lastName) {
		this.lastn = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTicketClass() {
		return tClass;
	}
	public void setTicketClass(String ticketClass) {
		this.tClass = ticketClass;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}
	
}
