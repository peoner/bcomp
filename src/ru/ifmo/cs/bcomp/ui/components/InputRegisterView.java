/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ru.ifmo.cs.bcomp.ui.Utils;
import ru.ifmo.cs.elements.Register;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class InputRegisterView extends RegisterView {
	private final ComponentManager cmanager;
	private final Register reg;
	private final ActiveBitView activeBitView;
	private boolean active = false;
	private int regWidth;
	private int bitno;
	private int formattedWidth;

	public InputRegisterView(ComponentManager cmanager, Register reg) {
		super(reg, COLOR_INPUT_TITLE);

		this.cmanager = cmanager;
		this.reg = reg;
		activeBitView = cmanager.getActiveBit();

		bitno = (regWidth = reg.getWidth()) - 1;
		formattedWidth = Utils.getBinaryWidth(regWidth);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!active)
					activeInputSwitch();
			}
		});

		value.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!active)
					activeInputSwitch();

				int bitno = Utils.getBitNo(e.getX(), formattedWidth, FONT_COURIER_BOLD_25_WIDTH);

				if (bitno < 0)
					return;

				setActiveBit(bitno);

				if (e.getClickCount() > 1)
					invertBit();
			}
		});
	}

	private void activeInputSwitch() {
		cmanager.activeInputSwitch(this);
	}

	public void setActive(boolean active) {
		this.active = active;
		setActiveBit(bitno);
	}

	private void setActiveBit(int bitno) {
		activeBitView.setValue(this.bitno = bitno);
		setValue();
	}

	public void moveLeft() {
		setActiveBit((bitno + 1) % regWidth);
	}

	public void moveRight() {
		setActiveBit((bitno == 0 ? regWidth : bitno) - 1);
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
			str.insert(pos, COLOR_ACTIVE_BIT);
			setValue(str.toString());
		} else
			super.setValue();
	}
}
