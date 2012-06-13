/**
* $Id$
*/

package ru.ifmo.it.bcomp;

import ru.ifmo.it.elements.*;

public class MicroIP extends Register {
	public MicroIP(int width) {
		super(width);

		super.setValue(1);
	}

	public void setValue(int value) {
		super.setValue(value == 0 ? this.value + 1 : value);
	}
}
