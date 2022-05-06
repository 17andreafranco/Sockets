package originServer;

import java.io.IOException;
import java.net.ServerSocket;

public class OriginServer {

	private int port = 0;
	private String IP = "";

	public OriginServer(int port, String IP) {
		this.port = port;
		this.IP = IP;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		while (true) {
			try {
				new OriginTCPThread(serverSocket.accept()).start();
			} catch (IOException e) {
				System.err.println("Accept failed");
			}
		}
	}
}