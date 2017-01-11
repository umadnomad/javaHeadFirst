/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author nomad
 */
public class ArrayListPlay {

    private static final String[] NAMES = {"Jessica", "Marika", "Erika", "Viktoria"};
    private static final byte PPL_TO_GENERATE = 72;
    private JFrame frame;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JLabel[] labels;
    private JButton eraseLabel;
    private static final Font BIG_FONT = new Font("sanserif", Font.BOLD, 15);

    public static void main(String[] args) {
        ArrayListPlay arraylistplay = new ArrayListPlay();
        arraylistplay.go();
    }

    public void go() {

        // build gui
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        frame = new JFrame("Random Girlie Generation Software");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        labels = new JLabel[PPL_TO_GENERATE];

        for (int i = 0; i < PPL_TO_GENERATE; i++) {
            int rand = (int) (Math.random() * 4);

            MrPerson genPerson = new MrPerson(NAMES[rand], (byte) (Math.random() * 26), 'f');
            labels[i] = new JLabel(genPerson.getName() + ", " + genPerson.getAge() + " years old, " + genPerson.getSex() + ".");
            labels[i].setFont(BIG_FONT);
            panel.add(labels[i]);
        }

        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);

        eraseLabel = new JButton("Save Virgos to File");
        eraseLabel.addActionListener(new saveButtonActionListener());

        frame.getContentPane().add(BorderLayout.SOUTH, eraseLabel);

        frame.setSize(500, 800);
        frame.setVisible(true);
    }

    class saveButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser fileSaver = new JFileChooser();
            fileSaver.showSaveDialog(frame);
            saveFile(fileSaver.getSelectedFile());
        }

    }

    private void saveFile(File file) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < PPL_TO_GENERATE; i++) {
                writer.write(labels[i].getText());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("major failure while writing to file");
        }

    }
}
