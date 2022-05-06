package originServer;

public class OriginMain {

	public static void main(String[] args) {
		OriginServer objetoServer = new OriginServer(3444, "127.0.0.3");
		objetoServer.run();
	}
}