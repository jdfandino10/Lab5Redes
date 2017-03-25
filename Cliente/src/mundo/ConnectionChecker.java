package mundo;

public class ConnectionChecker extends Thread{

	private ConnectionStateListener listener;
	private Cliente c;
	public ConnectionChecker(ConnectionStateListener listener, Cliente c) {
		this.listener = listener;
		this.c = c;
	}

	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			
		}
		while(true){
			if(!c.socketConnection()){
				if(!c.downloadHasStarted() || c.isPaused()) listener.changeConnectionState(c.goodConnection());
				else listener.changeConnectionState(true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

}
