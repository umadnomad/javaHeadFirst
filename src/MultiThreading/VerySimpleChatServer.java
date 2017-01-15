/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiThreading;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author nomad
 */
public class VerySimpleChatServer {

    ArrayList<Object> clientOutputStreams;  // Generic ArrayList (no diamond <> operator)

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket sock;

        // start constructor with one Socket parameter
        public ClientHandler(Socket clientSocket) {
            try {

                // take any socket I feed you and read its input stream
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } // close constructor

        @Override
        public void run() {
            String message;
            try {
                /**
                 * read one by one all the lines given by the BufferedReader and
                 * send them to all the other Clients, one by one.
                 */
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEveryone(message);

                } // close while
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } // close run
    } // close inner class

    public void go() {

        /**
         * important, this ArrayList holds all the outgoing streams, it
         * literally keep tracks of all the peeps it has to communicate with
         */
        clientOutputStreams = new ArrayList<Object>();

        // IOException wrapper
        try {
            // start the server's socket
            ServerSocket serverSock = new ServerSocket(5000);

            // MAIN WHILE LOOP
            while (true) {

                // listen for incoming connections...
                Socket clientSocket = serverSock.accept();

                // ... ... ... ... connection detected,
                // intercepting client - send him a message
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

                /**
                 * adding this stream to the ArrayList that holds all the
                 * outgoing streams, it literally keep tracks of all the peeps
                 * it has to communicate with
                 */
                clientOutputStreams.add(writer);

                /**
                 * instantiate a separate thread of executing that will take
                 * care exclusively of this client
                 */
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();

                // inform sysadmin
                System.out.println("got a connection");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void tellEveryone(String message) {

        /**
         * until now the book did not explain the use of Iterator class, I
         * believe it will be covered later in the Data structures chapter
         */
        /**
         * from what i can see ArrayList Class has a method named iterator()
         * which returns an Iterator class object instance
         */
        Iterator<Object> it = clientOutputStreams.iterator();

        /**
         * I interpret this as: give me a list of whats inside
         * clientOutputStreams ArrayList. now make a while loop and iterate
         * through all the objects contained in the ArrayList.
         */
        while (it.hasNext()) { // till there are elements in the list I did not work with...

            // IOException wrapper
            try {

                /**
                 * this line of code couldn't ever run if the Iterator did not
                 * have another element in the list
                 */
                PrintWriter writer = (PrintWriter) it.next(); // give me this element and assign it to writer local ref var
                /**
                 * // as the clientOutputStreams ArrayList contained
                 * PrintWriter type objects and Iterator's next() method returns
                 * a plain Object, cast this anonymous Object to what it really
                 * was at the begin, a PrintWriter Obj...
                 */

                writer.println(message);
                writer.flush();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } // end while
    } // close tellEveryone

} // close class

class VerySimpleChatServerTestDrive {

    public static void main(String[] args) {
        new VerySimpleChatServer().go();
    }
}
