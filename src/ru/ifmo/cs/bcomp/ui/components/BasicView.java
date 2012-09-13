/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.*;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class BasicView extends JComponent {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;

	public BasicView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();
	}

	@Override
	public void paintComponent(Graphics g) {

        Graphics2D rs = (Graphics2D) g;

		//cmanager.paintComponent(this, rs);
	}
}
