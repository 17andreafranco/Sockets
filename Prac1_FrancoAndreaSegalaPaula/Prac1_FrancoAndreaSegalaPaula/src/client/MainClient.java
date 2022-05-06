package client;

public class MainClient {

	public static void main(String[] args) {
		Client objetoCliente= new Client("127.0.0.1",4444);
        objetoCliente.run();
	}
}