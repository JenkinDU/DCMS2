package dcms.impl;

import java.util.Properties;

public class DCMSServerLVL extends Server {
	public static final String SERVER_ = "LVL";
	public static final String NAME = "lvl";
	public static final String PORT_ = "1051";
	public static final int UDP_ = 8021;
	public static final int UDP_T = 9021;
	
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", PORT_);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			(new DCMSServerLVL()).initServer(args, props, SERVER_, NAME, UDP_, UDP_T);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
