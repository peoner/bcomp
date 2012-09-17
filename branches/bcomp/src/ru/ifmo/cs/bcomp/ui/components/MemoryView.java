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
	//private final static int cellHeight = 25;

	private int width;
	private int height;
	private Memory mem;
	private int addrBitWidth;
	private int valueBitWidth;
	private int lineX;
	private int lastPage = 0;
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
		height = 3 + CELL_HEIGHT + 16 * CELL_HEIGHT;
		setBounds(x, y, width, height);

		JLabel title = new JLabel(name, JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_21);
		title.setBounds(1, 1, width - 2, CELL_HEIGHT);
		title.setBackground(COLOR_MEM_TITLE);
		title.setOpaque(true);
		add(title);

		for (int i = 0; i < 16; i++) {
			addrs[i] = new JLabel("", JLabel.CENTER);
			addrs[i].setFont(FONT_COURIER_BOLD_25);
			addrs[i].setBounds(1, 2 + CELL_HEIGHT * (i + 1), addrWidth, CELL_HEIGHT);
			addrs[i].setBackground(COLOR_MEM_TITLE);
			addrs[i].setOpaque(true);
			add(addrs[i]);

			values[i] = new JLabel("", JLabel.CENTER);
			values[i].setFont(FONT_COURIER_BOLD_25);
			values[i].setBounds(lineX + 1, 2 + CELL_HEIGHT * (i + 1), valueWidth, CELL_HEIGHT);
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
		rs.drawLine(1, CELL_HEIGHT + 1, width - 2, CELL_HEIGHT + 1);
		rs.drawLine(lineX, CELL_HEIGHT + 2, lineX, height - 2);
	}

	private void updateValue(int offset) {
		values[offset].setText(ComponentManager.toHex(
			mem.getValue(lastPage + offset), valueBitWidth));
	}

	public void updateMemory() {
		for (int i = 0; i < 16; i++) {
			addrs[i].setText(ComponentManager.toHex(lastPage + i, addrBitWidth));
			updateValue(i);
		}
	}

	private int getPage(int addr) {
		return addr & (~0xf);
	}

	private int getPage() {
		return getPage(mem.getAddrValue());
	}

	public void updateLastAddr() {
		lastPage = getPage();
	}

	public void eventRead() {
		int addr = getPage();

		if (addr != lastPage) {
			lastPage = addr;
			updateMemory();
		}
	}

	public void eventWrite() {
		int addr = mem.getAddrValue();
		int page = getPage(addr);

		if (page != lastPage) {
			lastPage = page;
			updateMemory();
		} else
			updateValue(addr - page);
	}
}
