/**
* $Id$
*/

package com.ifmo.it.elements;

public class DummyValve extends DataHandler
{
	private int defvalue;

	public DummyValve(int value, int width, DataSource ... ctrls)
	{
		super(width, ctrls);

		defvalue = value & mask;
	}

	public DummyValve(DataSource ... ctrls)
	{
		super(1, ctrls);

		defvalue = 1;
	}

	public void setValue(int ctrl)
	{
		super.setValue(defvalue);
	}
}
