/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiThreading;

/**
 *
 * @author nomad
 */
public class RunThreads implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 25; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " is running");
        }
    }
    
}

class TestDrive {
    public static void main(String[] args) {
        
        RunThreads runner = new RunThreads();
        
        Thread alpha = new Thread(runner);
        Thread beta = new Thread(runner);
        alpha.setName("Alpha");
        beta.setName("Beta");
        
        alpha.start();
        beta.start();
    }
}