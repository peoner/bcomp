/**
 * $Id$
 */

package ru.ifmo.it.bcomp;

import ru.ifmo.it.elements.*;

public class CPU {
	public enum Regs {
		ACCUM, BUF, DATA, ADDR, IP, INSTR, STATE, KEY, MIP, MINSTR
	}

	private Bus aluOutput = new Bus(16);
	private Bus intrReq = new Bus(1);
	private StateReg regState = new StateReg(13);
	private ControlUnit cu = new ControlUnit(aluOutput, intrReq, regState, StateReg.FLAG_EI, StateReg.FLAG_INTR);
	private DataHandler[] valves = new DataHandler[ControlUnit.CONTROL_SIGNAL_COUNT];
	private Register regAddr = new Register(11, getValve(18, aluOutput));
	private Memory mem = new Memory(16, regAddr);
	private Register regData = new Register(16, getValve(19, aluOutput), getValve(23, mem));
	private Register regInstr = new Register(16, getValve(20, aluOutput));
	private Register regIP = new Register(11, getValve(21, aluOutput));
	private Register regAccum = new Register(16, getValve(22, aluOutput));
	private Register regKey = new Register(16);
	private Register regBuf;
	private CPU2IO cpu2io;
	private ControlUnit.Cycle cycle = ControlUnit.Cycle.PANEL;
	private volatile boolean clock = true;
	private int runLimit = 4 * 1024 * 1024;
	private String mpname;

	public CPU(MicroProgram mp) throws Exception {
		getValve(24, regData);
		addDestination(24, mem);

		regState.setValue(2);

		Bus aluRight = new Bus(getValve(1, regData), getValve(2, regInstr), getValve(3, regIP));

		Bus aluLeft = new Bus(getValve(4, regAccum), getValve(5, regState), getValve(6, regKey));

		DataSource notLeft = getValve(7, aluLeft);
		DataSource notRight = getValve(8, aluRight);

		regBuf = new Register(17,
			getValve(9, notLeft, notRight, getValve(10, Consts.consts[1])),
			getValve(12, regAccum, regState),
			getValve(11, regAccum, regState));
		aluOutput.addInput(regBuf);

		PseudoRegister regStateEI = new PseudoRegister(regState, StateReg.FLAG_EI,
			getValve(27, Consts.consts[0]),
			getValve(28, Consts.consts[1]));

		PseudoRegister regStateC = new PseudoRegister(regState, StateReg.FLAG_C,
			getValve(13, regBuf),
			getValve(16, Consts.consts[0]),
			getValve(17, Consts.consts[1]));

		PseudoRegister regStateN = new PseudoRegister(regState, StateReg.FLAG_N, getValve(14, regBuf));
		PseudoRegister regStateZ = new PseudoRegister(regState, StateReg.FLAG_Z, getValve(15, regBuf));
		PseudoRegister regStateProg = new PseudoRegister(regState, StateReg.FLAG_PROG, getValve(0, Consts.consts[0]));

		cpu2io = new CPU2IO(regAccum, regState, intrReq, getValve(25, regData));

		cu.compileMicroProgram(mp);
		cu.jump(ControlUnit.LABEL_HLT);


		mpname = mp.getMicroProgramName();
	}

	private DataHandler getValve(int cs, DataSource ... inputs) {
		if (valves[cs] == null)
			valves[cs] = cu.getValve(cs, inputs);

		return valves[cs];
	}

	public CPU2IO getCPU2IO() {
		return cpu2io;
	}

	public final void addDestination(int cs, DataDestination dest) {
		valves[cs].addDestination(dest);
	}

	public void removeDestination(int cs, DataDestination dest) {
		valves[cs].removeDestination(dest);
	}

	public synchronized int getRegValue(Regs reg) {
		switch (reg) {
		case ACCUM:
			return regAccum.getValue();

		case BUF:
			return regBuf.getValue();

		case DATA:
			return regData.getValue();

		case ADDR:
			return regAddr.getValue();

		case IP:
			return regIP.getValue();

		case INSTR:
			return regInstr.getValue();

		case STATE:
			return regState.getValue();

		case KEY:
			return regKey.getValue();

		case MIP:
			return cu.getIP();

		case MINSTR:
			return cu.getInstr();
		}

		return 0;
	}

	public synchronized int getStateValue(int startbit) {
		return regState.getValue(startbit);
	}

	public synchronized int getMemory(int addr) {
		return mem.getValue(addr);
	}

	public synchronized int getMicroMemory(int addr) {
		return cu.getMemoryCell(addr);
	}

	public synchronized void setRegKey(int value) {
		regKey.setValue(value);
	}

	public synchronized void invertRunState() {
		regState.invertBit(StateReg.FLAG_RUN);
	}

	public synchronized void jump(int label) {
		cu.jump(label);
	}

	public synchronized void jump() {
		cu.setIP(regKey.getValue());
	}

	public synchronized void setMicroMemory() {
		cu.setMemoryCell(regKey.getValue());
		cu.setIP(0);
	}

	public synchronized boolean step() {
		ControlUnit.Cycle cycle = cu.getCycle();

		if (regState.getValue(StateReg.FLAG_PROG) == 0)
			regState.setValue(1, StateReg.FLAG_PROG);

		if (this.cycle != cycle) {
			regState.setValue(cycle == ControlUnit.Cycle.INSTRFETCH ? 1 : 0, StateReg.FLAG_CYCLE_INSTR);
			regState.setValue(cycle == ControlUnit.Cycle.ADDRFETCH ? 1 : 0, StateReg.FLAG_CYCLE_ADDR);
			regState.setValue(cycle == ControlUnit.Cycle.EXECUTION ? 1 : 0, StateReg.FLAG_CYCLE_EXEC);
			regState.setValue(cycle == ControlUnit.Cycle.INTERRUPT ? 1 : 0, StateReg.FLAG_CYCLE_INTR);

			this.cycle = cycle;
		}

		cu.step();
	
		return regState.getValue(StateReg.FLAG_PROG) == 1;
	}

	public void start() throws Exception {
		int i = 0;

		while (step() && clock)
			if ((++i) > runLimit)
				throw new Exception("Exceeded run limit");
	}

	public boolean getClockState() {
		return clock;
	}

	public boolean invertClockState() {
		return clock = !clock;
	}

	public int getRunLimit() {
		return runLimit;
	}

	public void setRunLimit(int runLimit) {
		this.runLimit = runLimit;
	}

	public String getMicroProgramName() {
		return mpname;
	}

	public int getIntrCycleStartAddr() {
		return cu.getIntrCycleStartAddr();
	}
}
