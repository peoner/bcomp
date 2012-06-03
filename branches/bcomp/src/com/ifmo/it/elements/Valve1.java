/**
* $Id$
*/

package com.ifmo.it.elements;

public class Valve1 extends Valve
{
	public Valve1(Register ctrl, int ctrlbit)
	{
		super(1, ctrl, ctrlbit);
	}

	public void setValue(int ctrl)
	{
		setValue(1, ctrl);
	}
}
