/*
 * $Id$
 */

package ru.ifmo.cs.io;

import ru.ifmo.cs.bcomp.CPU2IO;
import ru.ifmo.cs.elements.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
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
	private Valve valveClearFlag;
	private Valve valveOut;

	public IOCtrl(int addr, Direction dir, CPU2IO cpu2io) {
		this.addr = addr;
		this.dir = dir;

		DataComparer dc = new DataComparer(cpu2io.getAddr(), addr, cpu2io.getValveIO());
		ValveDecoder order = new ValveDecoder(cpu2io.getOrder(), dc);

		valveClearFlag = new Valve(Consts.consts[0], 0, order);
		flag = new Register(1, valveSetFlag, valveClearFlag);
		cpu2io.addIntrBusInput(flag);

		cpu2io.addIntrCtrlInput(valveClearFlag);
		cpu2io.addIntrCtrlInput(valveSetFlag);

		cpu2io.addFlagInput(new Valve(flag, 1, order));

		if (dir != Direction.IN) {
			valveOut = new Valve(cpu2io.getOut(), 3, order);
			valveOut.addDestination(data);
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

	public Register getRegData() {
		return data;
	}

	public void setData(int value) throws Exception {
		if (dir != Direction.OUT)
			data.setValue(value);
		else
			throw new Exception("Attempt to write to the output device " + addr);
	}

	public void addOutListener(DataDestination dest) {
		valveOut.addDestination(dest);
	}

	public void removeOutListener(DataDestination dest) {
		valveOut.removeDestination(dest);
	}

	public void addFlagListener(DataDestination dest) {
		valveClearFlag.addDestination(dest);
		valveSetFlag.addDestination(dest);
	}

	public void removeFlagListener(DataDestination dest) {
		valveClearFlag.addDestination(dest);
		valveSetFlag.addDestination(dest);
	}
}
