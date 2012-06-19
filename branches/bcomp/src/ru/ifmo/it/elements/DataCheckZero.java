/**
 * $Id$
 */

package ru.ifmo.it.elements;

public class DataCheckZero extends DataCtrl {
	private DataSource input;
	private int inputmask;

	public DataCheckZero(DataSource input, int width, int ctrlbit, DataSource ... ctrls) {
		super(1, ctrlbit, ctrls);

		this.input = input;
		this.inputmask = getMask(width);
	}

	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue((input.getValue() & inputmask) == 0 ? 1 : 0);
	}
}
