/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataStorage extends DataValue implements DataDestination
{
	public DataStorage(int width, DataSource ... inputs)
	{
		super(width, inputs);
	}

	public void setValue(int value)
	{
		this.value = value & mask;
		//System.out.println("Write " + this.getClass() + ": " + Integer.toString(this.value, 16));
	}
}
