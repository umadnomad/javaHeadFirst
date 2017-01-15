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
public class MyRunnable implements Runnable {

    @Override
    public void run() {
        go();
    }

    public void go() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doMore();
    }

    public void doMore() {
        System.out.println("top o' the stack");
    }

}

/**
 * i like a lot this psvm method separated from public class to get the balls
 * rolling in a clean, effortless way. this way all the meaningfull methods are
 * isolated in the public class
 */
class MyRunnableTestDrive {

    public static void main(String[] args) {
        Runnable theJob = new MyRunnable();

        Thread t = new Thread(theJob);
        t.start();
        System.out.println("back in main");
    }
}
