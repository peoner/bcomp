/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MemoryView {
	private String name;
	private final int x;
	private final int y;
	private static final int width = 150;
	private static final int height = 431;
	private MemoryInterface mem;

	public MemoryView(String name, int x, int y, MemoryInterface mem) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.mem = mem;
	}

	public void paintComponent(Graphics2D rs) {
		// XXX: move new Color() to static final
		rs.setPaint(Color.BLACK);
		rs.fillRect(x, y, width, height);
		rs.setPaint(new Color(157, 189, 165));
		rs.fillRect(x + 2, y + 2, width - 4, 20);
		rs.fillRect(x + 2, y + 2*2 + 20, 20, height - 3*2 - 20);
		rs.setPaint(new Color(219, 249, 235));
		rs.fillRect(x + 30, y + 30, width - 2 - 30, height - 2 - 30);
	}
	
}
