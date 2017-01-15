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

    private static final String[] SENDCOMMAND = {
        "/bin/bash", "script.sh",
        "arg1","arg2"};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JavaToPythonThruBash entity = new JavaToPythonThruBash();
        entity.go();
    }
    
    public void go() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        final String $PATH = "pythondir";
        processBuilder.directory(new File($PATH));
        processBuilder.environment().put("PYTHONPATH", $PATH);
        processBuilder.command(SENDCOMMAND);
        System.out.println(processBuilder.environment());
        
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException ex) {
        }
    }
}
