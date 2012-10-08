/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataSource;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RegisterView extends BCompComponent implements DataDestination {
	private int formatWidth;
	private boolean hex;

	private DataSource reg;
	protected JLabel value;

	public RegisterView(DataSource reg, Color colorTitleBG) {
		super("", colorTitleBG);

		this.reg = reg;

		value = addValueLabel();
	}

	public RegisterView(DataSource reg) {
		this(reg, COLOR_TITLE);
	}

	public void setProperties(String title, int x, int y, boolean hex, int regWidth) {
		this.hex = hex;
		this.formatWidth = regWidth;

		setBounds(x, y, getValueWidth(regWidth, hex));

		setTitle(title);

		setValue();
		value.setBounds(1, getValueY(), width - 2, CELL_HEIGHT);
	}

	public void setProperties(String title, int x, int y, boolean hex) {
		setProperties(title, x, y, hex, reg.getWidth());
	}

	protected int getRegWidth() {
		return reg.getWidth();
	}

	protected void setValue(String val) {
		value.setText(val);
	}

	public void setValue() {
		setValue(hex ?
			Utils.toHex(reg.getValue(), formatWidth) :
			Utils.toBinary(reg.getValue(), formatWidth));
	}

	@Override
	public void setValue(int value) {
		setValue();
	}
}
