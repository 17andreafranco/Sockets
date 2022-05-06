package client;

import java.io.File;

public class ComprovacioFitxer {
	
	public ComprovacioFitxer() {

	}

	public boolean processInput(String theInput) {

		boolean theOutput = false;

		File carpetaOrigin = new File(
				"C:\\Users\\Andrea\\eclipse-workspace\\Prac1_FrancoAndreaSegalaPaula\\ClientFitxers");

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
