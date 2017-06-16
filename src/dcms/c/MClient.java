package dcms.c;

import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import dcms.DCMS;
import dcms.DCMSHelper;
import dcms.Result;
import dcms.impl.DCMSServerM;
import dcms.impl.DCMSServerDDO;
import dcms.impl.DCMSServerLVL;

public class MClient {
	private static final String LOG_PATH = Log.LOG_DIR+"LOG_Manager"+"/";
	private static final String[] CITY = {"Montreal", "LVL", "DDO"};
	private static final String[] S_CITY = {"MTL", "LVL", "DDO"};
	private String managerName = "default";
	private String server = "";
	private String serverName = "";
	private DCMS dcmsImpl;
	
	private void showBMenu() {
		String m = "Please select the record type (1-4)";
		String m1 = "1. ";
		String m2 = "2. ";
		String m3 = "3. ";
		String m4 = "4. ";
		System.out.println(m);
		String s = "-"+managerName + " Choose ";
		
		Scanner keyboard = new Scanner(System.in);
		int userChoice = validInput(keyboard, 4);
		String type = "";
		switch (userChoice) {
		case 1:
			s+=m1;
			Log.i(LOG_PATH+managerName+".txt", s);
			type = F.FIRST_C;
			break;
		case 2:
			s+=m2;
			type = F.BUSINESS_C;
			break;
		case 3:
			s+=m3;
			type = F.ECONOMY_C;
			break;
		case 4:
			s+=m4;
			type = F.ALL_C;
			break;
		default:
			System.out.println("Invalid Input,  try again.");
		}
		try {
			String value = dcmsImpl.getCount(type);
			s = "-Get Count:" + value;
			Log.i(LOG_PATH+managerName+".txt", s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void showMenu() {
		System.out.println("\n****Welcome to DCMS Manage System****\nPlease enter:");
	}
	
	private int validInput(Scanner keyboard, int max) {
		int userChoice = 0;
		boolean valid = false;

		while (!valid) {
			try {
				userChoice = keyboard.nextInt();
				if(userChoice >=1 && userChoice <=max)
					valid = true;
				else {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("Invalid Input, please (1 - "+max+")\n");
				valid = false;
				keyboard.nextLine();
			}
		}
		return userChoice;
	}
	
	
	
	private int showCityMenu(Scanner keyboard) {
		System.out.println("Please choose");
		System.out.println("1. ");
		System.out.println("2. ");
		System.out.println("3. ");
		
		return validInput(keyboard, 3);
	}
	
	private void showeditMenu(int recordId) {
		String m[] = {"Please select the field name (1-6)",
				"1.  place",
				"2.  date",
				"3. Destination ",
				"4. First  ",
				"5. Business  ",
				"6. Economy  "};
		System.out.println(m[0]);
		System.out.println(m[1]);
		System.out.println(m[2]);
		System.out.println(m[3]);
		System.out.println(m[4]);
		System.out.println(m[5]);
		System.out.println(m[6]);
		String s = "-"+managerName + " Input record Is: "+recordId;
		Log.i(LOG_PATH+managerName+".txt", s);
		
		Scanner keyboard = new Scanner(System.in);
		int userChoice = validInput(keyboard, 6);
		
		s = "-"+managerName + " Choose " + m[userChoice];
		Log.i(LOG_PATH+managerName+".txt", s);
		
		int seats = -1;
		String fieldName = "";
		String value = "";
		if(userChoice == 1 || userChoice == 3) {
			int city = showCityMenu(keyboard);
			if(city == 1) {
				value = "mtl";
			} else if(city == 2) {
				value = "ddo";
			} else if(city == 3) {
				value = "llv";
			}
			s = "-"+managerName + " Choose " + value;
			Log.i(LOG_PATH+managerName+".txt", s);
		} else {
			System.out.println("Please enter new field value");
			if(userChoice > 3) {
				while (seats < 0) {
					try {
						seats = keyboard.nextInt();
					} catch (Exception e) {
						System.out.println("Invalid Input, please enter ");
						seats = -1;
						keyboard.nextLine();
					}
				}
				value = seats + "";
			} else if(userChoice == 2) {
				boolean valid = false;
				// Enforces a valid integer input.
				while (!valid) {
					try {
						value = keyboard.next();
						if(Tool.validDate(value))
							valid = true;
						else {
							throw new Exception();
						}
					} catch (Exception e) {
						System.out.println("Invalid Input, please enter Date like \n");
						valid = false;
						keyboard.nextLine();
					}
				}
			} else {
				value = keyboard.next();
			}
			s = "-"+managerName + " Enter New Value: " + value;
			Log.i(LOG_PATH+managerName+".txt", s);
		}
		try {
			switch (userChoice) {
			case 1:
				fieldName = F.DEPARTURE;
				break;
			case 2:
				fieldName = F.DATE;
				break;
			case 3:
				fieldName = F.DESTINATION;
				break;
			case 4:
				fieldName = F.F_S;
				break;
			case 5:
				fieldName = F.B_S;
				break;
			case 6:
				fieldName = F.E_S;
				break;
			default:
				System.out.println("Invalid Input, please try again.");
			}
			Result result = dcmsImpl.editRecord(recordId, fieldName, value);
			s = "-"+managerName + " " + result.content;
			Log.i(LOG_PATH+managerName+".txt", s);
			System.out.println(result.content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showeeeditMenu() {
		System.out.println("Please enter the record ID:");
		Scanner keyboard = new Scanner(System.in);
		boolean valid = false;
		int userInput = 0;
		
		while (!valid) {
			try {
				userInput = keyboard.nextInt();
				valid=true;
			} catch (Exception e) {
				System.out.println("Invalid Input, please enter the record ID:");
				valid = false;
				keyboard.nextLine();
			}
		}
		showeditMenu(userInput);
	}
	private void showTransferMenu() {
		System.out.println("Please enter the passenger ID:");
		Scanner keyboard = new Scanner(System.in);
		boolean valid = false;
		int userInput = 0;
		T t = null;
		while (!valid) {
			try {
				userInput = keyboard.nextInt();
				valid = true;
			} catch (Exception e) {
				System.out.println("Invalid Input, please enter the :");
				valid = false;
				keyboard.nextLine();
			}
		}
		String otherCity = showTransferMenu(t);
		Result result = dcmsImpl.transferReservation(userInput, serverName, otherCity);
		String s = "-"+managerName + " " + result.content;
		Log.i(LOG_PATH+managerName+".txt", s);
		System.out.println(result.content);
	}
	
	private void showOpMenu(Scanner keyboard) {
		String m = "Please select your option (1-3)";
		String m1 = "1. Get  Count";
		String m2 = "2. Edit  Record";
		String m3 = "3. Transfer ";
		String m4 = "4. Exit";
		System.out.println(m);
		System.out.println(m1);
		System.out.println(m2);
		System.out.println(m3);
		System.out.println(m4);
		String s = "-"+managerName + " Choose ";
		int userChoice = validInput(keyboard, 4);
		switch (userChoice) {
		case 1:
			s+=m1;
			Log.i(LOG_PATH+managerName+".txt", s);
			showBMenu();
			break;
		case 2:
			s+=m2;
			Log.i(LOG_PATH+managerName+".txt", s);
			showeeeditMenu();
			break;
		case 3:
			s+=m3;
			Log.i(LOG_PATH+managerName+".txt", s);
			showTransferMenu();
			break;
		case 4:
			s+=m4;
			Log.i(LOG_PATH+managerName+".txt", s);
			System.out.println("Have a nice day!");
			keyboard.close();
			System.exit(0);
		default:
			System.out.println("Invalid Input, please try again.");
		}
		initPage();
	}
	
	private boolean initConnection(String port) {
		try {
			// create and initialize the ORB
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", port);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			ORB orb = ORB.init(new String[]{}, props);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext,
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "DCMS";
			dcmsImpl = DCMSHelper.narrow(ncRef.resolve_str(name));
			String s = "\nNew Manager:"+managerName+"\n-Connect Server Successful";
			System.out.println(s);
			Log.i(LOG_PATH+managerName+".txt", s);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	private boolean validManagerId(String input) {
		String pat = "(MTL|WST|NDL)\\d{4}" ;
        Pattern p = Pattern.compile(pat) ;
        Matcher m = p.matcher(input) ;
        return m.matches();
	}
	
	private String getServerPort(String input) {
		String pat = "(MTL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return DCMSServerM.PORT_NUM;
        }
        pat = "(LVL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return DCMSServerLVL.PORT_;
        }
		pat = "(DDO)\\d{4}" ;
		if(Pattern.compile(pat).matcher(input).matches()) {
        	return DCMSServerDDO.PORT_;
        }
		return "";
	}
	
	private String getServer(String input) {
		String pat = "(MTL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return S_CITY[0];
        }
        pat = "(LVL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return S_CITY[1];
        }
		pat = "(DDO)\\d{4}" ;
		if(Pattern.compile(pat).matcher(input).matches()) {
        	return S_CITY[2];
        }
		return "";
	}
	
	private String getServerName(String input) {
		String pat = "(MTL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return CITY[0];
        }
        pat = "(LVL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return CITY[1];
        }
		pat = "(DDO)\\d{4}" ;
		if(Pattern.compile(pat).matcher(input).matches()) {
        	return CITY[2];
        }
		return "";
	}
	
	private void initPage() {
		String userInput = "";
		Scanner keyboard = new Scanner(System.in);
		boolean valid = false;
		
		showMenu();

		// Enforces a valid integer input.
		while (!valid) {
			try {
				userInput = keyboard.next();
				valid = validManagerId(userInput);
				if (!valid) {
					System.out.println("Invalid InputerID");
				}
			} catch (Exception e) {
				System.out.println("Invalid Input ID");
				valid = false;
				keyboard.nextLine();
			}
		}
		managerName = userInput;
		server = getServer(userInput);
		serverName = getServerName(userInput);
		if (initConnection(getServerPort(userInput))) {
			showOpMenu(keyboard);
		} else {
			initPage();
		}
	}
	private String showTransferMenu(T t) {
		System.out.println("Please choose the for ");
		int j=1;
		HashMap<String, String> map = new HashMap<String, String>();
		for(int i=0;i<CITY.length;i++) {
			
			if(!CITY[i].equals(serverName)) {
				System.out.println(j+"."+CITY[i]);
				map.put(j+"", CITY[i]);
				j++;
			}
		}
		Scanner keyboard = new Scanner(System.in);
		int input = validInput(keyboard, j-1);
		return map.get(input+"");
	}
	
	
	
	public static void main(String[] args) {
		Log.createLogDir(LOG_PATH);
		new MClient().initPage();
	}
}
