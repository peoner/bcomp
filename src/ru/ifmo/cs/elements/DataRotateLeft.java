/**
 * $Id$
 */

package ru.ifmo.cs.elements;

public class DataRotateLeft extends DataCtrl {
	private DataSource input;
	private DataSource c;

	public DataRotateLeft(DataSource input, DataSource c, int ctrlbit, DataSource ... ctrls) {
		super(input.getWidth() + 1, ctrlbit, ctrls);

		this.input = input;
		this.c = c;
	}

	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue((input.getValue() << 1) | (c.getValue() & 1));
	}
}
