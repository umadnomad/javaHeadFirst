/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Swing;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import java.util.*;
import javax.sound.midi.*;

/**
 *
 * @author nomad
 */
public class BeatBox {

    private static boolean playAuth = false;
    JPanel mainPanel;

    // we store checkboxes in an ArrayList
    ArrayList<JCheckBox> checkboxList;

    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame frame;

    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat",
        "Acoustic Snare", "Crash Cymbal", "Hand Clap",
        "High Tom", "Hi Bongo", "Maracas", "Whistle",
        "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom",
        "High Agogo", "Open Hi Conga"};

    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    public static void main(String[] args) {

        // aggiungo un commento per mia mamma
        new BeatBox().buildGUI();
    }

    public void buildGUI() {

        frame = new JFrame("Cyber BeatBox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();

        /**
         * we pass to this JPanel constructor a BorderLayout layout as by
         * default JPanels use FlowLayout
         */
        JPanel background = new JPanel(layout);

        /**
         * an 'empty border' gives us a margin between the edges of the panel
         * and where the components are placed
         */
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        frame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2);

        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        /**
         * make the checkboxes, set them to 'false' (so they aren't checked) and
         * add them to the ArrayList AND to the GUI panel
         */
        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            c.addItemListener(new MyCheckBoxChangeListener());
            checkboxList.add(c);
            mainPanel.add(c);
        }

        playAuth = true;
        setUpMidi();

        frame.setBounds(50, 50, 300, 300);
        frame.pack();
        frame.setVisible(true);
    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);

            /**
             * i believe I've never seen this way to create a track back in the
             * book
             */
            track = sequence.createTrack();

            sequencer.setTempoInBPM(120);

        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart() {
        /**
         * we'll make a 16-element array to hold the values for one instrument,
         * across all 16 beats. if the instrument is supposed to play on that
         * beat, the value at that element will be the key. If that instrument
         * is NOT supposed to play on that beat, put in a zero
         */
        int[] trackList = null;

        // Get rid of old track, make a fresh one
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        // do this for each of the 16 ROWS (i.e. Bass, Congo, etc
        for (int i = 0; i < 16; i++) {
            trackList = new int[16];

            /**
             * set the 'key' that represents which instrument this is (Bass,
             * Hi-Hat, etc. The instruments array holds actual MIDI numbers for
             * each instrument
             */
            int key = instruments[i];

            for (int j = 0; j < 16; j++) {

                /**
                 * is the checkbox at this beat selected? if yes, put the key
                 * value in this slot in the array (the slot that represents
                 * this beat). Otherwise, the instrument is NOT supposed to play
                 * at this beat, so set it to zero
                 */
                JCheckBox jc = checkboxList.get(j + 16 * i);
                if (jc.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }

            } // close inner loop

            /**
             * for this instrument, and for all 16 beats, make events and add
             * them to the track
             */
            makeTracks(trackList);
            track.add(makeEvent(176, 1, 127, 0, 16));

        } // close outer loop

        /**
         * we always want to make sure that there IS an event at beat 16 (it
         * goes 0 to 15). Otherwise, the BeatBox might not go the full 16 beats
         * before it starts over.
         */
        track.add(makeEvent(192, 9, 1, 0, 15));

        try {
            sequencer.setSequence(sequence);

            /**
             * lets you specify the number of loop iterations, or in this case,
             * continuous looping
             */
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);

            /**
             * now play the thing
             */
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    } // close buildTrackAndStart method

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

    /**
     * the tempo factor scales the sequencer's tempo by the factor provided. The
     * default is 1.0, so we're adjusting +/- 3% per click
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

            // Make a boolean array to hold the state of each checkbox
            boolean[] checkboxState = new boolean[256];

            /**
             * Walk through the checkboxList (ArrayList of checkboxes), and get
             * the state of each one and add it to the boolean array
             */
            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if (check.isSelected()) {
                    checkboxState[i] = true;
                }
            }

            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(frame);

                FileOutputStream fileStream = new FileOutputStream(fileChooser.getSelectedFile());
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(checkboxState);
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

    }

    public class MyLoadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxState = null;

            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(frame);

                FileInputStream fileStream = new FileInputStream(fileChooser.getSelectedFile());
                ObjectInputStream is = new ObjectInputStream(fileStream);

                /**
                 * Read the single object in the file (the boolean array
                 * (remember, readObject() returns a reference of type Object
                 */
                checkboxState = (boolean[]) is.readObject();

                /**
                 * Now restore the state of each of the checkboxes in the
                 * ArrayList of actual JCheckBox objects (checkboxList)
                 */
                playAuth = false;
                for (int i = 0; i < 256; i++) {
                    JCheckBox check = (JCheckBox) checkboxList.get(i);
                    if (checkboxState[i]) {
                        check.setSelected(true);
                    } else {
                        check.setSelected(false);
                    }
                }

                /**
                 * Now stop whatever is currently playing, and rebuild the
                 * sequence using the new state of the checkboxes in the
                 * ArrayList
                 */
                
                playAuth = true;
                sequencer.stop();

            } catch (IOException | ClassNotFoundException ioex) {
                ioex.printStackTrace();
            }
        }

    }

    /*
             * this makes events for one instrument at a time, for all 16 beats.
             * So it might get an int[] for the Bass drum, and each index in the
             * array will hold either the key of that instrument or a zero. If
             * it's a zero, the instrument isn't supposed to play at that beat.
             * Otherwise, make an event and add it to the track
             *
             * @param list
     */
    public void makeTracks(int[] list) {

        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                /**
                 * make NOTE ON and NOTE OFF events, and add them to the track
                 */
                track.add(makeEvent(144, 9, key, 100, i));
                track.add(makeEvent(128, 9, key, 100, i + 1));
            }
        }
    }

    // this is the utility method from last chapter's CodeKitchen. nothing new
    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }

} // close class
