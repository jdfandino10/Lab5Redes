package mundo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Connection extends Thread {
	
	private final int PACKET_SIZE;
	public final int MINUTE_LIMIT;
	public static final String DATA = "./data/";
	
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
	private int totPacket;
	private long totBytes;
	private Socket socket;
	private FileChannel channel;

	
	public Connection(ServerSocket sk, int packetSize, int minuteLimit){
		this.sk = sk;
		PACKET_SIZE = packetSize;
		MINUTE_LIMIT = minuteLimit;
	}
	
	public void run() {
		while(true){
			try {
				socket = sk.accept();
				System.out.println("Conexion Establecida");
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				os = socket.getOutputStream();
				while(!socket.isClosed()){
					readMsg();	
				}
			} catch (IOException e) {
				System.out.println("Se ha cerrado la conexión.");
			}
		}
	}
	
	private void readMsg() {
		Duration timeout = Duration.ofMinutes(MINUTE_LIMIT);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> handler = executor.submit(new Callable() {
		    @Override
		    public String call() throws Exception {
		    	String ans = br.readLine();
		    	
		    	while(ans.equals(HEARTBEAT)){
		    		sendOk();
		    		ans = br.readLine();
		    	}
		        return ans;
		    }
		});
		String msg;
		try {
			msg = handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
			processMsg(msg);
		} catch (Exception e) {
			handler.cancel(true);
			try {
				socket.close();
			} catch (IOException e1) {

			}
		}
		executor.shutdownNow();
	}
	
	private void processMsg(String msg) {
		if (HELLO.equals(msg)){
			sendOptions();
		}else if (PAUSE.equals(msg)) {
//			readMsg();
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
	}

	private void sendError(String msg) {
		PrintWriter out = new PrintWriter(os, true);
		out.println(ERROR);
	}
	
	private boolean isFileRequest(String msg) {
		boolean isFile = false;
		File dir = new File(DATA);
		String[] files = dir.list();
		for(int i=0; i<files.length && !isFile; i++){
			isFile = msg.equals(files[i]);
		}
		return isFile;
	}
	
	private void sendOptions() {
		PrintWriter out = new PrintWriter(os, true);
		String options = "";
		File dir = new File(DATA);
		String[] files = dir.list();
		for(int i=0; i<files.length; i++){
			options+=files[i];
			if(i!=files.length-1) options+=";";
		}
		out.println(options);
	}
	
	private void sendPacket(int packetNum) {
		try {
			DataOutputStream out = new DataOutputStream(os);
			int off = packetNum*PACKET_SIZE;
			int len = PACKET_SIZE;
			len = (int) Math.min(len, totBytes-off);
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, off, len);
			ByteBuffer buf = buffer.asReadOnlyBuffer();
			byte[] b = new byte[buf.remaining()];
			buf.get(b);
			os.write(b);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void setFile(String fileName) {
		try {
			File f = new File(DATA+fileName);
			PrintWriter pw = new PrintWriter(os, true);
			channel = new FileInputStream(f).getChannel();
			totBytes = f.length();
			totPacket = (int) (totBytes/PACKET_SIZE);
			if (totBytes%PACKET_SIZE>0) totPacket++;
			pw.println(PACKETS+SEPARATOR+totPacket);
			pw.println(TOTBYTES+SEPARATOR+totBytes);
			pw.println(PACKSIZE+SEPARATOR+PACKET_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


