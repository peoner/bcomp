/**
 * $Id$
 */

package ru.ifmo.cs.elements;

public class DataPart extends DataHandler {
	protected int startbit;

	public DataPart(int startbit, int width, DataStorage ... inputs) {
		super(width, inputs);

		this.startbit = startbit;
	}

	public DataPart(int startbit, DataStorage ... inputs) {
		this(startbit, 1, inputs);
	}

	public void setValue(int value) {
		super.setValue(value >> startbit);
	}
}
