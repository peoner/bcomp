/**
* $Id$
*/

package com.ifmo.it.elements;

public class Comparer extends DataHandler
{
	protected DataSource input;

	public Comparer(DataSource input, DataSource ctrl)
	{
		super(1, ctrl);

		this.input = input;
	}

	public void setValue(int value)
	{
		super.setValue(value == input.getValue() ? 1 : 0);
	}
}
