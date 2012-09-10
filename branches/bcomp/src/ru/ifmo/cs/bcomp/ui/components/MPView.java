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
public class MPView extends JComponent {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private MemoryView mem;

	public MPView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();
		mem = new MemoryView("Память МК", 711, 1, new MemoryInterface() {
			public int getValue(int addr) {
				return cpu.getMicroMemory(addr);
			}

			public int getWidth() {
				return cpu.getRegWidth(CPU.Regs.MIP);
			}
		});
	}

    public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;

		cmanager.paintComponent(this, rs);
		mem.paintComponent(this, rs);
	}
}
