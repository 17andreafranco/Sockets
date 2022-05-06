package edgeServerDallas;

public class EdgeMainDallas {

	public static void main(String[] args) {

		EdgeServer dallas = new EdgeServer("192.168.255.255", 4445);
		dallas.run();
	}
}