/**
* $Id$
*/

package com.ifmo.it.elements;

public class Register extends DataStorage
{
	public Register(int width, DataSource ... inputs)
	{
		super(width, inputs);
	}

	public void setValue(int value, int startbit, int width)
	{
		int valuemask = getMask(width);

		setValue((this.value & (~(valuemask << startbit))) | ((value & valuemask) << startbit));
	}

	public void setValue(int value, int startbit)
	{
		setValue(value, startbit, 1);
	}

	public void invertBit(int startbit)
	{
		int bitpos = 1 << startbit;

		value = (value & ~bitpos) | (~(value & bitpos) & bitpos);
	}
}
