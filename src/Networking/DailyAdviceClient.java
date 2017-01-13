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
public class DailyAdviceClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DailyAdviceClient client = new DailyAdviceClient();
        client.go();
    }

    public void go() {

        try {

            /**
             * Make a Socket connection to whatever is running on port 4242, on
             * the same host this code is running on. (The 'localhost')
             */
            Socket s = new Socket("127.0.0.1", 4242);

            InputStreamReader streamRead = new InputStreamReader(s.getInputStream());

            /**
             * Chain a BufferedReader to an inputStreamReader to the input
             * stream from the Socket
             */
            BufferedReader reader = new BufferedReader(streamRead);

            /**
             * This readLine() is EXACTLY the same as if you were using a
             * BufferedReader chained to a FILE... In other words, by the time
             * you call a BufferedReader method, the reader doesn't know or care
             * where the characters came from
             */
            String advice = reader.readLine();
            
            System.out.println("Today you should: " + advice);

            reader.close(); // this closes ALL the streams in a cascading style
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
