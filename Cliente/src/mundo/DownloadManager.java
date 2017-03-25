package mundo;

import java.io.IOException;

public class DownloadManager extends Thread{

	private Cliente c;
	
	public DownloadManager(Cliente c) {
		this.c = c;
	}
	
	public void run() {
		boolean error = false;
		long tInicial = System.currentTimeMillis();
		while (!c.doneDownload()){
			int p = c.getPacketIndexToGet();
			try {
				if(c.isPaused()) {
					synchronized(this){
						this.wait();
					}
				}
				c.requestPacket(p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				error=true;
				System.out.println("Descarga incompleta");
				break;
			}
		}
		if(!error) {
			System.out.println("Tiempo descarga: "+(System.currentTimeMillis()-tInicial));
			try {
				c.writeFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
