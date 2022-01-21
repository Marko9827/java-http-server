package eronelit_java_server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import eronelit_java_server.server_conf;
/* 
	@author Marko9827

*/

public class Eronelit_java_server implements Runnable {

	static final File WEB_ROOT = new File("Q:/test/math/app"); // SET YOUR WEB ROOT
	static final String DEFAULT_FILE = "index.html"; // SET YOUR DEFAULT DOCUMENT(index.html, default.html, default.htm)
	static final String FILE_NOT_FOUND = "404.html"; // SET YOUR 404 ERROR DOCUMENT
	static final String METHOD_NOT_SUPPORTED = "not_supported.html"; //	SET YOUR METHOD NOT SUPPORTED DOCUMENT
	static final int PORT = 3000; // SET YOUR PORT NUMBER
	static final boolean verbose = true;
	private Socket connect;

	// index.html, 404.html, not_supported.html Reads from the root directory

	public Eronelit_java_server(Socket c) {
		connect = c;
	}

	public static void main(String[] args) {
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Local IP : " + ip);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			for (Object propertyKeyName : System.getProperties().keySet()) {
				System.out.println(propertyKeyName + " - " + System.getProperty(propertyKeyName.toString()));
			}
			while (true) {
				Eronelit_java_server myServer = new Eronelit_java_server(serverConnect.accept());

				if (verbose) {
					System.out.println("Connecton opened. (" + new Date() + ")");
				}
				Thread thread = new Thread(myServer);
				thread.start();
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}

	Eronelit_java_server() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		BufferedOutputStream dataOut = null;
		String fileRequested = null;

		try {
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			out = new PrintWriter(connect.getOutputStream());
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			String input = in.readLine();
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
			fileRequested = parse.nextToken().toLowerCase();
			if (!method.equals("GET") && !method.equals("HEAD")) {
				if (verbose) {
					System.out.println("501 Not Implemented : " + method + " method.");
				}
				File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
				int fileLength = (int) file.length();
				String contentMimeType = "text/html";
				byte[] fileData = readFileData(file, fileLength);
				out.println("HTTP/1.1 501 Not Implemented");
				out.println("Server: Eronelit OS V35/0.5");
				out.println("Date: " + new Date());
				out.println("Content-type: " + contentMimeType);
				out.println("Content-length: " + fileLength);
				out.println("X-Text: " + fileLength);
				out.println(); // blank line between headers and content, very important !
				out.flush(); // flush character output stream buffer
				dataOut.write(fileData, 0, fileLength);
				dataOut.flush();

			} else {
				if (fileRequested.endsWith("/")) {
					fileRequested += DEFAULT_FILE;
				}

				File file = new File(WEB_ROOT, fileRequested);
				int fileLength = (int) file.length();
				String content = getContentType(fileRequested);

				if (method.equals("GET")) { // GET method so we return content
					byte[] fileData = readFileData(file, fileLength);
					out.println("HTTP/1.1 200 OK");
					out.println("Server: Eronelit OS V35/0.5");
					out.println("Date: " + new Date());
					out.println("Content-type: " + content);
					out.println("Content-length: " + fileLength);
					out.println(); // blank line between headers and content, very important !
					out.flush(); // flush character output stream buffer

					dataOut.write(fileData, 0, fileLength);
					dataOut.flush();
				}

				if (verbose) {
					System.out.println("File " + fileRequested + " of type " + content + " returned");
				}

			}

		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}

			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}

	}

	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];

		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null)
				fileIn.close();
		}

		return fileData;
	}

	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
			return "text/html";
		else
			return "text/plain";
	}

	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);

		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Eronelit OS V35/0.5");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer

		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();

		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
	}

}
