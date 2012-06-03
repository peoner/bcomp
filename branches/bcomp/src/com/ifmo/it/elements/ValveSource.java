/**
* $Id$
*/

package com.ifmo.it.elements;

public class ValveSource extends Valve
{
	protected int bitno;
	protected DataSource input;

	public ValveSource(DataSource input, int bitno, int width, DataSource ctrl, int ctrlbit)
	{
		super(width, ctrl, ctrlbit);

		this.bitno = bitno;
		this.input = input;
	}

	public void setValue(int ctrl)
	{
		setValue(input.getValue() >> bitno, ctrl);
	}
}
