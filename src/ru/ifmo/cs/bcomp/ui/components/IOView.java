/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import ru.ifmo.cs.bcomp.CPU;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOView extends JComponent {
	private CPU cpu;
	private ComponentManager cmanager;

	public IOView(CPU cpu, ComponentManager cmanager) {
		this.cpu = cpu;
		this.cmanager = cmanager;
	}

	public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;

		cmanager.paintComponent(this, rs);
	}
}
