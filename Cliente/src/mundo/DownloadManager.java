package mundo;

import java.io.IOException;

public class DownloadManager extends Thread{

	private Cliente c;
	
	public DownloadManager(Cliente c) {
		this.c = c;
	}
	
	public void run() {
		boolean error = false;
		while (!c.doneDownload()){
			int p = c.getPacketIndexToGet();
			try {
				c.requestPacket(p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				error=true;
				e.printStackTrace();
			}
		}
		if(!error) {
			try {
				c.writeFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
