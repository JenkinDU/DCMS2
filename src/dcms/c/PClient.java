package dcms.c;

import java.util.Properties;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import dcms.DCMS;
import dcms.DCMSHelper;
import dcms.Result;
import dcms.impl.DCMSServerM;
import dcms.impl.DCMSServerDDO;
import dcms.impl.DCMSServerLVL;

public class PClient {
	private DCMS dcmsImpl;
	private T t;
	
	
	
	
	private boolean manageInput(int userChoice, Scanner keyboard) {
		boolean success = false;
		switch (userChoice) {
		case 1:
			success = initConnection(DCMSServerM.PORT_NUM);
			break;
		case 2:
			success = initConnection(DCMSServerLVL.PORT_);
			break;
		case 3:
			success = initConnection(DCMSServerDDO.PORT_);
			break;
		case 4:
			keyboard.close();
			System.exit(0);
		default:
			System.out.println("Invalid Input, please.");
		}
		return success;
	}
	public static void showDesMenu(int departure) {
		System.out.println("\nPlease select");
		if(departure == 1) {
			System.out.println("1. ");
			System.out.println("2. i");
		} else if(departure == 2) {
			System.out.println("1. ");
			System.out.println("2. ");
		} else if(departure == 3) {
			System.out.println("1. ");
			System.out.println("2. ");
		}
		System.out.println("3. Back");
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
				System.out.println("Invalid Input, please enter max "+max+")\n");
				valid = false;
				keyboard.nextLine();
			}
		}
		return userChoice;
	}
	private boolean manageDes(int userChoice, int departure) {
		boolean success = false;
		t = new T();
		if(departure == 1) {
			t.setDeparture("mtl");
		} else if(departure == 2) {
			t.setDeparture("ws");
		} else if(departure == 3) {
			t.setDeparture("nd");
		}
		switch (userChoice) {
		case 1:
			if(departure == 1) {
				t.setDestination("ws");
			} else if(departure == 2) {
				t.setDestination("mtl");
			} else if(departure == 3) {
				t.setDestination("mtl");
			}
			success = true;
			break;
		case 2:
			if(departure == 1) {
				t.setDestination("nd");
			} else if(departure == 2) {
				t.setDestination("nd");
			} else if(departure == 3) {
				t.setDestination("ws");
			}
			success = true;
			break;
		case 3:
			success = false;
			break;
		default:
			System.out.println("Invalid Input, .");
		}
		
		return success;
	}
	public static void showMenu() {
		System.out.println("\n****Welcome to DCMS System****\n");
		System.out.println("1. mtl");
		System.out.println("2. lvl");
		System.out.println("3. ddo");
		System.out.println("4. Exit");
	}
	private void initPage() {
		int userChoice = 0;
		int step = 1;
		int max = 0;
		int departure = 0;
		Scanner keyboard = new Scanner(System.in);

		while (true) {
			if(step == 1) {
				max = 4;
				showMenu();
			} else if(step == 2) {
				max = 3;
				showDesMenu(userChoice);
			} else if(step == 3) {
				firstName(keyboard);
				step++;
			} else if(step == 4) {
				lastName(keyboard);
				step++;
			} else if(step == 5) {
				iaddress(keyboard);
				step++;
			} else if(step == 6) {
				iphone(keyboard);
				step++;
			} else if(step == 7) {
				chooseClass(keyboard);
				step++;
			} else if(step == 8) {
				departureD(keyboard);
				step++;
				try {
					Result success = dcmsImpl.book(t.getFirstName(), t.getLastName(), t.getAddress(),
							t.getPhone(), t.getDestination(), t.getDepartureDate(), t.getTicketClass());
					System.out.println(success.content);
				} catch (Exception e) {
					e.printStackTrace();
				}
				step = 1;
				continue;
			}
			
			Boolean valid = false;

			// Enforces a valid integer input.
			while (!valid && step <=2) {
				try {
					userChoice = keyboard.nextInt();
					if(userChoice >=1 && userChoice <=max)
						valid = true;
					else {
						throw new Exception();
					}
				} catch (Exception e) {
					System.out.println("Invalid Input, please max"+max+")");
					valid = false;
					keyboard.nextLine();
				}
			}

			if(step == 1) {
				departure = userChoice;
				if(manageInput(userChoice, keyboard))
					step++;
			} else if(step == 2) {
				if (manageDes(userChoice, departure))
					step++;
				else
					step--;
			}
		}
	}
	public void firstName(Scanner keyboard) {
		System.out.println("\nPlease :\n");
		t.setFirstName(keyboard.next());
	}
	
	public void lastName(Scanner keyboard) {
		System.out.println("\nPlease :\n");
		t.setLastName(keyboard.next());
	}
	
	public void iaddress(Scanner keyboard) {
		System.out.println("\nPlease :\n");
		t.setAddress(keyboard.next());
	}
	
	public void iphone(Scanner keyboard) {
		System.out.println("\nPlease :\n");
		t.setPhone(keyboard.next());
	}
	
	public void chooseClass(Scanner keyboard) {
		System.out.println("\nPlease \n");
		System.out.println("1. \n2. \3.");
		int choose = validInput(keyboard, 3);
		if(choose == 1) {
			t.setTicketClass(F.FIRST_C);
		} else if(choose == 2) {
			t.setTicketClass(F.BUSINESS_C);
		} else if(choose == 3) {
			t.setTicketClass(F.ECONOMY_C);
		}
	}
	
	public void departureD(Scanner keyboard) {
		System.out.println("\nPlease input your Date:\n");
		boolean valid = false;
		String input = "";
		while (!valid) {
			try {
				input = keyboard.next();
				if(Tool.validDate(input))
					valid = true;
				else {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("Invalid Input, please \n");
				valid = false;
				keyboard.nextLine();
			}
		}
		t.setDepartureDate(input);
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
			String name = "DFRS";
			dcmsImpl = DCMSHelper.narrow(ncRef.resolve_str(name));
			
			System.out.println("Lookup completed and Connect Successful");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	

	public static void main(String[] args) {
		new PClient().initPage();
	}
}
