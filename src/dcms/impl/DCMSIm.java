package dcms.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;

import dcms.DCMSPOA;
import dcms.Result;
import dcms.c.F;
import dcms.c.FData;
import dcms.c.Log;
import dcms.c.T;
import dcms.c.TData;
import dcms.c.Tool;
import dcms.transaction.ITransaction;
import dcms.transaction.TransactionException;
import dcms.transaction.TransferReservation;

public class DCMSIm extends DCMSPOA {
	
	private ORB orb;
	
	private String LOG_PATH = Log.LOG_DIR+"LOG_";
	private String server;
	private String name;
	private int UDP_PORT;
	private int T_UDP_PORT;
	
	protected DCMSIm(String server, String name, int udp, int tudp) {
		super();
		this.server = server;
		this.name = name;
		UDP_PORT = udp;
		T_UDP_PORT = tudp;
		LOG_PATH=LOG_PATH+server+"/"+server+"_LOG.txt";
		Tool.printF(server);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				initSe();
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				initTransServer();
			}
		}).start();
	}
	
	public void setORB(ORB orb_val) {
		orb = orb_val; 
	}
	
	
	
	private String getCfoServers(String recordType, String ip, int port) {
		DatagramSocket aSocket = null;
		String receive = "";
		try {
			aSocket = new DatagramSocket();
			byte[] m = recordType.getBytes();
			InetAddress aHost = InetAddress.getByName(ip);
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, port);
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			receive = new String(reply.getData(), 0, reply.getLength()).trim();
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
		return receive;
	}
	
	@Override
	public synchronized Result book(String firstName, String lastName, String address, String phone, String destination,
			String date, String ticketClass) {
		String s = "["+server+"]-"+"Request Book Flight Order Passenger Info Is\n     -FirstName:"+firstName+"\n"
				+"     -lastName:"+lastName +"\n"
				+"     -address:"+address +"\n"
				+"     -phone:"+phone +"\n"
				+"     -destination:"+destination +"\n"
				+"     -date:"+date +"\n"
				+"     -ticketClass:"+ticketClass;
		System.out.println(s);
		Log.i(LOG_PATH, s);
		Result result = new Result();
		boolean r = false;
		String info = " Success";
		try {
			T t = new T(firstName, lastName, address, phone, destination, date, ticketClass, this.name);
			r = TData.getInstance().sellTi(server, t);
		} catch(TransactionException e) {
			info = " Failed, "+e.getMessage();
		}
		s = "     -"+info;
		System.out.println(s);
		Log.i(LOG_PATH, s);
		result.success = r;
		result.content=info;
		Tool.printF(server);
		return result;
	}
	
	@Override
	public Result editRecord(int recordID, String fieldName, String newValue) {
		ArrayList<F> flight = (ArrayList<F>)FData.getInstance().init(server);
		Result result = new Result();
		boolean find = false;
		boolean r = false;
		String info = "success, ";
		for(F f:flight) {
			if(f.getRecordID() == recordID) {
//				s="["+server+"]-"+"Find recordID:" + f.getRecordID();
//				System.out.println(s);
//				Log.i(LOG_PATH, s);
//				s="     -"+f.toString();
//				System.out.println(s);
//				Log.i(LOG_PATH, s);
				find = true;
				if(F.DEPARTURE.equals(fieldName)) {
					if(newValue!=null&&!newValue.equals(f.getDestination())) {
						f.setDeparture(newValue);
						r = true;
					} else {
						info = " failed";
					}
				} else if(F.DATE.equals(fieldName)) {
					f.setDepartureDate(newValue);
					r = true;
				} else if(F.DESTINATION.equals(fieldName)) {
					if(newValue!=null&&!newValue.equals(f.getDeparture())) {
						f.setDestination(newValue);
						r = true;
					} else {
						info = " failed,  des same with dep.";
					}
				} else if(F.F_S.equals(fieldName)) {
					int old = f.getTotalFirstTickets()-f.getBalanceFirstTickets();
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalFirstTickets(Integer.valueOf(newValue));
						r = true;
					} else {
						info = " failed";
					}
				} else if(F.B_S.equals(fieldName)) {
					int old = f.getTotalBusinessTickets()-f.getBalanceBusinessTickets();
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalBusinessTickets(Integer.valueOf(newValue));
						r = true;
					} else {
						info = " failed";
					}
				} else if(F.E_S.equals(fieldName)) {
					int old = f.getTotalEconomyTickets()-f.getBalanceEconomyTickets();
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalEconomyTickets(Integer.valueOf(newValue));
						r = true;
					} else {
						info = " failed";
					}
				}
				if(r) {
					Tool.printF(this.server);
				}
				break;
			}
		}
		if(!find) {
			F f = new F();
			f.setRecordID(recordID);
			if(F.DEPARTURE.equals(fieldName)) {
				f.setDeparture(newValue);
			} else if(F.DATE.equals(fieldName)) {
				f.setDepartureDate(newValue);
			} else if(F.DESTINATION.equals(fieldName)) {
				f.setDestination(newValue);
			} else if(F.F_S.equals(fieldName)) {
				f.setTotalFirstTickets(Integer.valueOf(newValue));
			} else if(F.B_S.equals(fieldName)) {
				f.setTotalBusinessTickets(Integer.valueOf(newValue));
			} else if(F.E_S.equals(fieldName)) {
				f.setTotalEconomyTickets(Integer.valueOf(newValue));
			}
			FData.getInstance().addNewFlight(server, f);
			r = true;
			info = "add success";
			Tool.printF(this.server);
		}
		result.success = r;
		result.content=info;
		return result;
	}
	private synchronized int gettyCount(String recordType) {
		HashMap<String,List<T>> tickets = TData.getInstance().init(server);
		Iterator iter = tickets.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<T> value = (ArrayList<T>)entry.getValue();
			Iterator<T> i = value.iterator();
			while (i.hasNext()) {
				T f = i.next();
				if (f != null) {
					if(f!=null) {
						if (!recordType.equals(F.ALL_C)) {
							if (recordType.equals(f.getTicketClass())) {
								count++;
							}
						} else {
							count++;
						 }
					}
				}
			}
		}
		return count;
	}
	@Override
	public Result transferReservation(int passengerID, String currentCity, String otherCity) {
		int port = 0;
		if(DCMSServerM.NAME.equals(otherCity)) {
			port = DCMSServerM.UDP_T;
		} else if(DCMSServerLVL.NAME.equals(otherCity)) {
			port = DCMSServerLVL.UDP_T;
		} else if(DCMSServerDDO.NAME.equals(otherCity)) {
			port = DCMSServerDDO.UDP_T;
		}
		Result result = startTransferTransaction(passengerID, otherCity, "localhost", port);
		return result;
	}
	@Override
	public String getCount(String recordType) {
		int count = gettyCount(recordType);
		String value = "";
		if(DCMSServerM.SERVER_.equals(server)) {
			value = server + " " +count+",";
			value +=getCfoServers(recordType, "localhost", DCMSServerLVL.UDP_);
			value +=",";
			value +=getCfoServers(recordType, "localhost", DCMSServerDDO.UDP_);
		} else if(DCMSServerLVL.SERVER_.equals(server)) {
			value =getCfoServers(recordType, "localhost", DCMSServerM.UDP_);
			value += ("," + server + " " +count+",");
			value +=getCfoServers(recordType, "localhost", DCMSServerDDO.UDP_);
		} else if(DCMSServerDDO.SERVER_.equals(server)) {
			value +=getCfoServers(recordType, "localhost", DCMSServerM.UDP_);
			value +=",";
			value +=getCfoServers(recordType, "localhost", DCMSServerLVL.UDP_);
			value += ("," + server + " " +count);
		}
		return value;
	}
	private void initSe() {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(UDP_PORT);
			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String receive = new String(request.getData(), 0, request.getLength()).trim();
				int count = 0;
				if (F.FIRST_C.equals(receive)) {
					count = gettyCount(F.FIRST_C);
				} else if (F.BUSINESS_C.equals(receive)) {
					count = gettyCount(F.BUSINESS_C);
				} else if (F.ECONOMY_C.equals(receive)) {
					count = gettyCount(F.ECONOMY_C);
				} else if (F.ALL_C.equals(receive)) {
					count = gettyCount(F.ALL_C);
				}
				String re = server + " " + count;
				request.setData(re.getBytes());
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	private void initTransServer() {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(T_UDP_PORT);
			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				
				Object b = Tool.resolve(request.getData());
				boolean result = false;
				String re = "";
				if(b != null) {
					T t = (T) b;
					TransferReservation.getInstance().addTransactionOperation(t.getRecordID()+"", new ITransaction() {

						@Override
						public void doCommit() throws TransactionException {
							t.setDeparture(name);
							TData.getInstance().sellTi(server, t);
						}

						@Override
						public void backCommit() {
							if(TData.getInstance().isExistTicket(server, t.getRecordID())) {
								TData.getInstance().returnTi(server, t.getRecordID());
							}
						}
					});
					Result r = TransferReservation.getInstance().doTransaction(t.getRecordID()+"");
					re = t.getRecordID()+":"+(r.success?"TRUE":"FALSE")+":"+r.content;
				} else {
					result = false;
					re = "0:FALSE:Object Error";
				}
				request.setData(re.getBytes());
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	
	private Result startTransferTransaction(int passengerID, String otherCity, String ip, int port) {
		Result result = new Result();
		result.success = false;
		result.content = "ID "+passengerID+" f";
		final T t = TData.getInstance().gettRecord(server, passengerID);
		
		if(t == null) {
			result.success = false;
			result.content = "ID "+passengerID+" Failed! iD";
			return result;
		}
		if(otherCity==null||otherCity.equals(t.getDestination())) {
			result.success = false;
			result.content = "ID "+passengerID+" Failed! dess";
			return result;
		}
		final F f = FData.getInstance().getFByT(server, t);
		boolean r = TransferReservation.getInstance().initTransaction(t.getRecordID()+"", new ITransaction() {

			@Override
			public void doCommit() throws TransactionException {
				TData.getInstance().removeTi(server, t.getRecordID());
			}

			@Override
			public void backCommit() {
				if(!TData.getInstance().isExistTicket(server, t.getRecordID())) {
					TData.getInstance().addTi(server, t);
				}
			}
		});
		if(!r) {
			result.success = false;
			result.content = "ID "+passengerID+" f transfering ";
			return result;
		}
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			byte[] m = Tool.convert(t);
			InetAddress aHost = InetAddress.getByName(ip);
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, port);
			aSocket.send(request);
			
			byte[] buffer = new byte[1000];
			Result v = null;
			while (v==null) {
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.setSoTimeout(2 * 1000);
				aSocket.receive(reply);
			
				String receive = new String(reply.getData(), 0, reply.getLength()).trim();
				if (receive != null) {
					String re[] = receive.split(":");
					if(re.length == 3) {
						boolean s = false;
						if ("TRUE".equals(re[1])) {
							s = true;
						} else {
							s = false;
						}
						TransferReservation.getInstance().pushNetPackage(re[0], new Result(s, re[2]));
					}
				}
				v = TransferReservation.getInstance().popNetPackage(passengerID+"");
				if(v != null) {
					if (v.success) {
						result.success = true;
						result.content = "ID " + passengerID + " Success!";
						f.sellTi(t.getTicketClass(), false);
					} else {
						TransferReservation.getInstance().removeTransaction(passengerID + "");
						result.success = false;
						result.content = "ID " + passengerID + " Failed! " + v.content;
					}
				}
			}
		} catch (Exception e) {
			result.success = !TransferReservation.getInstance().removeTransaction(passengerID + "");
			if (result.success) {
				result.content = "ID " + passengerID + " Success!";
				f.sellTi(t.getTicketClass(), false);
			} else {
				result.content = "ID " + passengerID + " Failed! " + e.getMessage();
			}
		} finally {
			if (aSocket != null)
				aSocket.close();
		}

		return result;
	}
	
	

	@Override
	public String getAllInfo() {
		String result = "";
		ArrayList<F> flight = (ArrayList<F>)FData.getInstance().init(server);
		for(F f:flight) {
			result+=f.toString();
			result+="\n";
		}
		return result;
	}
  
}
