/*
 * $Id$
 */
package ru.ifmo.cs.io;

import ru.ifmo.cs.elements.Valve;
import ru.ifmo.cs.elements.Register;
import ru.ifmo.cs.elements.DataComparer;
import ru.ifmo.cs.elements.Consts;
import ru.ifmo.cs.elements.ValveDecoder;
import ru.ifmo.cs.bcomp.CPU2IO;

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
	private Valve valveSetFlag = new Valve(Consts.consts[1]);

	public IOCtrl(int addr, Direction dir, CPU2IO cpu2io) {
		this.addr = addr;
		this.dir = dir;

		DataComparer dc = new DataComparer(cpu2io.getAddr(), addr, cpu2io.getValveIO());
		ValveDecoder order = new ValveDecoder(cpu2io.getOrder(), dc);

		Valve valveClearFlag = new Valve(Consts.consts[0], 0, order);
		flag = new Register(1, valveSetFlag, valveClearFlag);
		cpu2io.addIntrBusInput(flag);

		cpu2io.addIntrCtrlInput(valveClearFlag);
		cpu2io.addIntrCtrlInput(valveSetFlag);

		cpu2io.addFlagInput(new Valve(flag, 1, order));

		if (dir != Direction.IN) {
			Valve out = new Valve(cpu2io.getOut(), 3, order);
			out.addDestination(data);
		}

		if (dir != Direction.OUT)
			cpu2io.addInInput(new Valve(data, 2, order));
	}

	public int getFlag() {
		return flag.getValue();
	}

	public void setFlag() {
		valveSetFlag.setValue(1);
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
