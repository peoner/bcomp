/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataPart extends DataHandler {
	protected int startbit;

	public DataPart(DataStorage input, int startbit, int width) {
		super(width, input);

		this.startbit = startbit;
	}

	public void setValue(int value) {
		super.setValue(value >> startbit);
	}
}
