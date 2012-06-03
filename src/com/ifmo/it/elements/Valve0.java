/**
* $Id$
*/

package com.ifmo.it.elements;

public class Valve0 extends Valve
{
	public Valve0(Register ctrl, int ctrlbit)
	{
		super(1, ctrl, ctrlbit);
	}

	public void setValue(int ctrl)
	{
		setValue(0, ctrl);
	}
}
