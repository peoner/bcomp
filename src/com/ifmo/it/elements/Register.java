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

	public void setValue(int value, int bitno)
	{
		this.value = (this.value & (~(1 << bitno))) | (value << bitno);
	}

	public void setValue(int value, int bitno, int width)
	{
		int mask = (1 << width) - 1;

		this.value = (this.value & (~(mask << bitno))) | ((value & mask) << bitno);
	}

	public void invertBit(int bitno)
	{
		int bitpos = 1 << bitno;

		value = (value & ~bitpos) | (~(value & bitpos) & bitpos);
	}
}
