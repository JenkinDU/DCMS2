package dcms.impl;

import java.util.Properties;

public class DCMSServerDDO extends Server {
	public static final String SERVER_ = "DDO";
	public static final String NAME = "DDO";
	public static final String PORT_ = "1052";
	public static final int UDP_ = 8022;
	public static final int UDP_T = 9022;
	
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", PORT_);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			(new DCMSServerDDO()).initServer(args, props, SERVER_, NAME, UDP_, UDP_T);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
