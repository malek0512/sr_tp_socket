package jus.aor.printing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;

class Slave extends Thread {
	Socket soc;
	String data = null;
	public Slave(Socket soc)  {
		this.soc = soc;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			data = TCP.readData(soc);
			System.out.println(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

public class Esclave {
	
	static public String newSlave(Socket soc) {
			Slave s = new Slave (soc);
			s.start();
			return s.data;
	}
}




//class Slave extends Thread {
//	Socket soc;
//	java.lang.reflect.Method function;
//	public Slave(Socket soc, String s ) throws IOException, NoSuchMethodException, SecurityException {
//		this.soc = soc;
//		function = TCP.class.getMethod(s, Socket.class);
//	}
//	
//	@Override
//	public void run() {
//		super.run();
//		try {
//			function.invoke(TCP.class);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
//
//public class Esclave {
//	
//	static public void newSlave(Socket soc, String methode) {
//		try {
//			try {
//				new Slave (soc, methode);
//			} catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SecurityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}