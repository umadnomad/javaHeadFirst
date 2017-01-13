/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

import java.io.*;
import java.net.*;

/**
 *
 * @author nomad
 */
public class DailyAdviceServer {

    private static final String[] ADVICE_LIST = {
        "Take smaller bites",
        "Go for the tight jeans. No they do NOT make you look fat.",
        "One word: inappropiate",
        "Just for today, be honest. Tell your boss what you *really* think",
        "You might want to rethink that haircut."};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DailyAdviceServer server = new DailyAdviceServer();
        server.go();
    }

    public void go() {

        try {

            /**
             * ServerSocket makes this server application 'listen' for client
             * requests on port 4242 on the machine this code is running on
             */
            ServerSocket serverSock = new ServerSocket(4242);

            /**
             * The server goes into a permanent loop, waiting for (and
             * servicing) client requests
             */
            while (true) {

                /**
                 * The accept method blocks (just sits there) until a request
                 * comes in, and then the method returns a Socket (on some
                 * anonymous port) for communicating with the client
                 */
                Socket newClient = serverSock.accept();

                /**
                 * Now we use the Socket Connection to the client to make a
                 * PrintWrite and send it (println() a String advice message.
                 * Then we close the Socket because we're done with this
                 * client
                 */
                PrintWriter writer = new PrintWriter(newClient.getOutputStream());
                String advice = getAdvice();

                writer.println(advice);
                writer.close();

                System.out.println(advice);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAdvice() {

        int random = (int) (Math.random() * ADVICE_LIST.length);
        return ADVICE_LIST[random];
    }
}
