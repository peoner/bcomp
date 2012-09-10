/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOView extends JComponent {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;

	public IOView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();
	}

	public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;

		cmanager.paintComponent(this, rs);
	}
}
