package edgeServerRoma;

import java.net.*;
import java.io.*;

public class EdgeServer {

	private int port = 0;
	private String IP = "";

	public EdgeServer(String IP, int port) {
		this.port = port;
		this.IP = IP;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(1);
		}

		while (true) {
			try {
				new EdgeTCPThread(serverSocket.accept(),IP).start();
			} catch (IOException e) {
				System.err.println("Accept failed");
			}
		}
	}
}