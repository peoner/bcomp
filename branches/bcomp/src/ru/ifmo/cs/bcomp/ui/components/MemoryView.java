/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.Memory;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class MemoryView extends JComponent {
	private final static int titleHeight = 28;

	private int width;
	private int height;
	private Memory mem;
	private int addrBitWidth;
	private int valueBitWidth;
	private int lineX;
	private int addrLast = 0;
	// Components
	private JLabel[] addrs = new JLabel[16];
	private JLabel[] values = new JLabel[16];

	public MemoryView(Memory mem, String name, int x, int y) {
		this.mem = mem;

		addrBitWidth = ComponentManager.getHexWidth(mem.getAddrWidth());
		int addrWidth = FONT_COURIER_BOLD_25_WIDTH * (1 + addrBitWidth);
		valueBitWidth = ComponentManager.getHexWidth(mem.getWidth());
		int valueWidth = FONT_COURIER_BOLD_25_WIDTH * (1 + valueBitWidth);
		lineX = 1 + addrWidth;

		width = 3 + addrWidth + valueWidth;
		height = 3 + titleHeight + 16 * 25;
		setBounds(x, y, width, height);

		JLabel title = new JLabel(name, JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_21);
		title.setBounds(1, 1, width - 2, titleHeight);
		title.setBackground(COLOR_MEM_TITLE);
		title.setOpaque(true);
		add(title);

		for (int i = 0; i < 16; i++) {
			addrs[i] = new JLabel(ComponentManager.toHex(addrLast + i, addrBitWidth), JLabel.CENTER);
			addrs[i].setFont(FONT_COURIER_BOLD_25);
			addrs[i].setBounds(1, 2 + titleHeight + 25 * i, addrWidth, 25);
			addrs[i].setBackground(COLOR_MEM_TITLE);
			addrs[i].setOpaque(true);
			add(addrs[i]);

			values[i] = new JLabel(ComponentManager.toHex(
				mem.getValue(addrLast + i), valueBitWidth), JLabel.CENTER);
			values[i].setFont(FONT_COURIER_BOLD_25);
			values[i].setBounds(lineX + 1, 2 + titleHeight + 25 * i, valueWidth, 25);
			values[i].setBackground(COLOR_MEM_VALUE);
			values[i].setOpaque(true);
			add(values[i]);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(Color.BLACK);
		rs.drawRect(0, 0, width - 1, height - 1);
		rs.drawLine(1, titleHeight + 1, width - 2, titleHeight + 1);
		rs.drawLine(lineX, titleHeight + 2, lineX, height - 2);
	}

	public void tmp() {
		//addrs[0].setText(ComponentManager.toHex(++lastaddr, addrwidth));
	}
}
