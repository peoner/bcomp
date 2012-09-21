/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import ru.ifmo.cs.bcomp.ui.Utils;
import ru.ifmo.cs.elements.Register;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class InputRegisterView extends RegisterView {
	private Register reg;
	private boolean active = false;
	private int regWidth;
	private int bitno;
	private int formattedWidth;

	public InputRegisterView(Register reg) {
		super(reg, COLOR_INPUT_TITLE);
		this.reg = reg;
		bitno = (regWidth = reg.getWidth()) - 1;
		formattedWidth = Utils.getBinaryWidth(regWidth);
	}

	public void setActive(boolean active) {
		this.active = active;
		setValue();
		repaint();
	}

	public void moveLeft() {
		bitno = (bitno + 1) % regWidth;
		setValue();
	}

	public void moveRight() {
		bitno = (bitno == 0 ? regWidth : bitno) - 1;
		setValue();
	}

	public void invertBit() {
		reg.invertBit(bitno);
		setValue();
	}

	public void setBit(int value) {
		reg.setValue(value, bitno);
		moveRight();
	}

	@Override
	public void setValue() {
		if (active) {
			StringBuilder str = new StringBuilder("<html>" +
				Utils.toBinary(reg.getValue(), regWidth) + "</html>");

			int pos = 6 + formattedWidth - Utils.getBinaryWidth(bitno + 1);
			str.insert(pos + 1, "</font>");
			str.insert(pos, "<font color=\"#FF0000\">");
			setValue(str.toString());
		} else
			super.setValue();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (active) {
			drawBorder(g, Color.RED);
		} else
			super.paintComponent(g);
	}
}
