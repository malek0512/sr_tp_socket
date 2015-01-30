package jus.aor.printing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.State;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

//import jus.aor.printing.Esclave.Slave;
import static jus.aor.printing.Notification.*;
/**
 * Représentation du serveur d'impression.
 * @author Morat
 */
public class Server {
	/** 1 second timeout for waiting replies */
	protected static final int TIMEOUT = 1000;
	protected static final int MAX_REPONSE_LEN = 1024;
	/** la taille de la temporisation */
	protected int backlog =10;
	/** le port de mise en oeuvre du service */
	protected int port=3000;
	/** le nombre d'esclaves maximum du pool */
	protected int poolSize = 10;
	/** le contrôle d'arret du serveur */
	protected boolean alive = true;
	/** le logger du server */
	Logger log = Logger.getLogger("Jus.Aor.Printing.Server","jus.aor.printing.Server");
	private ServerSocket serverTCPSoc;
	/**
	 * Construction du server d'impression
	 */
	public Server() {
		log.setLevel(Level.INFO_1);
	}
	/**
	 * le master thread TCP.
	 */
	private void runTCP(){
		try{
			Socket soc=null;
			serverTCPSoc = new ServerSocket(port, backlog);
			Notification protocole=null;
			log.log(Level.INFO_1,"Server.TCP.Started",new Object[] {port,backlog});
			log.log(Level.INFO_1, "WAZAAAAAAAA");
			while(alive) {
				log.log(Level.INFO_1,"Server.TCP.Waiting");
				try{
					//---------------------------------------------------------------------- A COMPLETER
					//Server waits for a connection
					soc = serverTCPSoc.accept();
					
					//A client connected
					log.log(Level.INFO_1, "Client" + soc.getInetAddress() + "connected");
					
					
					//Server receives bytes from client
					OutputStream so = soc.getOutputStream();
					InputStream si = soc.getInputStream();
					DataOutputStream dout = new DataOutputStream(so);
					DataInputStream din = new DataInputStream(si);
					
					int notif_ordinal = din.readInt();
					Notification notif_received = Notification.values()[notif_ordinal];
					log.log(Level.INFO_1, "Server : notif ordinal" + notif_ordinal);
					log.log(Level.INFO_1, "Server : " + notif_received.toString() + " received !");
					
					switch (notif_received) {
					case QUERY_PRINT :
						
//						//Recuperation de la taille du fichier
//						int length = din.readInt();
//						//Recuperation du fichier
//						byte[] b = new byte[length];
//						din.read(b, 0, length);
//						//Impression du fichier sur la fenetre de Log_1
//						log.log(Level.INFO_1, "Server : " + "Data receveived " + new String(b));
//						//Notification de de bonne reception
//						dout.writeInt(Notification.REPLY_PRINT_OK.ordinal());
//						//Reemission du messsage pour verification aupres du client
//						dout.writeInt(b.length);
//						dout.write(b);
						
						new 
						break;
						
					default : 
						break;
					}
					
				}catch(SocketException e){
						// socket has been closed, master serverTCP will stop.
					soc.close();
					serverTCPSoc.close();
				}catch(ArrayIndexOutOfBoundsException e){
					TCP.writeProtocole(soc,Notification.REPLY_UNKNOWN_NOTIFICATION);
				}catch(Exception e){
					TCP.writeProtocole(soc,Notification.REPLY_UNKNOWN_ERROR);
				}
			}
			log.log(Level.INFO_1,"Server.TCP.Stopped");
			//serverTCPSoc.close();
		}catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	protected void setBacklog(int backlog) {
		System.out.println("baslog" + backlog);
		this.backlog=backlog;}
	protected void setport(int port) {
		System.out.println("port" + port);
		this.port=port;}
	protected void setPoolSize(int poolSize) { 
		System.out.println("NONNNNNNNN" + poolSize);
		this.poolSize=poolSize;}
	/**
	 * 
	 * @param args
	 */
	public static void main (String args[]) {
		Server s = new Server();
		ServerGUI sg = new ServerGUI(s);
		System.out.println(sg.PoolSizeWidget.getText());
		s.runTCP();
	}
}