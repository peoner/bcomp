/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlUnit;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class AsmLabel {
	public final String label;
	private Assembler.Address addr = null;
	private Assembler.Command cmd = null;

	protected AsmLabel(String label) {
		this.label = label;
	}

	protected void setAddr(Assembler.Address addr) {
		this.addr = addr;
	}

	protected boolean hasAddress() {
		return addr != null;
	}

	protected int getAddress() throws Exception {
		if (addr == null)
			throw new Exception("Ссылка на неопределённую метку " + label);

		return addr.getAddress();
	}

	protected void setCommand(Assembler.Command cmd) {
		this.cmd = cmd;
	}

	protected int getCommand() throws Exception {
		return cmd.getCommand();
	}

	public int getSize() throws Exception {
		return cmd.getCommand();
	}

	public void setValue(CPU cpu, int value, int pos) throws Exception {
		cpu.setRegKey(addr.getAddress() + pos);
		cpu.startFrom(ControlUnit.LABEL_ADDR);
		if (pos == 0)
			cmd.setCommand(value);
		cpu.setRegKey(value);
		cpu.startFrom(ControlUnit.LABEL_WRITE);
	}

	public void setValue(CPU cpu, int value) throws Exception {
		setValue(cpu, value, 0);
	}
}
