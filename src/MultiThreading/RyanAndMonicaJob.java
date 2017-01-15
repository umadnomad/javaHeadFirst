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
class BankAccount {

    private int balance = 100;

    /**
     * Get the value of balance
     *
     * @return the value of balance
     */
    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        balance = balance - amount;
    }

}

public class RyanAndMonicaJob implements Runnable {

    /**
     * there will be only ONE instance of the RyanAndMonicaJob. That means only
     * ONE instance of the bank account. Both threads will access this one
     * account
     */
    BankAccount account = new BankAccount();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        RyanAndMonicaJob theJob = new RyanAndMonicaJob();   // istantiate runnable job

        /**
         * make two threads, giving each thread the same Runnable job. That
         * means both threads will be accessing the account instance variable in
         * the Runnable class
         */
        Thread one = new Thread(theJob);
        Thread two = new Thread(theJob);
        one.setName("Ryan");
        two.setName("Monica");
        one.start();
        two.start();
    }

    @Override
    public void run() {

        /**
         * in the run() method, a thread loops through and tries to make a
         * withdrawal with each iteration. After the withdrawal, it checks the
         * balance once again to see if the account is overdrawn
         */
        for (int i = 0; i < 10; i++) {
            makeWithdrawal(10);
            if (account.getBalance() < 0) {
                System.out.println("Overdrawn!");
            }
        }
    }

    private synchronized void makeWithdrawal(int amount) {

        /**
         * check the account balance, and if there's not enough money, we just
         * print a message. If there IS enough, we go to sleep, then wake up and
         * complete the withdrawal, just like Ryan did
         */
        if (account.getBalance() >= amount) {
            System.out.println(Thread.currentThread().getName() + " is about to withdraw");
            try {
                System.out.println(Thread.currentThread().getName() + " is going to sleep");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " woke up.");
            account.withdraw(amount);
            System.out.println(Thread.currentThread().getName() + " completes the withdrawal");
        } else {
            System.out.println("Sorry, not enough for " + Thread.currentThread().getName());
        }
    }

}
