/**
* $Id$
*/

package com.ifmo.it.elements;

public class Decoder extends DataHandler
{
	public Decoder(DataStorage input)
	{
		super(1 << input.getWidth(), input);
	}

	public void setValue(int value)
	{
		super.setValue(1 << value);
	}
}
