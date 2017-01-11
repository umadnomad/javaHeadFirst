/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author nomad
 */
public class SeventytwoVirgosLoad {

    private JFrame frame;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JButton loadButton;
    private static final Font BIG_FONT = new Font("sanserif", Font.BOLD, 15);

    public static void main(String[] args) {
        SeventytwoVirgosLoad seventytwovirgosload = new SeventytwoVirgosLoad();
        seventytwovirgosload.go();
    }

    public void go() {

        // build gui
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        frame = new JFrame("Random Girlie Generation Load Software");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);

        loadButton = new JButton("Load Virgos File");
        loadButton.addActionListener(new loadButtonActionListener());

        frame.getContentPane().add(BorderLayout.SOUTH, loadButton);

        frame.setSize(500, 800);
        frame.setVisible(true);
    }

    class loadButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser fileLoader = new JFileChooser();
            fileLoader.showSaveDialog(frame);
            readFile(fileLoader.getSelectedFile());
        }

    }

    private void readFile(File file) {

        try {
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                panel.add(new JLabel(line));
            }
            panel.revalidate();
            frame.validate();
        } catch (IOException ex) {
            System.out.println("major failure while writing to file");
        }

    }
}
