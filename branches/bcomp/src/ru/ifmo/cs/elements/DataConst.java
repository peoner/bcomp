/**
 * $Id$
 */

package ru.ifmo.cs.elements;

public class DataConst extends DataValue {
	public DataConst(int value, int width) {
		super(width);

		this.value = value & mask;
	}
}
