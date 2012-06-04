/**
* $Id$
*/

package com.ifmo.it.elements;

public class ValveActive extends DataHandler
{
	protected DataSource input;

	public ValveActive(DataSource input, DataSource ... ctrls)
	{
		super(input.getWidth(), ctrls);

		this.input = input;
	}

	public void setValue(int ctrl)
	{
		super.setValue(ctrl == 1 ? input.getValue() : 0);
	}
}
