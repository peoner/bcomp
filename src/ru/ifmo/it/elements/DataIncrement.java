/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataIncrement extends DataHandler
{
	private DataSource input;
	
	public DataIncrement(DataSource input, DataSource ... ctrls)
	{
		super(input.getWidth(), ctrls);

		this.input = input;
	}

	public void setValue(int ctrl)
	{
		super.setValue(input.getValue() + (ctrl & 1));
	}
}
