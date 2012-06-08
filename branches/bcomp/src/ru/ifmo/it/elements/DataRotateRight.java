/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataRotateRight extends DataHandler
{
	private DataSource input;
	private DataSource c;
	
	public DataRotateRight(DataSource input, DataSource c, DataSource ... ctrls)
	{
		super(input.getWidth() + 1, ctrls);

		this.input = input;
		this.c = c;
	}

	public void setValue(int ctrl)
	{
		int i = input.getValue();
		super.setValue(((i & 1) << 16) | ((c.getValue() & 1) << 15) | (i >> 1));
	}
}
