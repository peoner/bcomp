/**
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class Memory extends DataWidth implements DataSource, DataDestination {
	private int memory[];
	private DataSource addr;
	private int size;

	public Memory(int width, DataSource addr) {
		super(width);

		memory = new int[size = 1 << (this.addr = addr).getWidth()];
	}

	public synchronized int getValue(int addr) {
		return memory[addr];
	}

	@Override
	public synchronized int getValue() {
		return getValue(addr.getValue());
	}

	public synchronized void setValue(int addr, int value) {
		memory[addr] = value & mask;
	}

	@Override
	public synchronized void setValue(int value) {
		setValue(addr.getValue(), value);
	}

	public int getSize() {
		return size;
	}

	public int getAddrWidth() {
		return addr.getWidth();
	}
}
