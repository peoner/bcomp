/*
 * $Id$
 */
package ru.ifmo.it.bcomp;

import ru.ifmo.it.io.IOCtrl;
import ru.ifmo.it.io.IODevTimer;

/**
 *
 * @author dima
 */

public class BasicComp {
	private CPU cpu;
	private CPU2IO cpu2io;
	private IOCtrl[] ioctrls = new IOCtrl[4];
	private IODevTimer timer;

	public BasicComp(MicroPrograms.Type mptype) throws Exception {
		cpu = new CPU(MicroPrograms.getMicroProgram(mptype));
		cpu2io = cpu.getCPU2IO();

		ioctrls[0] = new IOCtrl(0, IOCtrl.Direction.OUT, cpu2io);
		ioctrls[1] = new IOCtrl(1, IOCtrl.Direction.OUT, cpu2io);
		ioctrls[2] = new IOCtrl(2, IOCtrl.Direction.IN, cpu2io);
		ioctrls[3] = new IOCtrl(3, IOCtrl.Direction.INOUT, cpu2io);

		timer = new IODevTimer(ioctrls[0]);
	}

	public CPU getCPU() {
		return cpu;
	}

	public IOCtrl[] getIOCtrls() {
		return ioctrls;
	}

	public void startTimer() {
		timer.start();
	}

	public void stopTimer() {
		timer.done();
	}
}
