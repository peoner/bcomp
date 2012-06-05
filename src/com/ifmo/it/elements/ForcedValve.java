/**
* $Id$
*/

package com.ifmo.it.elements;

public class ForcedValve extends DataCtrl
{
	private DataSource input;
	private int startbit;

	public ForcedValve(DataSource input, int startbit, int width, int ctrlbit, DataSource ... ctrls)
	{
		super(width, ctrlbit, ctrls);

		this.input = input;
		this.startbit = startbit;
	}

	public ForcedValve(DataSource input, int startbit, int width, DataSource ... ctrls)
	{
		this(input, startbit, width, 0, ctrls);
	}

	public void setValue(int ctrl)
	{
		super.setValue(isOpen(ctrl) ? (input.getValue() >> startbit) : 0);
	}
}
