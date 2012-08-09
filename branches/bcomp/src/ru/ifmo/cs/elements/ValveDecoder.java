/**
 * $Id$
 */

package ru.ifmo.cs.elements;

public class ValveDecoder extends DataCtrl {
	private DataSource input;
	private int inputmask;

	public ValveDecoder(DataSource input, DataSource ... ctrls) {
		super(1 << input.getWidth(), ctrls);

		this.input = input;
	}

	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue(1 << input.getValue());
	}
}
