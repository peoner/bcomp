/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataWidth
{
	protected int width;
	protected int mask;

	public DataWidth(int width)
	{
		this.width = width;
		mask = (1 << width) - 1;
	}

	public int getWidth()
	{
		return width;
	}
}
