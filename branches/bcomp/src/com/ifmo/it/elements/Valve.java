/**
* $Id$
*/

package com.ifmo.it.elements;

public class Valve extends DataHandler
{
	public Valve(int width, DataSource ... ctrls)
	{
		super(width, ctrls);
	}

	protected void setValue(int ctrl, int value)
	{

		if (ctrl == 1)
			super.setValue(value);
		else
			super.resetValue();
	}
}
