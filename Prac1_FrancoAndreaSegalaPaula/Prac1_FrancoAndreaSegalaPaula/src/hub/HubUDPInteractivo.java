package hub;

import java.io.*;
import java.net.*;
import java.util.*;

public class HubUDPInteractivo {

	private int port = 0;
	private int portOrigin = 3444;
	private String IP = "";

	public HubUDPInteractivo(int port, String IP) {
		this.port = port;
		this.IP = IP;
	}

	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(1);
		}

		byte[] buf = new byte[256];

		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			System.err.println("Error when receiving");
			System.exit(1);
		}

		int len = 0;
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] != 0)
				len++;
		}
		byte[] localitzacioLlegidaClient = new byte[len];
		for (int i = 0, j = 0; i < buf.length; i++) {
			if (buf[i] != 0) {
				localitzacioLlegidaClient[j] = buf[i];
				j++;
			}
		}

		// localització llegida partida
		StringTokenizer localitzaciollegidaCompleta = new StringTokenizer(new String(localitzacioLlegidaClient), ",");
		String localitzacioContinent = localitzaciollegidaCompleta.nextToken();

		BufferedReader fitxer = null;

		boolean trobat = false;
		String ip = "";
		int port = 0;
		String serverTrobat = "";

		try {
			fitxer = new BufferedReader(new FileReader("EdgeLocalitzacio.txt"));
			String frase = fitxer.readLine();

			while (frase != null && trobat == false) {
				
				// localitzacio fitxer
				StringTokenizer localitzacioFitxerCompleta = new StringTokenizer(frase, ";");
				String localitzacioFitxer = localitzacioFitxerCompleta.nextToken();

				// localització fitxer partida
				StringTokenizer fraseFitxerCompleta = new StringTokenizer(localitzacioFitxer, ",");
				String localitzacioContinentFitxer = fraseFitxerCompleta.nextToken();
				String localitzacioCiutatFitxer = fraseFitxerCompleta.nextToken();

				if (localitzacioFitxer.equalsIgnoreCase(new String(localitzacioLlegidaClient))) {
					ip = localitzacioFitxerCompleta.nextToken();
					port = Integer.parseInt(localitzacioFitxerCompleta.nextToken());
					trobat = true;
					serverTrobat = localitzacioCiutatFitxer;
					fitxer.close();
					

				} else if (localitzacioContinentFitxer.equalsIgnoreCase(localitzacioContinent)) {
					ip = localitzacioFitxerCompleta.nextToken();
					port = Integer.parseInt(localitzacioFitxerCompleta.nextToken());
					serverTrobat = localitzacioCiutatFitxer;

				}
				frase = fitxer.readLine();
			}
			fitxer.close();

		} catch (FileNotFoundException e) {
			System.out.println("El fitxer d'entrada no existeix.");

		} catch (IOException e) {
			System.out.println("Error en l'arxiu de sortida de text.");
		}

		if (ip.equals("")) {
			port = portOrigin;
			serverTrobat = "Origin";
		}

		String portString = String.valueOf(port);
		String dades = (serverTrobat + "," + portString);

		buf = dades.getBytes();

		InetAddress addressOrigen = packet.getAddress();
		int puertoOrigen = packet.getPort();
		packet = new DatagramPacket(buf, buf.length, addressOrigen, puertoOrigen);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Error when sending");
			System.exit(1);
		}
		socket.close();
	}
}
