/* This class is a class that carries out all the features provided by our server,
 * like: logging in, writing messages, reading messages
 * Only extra things imported are those necessary to read and write files,
 * No library that performs its primary functionality!
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

// Class starts here, EXTENDS THREAD, so a call to this class is usually a call to start a new thread
public class ClientHandler extends Thread {


	/* Here we define some strings which are different pages in HTML and HTTP responses,
	 * They are never edited, although things are appended to them like Cookies and Length of the content,
	 * and they belong to the class itself, not a object currently running!
	 */
	final static String loginPage = "<html>\r\n" + "<body>\r\n" + "<h1>Login Page for CS352</h1>\r\n" + "<p>\r\n"
			+ "  Welcome! Enter Credentials : \r\n" + "<p>\r\n" + "<form action=\"/\" method=\"post\">\r\n"
			+ "		<p>Enter Username : </p>\r\n" + "		<input type=\"text\" name=\"username\">\r\n"
			+ "    	<p>Enter Password : </p>\r\n" + "    	<input type=\"text\" name=\"password\">\r\n"
			+ "    	<p></p>\r\n" + "    	<input type=\"submit\" value=\"Login\">\r\n" + "</form>\r\n" + "</body>\r\n"
			+ "</html>";
	final static String chatSpace = "<html>\r\n" + "<body>\r\n" + "<h1>Chat Page for CS352</h1>\r\n" + "<p>\r\n"
			+ "  Chat Space :";
	final static String chatPage1 = "<html>\r\n" + "<body>\r\n" + "<h1>Chat Page for CS352</h1>\r\n" + "<p>\r\n"
			+ "  Chat Space :\r\n";
	final static String chatPage2 = "</p> \r\n <p>\r\n" + "<div id=\"chat-window\">\r\n" + "</div>\r\n"
			+ "<form action=\"/\" method=\"post\">\r\n" + "    <p>Enter Message : </p>\r\n"
			+ "    <input type=\"text\" name=\"message\">\r\n" + "    <p></p>\r\n"
			+ "    <input type=\"submit\" value=\"Enter\">\r\n" + "</form>\r\n" + "</body>\r\n" + "</html>";
	final static String loginOK = "HTTP/1.0 200 OK\r\n" + "Content-Type: text/html\r\n" + "Content-Lenght: "
			+ loginPage.getBytes().length + "\r\n" + "Content-Encoding: identity\r\n" + "Allow: GET, POST, HEAD\r\n";
	final static String OK1 = "HTTP/1.0 200 OK\r\n" + "Content-Type: text/html\r\n" + "Content-Lenght: ";
	final static String OK2 = "\r\n" + "Content-Encoding: identity\r\n" + "Allow: GET, POST, HEAD\r\n";
	final static String errorPage = "\r\n" + "<html>\r\n" + "<body>\r\n" + "<h1>Error Page for CS352</h1>\r\n"
			+ "<p>\r\n" + "  You have entered wrong credentials or cookie mismatch took place. Please try again !\r\n"
			+ "<p>\r\n" + "</body>\r\n" + "</html>";

	private Socket client;
	private String username = "";

	// This class takes a Socket object as a parameter, which is the client's socket, it (the thread) is going to work on

	public ClientHandler(Socket client) {
		this.client = client;
	}

	// Thread's run function
	public void run() {
		try {

			// We create a PrintWriter Object that is going to write to the OutputStream of the client's socket
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			// A BufferedReader object that is going to read from the InputStream of the client's socket
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			//A string builder object we are going to use to create a well structured String from the bytes received by the BufferedReader
			StringBuilder requestBuilder = new StringBuilder();

			// A string which is going to be the line read by the BufferedReader
			String line;
			//  If the buffered reader successfully reads a line from HTTP request of client
			while (!(line = in.readLine()).isEmpty()) {
				// Append the line to the StringBuilder
				requestBuilder.append(line + "\r\n");
			}
			// The request is going to be the requestBuilder converted to string
			String request = requestBuilder.toString();

			// If the request is a GET request
			if (request.contains("GET")) {
				// If the client wants to GET the chat page
				if (request.contains("chat")) {
					// If the client has a cookieId which was provided to the client upon successful login
					if (request.contains("cookieId")) {
						/* Validate the cookieId, the validate function will return a String which is the
						 * UserName associated with the cookieId upon successful validation, and a null value
						 * if it was not validated,
						 * the returned UserName is then stored in a String for future uses within the session
						 */
						username = validateCookie((request.substring(request.indexOf("cookieId") + 9)).trim());
						/* The userName string not being null means that the cookieId was validated successfully and we have a
						 * userName associated with that cookieId
						 */

						if (username != null) {

							// We get the chat page, HTML code for the chat page in the form of a String
							String chatpage = getChatPage();
							/* We respond to the client that everything is working out smoothly, the userName, and the cookieId
							 * provided by the client was correct, and also provide the current length of the chat page in bytes
							 * that is going to be sent next,
							 * with the help of OK HTTP response stored above
							 */
							out.println(OK1 + chatpage.getBytes().length + OK2 + "\r\n");
							out.flush();
							// Once we send the OK response, we then send the chat page with current data
							out.println(chatpage);
							out.flush();
							System.out.println("Sent Chat Page");
							client.close();
						}
						/* If the userName is null, the information provided by the client was incorrect and hence
						 * we send the error page similarly
						 */

					} else {

						out.println(OK1 + errorPage.getBytes().length + OK2);
						out.flush();
						out.println(errorPage);
						out.flush();
						client.close();
					}
					/* If the GET request was not a request to GET the chat page,
					 * it is going to be a login page request and we are going to send the login page
					 * in a similar fashion
					 *
					 */
				} else {
					System.out.println("Sent Login Page");
					out.println(loginOK);
					out.flush();
					out.println(loginPage);
					out.flush();
					client.close();
				}
			}
			// If the request is a post request,
			if (request.contains("POST")) {
				// We fetch the data the client wants to post like we fetched the request headers
				requestBuilder = new StringBuilder();
				/* But this time, we start fetching the data once the user is ready to post,
				 * i.e. once the user submits the login form or the message form,
				 * append to request builder and convert to String as before!
				 */
				while (in.ready()) {
					requestBuilder.append((char) in.read());
				}
				// The response string contains the response given by the user, which is data: login credentials or chat message
				String response = requestBuilder.toString();

				/* If the user posted the userName and password,
				 * response is going to be of the form
				 * "username=<username>&password=<password>",
				 * and we parse the string accordingly with the help of java String methods
				 */
				if (response.matches("username=(.*)&password=(.*)")) {
					System.out.println("Login Request: " + response);

					// The
					username = response.substring(9, response.indexOf('&'));
					String password = response.substring(response.indexOf('&') + 10);
					System.out.println("Username: " + username + " Password: " + password);

					// Once we have the userName and password entered by the client, we try to validate it, upon successful validation:
					if (validateLogin(username, password)) {
						// Fetch the chat page, assign a cookie, which is done by addCookie helper method and
						String chatpage = getChatPage();
						// Send a OK response along with a cookie for the user
						out.println(OK1 + chatpage.getBytes().length + OK2 + "Set-Cookie: cookieId="
								+ addCookie(username) + "\r\n");
						out.flush();
						// Send the chat page after sending the OK response
						out.println(chatpage);
						out.flush();
						client.close();
						/* If the userName was not validated successfully
						 * return an error response with the use of error page stored above
						 */
					} else {

						out.println(OK1 + errorPage.getBytes().length + OK2);
						out.flush();
						out.println(errorPage);
						out.flush();
						client.close();
					}
					/* If the data posted by the client does not match the credentials posting format,
					 * it is going to be a message the user wants to post
					 */
				} else {

					/* If the client has a cookie, validate it and allow the post,
					 * if the client does not have a cookie or if the cookie is not a valid one,
					 * it is a fraud attempt and do not allow it,
					 * everything is done similarly as above
					 */
					if (request.contains("Cookie") && request.contains("cookieId")) {
						username = validateCookie((request.substring(request.indexOf("cookieId") + 9)).trim());
						if (username != null) {

							System.out.println("Write chat: " + response + "from: " + username);
							addMessage(username, response.substring(8));
							out.println(OK1 + OK2);
							out.flush();
							client.close();
						}

						else {
							out.println(OK1 + errorPage.getBytes().length + OK2);
							out.flush();
							out.println(errorPage);
							out.flush();
							client.close();
						}

					} else {
						out.println(OK1 + errorPage.getBytes().length + OK2);
						out.flush();
						out.println(errorPage);
						out.flush();
						client.close();
					}

				}

			}
			// The connection closes here
			System.out.println("Closed");
		} catch (Exception ex1) {
			System.out.println(ex1);
		}

	}

	/*
	 * This method uses basic java.file.IO features to append a new message to the
	 * 'allMessages.txt' file,
	 * It creates a new 'allMessages.txt' file if the file does not exist,
	 * It stores messages in the form <userName:message>
	 */
	public static void addMessage(String username, String message) {
		try {
			File allMessages = new File("allMessages.txt");
			if (!allMessages.exists()) {
				allMessages.createNewFile();
			}
			System.out.println("Request to add a message " + message + " from: " + username);
			FileWriter fw = new FileWriter(allMessages, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(username + ":" + message);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.print(ex);
			System.exit(1);
		}

	}

	/*
	 * This method uses basic java.file.IO features to append a new cookie to the
	 * 'cookies.txt' file, It creates a new 'cookies.txt' file if the file does not exist,
	 * It stores cookies in the form <userName,cookieId>,
	 * returns null if it crashes because of some unexpected exceptions
	 */
	public static String addCookie(String username) {
		try {
			File cookies = new File("cookies.txt");
			if (!cookies.exists()) {
				cookies.createNewFile();
			}
			System.out.println("Request to write cookie for" + username);
			String cookieId = username;
			cookieId += System.currentTimeMillis();
			FileWriter fw = new FileWriter(cookies, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(username + "," + cookieId);
			bw.newLine();
			bw.close();
			return cookieId;
		} catch (IOException ex) {
			System.out.print(ex);
			System.exit(1);
			return null;
		}

	}

	/*
	 * This method uses basic java.file.IO features to read from the 'cookies.txt' file,
	 * create the file if it does not exist, and
	 * returns the userName associated with the cookieId if the cookieId exists, i.e.
	 * upon successful validation,
	 * returns null if not successful
	 */
	public static String validateCookie(String cookieId) {
		try {
			File cookies = new File("cookies.txt");
			if (!cookies.exists()) {
				cookies.createNewFile();
				return null;
			}
			Scanner sc = new Scanner(cookies);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				int i = line.indexOf(',');
				String user = line.substring(0, i);
				String cookie = line.substring(i + 1);
				if (cookie.equals(cookieId)) {
					sc.close();
					return user;

				}
			}
			sc.close();
			return null;
		} catch (IOException ex) {
			System.out.print(ex);
			return null;
		}
	}
	/*
	 * This method uses basic java features to return the chatPage,
	 * uses getMessages() method described below to retrieve the messages currently stored in the
	 * 'allMessages.txt' file, and returns a fully functional String of HTML codes that produce the
	 * chat page.
	 */
	public static String getChatPage() {
		String chat = chatPage1;
		ArrayList<String> messages = readMessages();
		if (messages != null && !messages.isEmpty()) {
			chat = chatSpace;
			int i = 0;
			while (i < messages.size()) {
				chat += "<p>" + messages.get(i++) + "</p>";
			}
		}
		chat += chatPage2;
		return chat;

	}
	/* This method uses basic java.file.IO features to read messages from the 'allMessages.txt' file,
	 * and returns an arrayList that contains all the messages currently in the server,
	 * if the file does not exist, it creates a new 'allMessages.txt' file and returns an empty arrayList
	 */

	public static ArrayList<String> readMessages() {
		ArrayList<String> messages = new ArrayList<String>();
		File allMessages = new File("allMessages.txt");
		if (!allMessages.exists()) {
			try {
				allMessages.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return messages;
		}
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(allMessages);
			while (sc.hasNext()) {
				messages.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;
	}

	/* This method is similar to validateCookie but instead validates the credentials
	 * from the file 'credentials.txt' and returns true or false depending on its success or failure
	 * to validate the credentials
	 */
	public static boolean validateLogin(String username, String password) {
		try {
			File credentials = new File("credentials.txt");
			if (!credentials.exists()) {
				credentials.createNewFile();
			}
			Scanner sc = new Scanner(credentials);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				int i = line.indexOf(',');
				String user = line.substring(0, i);
				String pass = line.substring(i + 1);
				if (user.equals(username) && pass.equals(password)) {
					sc.close();
					return true;
				}
			}
			sc.close();
			return false;
		} catch (IOException ex) {
			System.out.print(ex);
			return false;
		}
	}
}
