/**
* $Id$
*/

package com.ifmo.it.elements;

public class Memory extends DataWidth implements DataSource, DataDestination
{
	private int memory[];
	private DataSource addr;
	private int size;

	public Memory(int width, DataSource addr)
	{
		super(width);

		memory = new int[size = 1 << (this.addr = addr).getWidth()];
	}

	public int getValueAt(int addr)
	{
		return memory[addr];
	}

	public int getValue()
	{
		return getValueAt(addr.getValue());
	}

	public void setValueAt(int addr, int value)
	{
		memory[addr] = value & mask;
	}

	public void setValue(int value)
	{
		setValueAt(addr.getValue(), value);
	}

	public int getSize()
	{
		return size;
	}
}
