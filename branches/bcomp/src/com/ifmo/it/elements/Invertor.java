/**
* $Id$
*/

package com.ifmo.it.elements;

public class Invertor extends DataHandler
{
	public Invertor(DataSource ... inputs)
	{
		super(1, inputs);
	}

	public void setValue(int value)
	{
		super.setValue(~value);
	}
}
