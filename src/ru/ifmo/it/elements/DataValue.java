/**
* $Id$
*/

package ru.ifmo.it.elements;

public class DataValue extends DataInputs implements DataSource {
	protected int value = 0;

	public DataValue(int width, DataSource ... inputs) {
		super(width, inputs);
	}

	public final int getValue() {
		//System.out.println("Read: " + this.getClass() + ": " + Integer.toString(this.value, 16));
		return value;
	}
}
