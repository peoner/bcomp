/**
 * $Id$
 */

package ru.ifmo.it.elements;

public class DataDecoder extends DataHandler {
	private int startbit;
	private int inputmask;

	public DataDecoder(DataStorage input, int startbit, int width) {
		super(1 << width, input);

		this.startbit = startbit;
		this.inputmask = getMask(width);
	}

	public void setValue(int value) {
		super.setValue(1 << ((value >> startbit) & inputmask));
	}
}
