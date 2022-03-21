/* This class starts a server by opening up a new socket with the provided
 *  portNumber by the user, it then creates a new thread for every new connection
 * with the help of ClientHandler class, which handles the requests and sends appropriate
 * responses.
 */


// No additional libraries or classes are imported, only .net.ServerSocket and .net.Socket
import java.net.ServerSocket;
import java.net.Socket;

// Class Begins Here
public class HTTPChatServer {

	/* The main method for the entire program.
	* It takes one argument as input which is going to be the
	* desired port Number for starting the server
	*/
	public static void main(String[] args) {

		// If no port number is passed as argument, notify the user!
		if (args.length != 1) {
			System.err.println("Usage: java Server <port number>");
			System.exit(1);
		}


		// If we have a port number, parse it to Integer (int), because port numbers are 'numbers'
		int portNumber = Integer.parseInt(args[0]);

		/* Try creating a server with that port number, fails if that port number is already in use
		 * by some other application
		 */

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			System.out.println("Server Initialized at Port: " + portNumber);

			// After we successfully start a server, run the server forever
			while (true) {
				// Now we are going to try to connect a client!
				Socket client;
				//Try to get a connection
				try {
					// If we get a connection, get the client's socket and run it until the connection is closed
					while ((client = serverSocket.accept()) != null) {
						System.out.println("Connected Client from: " + client.getInetAddress().getHostAddress()
								+ " and remotePort: " + client.getPort());

						// Now comes the fun part, create a new ClientHandler object which is going to create a new Thread
						ClientHandler handler = new ClientHandler(client);
						// Instruct the Thread (object) to start its work!
						handler.start();
					}
					// Catches exceptions when trying to connect a client
				} catch (Exception ex2) {
					System.out.println(ex2);
				}
			}

			// Catches exceptions when trying to create a new server, like: port already being used
		}catch (Exception ex2) {
			System.out.println(ex2);
		}
	}
}
