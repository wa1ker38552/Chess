import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish {
	private String path;
	private BufferedReader processReader;
    private OutputStreamWriter processWriter;
	
	public Stockfish(String path) {
		this.path = path;
		startStockfish();
	}
	
	public void startStockfish() {
		try {
			Process engineProcess = Runtime.getRuntime().exec(path);
	        processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
	        processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
		} catch (Exception e) {}
	}
	
	public void quitStockfish() {
		try {
			sendCommand("quit");
            processReader.close();
            processWriter.close();
		} catch (Exception e) {}
	}
	
	public void sendCommand(String command) {
		try {
			this.processWriter.write(command + "\n");
	        this.processWriter.flush();
		} catch (Exception e) {}
	}
	
	public String getBestMove(String notation, int depth) {
		sendCommand("position fen "+notation);
		sendCommand("go depth "+depth);
		
		StringBuffer stringBuffer = new StringBuffer();
        try{
            String text = " ";
            while (!text.contains("bestmove")){
                text = processReader.readLine();
                stringBuffer.append(text).append("\n");
            }
        } catch (Exception e) {}

        String output = stringBuffer.toString();
        String move = output.split("bestmove")[1].substring(1).split(" ")[0];
        return move;
	}
}
