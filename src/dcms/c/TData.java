package dcms.c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dcms.impl.DCMSServerM;
import dcms.impl.DCMSServerDDO;
import dcms.impl.DCMSServerLVL;
import dcms.transaction.TransactionException;

public class TData {
	private static TData instance;
	private HashMap<String, HashMap<String, List<T>>> data;
	private int recordID = 0;
	
	private TData() {
		data = new HashMap<String, HashMap<String, List<T>>>();
	}

	public static synchronized TData getInstance() {
		if (instance == null) {
			instance = new TData();
		}
		return instance;
	}

	public synchronized HashMap<String, List<T>> init(String name) {
		HashMap<String, List<T>> o = data.get(name);
		if (o == null) {
			o = new HashMap<String, List<T>>();
			data.put(name, o);
		}
		return data.get(name);
	}
	
	public synchronized void addTi(String server, T t) {
		String index = Character.toUpperCase(t.getLastName().charAt(0)) + "" ;
		HashMap<String, List<T>> o = data.get(server);
		ArrayList<T> list = (ArrayList<T>) o.get(index);
		if(list == null)
			list = new ArrayList<T>();
		if(t.getRecordID() <= 0)
			t.setRecordID(++recordID);
		list.add(t);
		o.put(index, list);
	}

	public synchronized boolean sellTi(String server, T t) throws TransactionException {
		boolean result = false;
		if(server == null || t == null)
			return false;
		ArrayList<F> flight = (ArrayList<F>)FData.getInstance().init(server);
		boolean r = false;
		F book = null;
		for(F f:flight) {
			if(f.getDeparture().equals(t.getDeparture())&&f.getDestination().equals(t.getDestination())&&f.getDepartureDate().equals(t.getDepartureDate())) {
				book = f;
				r = true;
				break;
			}
		}
		if(r) {
			if(book!=null&book.sellTi(t.getTicketClass(), true)) {
				addTi(server, t);
				result = true;
			} else {
				result = false;
				throw new TransactionException("n Enough s");
			}
		} else {
			result = false;
			throw new TransactionException("n Flightt");
		}
		return result;
	}
	
	public synchronized void removeTi(String server, int id) {
		T t = gettRecord(server, id);
		if(server == null || t == null)
			return;
		
		String index = Character.toUpperCase(t.getLastName().charAt(0)) + "" ;
		HashMap<String, List<T>> o = data.get(server);
		ArrayList<T> list = (ArrayList<T>) o.get(index);
		
		list.remove(t);
	}
	
	public synchronized void returnTi(String server, int id) {
		T t = gettRecord(server, id);
		if(server == null || t == null)
			return;
		
		String index = Character.toUpperCase(t.getLastName().charAt(0)) + "" ;
		HashMap<String, List<T>> o = data.get(server);
		ArrayList<T> list = (ArrayList<T>) o.get(index);
		
		ArrayList<F> flight = (ArrayList<F>)FData.getInstance().init(server);
		boolean r = false;
		F book = null;
		for(F f:flight) {
			if(f.getDeparture().equals(t.getDeparture())&&f.getDestination().equals(t.getDestination())&&f.getDepartureDate().equals(t.getDepartureDate())) {
				book = f;
				r = true;
				break;
			}
		}
		if(r) {
			if(book!=null&book.sellTi(t.getTicketClass(), false)) {
				list.remove(t);
			}
		}
	}
	
	public boolean isExistTicket(String server, int id) {
		T t = gettRecord(server, id);
		if(t == null)
			return false;
		return true;
	}
	
	public synchronized T gettRecord(String server, int id) {
		HashMap<String, List<T>> o = data.get(server);
		Iterator iter = o.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<T> value = (ArrayList<T>) entry.getValue();
			
			Iterator<T> i = value.iterator();
			while (i.hasNext()) {
				T f = i.next();
				if (f != null) {
					if (id == f.getRecordID()) {
						return f;
					}
				}
			}
		}
		return null;
	}
}
