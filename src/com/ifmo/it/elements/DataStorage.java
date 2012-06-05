/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataStorage extends DataValue implements DataDestination
{
	public DataStorage(int width, DataSource ... inputs)
	{
		super(width, inputs);
	}

	public void setValue(int value)
	{
		this.value = value & mask;
		// Debug
		System.out.println("New value for " + this.getClass() + ": " + Integer.toString(this.value, 16));
	}
}
