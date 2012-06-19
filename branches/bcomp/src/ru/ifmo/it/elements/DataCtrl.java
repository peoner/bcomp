/**
 * $Id$
 */

package ru.ifmo.it.elements;

public class DataCtrl extends DataHandler {
	private int ctrlbit;

	public DataCtrl(int width, int ctrlbit, DataSource ... ctrls) {
		super(width, ctrls);

		this.ctrlbit = ctrlbit;
	}

	public DataCtrl(int width, DataSource ... ctrls) {
		this(width, 0, ctrls);
	}

	public final boolean isOpen(int ctrl) {
		return ((ctrl >> ctrlbit) & 1) == 1;
	}
}
