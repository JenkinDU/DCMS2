package dcms.impl;

import java.util.Properties;

public class DCMSServerM extends Server {
	public static final String SERVER_ = "MTL";
	public static final String NAME = "Montreal";
	public static final String PORT_NUM = "1050";
	public static final int UDP_ = 8020;
	public static final int UDP_T = 9020;
	
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", PORT_NUM);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			(new DCMSServerM()).initServer(args, props, SERVER_, NAME, UDP_, UDP_T);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
