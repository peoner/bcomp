/**
* $Id$
*/

package com.ifmo.it.bcomp;

import com.ifmo.it.elements.*;

public class MicroIP extends Register
{
	public MicroIP(int width)
	{
		super(width);
	}

	public void setValue(int value)
	{
		super.setValue(value == 0 ? this.value + 1 : value);
	}
}
