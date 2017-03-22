package mundo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Connection extends Thread {
	
	public static final int PACKET_SIZE = 8000;
	public static final int MINUTE_LIMIT = 5;
	public static final String DATA = "./data/";
	public static final String FILE_A = "5mb.jpg";
	public static final String FILE_B = "20MB.zip";
	public static final String FILE_C = "50MB.zip";
	private static final String[] FILES = {FILE_A, FILE_B, FILE_C};
	
	public static final String HELLO = "hello";
	public static final String CONTINUE = "continue";
	public static final String PAUSE = "pause";
	public static final String PACKETS = "packets";
	public static final String PACKSIZE = "packsize";
	public static final String TOTBYTES = "totbytes";
	public static final String ERROR = "error";
	public static final String SEPARATOR = ":";

	public static final String HEARTBEAT = "hb";
	public static final String OK = "ok";
	public static final String BYE = "bye";
	
	private ServerSocket sk;
	private BufferedReader br;
	private OutputStream os;
	private byte[] fileBytes;
	private int totPacket;
	private Socket socket;

	
	public Connection(ServerSocket sk){
		this.sk=sk;
	}
	
	public void run() {
		while(true){
			try {
				socket = sk.accept();
				System.out.println("Conexion Establecida");
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				os = socket.getOutputStream();
				readMsg();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readMsg() {
		Duration timeout = Duration.ofMinutes(MINUTE_LIMIT);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> handler = executor.submit(new Callable() {
		    @Override
		    public String call() throws Exception {
		        return br.readLine();
		        
		    }
		});
		String msg;
		try {
			msg = handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
			System.out.println(msg);
			processMsg(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.cancel(true);
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		executor.shutdownNow();
	}
	
	private void processMsg(String msg) {
		if (HELLO.equals(msg)){
			sendOptions();
		}else if (PAUSE.equals(msg)) {
			readMsg();
		}else if (msg.startsWith(CONTINUE)) {
			sendPacket(Integer.parseInt(msg.split(":")[1]));
		}else if (isFileRequest(msg)) {
			setFile(msg);
		}else if (msg.equals(HEARTBEAT)) {
			sendOk();
		}else if (msg.equals(BYE)) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			sendError(msg);
		}
	}
	
	private void sendOk() {
		PrintWriter out = new PrintWriter(os, true);
		out.println(OK);
		System.out.println(OK);
		readMsg();
	}

	private void sendError(String msg) {
		PrintWriter out = new PrintWriter(os, true);
		out.println(ERROR);
		readMsg();
	}
	
	private boolean isFileRequest(String msg) {
		boolean isFile = false;
		for(int i=0; i<FILES.length && !isFile; i++){
			isFile = msg.equals(FILES[i]);
		}
		return isFile;
	}
	
	private void sendOptions() {
		PrintWriter out = new PrintWriter(os, true);
		String options = "";
		for(int i=0; i<FILES.length; i++){
			options+=FILES[i];
			if(i!=FILES.length-1) options+=";";
		}
		out.println(options);
		readMsg();
	}
	
	private void sendPacket(int packetNum) {
		try {
			DataOutputStream out = new DataOutputStream(os);
			int off = packetNum*PACKET_SIZE;
			int len = PACKET_SIZE;
			len = Math.min(len, fileBytes.length-off);
			OutputStream console = new OutputStream() {
				
				@Override
				public void write(int arg0) throws IOException {
					// TODO Auto-generated method stub
					System.out.println(arg0);
				}
			};
			os.write(fileBytes, off, len);
			os.flush();
//			console.write(fileBytes, off, len);
//			console.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readMsg();
		
	}
	
	private void setFile(String fileName) {
		try {
			File f = new File(DATA+fileName);
			PrintWriter pw = new PrintWriter(os, true);
			fileBytes = Files.readAllBytes(f.toPath());
			totPacket = fileBytes.length/PACKET_SIZE;
			if (fileBytes.length%PACKET_SIZE>0) totPacket++;
			pw.println(PACKETS+SEPARATOR+totPacket);
			pw.println(TOTBYTES+SEPARATOR+fileBytes.length);
			pw.println(PACKSIZE+SEPARATOR+PACKET_SIZE);
			System.out.println("Packets: "+ totPacket + " - Bytes: "+fileBytes.length +" - PSize: " + PACKET_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		readMsg();
	}
}


