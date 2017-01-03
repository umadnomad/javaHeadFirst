/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author nomad
 */
public class TwoButtonGui {

    JFrame frame;
    JLabel label;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TwoButtonGui gui = new TwoButtonGui();
        gui.go();

    }

    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton labelButton = new JButton("Change label");
        JButton colorButton = new JButton("Change colors");
        
        labelButton.addActionListener(new LabelListener());
        colorButton.addActionListener(new ColorListener());
        
        MyDrawPanel drawPanel = new MyDrawPanel();
        label = new JLabel("I'm a label");

        frame.getContentPane().add(BorderLayout.SOUTH, colorButton);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.getContentPane().add(BorderLayout.WEST, label);
        frame.getContentPane().add(BorderLayout.EAST, labelButton);
        
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }
    
    class ColorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.repaint();
        }
        
    }

    class LabelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            label.setText("Ouch!");
        }
        
    }
}
