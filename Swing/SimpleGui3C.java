/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author nomad
 */
public class SimpleGui3C implements ActionListener {

    JFrame frame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Change colors");
        button.addActionListener(this);

        MyDrawPanel drawPanel = new MyDrawPanel();

        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(300, 300);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.repaint();
    }

    private static class MyDrawPanel extends JPanel {

        public MyDrawPanel() {
        }

        ;
        
        @Override
        public void paintComponent(Graphics g) {

        }
    }
    
    
    
}
