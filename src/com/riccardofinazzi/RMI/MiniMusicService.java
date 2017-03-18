/**
 * Created by nomad on 16/03/17.
 */
package com.riccardofinazzi.RMI;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiniMusicService implements Service {

	MyDrawPanel myPanel;

	@Override
	public JPanel getGuiPanel() {
		JPanel mainPanel = new JPanel();
		myPanel = new MyDrawPanel();
		JButton playItButton = new JButton("Play it!");
		playItButton.addActionListener(new PlayItListener());
		mainPanel.add(myPanel);
		mainPanel.add(playItButton);
		return mainPanel;
	}

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

	public class PlayItListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				Sequencer sequencer = MidiSystem.getSequencer();
				sequencer.open();

				sequencer.addControllerEventListener(myPanel, new int[]{127});
				Sequence seq = new Sequence(Sequence.PPQ, 4);

				Track track = seq.createTrack();

				for (int i = 0; i < 100; i += 4) {

					int rNum = (int) ((Math.random() * 50) + 1);
					if (rNum < 38) {
						track.add(makeEvent(144, 1, rNum, 100, i));
						track.add(makeEvent(176, 1, 127, 0, i));
						track.add(makeEvent(128, 1, rNum, 100, i + 2));
					}
				} // end loop

				sequencer.setSequence(seq);
				sequencer.start();
				sequencer.setTempoInBPM(220);

			} catch (MidiUnavailableException | InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		} // close actionperformed
	} // close inner class

	class MyDrawPanel extends JPanel implements ControllerEventListener {

		// only if we got an event do we want to repaint
		boolean msg = false;

		public void controlChange(ShortMessage event) {
			msg = true;
			repaint();
		}

		public Dimension getPreferredSize() {
			return new Dimension(300, 300);
		}

		public void paintComponent(Graphics g) {
			if (msg) {
				/**
				 * no need to make a Graphics2D reference variable out of a Graphics2D Graphics cast. The methods you
				 * can call on a Graphics2D reference are: fill3DRect(), draw3DRect(), rotate(), scale(), shear(),
				 * transform (), setRenderingHints() and we are using noone of this.add() just plain Graphics'
				 * fillRect() method
				 */
				//Graphics2D g2 = (Graphics2D) g;

				int r  = (int) (Math.random() * 250);
				int gr = (int) (Math.random() * 250);
				int b  = (int) (Math.random() * 250);

				g.setColor(new Color(r, gr, b));

				int ht    = (int) ((Math.random() * 120) + 10);
				int width = (int) ((Math.random() * 120) + 10);

				int x = (int) ((Math.random() * 40) + 10);
				int y = (int) ((Math.random() * 40) + 10);

				g.fillRect(x, y, ht, width);
				msg = false;

			} // close if
		} // close method
	} // close inner class
} // close class

