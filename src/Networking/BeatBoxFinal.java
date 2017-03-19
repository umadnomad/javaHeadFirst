/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/*
 * @author nomad
 */
public class BeatBoxFinal {

    private static boolean playAuth = false;

    JFrame     theFrame;
    JPanel     mainPanel;
    JList      incomingList;
    JTextField userMessage;

    int                  nextNum;
    // we store checkboxes in an ArrayList
    ArrayList<JCheckBox> checkboxList;
    Vector<String> listVector = new Vector<String>();
    String             userName;
    ObjectOutputStream out;
    ObjectInputStream  in;
    HashMap<String, boolean[]> otherSeqsMap = new HashMap<String, boolean[]>();

    Sequencer sequencer;
    Sequence  sequence;
    Track     track;

    String[] instrumentNames = {
            "Bass Drum","Closed Hi-Hat","Open Hi-Hat","Acoustic Snare","Crash Cymbal","Hand Clap","High Tom","Hi Bongo",
            "Maracas","Whistle","Low Conga","Cowbell","Vibraslap","Low-mid Tom","High Agogo","Open Hi Conga"
    };

    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main(String[] args) {

		/* aggiungo un commento per mia mamma */
        try {
            new BeatBoxFinal().startUp(args[0]); // args[0] is your user ID/screen name
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("you must pass this program an argument in order to get it running");
            System.exit(0);
        }

    }

    public void startUp(String name) {
        userName = name;
        /* open connection to the server */
        try {
            Socket sock = new Socket("127.0.0.1",4242);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            Thread remote = new Thread(new RemoteReader());
            remote.start();
        } catch (Exception ex) {
            System.out.println("couldn't connect - you'll have to play alone.");
        }
        setUpMidi();
        buildGUI();
    } // close startUp

    public void buildGUI() {

        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();

		/* we pass to this JPanel constructor a BorderLayout layout as by default JPanels use FlowLayout */
        JPanel background = new JPanel(layout);

		/* an 'empty border' gives us a margin between the edges of the panel and where the components are placed */
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkboxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton serialize = new JButton("Save Pattern");
        serialize.addActionListener(new MySerializeListener());
        buttonBox.add(serialize);

        JButton load = new JButton("Load Pattern");
        load.addActionListener(new MyLoadListener());
        buttonBox.add(load);

        JButton sendIt = new JButton("Send It!");
        sendIt.addActionListener(new MySendListener());
        buttonBox.add(sendIt);

        userMessage = new JTextField();
        buttonBox.add(userMessage);

		/* JList is a component we haven't used before. This is where the incoming messages are displayed. Only
         * instead of a normal chat where you just LOOK at the messages, in this app you can SELECT a message from the
		 * list to load and play the attached beat pattern. */
        incomingList = new JList();
        incomingList.addListSelectionListener(new MyListSelectionListener());
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane theList = new JScrollPane(incomingList);
        buttonBox.add(theList);
        incomingList.setListData(listVector);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }

        background.add(BorderLayout.EAST,buttonBox);
        background.add(BorderLayout.WEST,nameBox);

        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(2);

        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER,mainPanel);

		/* make the checkboxes, set them to 'false' (so they aren't checked) and add them to the ArrayList AND to the
         *  GUI panel */
        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            c.addItemListener(new MyCheckBoxChangeListener());
            checkboxList.add(c);
            mainPanel.add(c);
        }

        playAuth = true;
        setUpMidi();

        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
    } // close buildGUI

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ,4);

			/* i believe I've never seen this way to create a track back in the book */
            track = sequence.createTrack();

            sequencer.setTempoInBPM(120);

        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    } // close setUpMidi

    public void buildTrackAndStart() {
        /* we'll make a 16-element array to hold the values for one instrument, across all 16 beats. if the
         * instrument is supposed to play on that beat, the value at that element will be the key. If that instrument
		 * is NOT supposed to play on that beat, put in a zero */
        ArrayList<Integer> trackList = null; /* this will hold the instruments */

		/* Get rid of old track, make a fresh one*/
        sequence.deleteTrack(track);
        track = sequence.createTrack();

		/* do this for each of the 16 ROWS (i.e. Bass, Congo, etc */
        for (int i = 0; i < 16; i++) {
            trackList = new ArrayList<Integer>();

            for (int j = 0; j < 16; j++) {
                /* is the checkbox at this beat selected? if yes, put the key value in this slot in the array (the
                 * slot that represents this beat). Otherwise, the instrument is NOT supposed to play at this beat, so
				 * set it to zero */
                JCheckBox jc = checkboxList.get(j + 16 * i);

                if (jc.isSelected()) {
                    /* set the 'key' that represents which instrument this is (Bass, Hi-Hat, etc. The instruments
                     * array holds
					 *  actual MIDI numbers for each instrument */
                    int key = instruments[i];
                    trackList.add(new Integer(key));
                } else {
                    trackList.add(null); /* because this slot should be empty in the track */
                }
            } // close inner loop

			/* for this instrument, and for all 16 beats, make events and add them to the track */
            makeTracks(trackList);
        } // close outer loop

		/* we always want to make sure that there IS an event at beat 16 (it goes 0 to 15). Otherwise, the BeatBox
         * might not go the full 16 beats before it starts over. */
        track.add(makeEvent(192,9,1,0,15));

        try {
            sequencer.setSequence(sequence);

			/* lets you specify the number of loop iterations, or in this case, continuous looping */
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);

			/* now play the thing */
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    } // close buildTrackAndStart method

    /*
     * this makes events for one instrument at a time, for all 16 beats. So it might get an int[] for the Bass drum,
     * and each index in the array will hold either the key of that instrument or a zero. If it's a zero, the
     * instrument isn't supposed to play at that beat. Otherwise, make an event and add it to the track
     */
    public void makeTracks(ArrayList list) {
        Iterator it = list.iterator();
        for (int i = 0; i < 16; i++) {
            Integer num = (Integer) it.next();
            if (num != null) {
                int numKey = num.intValue();
                /*
                 * make NOTE ON and NOTE OFF events, and add them to the track
				 */
                track.add(makeEvent(144,9,numKey,100,i));
                track.add(makeEvent(128,9,numKey,100,i + 1));
            }
        } // close loop
    } // close makeTracks()

    // this is the utility method from last chapter's CodeKitchen. nothing new
    public MidiEvent makeEvent(int comd,int chan,int one,int two,int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd,chan,one,two);
            event = new MidiEvent(a,tick);

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }

    public void changeSequence(boolean[] checkboxState) {

        for (int i = 0; i < 256; i++) {
            JCheckBox check = (JCheckBox) checkboxList.get(i);
            if (checkboxState[i]) {
                check.setSelected(true);
            } else {
                check.setSelected(false);
            }
        } // close loop
    } // close changeSequence

    private void save() {
        // Make a boolean array to hold the state of each checkbox
        boolean[] checkboxState = new boolean[256];

		/* Walk through the checkboxList (ArrayList of checkboxes), and get the state of each one and add it to
         * the boolean array */
        for (int i = 0; i < 256; i++) {
            JCheckBox check = (JCheckBox) checkboxList.get(i);
            if (check.isSelected()) {
                checkboxState[i] = true;
            }
        }

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(theFrame);

            FileOutputStream   fileStream = new FileOutputStream(fileChooser.getSelectedFile());
            ObjectOutputStream os         = new ObjectOutputStream(fileStream);
            os.writeObject(checkboxState);
        } catch (NullPointerException nullex) {
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void load() {
        boolean[] checkboxState = null;

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(theFrame);

            FileInputStream   fileStream = new FileInputStream(fileChooser.getSelectedFile());
            ObjectInputStream is         = new ObjectInputStream(fileStream);

			/* Read the single object in the file (the boolean array (remember, readObject() returns a reference
             * of type Object */
            checkboxState = (boolean[]) is.readObject();

			/* Now restore the state of each of the checkboxes in the ArrayList of actual JCheckBox objects
             * (checkboxList) */
            playAuth = false;
            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if (checkboxState[i]) {
                    check.setSelected(true);
                } else {
                    check.setSelected(false);
                }
            }

			/* Now stop whatever is currently playing, and rebuild the sequence using the new state of the
             * checkboxes in the ArrayList */
            playAuth = true;
            sequencer.stop();

        } catch (NullPointerException nullex) {
        } catch (IOException | ClassNotFoundException ioex) {
            ioex.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        String string = (String) obj;
        return super.equals(string.trim());
    }

    public class MyCheckBoxChangeListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if (playAuth) {
                sequencer.stop();
                buildTrackAndStart();
            }
        }

    }

    public class MyStartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndStart();
        }

    } // close inner class

    public class MyStopListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }

    } // close inner class

    /*
     * the tempo factor scales the sequencer's tempo by the factor provided. The default is 1.0, so we're adjusting +/-
     * 3% per click
     */
    public class MyUpTempoListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));
        }

    } // close inner class

    public class MyDownTempoListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * .97));
        }

    } // close inner class

    public class MySerializeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            save();
        }

    }

    public class MyLoadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            load();
        }

    }

    public class MySendListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if (check.isSelected()) {
                    checkboxState[i] = true;
                }
            } // close loop
            String messageToSend = null;
            try {
                out.writeObject(userName + " [" + nextNum++ + "]: " + userMessage.getText());
                out.writeObject(checkboxState);
            } catch (IOException e1) {
                System.out.println("Sorry dude. Could not send it to the server.");
            }
            userMessage.setText("");
        } // close actionPerformed
    } // close inner class

    public class MyListSelectionListener implements javax.swing.event.ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String selected = (String) incomingList.getSelectedValue();
                if (selected != null) {
                    String[] chatUserNick = selected.split(" ");
                    if (!chatUserNick[0].equals(userName)) {
                        if (JOptionPane.showConfirmDialog(null,"Do you want to save the current pattern?","Saving",
                                                          JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            save();
                        }
                    }
                    /* now go to the map, and change the sequence */
                    boolean[] selectedState = (boolean[]) otherSeqsMap.get(selected);
                    changeSequence(selectedState);
                    sequencer.stop();
                    buildTrackAndStart();
                }
            }
        } // close valueChanged
    } // close inner class

    public class RemoteReader implements Runnable {

        boolean[] checkboxState = null;
        String    nameToShow    = null;
        Object    obj           = null;

        @Override
        public void run() {
            try {
                while ((obj = in.readObject()) != null) {
                    System.out.println("got an object from server");
                    System.out.println(obj.getClass());
                    String nameToShow = (String) obj;
                    checkboxState = (boolean[]) in.readObject();
                    otherSeqsMap.put(nameToShow,checkboxState);
                    listVector.add(nameToShow);
                    incomingList.setListData(listVector);
                } // close while
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } // close run
    } // close inner class

} // close class
