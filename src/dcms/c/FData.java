package dcms.c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dcms.impl.DCMSServerM;
import dcms.impl.DCMSServerDDO;
import dcms.impl.DCMSServerLVL;

public class FData {
	private static FData instance;
	private HashMap<String, List<F>> data;
	private int recordID = 0;

	private FData() {
		data = new HashMap<String, List<F>>();
	}

	public static synchronized FData getInstance() {
		if (instance == null) {
			instance = new FData();
		}
		return instance;
	}

	public synchronized List<F> init(String name) {
		List<F> o = data.get(name);
		if (o == null) {
			data.put(name, addInitFlight(name));
		}
		return data.get(name);
	}
	
	public synchronized void addNewFlight(String name, F f) {
		List<F> list = data.get(name);
		if(list == null)
			list = new ArrayList<F>();
		if(f.getRecordID() <= 0 || isRecordIdExist(f.getRecordID()))
			f.setRecordID(++recordID);
		list.add(f);
	}
	
	private boolean isRecordIdExist(int id) {
		Iterator iter = this.data.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<F> value = (ArrayList<F>) entry.getValue();
			for (F f : value) {
				if (f != null) {
					if (id == f.getRecordID()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public F getFByT(String server, T t) {
		if(t == null)
			return null;
		ArrayList<F> fList = (ArrayList<F>)FData.getInstance().init(server);
		for(F f:fList) {
			if(f.getDeparture().equals(t.getDeparture())&&f.getDestination().equals(t.getDestination())&&f.getDepartureDate().equals(t.getDepartureDate())) {
				return f;
			}
		}
		return null;
	}
	
	private ArrayList<F> addInitFlight(String name) {
		ArrayList<F> f = new ArrayList<F>();
		
		if(DCMSServerM.SERVER_.equals(name)) {
		} else if(DCMSServerLVL.SERVER_.equals(name)) {
		} else if(DCMSServerDDO.SERVER_.equals(name)) {
		}
		
		return f;
	}
}
