/*
 * $Id$
 */
package ru.ifmo.it.bcomp;

import ru.ifmo.it.elements.*;

/**
 *
 * @author dima
 */

public class CPU2IO {
	private DataSource valveio;
	private Bus addr = new Bus(8);
	private BusSplitter order;
	private Bus out = new Bus(8);
	private Bus intr;
	private PseudoRegister flag;
	private PseudoRegister in;

	public CPU2IO(Register accum, StateReg state, Bus intrReq, DataSource valveio) {
		this.intr = intrReq;

		addr.addInput(this.valveio = valveio);
		order = new BusSplitter(valveio, 8, 4);

		out.addInput(accum);

		flag = new PseudoRegister(state, StateReg.FLAG_READY);
		in =  new PseudoRegister(accum, 0, 8);
	}

	public DataSource getValveIO() {
		return valveio;
	}

	public Bus getAddr() {
		return addr;
	}

	public DataSource getOrder() {
		return order;
	}

	public DataDestination getIn() {
		return in;
	}

	public Bus getOut() {
		return out;
	}

	public DataDestination getFlag() {
		return flag;
	}

	public Bus getBusIntr() {
		return intr;
	}
}
