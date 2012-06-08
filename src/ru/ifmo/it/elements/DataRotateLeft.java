/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataRotateLeft extends DataHandler
{
	private DataSource input;
	private DataSource c;
	
	public DataRotateLeft(DataSource input, DataSource c, DataSource ... ctrls)
	{
		super(input.getWidth() + 1, ctrls);

		this.input = input;
		this.c = c;
	}

	public void setValue(int ctrl)
	{
		super.setValue((input.getValue() << 1) | (c.getValue() & 1));
	}
}
