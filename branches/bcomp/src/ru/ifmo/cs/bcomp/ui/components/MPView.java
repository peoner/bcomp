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
public class MPView extends JComponent {
	private CPU cpu;
	private ComponentManager cmanager;
	private MemoryView mem;

	public MPView(CPU bcpu, ComponentManager cmanager) {
		this.cpu = bcpu;
		this.cmanager = cmanager;
		mem = new MemoryView("Память МК", 711, 1, new MemoryInterface() {
			public int getValue(int addr) {
				return cpu.getMicroMemory(addr);
			}
		});
	}

    public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;

		cmanager.paintComponent(this, rs);
		mem.paintComponent(rs);
	}
}
