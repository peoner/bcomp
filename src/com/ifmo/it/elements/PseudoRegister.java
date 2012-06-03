/**
* $Id$
*/

package com.ifmo.it.elements;

public class PseudoRegister extends DataInputs implements DataDestination
{
	private Register reg;
	private int bitno;

	public PseudoRegister(Register reg, int bitno, int width, DataSource ... inputs)
	{
		super(width, inputs);

		this.reg = reg;
		this.bitno = bitno;
	}

	public void setValue(int value)
	{
		reg.setValue(value, bitno, width);
	}
}
