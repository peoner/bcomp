/**
* $Id$
*/

package com.ifmo.it.elements;

public class Valve1 extends Valve
{
	public Valve1(DataSource ... ctrls)
	{
		super(1, ctrls);
	}

	public void setValue(int ctrl)
	{
		setValue(ctrl, 1);
	}
}
