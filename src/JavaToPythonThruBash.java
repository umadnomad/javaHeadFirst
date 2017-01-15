/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;

/**
 *
 * @author nomad
 */
public class JavaToPythonThruBash {

    /**
     * this path is subject to all kind of failures, it is the first thing you
     * have to check if something is not working. TO-DO: make a robust method
     * that perform checks and reliably returns the correct $PATH. This path
     * works as intended if the file you are trying to run is a compiled .class
     * file and the directory structure is as follows:
     * 
     * current_dir
     * ├── JavaToPythonThruBash.class
     * └── pythondir
     *     ├── ignition.sh
     *     └── pythontest.py
     *
     */
    private static final String $PATH
            = System.getProperty("user.dir") + "/pythondir";

    private static final String[] SENDCOMMAND = {
        "/bin/bash", "ignition.sh",
        "arg1", "arg2"};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JavaToPythonThruBash entity = new JavaToPythonThruBash();
        entity.go();
    }

    public void go() {
        System.out.println("Debug:" + $PATH);
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.directory(new File($PATH));
        processBuilder.environment().put("PYTHONPATH", $PATH);
        processBuilder.command(SENDCOMMAND);
        //System.out.println(processBuilder.environment());

        try {
            Process process = processBuilder.start();
            process.waitFor();

            InputStream stream = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
        } catch (IOException | InterruptedException ex) {
        }
    }
}
