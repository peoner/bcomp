/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataAdder extends DataHandler
{
	private DataSource input;
	
	public DataAdder(DataSource input, DataSource ... ctrls)
	{
		super(input.getWidth(), ctrls);

		this.input = input;
	}

	public void setValue(int ctrl)
	{
		super.setValue(input.getValue() + ctrl);
	}
}
