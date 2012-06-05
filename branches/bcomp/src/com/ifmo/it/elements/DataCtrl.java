/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataCtrl extends DataHandler
{
	private int ctrlbit;

	public DataCtrl(int width, int ctrlbit, DataSource ... ctrls)
	{
		super(width, ctrls);

		this.ctrlbit = ctrlbit;
	}

	public final boolean isOpen(int ctrl)
	{
		return ((ctrl >> ctrlbit) & 1) == 1;
	}
}
