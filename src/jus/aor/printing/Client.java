package jus.aor.printing;

import java.net.*;
import java.util.logging.Logger;
import java.io.*;

import sun.security.util.Length;

import jus.aor.printing.Client;
import jus.aor.printing.JobKey;
import jus.aor.printing.JobState;
import jus.aor.printing.Level;
import jus.aor.printing.Notification;
import jus.aor.printing.Paire;
import jus.aor.printing.ServerStatus;
import jus.aor.printing.TCP;
import jus.aor.printing.UDP;

import static jus.aor.printing.Notification.*;
/**
 * Représentation du Client du serveur d'impression.
 * @author Morat
 */
public class Client {
	/** 1 second timeout for waiting replies */
	protected static final int TIMEOUT = 1000;
	/** la machine supportant le serveur d'impression */
	private String host = "localhost";
	/** le port d'installation du serveur d'impression */
	private int port=3000;
	/** le listener UDP est-il vivant */
	private boolean alive=true;
	/** le logger du client */
	private Logger log = Logger.getLogger("Jus.Aor.Printing.Client","jus.aor.printing.Client");
	/** l'interfaçage avec la console du client */
	private ClientGUI GUI;
	/**
	 * construction d'un client
	 */
	public Client() {
		GUI = new ClientGUI(this);
		log.setLevel(Level.INFO_2);
	}
	/**
	 * Choix d'une imprimante distante en indiquant l'@ du servive d'impression associé :
	 * se connecte à ce serveur d'impression, dès lors on peut envoyer des requêtes à celui-ci.
	 */
	public void selectPrinter() {
		// constuction du listener de spooler
		//----------------------------------------------------------------------------- A COMPLETER
		
	}
	/**
	 * Se déconnecte d'un serveur d'impression, on ne peut plus envoyer de requête au serveur.
	 */
	public void deselectPrinter() {
		// arrêt du listener de spooler
		//----------------------------------------------------------------------------- A COMPLETER
	}
	/**
	 * L'écoute des communications du spooler
	 */
	protected void runUDP(){
		//----------------------------------------------------------------------------- A COMPLETER
	}
	/**
	 * @param key la cle du du job
	 * @throws IOException 
	 */
	private void jobTerminated(JobKey key) throws IOException{GUI.removePrintList(key);}
	/**
	 * Fixe le remote host
	 * @param host le remote host supportant le serveur d'impression
	 */
	public void setHost(String host) {this.host=host;}
	/**
	 * Fixe le port du serveur d'impression
	 * @param port le port supportant le serveur d'impression
	 */
	public void setPort(int port) {this.port=port;}
	/**
	 * protocole du print d'un fichier
	 * @param f le fichier à imprimer
	 */
	private void onePrint(File f){
		Socket soc = null;
		try(InputStream fis = new FileInputStream(f)){
			//-------------------------------------------------------------------------- A COMPLETER
			Notification ret = null;
			soc = new Socket(host, port);
			
			//Client connected
			log.log(Level.INFO_1, "Connected to "+ soc.getInetAddress());
			
			//Client sends bytes to server
			OutputStream so = soc.getOutputStream();
			InputStream si = soc.getInputStream();
			DataOutputStream dout = new DataOutputStream(so);
			DataInputStream din = new DataInputStream(si);
			
			JobKey key = new JobKey(30012015);
			byte[] b =  key.marshal(), b_bis = null;
			dout.writeInt(QUERY_PRINT.ordinal());
			dout.writeInt(b.length);
			dout.write(b);
			log.log(Level.INFO_1, b.toString());
			//Wait for the server reply
			int notif_reply = din.readInt();
			ret = Notification.values()[notif_reply];
			log.log(Level.INFO_1, ret.toString());
			if(ret == Notification.REPLY_PRINT_OK) {
				//On récupère le fichier envoyé prececdement afin de verifier son intégrité. Et on l'affiche dans le fenetre
				log.log(Level.INFO_1,"Client : Verification du message ");
				int length = din.readInt();
				b_bis = new byte[length];
				din.read(b_bis, 0, length);
//				log.log(Level.INFO_1,"Client : byte[] = "+b_bis.toString());
//				System.out.println("Client : byte[] = "+b_bis.toString());
				
				if (! b.equals(b_bis))
					log.log(Level.INFO_1,"Client : WARNING Message de verification erroné " + b.toString()+ " != " + b_bis.toString());
				else 
					log.log(Level.INFO_1,"Client : WARNING Message de verification OK " + b.toString()+ " == " + b_bis.toString());
				// Dans le cas où tout est correct on ajoute le job à la liste des encours.
				log.log(Level.INFO_3,"Client.QueryPrint.Processing",key);
				GUI.addPrintList(key);
			} else {
				log.log(Level.WARNING,"Client.QueryPrint.Failed",ret.toString());
				log.log(Level.INFO_1,"Client.QueryPrint.Failed",ret.toString());
			}
		}catch(NumberFormatException e){
			log.log(Level.SEVERE,"Client.QueryPrint.Port.Error",e.getMessage());
		}catch(UnknownHostException e){
			log.log(Level.SEVERE,"Client.QueryPrint.Remote.Error",e.getMessage());
		}catch(IOException e){
			log.log(Level.SEVERE,"Client.QueryPrint.IO.Error",e.getMessage()); 
		}
	}
	/**
	 * Réalise l'émission quasi-simultanée de n requêtes d'impression à l'aide de onePrint
	 * @param f le fichier à imprimer
	 * @param n nombre de requêtes d'impression à faire
	 */
	public void queryPrint(final File f,int n) {
		//-------------------------------------------------------------------------- A COMPLETER
		for (int i=0; i<n; i++)
			onePrint(f);
	}
	/**
	 * protocole du server status
	 */
	public void queryStatus(){		
		//-------------------------------------------------------------------------- A COMPLETER
	}
	/**
	 * protocole de délai d'impression
	 * @param key la clé du job
	 */
	public void queryJobs(JobKey key){
		//-------------------------------------------------------------------------- A COMPLETER	
	}
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		Client client = new Client();
		Client client2 = new Client();
//		System.out.println(Client.class.getResource("Client.properties").getPath());
		String path = Client.class.getResource("Client.properties").getPath();
		client.onePrint(new File(path));
		client.queryPrint(new File(path), 10);
		
		client2.onePrint(new File(path));
	}
}
