package edgeServerParis;

import java.io.File;

public class EdgeProtocol {

	public boolean processInput(String theInput, String localitzacio) {

		boolean theOutput = false;

		File carpetaOrigin = new File(

				"C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\EdgeServer" + localitzacio);

		String[] llista = carpetaOrigin.list();

		if (!(llista == null || llista.length == 0)) {
			for (int i = 0; i < llista.length; i++) {
				if (theInput.equalsIgnoreCase(llista[i])) {
					theOutput = true;
				}
			}
		}
		return theOutput;
	}
}