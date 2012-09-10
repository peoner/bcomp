/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MemoryView {
	private final static int sepSize = 2;
	private final static int titleHeight = 28;

	private String name;
	private int x;
	private int y;
	private int width;
	private int height;
	private int fontCourier25Width;
	private MemoryInterface mem;
	private int addrwidth;
	private JLabel title;
	private int lastaddr = 0;
	private JLabel[] addrs = new JLabel[16];
	private JLabel[] values = new JLabel[16];

	public MemoryView(String name, int x, int y, MemoryInterface mem) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.mem = mem;

		fontCourier25Width = (int)ComponentManager.FONT_COURIER_BOLD_25.getStringBounds("0",
			new FontRenderContext(null, true, true)).getWidth();

		addrwidth = (mem.getWidth() + 3) >> 2;
		width = 3 * sepSize + fontCourier25Width * (2 + 4 + addrwidth);
		height = 3 * sepSize + titleHeight + 16 * 25;

		title = new JLabel(name, JLabel.CENTER);
		title.setFont(ComponentManager.FONT_COURIER_BOLD_23);
		title.setBounds(x + sepSize, y + sepSize, width - 2 * sepSize, titleHeight);
		title.setBackground(ComponentManager.COLOR_MEM_BGADDR);
		title.setOpaque(true);
		//title.setBorder(BorderFactory.createLineBorder(Color.black, 2));

		for (int i = 0; i < 16; i++) {
			addrs[i] = new JLabel(ComponentManager.toHex(lastaddr + i, addrwidth), JLabel.CENTER);
			addrs[i].setFont(ComponentManager.FONT_COURIER_BOLD_25);
			addrs[i].setBounds(x + sepSize, y + 2 * sepSize + titleHeight + 25 * i,
					fontCourier25Width * (addrwidth + 1), 25);
			addrs[i].setBackground(ComponentManager.COLOR_MEM_BGADDR);
			addrs[i].setOpaque(true);

			values[i] = new JLabel(ComponentManager.toHex(mem.getValue(lastaddr + i), 4), JLabel.CENTER);
			values[i].setFont(ComponentManager.FONT_COURIER_BOLD_25);
			values[i].setBounds(x + 2 * sepSize + fontCourier25Width * (addrwidth + 1),
					y + 2 * sepSize + titleHeight + 25 * i,
					fontCourier25Width * (4 + 1), 25);
			values[i].setBackground(ComponentManager.COLOR_MEM_BGVALUE);
			values[i].setOpaque(true);
		}
	}

	public void paintComponent(JComponent component, Graphics2D rs) {
		rs.setPaint(Color.BLACK);
		rs.fillRect(x, y, width, height);
		component.add(title);
		for (int i = 0; i < 16; i++) {
			component.add(addrs[i]);
			component.add(values[i]);
		}
		//rs.setPaint(ComponentManager.COLOR_MEM_BGADDR);
		//rs.fillRect(x + lineWidth, y + lineWidth, width - 2 * lineWidth, titleHeight);
		//rs.fillRect(x + lineWidth, y + 2 * lineWidth + titleHeight, addrWidth, height - 3 * lineWidth - titleHeight);
		//rs.setPaint(ComponentManager.COLOR_MEM_BGVALUE);
		//rs.fillRect(x + 30, y + 30, width - 2 - 30, height - 2 - 30);
	}

	public void tmp() {
		addrs[0].setText(ComponentManager.toHex(++lastaddr, addrwidth));
	}
}
