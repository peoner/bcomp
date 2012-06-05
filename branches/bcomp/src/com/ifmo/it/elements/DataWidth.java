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
		setWidth(width);
	}

	protected void setWidth(int width)
	{
		this.width = width;
		this.mask = getMask(width);
	}

	public static int getMask(int width)
	{
		return (1 << width) - 1;
	}

	public int getWidth()
	{
		return width;
	}
}
