/**
 * $Id$
 */

package ru.ifmo.cs.elements;

import java.util.ArrayList;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class Bus extends DataWidth implements DataSource {
	private ArrayList<DataSource> inputs = new ArrayList<DataSource>();

	public Bus(DataSource ... inputs) {
		super(getMaxWidth(inputs));

		for (DataSource input : inputs)
			this.inputs.add(input);
	}

	public Bus(int width) {
		super(width);
	}

	private static int getMaxWidth(DataSource ... inputs) {
		int width = 0;

		for (DataSource input : inputs)
			if (width < input.getWidth())
				width = input.getWidth();

		return width;
	}

	public void addInput(DataSource ... newinputs) {
		for (DataSource input : newinputs)
			inputs.add(input);
	}

	public int getValue() {
		int value = 0;

		for (DataSource input : inputs)
			value |= input.getValue();

		return value & mask;
	}
}
