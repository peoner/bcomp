/**
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class DataInverter extends DataCtrl {
	private DataSource input;

	public DataInverter(DataSource input, DataSource ... ctrls) {
		super(input.getWidth(), ctrls);

		this.input = input;
	}

	public void setValue(int ctrl) {
		super.setValue(isOpen(ctrl) ? ~input.getValue() : input.getValue());
	}
}
