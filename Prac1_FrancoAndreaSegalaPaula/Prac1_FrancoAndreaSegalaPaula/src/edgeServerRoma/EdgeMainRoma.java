package edgeServerRoma;

public class EdgeMainRoma {

	public static void main(String[] args) {

		EdgeServer roma = new EdgeServer("192.168.255.26", 4447);
		roma.run();
	}
}