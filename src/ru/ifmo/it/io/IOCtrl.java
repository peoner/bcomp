/*
 * $Id$
 */
package ru.ifmo.it.io;

import ru.ifmo.it.bcomp.CPU2IO;
import ru.ifmo.it.elements.*;

/**
 *
 * @author dima
 */

public class IOCtrl {
	public enum Direction {
		IN, OUT, INOUT
	};

	private Register flag;
	private Register data = new Register(8);
	private int addr;
	private Direction dir;

	public IOCtrl(int addr, Direction dir, CPU2IO cpu2io) {
		this.addr = addr;
		this.dir = dir;

		DataComparer dc = new DataComparer(cpu2io.getAddr(), addr, cpu2io.getValveIO());
		ValveDecoder order = new ValveDecoder(cpu2io.getOrder(), dc);

		flag = new Register(1, new Valve(Consts.consts[0], 0, order));
		cpu2io.getBusIntr().addInput(flag);

		Valve valveflag = new Valve(flag, 1, order);
		valveflag.addDestination(cpu2io.getFlag());

		if (dir != Direction.IN) {
			Valve out = new Valve(cpu2io.getOut(), 3, order);
			out.addDestination(data);
		}

		if (dir != Direction.OUT) {
			Valve in = new Valve(data, 2, order);
			in.addDestination(cpu2io.getIn());
		}
	}

	public int getFlag() {
		return flag.getValue();
	}

	public void setFlag() {
		flag.setValue(1);
	}

	public int getData() {
		return data.getValue();
	}

	public void setData(int value) throws Exception {
		if (dir != Direction.OUT)
			data.setValue(value);
		else
			throw new Exception("Attempt to write to the output device " + addr);
	}
}