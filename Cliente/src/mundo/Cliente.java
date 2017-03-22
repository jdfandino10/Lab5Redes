package mundo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
	
	public static final String PAUSE = "pause";
	public static final String DOWNLOADING = "downloading";
	public static final String HEARTBEAT = "hb";
	public static final String OK = "ok";
	public static final String CONTINUE = "continue";
	public static final String BYE = "bye";
	public static final String DATA = "./data/";
	
	private static final String HOST = "localhost";
	private static final int PORT = 8081;
	
	
	private Socket server;
	private PrintWriter out;
	private BufferedReader in;
	private String selectedFile;
	private int packets;
	private int bytes;
	private int packetSize;
	private byte[] file;
	private int actualPacket;
	
	public Cliente() {
		reset();
	}
	
	public void reset() {
		actualPacket=0;
		packets=0;
		bytes=0;
		packetSize=0;
		file=null;
	}
	
	public boolean connect() {
		try{
			server = new Socket(HOST, PORT);
			in = new BufferedReader( new InputStreamReader(server.getInputStream()) );
			out = new PrintWriter(server.getOutputStream(), true);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public int getPacketIndexToGet() {
		return actualPacket;
	}
	
	public boolean goodConnection() {
		
		try {
			out.println(HEARTBEAT);
			char c;
			c = ( char ) in.read();
			if (c==-1) return false;
			else {
				String ans = c+in.readLine();
				if (!ans.equals(OK)){
					server.close();
					return false;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String[] requestFiles() throws Exception {
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		return in.readLine().split(";");
	}
	
	public void selectFile(String fileName) throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(fileName);
		selectedFile = fileName;
		String numPackets = in.readLine();
		String totBytes = in.readLine();
		packets = Integer.parseInt(numPackets.split(":")[1]);
		bytes = Integer.parseInt(totBytes.split(":")[1]);
		packetSize = bytes/packets;
		file = new byte[bytes];
	}
	
	
	public void requestPacket(int packetIndex) throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(CONTINUE+":"+packetIndex);
		DataInputStream flujo = new DataInputStream(server.getInputStream());
		int off = packetIndex*packetSize;
		int len = Math.min(packetSize, bytes-off);
		flujo.read(file, off, len);
		actualPacket++;
	}
	
	public void writeFile() throws IOException {
		FileOutputStream outStream = new FileOutputStream(DATA+selectedFile);
		outStream.write(bytes);
		outStream.close();
		reset();
	}
	
	public void pauseDownload() {
		out.println(PAUSE);
	}
	
	public void resumeDownload() throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(selectedFile);
		String numPackets = in.readLine();
		String totBytes = in.readLine();
	}
	
	public void closeConnection(){
		try {
			out.println(BYE);
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean doneDownload() {
		if(packets == 0) return false;
		return packets==actualPacket;
	}
}
