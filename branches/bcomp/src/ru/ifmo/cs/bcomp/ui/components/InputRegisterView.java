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

	public InputRegisterView(Register reg) {
		super(reg);
		this.reg = reg;
	}

	public void setActive(boolean active) {
		this.active = active;
		setValue();
	}

	@Override
	public void setValue() {
		//StringBuilder s = new StringBuilder("asshole");
		//s.insert(3, " ");
		//System.out.println(s.toString());

		if (active)
			setValue("<html>0000 <font color=\"#FF0000\">0</FONT>000 0000 0000</html>");
		else
			super.setValue();
	}
}
