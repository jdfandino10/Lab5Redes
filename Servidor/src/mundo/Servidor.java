package mundo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Servidor {
	
	public Servidor(int conexiones, int bufferSize, int packetSize, int minuteLimit) throws IOException {
		
		ServerSocket sk = new ServerSocket();
		sk.setReceiveBufferSize(bufferSize);
		sk.bind(new InetSocketAddress(8010));		
		for(int i=0; i<conexiones; i++){
			new Connection(sk, packetSize, minuteLimit).start();
		}
		System.out.println("Servidor creado con las siguientes características: ");
		System.out.print("Conexiones: "+conexiones);
		System.out.print(", Buffer: "+bufferSize);
		System.out.print(", Tamaño paquetes: "+packetSize);
		System.out.println(", Tiempo limite (minutos): "+minuteLimit);
		System.out.println("Esperando conexiones.");
	}
}
