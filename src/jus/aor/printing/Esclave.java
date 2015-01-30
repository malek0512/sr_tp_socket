package jus.aor.printing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Esclave {

	
	public class Slave extends Thread {
		Socket soc;
		
		//Server receives bytes from client
		OutputStream so 		;
		InputStream si 			;
		DataOutputStream dout 	;
		DataInputStream din 	;
		public Slave(Socket soc) throws IOException {
			this.soc = soc;
			so = soc.getOutputStream();
			si = soc.getInputStream();
			dout = new DataOutputStream(so);
			din = new DataInputStream(si);
		}
		
		@Override
		public void run() {
			super.run();
			try {
				//Recuperation de la taille du fichier
				int length = din.readInt();
				//Recuperation du fichier
				byte[] b = new byte[length];
				din.read(b, 0, length);
				
				//Impression du fichier sur la fenetre de Log_1
				//log.log(Level.INFO_1, "Server : " + "Data receveived " + new String(b));
				
				System.out.println("Slave : received data "+ new String(b));
				//Notification de de bonne reception
				dout.writeInt(Notification.REPLY_PRINT_OK.ordinal());
				//Reemission du messsage pour verification aupres du client
				dout.writeInt(b.length);
				dout.write(b);
				
			}catch(SocketException e){
				// socket has been closed, master serverTCP will stop.
				try {
					soc.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}catch(ArrayIndexOutOfBoundsException e){
				//TCP.writeProtocole(soc,Notification.REPLY_UNKNOWN_NOTIFICATION);
			}catch(Exception e){
				//TCP.writeProtocole(soc,Notification.REPLY_UNKNOWN_ERROR);
	}
		}
	}
	
	public static void newSlave(Socket soc) throws IOException {
		new Slave (soc);
	}
}
