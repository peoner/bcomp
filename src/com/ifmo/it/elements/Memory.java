/**
* $Id$
*/

package com.ifmo.it.elements;

public class Memory extends DataWidth implements DataSource, DataDestination
{
	private int memory[];
	private Register address;
	private int size;

	public Memory(int width, Register address)
	{
		super(width);

		memory = new int[size = 1 << (this.address = address).getWidth()];
	}

	public int getValueAt(int addr)
	{
		return memory[addr];
	}

	public int getValue()
	{
		return getValueAt(address.getValue());
	}

	public void setValueAt(int value, int addr)
	{
		memory[addr] = value & mask;
	}

	public void setValue(int value)
	{
		setValueAt(value, address.getValue());
	}

	public int getSize()
	{
		return size;
	}
}
