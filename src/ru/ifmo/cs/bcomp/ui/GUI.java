/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class GUI extends JApplet {
	public void init() {
		getContentPane().add(new JLabel("Applet!"));
	}

	public static void main(String[] args) {
		GUI applet = new GUI();
		JFrame frame = new JFrame("БЭВМ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(applet);
		frame.setSize(852, 586);
		applet.init();
		applet.start();
		frame.setVisible(true);
	}
}
