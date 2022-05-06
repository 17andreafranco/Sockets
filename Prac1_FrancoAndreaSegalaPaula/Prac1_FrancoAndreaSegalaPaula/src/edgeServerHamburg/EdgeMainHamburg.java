package edgeServerHamburg;

public class EdgeMainHamburg {

	public static void main(String[] args) {

		EdgeServer hamburg = new EdgeServer("192.168.255.27", 4448);
		hamburg.run();
	}
}