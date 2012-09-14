/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.elements.Register;

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
		super(reg);
		this.reg = reg;
		bitno = (regWidth = reg.getWidth()) - 1;
		formattedWidth = ComponentManager.getBinWidth(regWidth);
	}

	public void setActive(boolean active) {
		this.active = active;
		setValue();
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
				ComponentManager.toBin(reg.getValue(), regWidth) + "</html>");

			int pos = 6 + formattedWidth - ComponentManager.getBinWidth(bitno + 1);
			str.insert(pos + 1, "</font>");
			str.insert(pos, "<font color=\"#FF0000\">");
			setValue(str.toString());
		} else
			super.setValue();
	}
}
