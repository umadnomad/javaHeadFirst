/**
 * Created by nomad on 18/03/17.
 */
package Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class MusicServer {

	ArrayList<ObjectOutputStream> clientOutputStreams;

	public static void main(String[] args) {
		new MusicServer().go();
	}

	public class ClientHandler implements Runnable {

		ObjectInputStream in;
		Socket            clientSocket;

		public ClientHandler(Socket socket) {
			try {
				clientSocket = socket;
				in = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // close constructor

		@Override
		public void run() {
			Object o2 = null;
			Object o1 = null;
			try {
				while ((o1 = in.readObject()) != null) {
					o2 = in.readObject();

					System.out.println("read two objects");
					tellEveryone(o1, o2);
				} // close while
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} // close run
	} // close inner class

	public void go() {
		clientOutputStreams = new ArrayList<ObjectOutputStream>();

		try {
			ServerSocket serverSocket = new ServerSocket(4242);

			while (true) {
				Socket             clientSocket = serverSocket.accept();
				ObjectOutputStream out          = new ObjectOutputStream(clientSocket.getOutputStream());
				clientOutputStreams.add(out);

				Thread thread = new Thread(new ClientHandler(clientSocket));
				thread.start();

				System.out.println("got a connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // close

	public void tellEveryone(Object one, Object two) {

		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				ObjectOutputStream out = (ObjectOutputStream) it.next();
				out.writeObject(one);
				out.writeObject(two);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} // close tellEveryone

} // close class
