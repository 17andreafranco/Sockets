package edgeServerParis;

import java.net.*;
import java.io.*;

public class EdgeTCPThread extends Thread {

	private Socket socket = null;
	private String IPOrigin = "127.0.0.3";
	private int portOrigin = 3444;
	private String IP = "";

	public EdgeTCPThread(Socket socket, String IP) {
		this.socket = socket;
		this.IP = IP;
	}

	@Override
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Create streams failed.");
			System.exit(1);
		}

		EdgeProtocol protocol = new EdgeProtocol();

		String localitzacio = "";
		try {
			localitzacio = in.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String inputLine;

		try {
			while ((inputLine = in.readLine()) != null) {

				String fitxerClient = (inputLine + ".txt");
				String nomFitxer = inputLine;

				boolean hihaFitxer = protocol.processInput(fitxerClient, localitzacio);

				if (hihaFitxer == true) {

					String path = ("C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\EdgeServer"
							+ localitzacio + "\\" + fitxerClient);

					try (BufferedReader fitxer = new BufferedReader(new FileReader(path))) {

						String frase = fitxer.readLine();

						out.println(frase);

						break;
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

				if (hihaFitxer == false) {

					out.println("No s'ha trobat el fitxer.");
					out.println(portOrigin);

					Socket socketTCP = null;
					PrintWriter outEdge = null;
					BufferedReader inOrigin = null;

					try {
						socketTCP = new Socket(IPOrigin, portOrigin);
						outEdge = new PrintWriter(socketTCP.getOutputStream(), true);
						inOrigin = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
					} catch (UnknownHostException e) {
						System.err.println("Don't know about host: " + IP);
						System.exit(1);
					} catch (IOException e) {
						System.err.println("Couldn't get I/O for the connection to: " + IP);
						System.exit(1);
					}

					BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

					String fromServer = "";

					try {

						outEdge.println(nomFitxer);

						if (!(fromServer = inOrigin.readLine()).equalsIgnoreCase("No s'ha trobat el fitxer.")) {

							BufferedWriter output = new BufferedWriter(new FileWriter(
									"C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\EdgeServer"
											+ localitzacio + "\\" + nomFitxer + ".txt"));

							output.write(fromServer);
							System.out.println("S'ha descarregat el fitxer");
							output.close();
						}
						
						break;

					} catch (IOException e) {
						System.err.println(e.getCause());
						System.exit(1);
					}

					try {
						out.close();
						in.close();
						stdIn.close();
						socket.close();
					} catch (IOException e) {
						System.err.println("Close failed.");
						System.exit(1);
					}

				}
			}

		} catch (IOException e) {
			System.err.println("Read failed.");
			System.exit(1);
		}

		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Close failed.");
			System.exit(1);
		}

	}

}
