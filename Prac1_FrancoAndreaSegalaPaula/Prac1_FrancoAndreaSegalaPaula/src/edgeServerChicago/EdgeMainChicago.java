package edgeServerChicago;

public class EdgeMainChicago {

	public static void main(String[] args) {

		EdgeServer chicago = new EdgeServer("192.168.255.29", 4449);
		chicago.run();
	}
}