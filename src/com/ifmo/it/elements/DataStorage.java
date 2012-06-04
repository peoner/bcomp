/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataStorage extends DataInputs implements DataSource, DataDestination
{
	protected int value = 0;

	public DataStorage(int width, DataSource ... inputs)
	{
		super(width, inputs);
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value & mask;
		// Debug
		System.out.println("New value for " + this.getClass() + ": " + Integer.toString(this.value, 16));
	}
}
