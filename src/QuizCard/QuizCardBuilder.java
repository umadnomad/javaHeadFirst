/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizCard;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author nomad
 */
public class QuizCardBuilder {

    private JFrame frame;
    private ArrayList<QuizCard> cardList;
    private JTextArea question;
    private JTextArea answer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.go();
    }

    public void go() {
        // build gui

        frame = new JFrame("Quiz Card Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        /**
         * creating a JTextArea
         */
        question = new JTextArea(6, 20);    // 6 rows, 20 columns
        question.setLineWrap(true);         // enabling word wrap

        /**
         * Sets the style of wrapping used if the text area is wrapping lines.
         * If set to true the lines will be wrapped at word boundaries
         * (whitespace) if they are too long to fit within the allocated width.
         * If set to false, the lines will be wrapped at character boundaries.
         * By default this property is false.
         */
        question.setWrapStyleWord(true);
        question.setFont(bigFont);          // setting JTextArea font

        /**
         * Creating a scroller for question JTextArea, without a scroller guess
         * what... you won't be able to scroll
         */
        JScrollPane qScroller = new JScrollPane(question);

        // Setting scroller's policy
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Another JTextArea, all the same as above
        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);

        // Scroller's policy as above
        JScrollPane aScroller = new JScrollPane(answer);
        aScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        aScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Make a button
        JButton nextButton = new JButton("Next Card");

        // Make me a QuizCard type ArrayList
        cardList = new ArrayList<QuizCard>();

        JLabel qLabel = new JLabel("Question:");    // Simple JLabels
        JLabel aLabel = new JLabel("Answer:");      // Simple JLabels

        /**
         * This adding which I don't understand... weren't panels born with a
         * FlowLayout? shouldn't i change it to BoxLayout to make all pretty?
         */
        mainPanel.add(qLabel);      // Add the label which says "Question:" to the Panel
        mainPanel.add(qScroller);   // Add the Scroller Obj which contains the now scrollable JTextArea question Obj
        mainPanel.add(aLabel);      // Add the label which says "Answer:" to the Panel
        mainPanel.add(aScroller);   // Add the Scroller Obj which contains the now scrollable JTextArea answer Obj
        mainPanel.add(nextButton);  // add the button

        nextButton.addActionListener(new NextCardListener()); // EventListener for next button

        JMenuBar menuBar = new JMenuBar();              // Okay this is the whole MENUBAR

        JMenu fileMenu = new JMenu("File");             // Make a MENU entry

        JMenuItem newMenuItem = new JMenuItem("New");   // Make content for a MENU entry (when you click/hover on it
        JMenuItem saveMenuItem = new JMenuItem("Save"); // Make content for a MENU entry (when you click/hover on it

        newMenuItem.addActionListener(new NewMenuListener());   // EventListener as always otherwise they do nothing
        saveMenuItem.addActionListener(new SaveMenuListener()); // EventListener as always otherwise they do nothing

        fileMenu.add(newMenuItem);  // ADD the New  SUB-MENU ENTRY TO the File MENU ENTRY
        fileMenu.add(saveMenuItem); // ADD the Save SUB-MENU ENTRY TO the File MENU ENTRY

        menuBar.add(fileMenu);      // ADD the File MENU ENTRY TO the MENU

        frame.setJMenuBar(menuBar); // Give this window a menubar

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel); // Slap the previously built panel on the frame, std borderlayout

        frame.setSize(500, 600);
        frame.setVisible(true);

    }

    public class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);
            clearCard();
        }

    } // end of listener

    public class SaveMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);

            /**
             * Brings up a file dialog box and waits on this line until the user
             * chooses 'Save' frame the dialog box. All the file dialog
             * navigation and selecting a file etc is done for you by the
             * JFileChooser.
             */
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());
        }

    } // end of listener

    public class NewMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            cardList.clear();
            clearCard();
        }

    } // end of listener

    private void clearCard() {

        question.setText("");
        answer.setText("");
        question.requestFocus();

    } // end of clearCard method

    /**
     *
     * @param file is the File object the user is saving
     */
    private void saveFile(File file) {
        try {
            BufferedWriter writer;

            /**
             * We chain a BufferedWriter on to a new FileWriter to make writing
             * more efficient. (The book will explain that in a few pages
             */
            writer = new BufferedWriter(new FileWriter(file));

            /**
             * Walk through the ArrayList of cards and write them out, one card
             * per line, with the question answer separated by a "/", and then
             * add a newline character
             */
            for (QuizCard card : cardList) {
                writer.write(String.format("%/", card.getQuestion()));
                writer.write(String.format("%\n", card.getAnswer()));
            }
            writer.close();

        } catch (IOException ex) {
            System.out.println("couldn't write the cardList out");
            ex.printStackTrace();
        }
    } // end of saveFile method

} // end of class
