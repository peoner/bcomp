/**
* $Id$
*/

package com.ifmo.it.elements;

public class DataAdder extends DataStorage
{
	private DataSource left;
	private DataSource right;
	
	public DataAdder(DataSource left, DataSource right, DataSource ... ctrls)
	{
		super(left.getWidth() + 1, ctrls);

		this.left = left;
		this.right = right;
	}

	public void setValue(int ctrl)
	{
		super.setValue(ctrl == 1 ? left.getValue() & right.getValue() : left.getValue() + right.getValue());
	}
}
