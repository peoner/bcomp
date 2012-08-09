/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author dima
 */

public class DataComparer extends DataHandler {
	private DataSource input;
	private int cmp2;

	public DataComparer(DataSource input, int cmp2, DataSource ... ctrls) {
		super(1, ctrls);

		this.input = input;
		this.cmp2 = cmp2;
	}

	public void setValue(int ctrl) {
		if (input.getValue() == cmp2)
			super.setValue(1);
	}
}
