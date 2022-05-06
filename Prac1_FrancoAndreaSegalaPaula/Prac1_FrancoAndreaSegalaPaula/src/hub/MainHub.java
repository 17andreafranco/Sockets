package hub;

public class MainHub {

	public static void main(String[] args) {
		HubUDPInteractivo objetoServer = new HubUDPInteractivo(4444, "127.0.0.2");
		objetoServer.run();
	}
}