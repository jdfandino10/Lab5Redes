package mundo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

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
	private long bytes;
	private int packetSize;
	private int actualPacket;
	private DownloadManager dm;
	private boolean downloadStart;
	private boolean paused;
	private FileOutputStream outStream;
	private DownloadListener listener;
	private ConnectionStateListener conListener;
	private ConnectionChecker chk;
	
	public Cliente(DownloadListener listener, ConnectionStateListener conListener) {
		this.listener = listener;
		this.conListener = conListener;
		reset();
		connect();
		chk = new ConnectionChecker(conListener, this);
		chk.start();
	}
	
	public void startDownload() throws Exception{
		if (downloadStart) {
			System.out.println("va a resumir descarga");
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
		downloadStart=false;
		outStream = null;
		paused = false;
	}
	
	public synchronized boolean downloadHasStarted() {
		return downloadStart;
	}
	
	public synchronized boolean connect() {
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
	
	public synchronized boolean goodConnection() {
		
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
		bytes = Long.parseLong(totBytes.split(":")[1]);
		packetSize = Integer.parseInt(packSize.split(":")[1]);
		System.out.println("Packets: "+ packets + " - Bytes: "+bytes +" - PSize: " + packetSize);
		File f = new File(DATA+selectedFile);
		f.createNewFile();
		outStream = new FileOutputStream(f);
	}
	
	
	public void requestPacket(int packetIndex) throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(CONTINUE+":"+packetIndex);
		DataInputStream flujo = new DataInputStream(server.getInputStream());
		int off = packetIndex*packetSize;
		int len = (int) Math.min(packetSize, bytes-off);
		byte[] b = new byte[len];
		flujo.read(b, 0, len);
		outStream.write(b);
		outStream.flush();
		System.out.println("Packete "+packetIndex+" recibido: "+Arrays.toString(b));
		actualPacket++;
	}
	
	public void writeFile() throws IOException {
		outStream.close();
		listener.downloadCompleted();
		System.out.println("Done");
		reset();
	}
	
	public synchronized void pauseDownload() {
		out.println(PAUSE);
		paused = true;
	}
	
	public synchronized boolean isPaused() {
		return paused;
	}
	
	public synchronized void resumeDownload() throws Exception{
		if (!goodConnection() && !connect()) throw new Exception("No se puede conectar al servidor");
		out.println(selectedFile);
		in.readLine();
		in.readLine();
		in.readLine();
		paused = false;
		try{
			synchronized(dm) {
				dm.notify();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception ("No pudo reestablecerse la conexión");
		}
	}
	
	public boolean socketConnection() {
		return server.isClosed();
	}
	
	public void closeConnection(){
		try {
			out.println(BYE);
			chk.interrupt();
			if(dm!=null) dm.interrupt();
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

	public File[] getFiles() {
		File folder = new File(DATA);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		   return listOfFiles;
	}
}
