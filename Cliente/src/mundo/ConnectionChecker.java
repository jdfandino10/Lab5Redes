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
			this.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			if(!c.socketConnection()){
				if(!c.downloadHasStarted() || c.isPaused()) listener.changeConnectionState(c.goodConnection());
				else listener.changeConnectionState(true);
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

}
