/**
 * $Id$
 */

package ru.ifmo.it.elements;

public class DataAdder extends DataCtrl {
	private DataSource left;
	private DataSource right;
	private DataSource c;
	
	public DataAdder(DataSource left, DataSource right, DataSource c, DataSource ... ctrls) {
		super(left.getWidth() + 1, ctrls);

		this.left = left;
		this.right = right;
		this.c = c;
	}

	public void setValue(int ctrl) {
		int c = this.c.getValue();

		super.setValue(isOpen(ctrl) ? left.getValue() & right.getValue() : left.getValue() + right.getValue() + c);
	}
}
