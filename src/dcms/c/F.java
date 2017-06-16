package dcms.c;

public class F {

	public static final String FIRST_C = "First";
	public static final String BUSINESS_C = "Business";
	public static final String ECONOMY_C = "Economy";
	public static final String ALL_C = "All";
	
	public static final String DEPARTURE = "DEPARTURE";
	public static final String DATE = "DATE";
	public static final String DESTINATION = "DESTINATION";
	public static final String F_S = "First";
	public static final String B_S = "Business";
	public static final String E_S = "Economy";
	
	private int recordID = 0;
	
	private String fName = "";

	private String departure = "No ";
	private String departureDate = "No ";
	private String destination = "No ";
	private String achieveDate = "";
	
	private int tBT = 0;
	private int tFT = 0;
	private int tET = 0;
	private int bBT = 0;
	private int bFT = 0;
	private int bET = 0;
	
	public String getFName() {
		return fName;
	}

	public void setFName(String fName) {
		this.fName = fName;
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

	public String getAchieveDate() {
		return achieveDate;
	}

	public void setAchieveDate(String achieveDate) {
		this.achieveDate = achieveDate;
	}

	public int getTotalBusinessTickets() {
		return tBT;
	}

	public synchronized boolean setTotalBusinessTickets(int totalBusinessTickets) {
		int add = totalBusinessTickets - this.tBT;
		if(editBalanceBusinessTickets(add)) {
			this.tBT = totalBusinessTickets;
			return true;
		}
		return false;
	}

	public int getTotalFirstTickets() {
		return tFT;
	}

	public synchronized boolean setTotalFirstTickets(int totalFirstTickets) {
		int add = totalFirstTickets - this.tFT;
		if(editBalanceFirstTickets(add)) {
			this.tFT = totalFirstTickets;
			return true;
		}
		return false;
	}

	public int getTotalEconomyTickets() {
		return tET;
	}

	public synchronized boolean setTotalEconomyTickets(int totalEconomyTickets) {
		int add = totalEconomyTickets - this.tET;
		if(editBalanceEconomyTickets(add)) {
			this.tET = totalEconomyTickets;
			return true;
		}
		return false;
	}
	
	public synchronized boolean sellTi(String type, boolean sell) {
		int add = 0;
		if(sell) {
			add = -1;
		} else {
			add = 1;
		}
		if (BUSINESS_C.equals(type)) {
			return editBalanceBusinessTickets(add);
		} else if (FIRST_C.equals(type)) {
			return editBalanceFirstTickets(add);
		} else if (ECONOMY_C.equals(type)) {
			return editBalanceEconomyTickets(add);
		}
		return false;
	}
	
	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public int getBalanceBusinessTickets() {
		return bBT;
	}

	private synchronized boolean editBalanceBusinessTickets(int v) {
		if(this.bBT + v < 0)
			return false;
		this.bBT += v;
		return true;
	}

	public int getBalanceFirstTickets() {
		return bFT;
	}

	private synchronized boolean editBalanceFirstTickets(int v) {
		if(this.bFT + v < 0)
			return false;
		this.bFT += v;
		return true;
	}

	public int getBalanceEconomyTickets() {
		return bET;
	}

	private synchronized boolean editBalanceEconomyTickets(int v) {
		if(this.bET + v < 0)
			return false;
		this.bET += v;
		return true;
	}
	
}
