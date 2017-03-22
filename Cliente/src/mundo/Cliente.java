package mundo;

public class Cliente {
	
	public static final String PAUSE = "pause";
	public static final String DOWNLOADING = "downloading";
	
	private String state;

	public void connect(){
		
	}
	
	public String[] requestFiles(){
		return null;
	}
	
	public void selectFile(String fileName){
		
	}
	
	
	public void requestPacket(int packetIndex){
		if (!state.equals(PAUSE)) {
			
		}
		
	}
	
	public void pauseDownload() {
		state = PAUSE;
	}
	
	public void resumeDownload() {
		
	}
	
	public void closeConnection(){
		
	}
}
