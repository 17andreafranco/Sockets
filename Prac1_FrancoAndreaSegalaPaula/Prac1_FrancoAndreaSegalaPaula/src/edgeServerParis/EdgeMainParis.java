package edgeServerParis;

public class EdgeMainParis {

	public static void main(String[] args) {

		EdgeServer paris = new EdgeServer("192.168.255.25", 4446);
		paris.run();
	}
}