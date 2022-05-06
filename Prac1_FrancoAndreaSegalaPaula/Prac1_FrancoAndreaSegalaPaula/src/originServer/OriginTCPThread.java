package originServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OriginTCPThread extends Thread {

	private Socket socket = null;

	public OriginTCPThread(Socket socket) {
		this.socket = socket;
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

		OriginProtocol protocol = new OriginProtocol();

		String inputLine;

		try {

			while ((inputLine = in.readLine()) != null) {

				if (!inputLine.equalsIgnoreCase("Pujar")) {

					String fitxerClient = (inputLine + ".txt");

					boolean hihaFitxer = protocol.processInput(fitxerClient);

					if (hihaFitxer == true) {

						String path = ("C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\OriginServerFitxers\\"
								+ fitxerClient);

						try (BufferedReader fitxer = new BufferedReader(new FileReader(path))) {

							String frase = fitxer.readLine();

							out.println(frase);

							break;

						} catch (IOException e) {
							System.err.println(e.getCause());
							System.exit(1);
						}

					}

					else if (hihaFitxer == false) {
						System.out.println("No s'ha trobat el fitxer.");
						out.println("No s'ha trobat el fitxer.");

						break;
					}
				} else if (inputLine.equalsIgnoreCase("Pujar")) {

					inputLine = in.readLine();
					String fitxerClient = (inputLine);

					if (protocol.processInput(fitxerClient) == false) {

						try (BufferedWriter output = new BufferedWriter(new FileWriter(
								"C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\OriginServerFitxers\\"
										+ fitxerClient))) {

							String fromUser = in.readLine();

							output.write(fromUser);
							System.out.println("S'ha descarregat el fitxer");
							output.close();
							break;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					break;
				}

			}

		} catch (

		IOException e) {
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
