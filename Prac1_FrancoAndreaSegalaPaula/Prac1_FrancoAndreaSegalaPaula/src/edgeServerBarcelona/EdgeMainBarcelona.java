package edgeServerBarcelona;

public class EdgeMainBarcelona {

	public static void main(String[] args) {
		
		EdgeServer barcelona = new EdgeServer("192.168.255.254", 4444);
		barcelona.run();
	}	
}