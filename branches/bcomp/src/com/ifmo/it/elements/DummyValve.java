/**
* $Id$
*/

package com.ifmo.it.elements;

public class DummyValve extends DataHandler
{
	public DummyValve(DataSource ... ctrls)
	{
		super(1, ctrls);
	}

	public void setValue(int ctrl)
	{
		super.setValue(1);
	}
}
