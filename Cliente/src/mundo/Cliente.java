package mundo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.w3c.dom.DOMConfiguration;

public class Cliente {
	
	public static final String HELLO = "hello";
	public static final String PAUSE = "pause";
	public static final String DOWNLOADING = "downloading";
	public static final String HEARTBEAT = "hb";
	public static final String OK = "ok";
	public static final String CONTINUE = "continue";
	public static final String PACKSIZE = "packsize";
	public static final String BYE = "bye";
	public static final String DATA = "./data/";
	
	private static final String HOST = "localhost";
	private static final int PORT = 8010;
	
	
	private Socket server;
	private PrintWriter out;
	private BufferedReader in;
	private String selectedFile;
	private int packets;
	private int bytes;
	private int packetSize;
	private byte[] file;
	private int actualPacket;
	private DownloadManager dm;
	private boolean downloadStart;
	
	public Cliente() {
		reset();
		connect();
	}
	
	public void startDownload() throws Exception{
		if (downloadStart) {
			resumeDownload();
		}else {
			dm = new DownloadManager(this);
			dm.start();
			downloadStart = true;
		}
	}
	
	public void reset() {
		actualPacket=0;
		packets=0;
		bytes=0;
		packetSize=0;
		file=null;
		downloadStart=false;
	}
	
	public boolean connect() {
		try{
			server = new Socket(HOST, PORT);
			in = new BufferedReader( new InputStreamReader(server.getInputStream()) );
			out = new PrintWriter(server.getOutputStream(), true);
			return true;
		}catch(Exception e){
			e.printStackTrace();
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
		out.println(HELLO);
		return in.readLine().split(";");
	}
	
	public void selectFile(String fileName) throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(fileName);
		selectedFile = fileName;
		String numPackets = in.readLine();
		String totBytes = in.readLine();
		String packSize = in.readLine();
		packets = Integer.parseInt(numPackets.split(":")[1]);
		bytes = Integer.parseInt(totBytes.split(":")[1]);
		packetSize = Integer.parseInt(packSize.split(":")[1]);
		file = new byte[bytes];
		System.out.println("Packets: "+ packets + " - Bytes: "+bytes +" - PSize: " + packetSize);
		
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
		File f = new File(DATA+selectedFile);
		f.createNewFile();
		FileOutputStream outStream = new FileOutputStream(f);
		outStream.write(bytes);
		outStream.close();
		reset();
	}
	
	public void pauseDownload() {
		out.println(PAUSE);
		try {
			dm.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resumeDownload() throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(selectedFile);
		String numPackets = in.readLine();
		String totBytes = in.readLine();
		dm.notify();
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
