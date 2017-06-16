package dcms.impl;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import dcms.DCMS;
import dcms.DCMSHelper;
import dcms.c.FData;
import dcms.c.Log;
import dcms.c.TData;

public class Server {

	public static void main(String[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DCMSServerM.main(null);
			}
		}).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				DCMSServerLVL.main(null);
			}
		}).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				DCMSServerDDO.main(null);
			}
		}).start();
	}
	public void initServer(String args[], Properties props, String server, String name, int udp, int tudp) {
		try {
			String s = "[" + server + "]-" + "Server up  waiting ...";
			System.out.println(s);
			FData.getInstance().init(server);
			TData.getInstance().init(server);
			ORB orb = ORB.init(args, props);

			POA rootpoa = (POA) orb.resolve_initial_references("RootPOA");
			rootpoa.the_POAManager().activate();

			DCMSIm helloImpl = new DCMSIm(server, name, udp, tudp);
			helloImpl.setORB(orb);

			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
			DCMS href = DCMSHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String n = "DCMS";
			NameComponent path[] = ncRef.to_name(n);
			ncRef.rebind(path, href);

			Log.createLogDir(Log.LOG_DIR + "LOG_" + server + "/");

			Log.i(Log.LOG_DIR + "LOG_" + server + "/" + server + "_LOG.txt", s);

			orb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
		System.out.println(" Exiting ...");
	}
}
