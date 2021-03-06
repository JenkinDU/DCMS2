package dcms.c;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
	public static boolean validDate(String input) {
		String pat = "\\d{8}" ;
        Pattern p = Pattern.compile(pat) ;
        Matcher m = p.matcher(input) ;
        return m.matches();
	}
	
	public static void printF(String server) {
		List<F> flight = FData.getInstance().init(server);
		for(F f:flight) {
			System.out.println(f.getRecordID()+"\t"+f.getDeparture()+"\t"+f.getDestination()+"\t"+f.getDepartureDate()
			+"\t"+f.getBalanceFirstTickets()+"/"+f.getBalanceBusinessTickets()+"/"+f.getBalanceEconomyTickets());
		}
	}
	
	public static byte[] convert(Object ts) {
		byte[] data = null;
		try {
			data = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));
			oos.writeObject(ts);
			oos.flush();
			data = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static Object resolve(byte[] data) {
		Object ts = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
			ts = ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ts;
	}
}
