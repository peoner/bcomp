/**
* $Id$
*/

package com.ifmo.it.elements;

public class Invertor extends DataHandler
{
	private int startbit;

	public Invertor(int startbit, DataSource ... inputs)
	{
		super(1, inputs);

		this.startbit = startbit;
	}

	public Invertor(DataSource ... inputs)
	{
		this(0, inputs);
	}

	public void setValue(int value)
	{
		super.setValue(~(value >> startbit));
	}
}
