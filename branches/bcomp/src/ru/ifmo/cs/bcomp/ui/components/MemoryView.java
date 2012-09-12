/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class MemoryView extends JComponent {
	private final static int titleHeight = 28;

	private String name;
	private int width;
	private int height;
	private MemoryInterface mem;
	private int addrBitWidth;
	private int titleWidth;
	private int addrWidth;
	private int valueWidth;
	private int titleY = titleHeight - 6;
	private int titleX;
	private int valueX;
	private int valueY;
	private int addrStrX;
	private int valueStrX;
	private int valueHeight;
	private JLabel title;
	private int addrLast = 0;

	public MemoryView(String name, int x, int y, MemoryInterface mem) {
		this.name = name;
		this.mem = mem;

		addrBitWidth = (mem.getWidth() + 3) >> 2;

		addrWidth = ComponentManager.FONT_COURIER_BOLD_25_WIDTH * (1 + addrBitWidth);
		valueWidth = ComponentManager.FONT_COURIER_BOLD_25_WIDTH * (1 + 4);
		valueX = 2 + addrWidth;
		valueY = 2 + titleHeight;
		valueHeight = 25 * 16;
		width = 3 + ComponentManager.FONT_COURIER_BOLD_25_WIDTH * (2 + 4 + addrBitWidth);
		height = 3 + titleHeight + valueHeight;
		addrStrX = 1 + (ComponentManager.FONT_COURIER_BOLD_25_WIDTH >> 1);
		valueStrX = valueX + (ComponentManager.FONT_COURIER_BOLD_25_WIDTH >> 1);
		titleX = (width - name.length() * ComponentManager.FONT_COURIER_BOLD_23_WIDTH) >> 1;
		titleWidth = width - 2;

		setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(ComponentManager.COLOR_MEM_BGADDR);
		rs.fillRect(1, 1, titleWidth, titleHeight);
		rs.fillRect(1, valueY, addrWidth, valueHeight);
		rs.setPaint(ComponentManager.COLOR_MEM_BGVALUE);
		rs.fillRect(valueX, valueY, valueWidth, valueHeight);

		rs.setPaint(Color.BLACK);
		rs.drawRect(0, 0, width - 1, height - 1);
		rs.drawLine(1, titleHeight + 1, width - 2, titleHeight + 1);
		rs.drawLine(valueX - 1, titleHeight + 2, valueX - 1, height - 2);

		rs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rs.setFont(ComponentManager.FONT_COURIER_BOLD_23);
		rs.drawString(name, titleX, titleY);

		rs.setFont(ComponentManager.FONT_COURIER_BOLD_25);
		for (int i = 0; i < 16; i++) {
			rs.drawString(ComponentManager.toHex(addrLast + i, addrBitWidth), addrStrX, valueY + 20 + 25 * i);
			rs.drawString(ComponentManager.toHex(mem.getValue(addrLast + i), 4), valueStrX, valueY + 20 + 25 * i);
		}
	}

	public void tmp() {
		//addrs[0].setText(ComponentManager.toHex(++lastaddr, addrwidth));
	}
}
