import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * 
 */

/**
 * @author JohnDoe
 * 
 */
public class Main {

	public static final String CONF_FILE_TO_STREAM = "./fileSequence18.ts";
	public static final int CONF_SERVER_LISTEN_PORT = 31337;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Vector<File> filesToStream = new Vector<File>();
		filesToStream.add(new File(CONF_FILE_TO_STREAM));

		try {
			ServerSocket listen = new ServerSocket(CONF_SERVER_LISTEN_PORT);

			while (true) {
				Socket sock = listen.accept();
				System.out.println(String.format("Client connected from %s",
						sock.getRemoteSocketAddress().toString()));

				try {
					sendHeaders(sock.getOutputStream());
					System.out.println("HTTP headers sent");
					sendAllFileBytes(filesToStream, sock.getOutputStream());
					System.out.println("All files sent");
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void sendHeaders(OutputStream outputStream)
			throws IOException {
		String headers = "HTTP/1.0 200 OK\n"
				+ "Date: Fri, 31 Dec 1999 23:59:59 GMT\n"
				+ "Content-Type: application/octet-stream\n"
				+ "Content-Length: 991354\n\n";

		byte write[] = headers.getBytes();
		outputStream.write(write);
	}

	static void sendAllFileBytes(Vector<File> files, OutputStream os)
			throws IOException {

		for (File f : files) {
			sendSingleFileBytes(f, os);
		}
	}

	static void sendSingleFileBytes(File file, OutputStream os)
			throws IOException {

		System.out.println("Sending " + file.getName() + "..");

		FileInputStream fis = new FileInputStream(file);
		byte buffer[] = new byte[2048];

		while (fis.available() > 0) {
			fis.read(buffer);
			os.write(buffer);
		}

		System.out.println("Done sending " + file.getName());
	}

}
