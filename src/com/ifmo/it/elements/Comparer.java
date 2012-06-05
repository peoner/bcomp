/**
* $Id$
*/

package com.ifmo.it.elements;

public class Comparer extends DataHandler
{
	private DataSource input;
	private int ctrlbit;

	public Comparer(DataSource input, int ctrlbit, DataSource ... ctrls)
	{
		super(1, ctrls);

		this.input = input;
		this.ctrlbit = ctrlbit;
	}

	public void setValue(int value)
	{
		super.setValue(((value >> ctrlbit) & 1) == input.getValue() ? 1 : 0);
	}
}
