/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataValue extends DataInputs implements DataSource
{
	protected int value = 0;

	public DataValue(int width, DataSource ... inputs)
	{
		super(width, inputs);
	}

	public final int getValue()
	{
		return value;
	}
}
