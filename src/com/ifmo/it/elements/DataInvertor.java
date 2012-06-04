/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataInvertor extends DataStorage
{
	private DataSource input;

	public DataInvertor(DataSource input, DataSource ... ctrls)
	{
		super(input.getWidth(), ctrls);

		this.input = input;
	}

	public void setValue(int ctrl)
	{
		super.setValue(ctrl == 1 ? ~input.getValue() : input.getValue());
	}
}
