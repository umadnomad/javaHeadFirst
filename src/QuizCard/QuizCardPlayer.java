/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizCard;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author nomad
 */
public class QuizCardPlayer {

    private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        QuizCardPlayer reader = new QuizCardPlayer();
        reader.go();
    }

    public void go() {

        // build gui
        frame = new JFrame("Quiz Card Plater");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        // start making a scrollable jtextarea
        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // end making a scrollable jtextarea

        nextButton = new JButton("Show Question");
        nextButton.addActionListener(new NextCardListener());

        mainPanel.add(qScroller);
        mainPanel.add(nextButton);

        // start menu section
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(new OpenMenuListener());

        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        // end menu section

        // start frame setup
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);
    } // end of go method

    public class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (isShowAnswer) {
                // show the answer because they've seen the question
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next Card");
                isShowAnswer = false;
            } else {
                // show the next question
                if (currentCardIndex < cardList.size()) {
                    
                    showNextCard();
                    
                } else {
                    // there are no more cards!
                    display.setText("That was last card");
                    nextButton.setEnabled(true);
                }
            }
        } // end of actionperformed method
        
    } // end of nextcardlistener
    
    public class OpenMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
        
    }
    
    private void loadFile(File file) {
        
        cardList = new ArrayList<QuizCard>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();
            
        } catch (IOException e) {
            System.out.println("couldn't reader the card file");
            e.printStackTrace();
        }
        
        // now time to start by showing the first card
        showNextCard();
        
    }
    
    private void makeCard(String lineToParse) {
        
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("make a card");
    }
    
    private void showNextCard() {
        
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;
    }
} // end of class
