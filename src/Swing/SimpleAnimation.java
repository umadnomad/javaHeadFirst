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
public class SimpleAnimation {
    
    JFrame frame;
    JButton animateButton;
    MyDrawPanel2 drawPanel;

    int x = 70;
    int y = 70;

    public static void main(String[] args) {

        SimpleAnimation gui = new SimpleAnimation();
        gui.go();

    }

    public void go() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        animateButton = new JButton("Animate this circle");
        animateButton.addActionListener(new animateButton());
        
        drawPanel = new MyDrawPanel2();

        frame.getContentPane().add(BorderLayout.SOUTH, animateButton);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(300, 300);
        frame.setVisible(true);

    }

    class MyDrawPanel2 extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            g.setColor(Color.green);
            g.fillOval(x, y, 40, 40);

        }
    }
    
    class animateButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            animateButton.setEnabled(false);
            for (int i = 0; i < 130; i++) {

                x++;
                y++;

                drawPanel.repaint();
                
                /*  painstackingly animation effect is not working if I make use
                    of a button
                
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                    }
                */
            }
        }
    }
    
}
