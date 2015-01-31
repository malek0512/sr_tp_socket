/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.printing;

import static jus.aor.printing.Notification.QUERY_PRINT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.security.util.Length;

/**
 * Classe de service fournissant toutes les interactions (read, write) en mode TCP.
 * @author Morat 
 */
class TCP{
	private static final int MAX_LEN_BUFFER = 1024;
	/**
	 * 
	 * @param soc the socket
	 * @param key the JobKey to write
	 * @throws IOException
	 */
	static void writeJobKey(Socket soc, JobKey key) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		OutputStream so = soc.getOutputStream();
		DataOutputStream dout = new DataOutputStream(so);
		
		byte[] b =  key.marshal();
		dout.writeInt(b.length);
		dout.writeLong(key.date);
		dout.write(b);
	}
	/**
	 * 
	 * @param soc the socket
	 * @return the JobKey
	 * @throws IOException
	 */
	static JobKey readJobKey(Socket soc) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		InputStream si = soc.getInputStream();
		DataInputStream din = new DataInputStream(si);
		
		int length = din.readInt();
		long l = din.readLong();
		byte[] b = new byte[length];
		din.read(b, 0, length);
		
		JobKey key = new JobKey(b);
		key.date = l;
		
		return key;
	}
	/**
	 * 
	 * @param soc the socket
	 * @param fis the input stream ti transfert
	 * @param len th len of the input stream
	 * @throws IOException
	 */
	static void writeData(Socket soc, InputStream fis, int len) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		OutputStream so = soc.getOutputStream();
//		BufferedInputStream bin = new BufferedInputStream(fis); 
//		BufferedOutputStream bout = new BufferedOutputStream(so);
		DataOutputStream bout = new DataOutputStream(so);
		byte[] b = new byte[MAX_LEN_BUFFER];

//		bout.writeInt(len);
		System.out.println("Start sending file...");
		int count;
		while ((count = fis.read(b)) > 0) {
			bout.writeInt(count);
			 bout.write(b, 0, count);
			 System.out.println("WRITING : "+new String(b));
		}
		System.out.println("Finished sending file !");
//		bout.flush();
//		bout.close();
//		bin.close();
	}
	/**
	 * 
	 * @param soc the socket
	 * @return string data 
	 * @throws IOException
	 */
	static String readData(Socket soc) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		InputStream si = soc.getInputStream();
		DataInputStream din = new DataInputStream(si);
		byte[] b = new byte[MAX_LEN_BUFFER];
		String s = "";
		System.out.println("Start receving file...");
		int count;
		while ((count = din.readInt()) > 0) {
			din.read(b, 0, count);
			 s.concat(new String(b));
			 System.out.println("READING : "+new String(b));
		}
		System.out.println("Finished receving file !");
		return s;
	}
	/**
	 * 
	 * @param soc the socket
	 * @param not the notification
	 * @throws IOException
	 */
	static void writeProtocole(Socket soc,  Notification not) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		OutputStream so = soc.getOutputStream();
		DataOutputStream dout = new DataOutputStream(so);
		
		dout.writeInt(not.ordinal());
	}
	/**
	 * 
	 * @param soc the socket 
	 * @return the notification
	 * @throws IOException
	 */
	static Notification readProtocole(Socket soc) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		InputStream si = soc.getInputStream();
		DataInputStream din = new DataInputStream(si);
		
		int not = din.readInt();
		return Notification.values()[not];
	}
	/**
	 * 
	 * @param soc the socket
	 * @param jobs the JobState
	 * @throws IOException
	 */
	static void writeJobState(Socket soc,  JobState jobs) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
		OutputStream so = soc.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(so);
		
		oos.writeObject(jobs);
	}
	/**
	 * 
	 * @param soc the socket 
	 * @return the JobState
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static JobState readJobState(Socket soc) throws IOException, ClassNotFoundException {
	//----------------------------------------------------------------------------- A COMPLETER
		InputStream so = soc.getInputStream();
		ObjectInputStream oos = new ObjectInputStream(so);
		
		return (JobState) oos.readObject();
	}
}
