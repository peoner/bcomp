/**
* $Id$
*/

package com.ifmo.it.elements;

public class Valve extends DataHandler
{
	private int ctrlbit;

	public Valve(int width, DataSource ctrl, int ctrlbit)
	{
		super(width, ctrl);

		this.ctrlbit = ctrlbit;
	}

	protected void setValue(int value, int ctrl)
	{
		super.setValue(((ctrl >> ctrlbit) & 1) == 1 ? (value & mask) : 0);
	}
}
