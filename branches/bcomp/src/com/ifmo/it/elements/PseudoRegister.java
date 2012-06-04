/**
* $Id$
*/

package com.ifmo.it.elements;

public class PseudoRegister extends DataInputs implements DataDestination
{
	private Register reg;
	private int startbit;

	public PseudoRegister(Register reg, int startbit, int width, DataSource ... inputs)
	{
		super(width, inputs);

		this.reg = reg;
		this.startbit = startbit;
	}

	public void setValue(int value)
	{
		reg.setValue(value, startbit, width);
	}
}
