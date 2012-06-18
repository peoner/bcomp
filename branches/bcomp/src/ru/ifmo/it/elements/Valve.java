/**
* $Id$
*/

package ru.ifmo.it.elements;

public class Valve extends DataCtrl {
	private DataSource input;
	private int startbit;

	public Valve(DataSource input, int startbit, int width, int ctrlbit, DataSource ... ctrls) {
		super(width, ctrlbit, ctrls);

		this.input = input;
		this.startbit = startbit;
	}

	public Valve(DataSource input, int ctrlbit, DataSource ... ctrls) {
		this(input, 0, input.getWidth(), ctrlbit, ctrls);
	}

	public Valve(DataSource input, DataSource ... ctrls) {
		this(input, 0, ctrls);
	}

	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue(input.getValue() >> startbit);
		else
			super.resetValue();
	}
}
