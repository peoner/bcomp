/**
* $Id$
*/

package com.ifmo.it.elements;

import java.util.ArrayList;

public class BusSplitter extends DataWidth implements DataSource
{
	private DataSource input;
	private int startbit;

	public BusSplitter(DataSource input, int startbit, int width)
	{
		super(width);

		this.input = input;
		this.startbit = startbit;
	}

	public int getValue()
	{
		return (input.getValue() >> startbit) & mask;
	}
}
