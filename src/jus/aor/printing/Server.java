package jus.aor.printing;

import java.io.IOException;
import java.lang.Thread.State;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

import jus.aor.printing.Esclave.Slave;
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
	protected boolean alive = false;
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
			while(alive) {
				log.log(Level.INFO,"Server.TCP.Waiting");
				try{
					//---------------------------------------------------------------------- A COMPLETER
					//Server waits for a connection
					soc = serverTCPSoc.accept();
					
					//A client connected
					System.out.println("Client" + soc.getInetAddress() + "connected");
					
					//Server receives bytes from client
					
					
				}catch(SocketException e){
						// socket has been closed, master serverTCP will stop.
					soc.close();
					serverTCPSoc.close();
				}catch(ArrayIndexOutOfBoundsException e){
					TCP.writeProtocole(soc,REPLY_UNKNOWN_NOTIFICATION);
				}catch(Exception e){
					TCP.writeProtocole(soc,REPLY_UNKNOWN_ERROR);
				}
			}
			log.log(Level.INFO_1,"Server.TCP.Stopped");
			serverTCPSoc.close();
		}catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	protected void setBacklog(int backlog) {this.backlog=backlog;}
	protected void setport(int port) {this.port=port;}
	protected void setPoolSize(int poolSize) { this.poolSize=poolSize;}
	/**
	 * 
	 * @param args
	 */
	public static void main (String args[]) { 
      new ServerGUI(new Server()); 
	}
}