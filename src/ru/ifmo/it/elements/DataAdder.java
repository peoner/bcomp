/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataAdder extends DataCtrl
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
		super.setValue(isOpen(ctrl) ? left.getValue() & right.getValue() : left.getValue() + right.getValue());
	}
}
