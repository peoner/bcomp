/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.io.IOCtrl;
import ru.ifmo.cs.io.IODevTimer;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicComp {
	private CPU cpu;
	private CPU2IO cpu2io;
	private IOCtrl[] ioctrls;
	private IODevTimer timer;

	public BasicComp(MicroProgram mp) throws Exception {
		cpu = new CPU(mp);
		cpu2io = cpu.getCPU2IO();

		ioctrls = new IOCtrl[] {
			new IOCtrl(0, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(1, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(2, IOCtrl.Direction.IN, cpu2io),
			new IOCtrl(3, IOCtrl.Direction.INOUT, cpu2io)
		};

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
