/**
* $Id$
*/

package com.ifmo.it.elements;

public class Bus extends DataWidth implements DataSource
{
	private DataSource inputs[];

	private static int getMaxWidth(DataSource ... inputs)
	{
		int width = 0;

		for (DataSource input : inputs)
			if (width < input.getWidth())
				width = input.getWidth();

		return width;
	}

	public Bus(DataSource ... inputs)
	{
		super(getMaxWidth(inputs));

		this.inputs = inputs;
	}

	public int getValue()
	{
		int value = 0;

		for (DataSource input : inputs)
			value |= input.getValue();

		return value;
	}
}
