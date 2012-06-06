/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataConst extends DataValue
{
	public DataConst(int value, int width)
	{
		super(width);

		this.value = value & mask;
	}
}
