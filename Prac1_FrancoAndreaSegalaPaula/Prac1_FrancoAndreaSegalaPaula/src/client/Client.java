package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client {

	static Scanner teclat = new Scanner(System.in);

	private String IP = "";
	private int port = 0;

	public Client(String IP, int port) {
		this.IP = IP;
		this.port = port;
	}

	public void run() {

		System.out.println("Vol sol.licitar un fitxer o pujar un fitxer al Origin Server?(Sol.licitar/Pujar)");
		String solicitarDocument = teclat.next();

		// Client UDP

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(1);
		}

		// send request

		byte[] buf = new byte[256];

		if (solicitarDocument.equalsIgnoreCase("Sol.licitar")) {

			System.out.println("Escriu la seva localitzacio (Continent,Ciutat)");
			String localitzacio = teclat.next();
			buf = localitzacio.getBytes();

		} else if (solicitarDocument.equalsIgnoreCase("Pujar")) {

			String localitzacio = ("pujar,document");
			buf = localitzacio.getBytes();
		}

		InetAddress address = null;
		try {
			address = InetAddress.getByName(IP);
		} catch (UnknownHostException ex) {
			System.err.println("Unknown Host Exception");
			System.exit(1);
		}

		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Error when sending");
			System.exit(1);
		}

		// get response
		byte[] buffer = new byte[256];
		packet = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			System.err.println("Error when receiving");
			System.exit(1);
		}

		// display response
		String received = new String(buffer, 0, packet.getLength());

		socket.close();

		StringTokenizer receivedServer = new StringTokenizer(received, ",");

		String ciutat = receivedServer.nextToken();
		String portServer = receivedServer.nextToken();

		int portReceived = Integer.parseInt(portServer);

		// CLient EdgeTCP
		String documentEdge = "";

		if (!ciutat.equals("Origin")) {

			Socket socketTCP = null;
			PrintWriter out = null;
			BufferedReader in = null;

			try {
				socketTCP = new Socket(IP, portReceived);
				out = new PrintWriter(socketTCP.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: " + IP);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: " + IP);
				System.exit(1);
			}

			out.println(ciutat);

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;

			try {
				System.out.println("Escriu el document que vol sol.licitar");
				fromUser = stdIn.readLine();
				out.println(fromUser);

				if ((fromServer = in.readLine()).equalsIgnoreCase("No s'ha trobat el fitxer.")) {

					System.out.println("EdgeServer: " + fromServer);

					documentEdge = fromUser;
					String portOrigin = in.readLine();
					portReceived = Integer.parseInt(portOrigin);
					ciutat = "Origin";

				} else {

					BufferedWriter output = new BufferedWriter(new FileWriter(
							"C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\ClientFitxers\\"
									+ fromUser + ".txt"));

					output.write(fromServer);
					System.out.println("S'ha descarregat el fitxer");
					output.close();

				}

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

		// Client OriginTCP
		if (ciutat.equals("Origin")) {

			Socket socketTCP = null;
			PrintWriter out = null;
			BufferedReader in = null;

			try {
				socketTCP = new Socket(IP, portReceived);
				out = new PrintWriter(socketTCP.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: " + IP);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: " + IP);
				System.exit(1);
			}

			if (solicitarDocument.equalsIgnoreCase("Sol.licitar")) {

				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

				String fromServer = "";
				String fromUser = "";

				try {
					if (!documentEdge.equals("")) {

						out.println(documentEdge);

					} else {
						System.out.println("Escriu el document que vol sol.licitar");
						fromUser = stdIn.readLine();
						documentEdge = fromUser;
						out.println(fromUser);
					}

					if ((fromServer = in.readLine()).equalsIgnoreCase("No s'ha trobat el fitxer.")) {
						System.out.println("OriginServer: " + fromServer);

					} else {

						BufferedWriter output = new BufferedWriter(new FileWriter(
								"C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\ClientFitxers\\"
										+ documentEdge + ".txt"));

						output.write(fromServer);
						System.out.println("S'ha descarregat el fitxer");
						output.close();

					}

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

			} else if (solicitarDocument.equalsIgnoreCase("Pujar")) {

				out.println("Pujar");

				System.out.println("Quin fitxer vol pujar?");
				String inputLine = teclat.next();

				ComprovacioFitxer comprovacio = new ComprovacioFitxer();
				String fitxerPujar = (inputLine + ".txt");

				boolean hihaFitxer = comprovacio.processInput(fitxerPujar);

				if (hihaFitxer == true) {

					out.println(fitxerPujar);

					String path = ("C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\ClientFitxers\\"
							+ fitxerPujar);

					try (BufferedReader fitxer = new BufferedReader(new FileReader(path))) {

						String frase = fitxer.readLine();

						out.println(frase);
						System.out.println("El fitxer s'ha enviat correctament");

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println("No s'ha trobat el fitxer");
				}

			}
		}
	}
}
