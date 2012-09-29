/**
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CPU {
	public enum Reg {
		ACCUM, BUF, DATA, ADDR, IP, INSTR, STATE, KEY, MIP, MINSTR
	}

	private Bus aluOutput = new Bus(16);
	private Bus intrReq = new Bus(1);
	private StateReg regState = new StateReg();
	private ControlUnit cu = new ControlUnit(aluOutput);
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
	private volatile boolean clock = true;
	private int runLimit = 4 * 1024 * 1024;
	private MicroProgram mp;

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

		DataAnd intrctrl = new DataAnd(regState, StateReg.FLAG_EI, intrReq, getValve(27), getValve(28));
		PseudoRegister intrwrite = new PseudoRegister(regState, StateReg.FLAG_INTR, intrctrl);

		cpu2io = new CPU2IO(regAccum, regState, intrReq, getValve(25, regData), intrctrl);

		cu.compileMicroProgram(this.mp = mp);
		cu.jump(ControlUnit.LABEL_STP);
	}

	private DataHandler getValve(int cs, DataSource ... inputs) {
		if (valves[cs] == null)
			valves[cs] = cu.getValve(cs, inputs);

		return valves[cs];
	}

	public CPU2IO getCPU2IO() {
		return cpu2io;
	}

	public synchronized final void addDestination(int cs, DataDestination dest) {
		valves[cs].addDestination(dest);
	}

	public synchronized void removeDestination(int cs, DataDestination dest) {
		valves[cs].removeDestination(dest);
	}

	public DataSource getRegister(Reg reg) {
		switch (reg) {
		case ACCUM:
			return regAccum;

		case BUF:
			return regBuf;

		case DATA:
			return regData;

		case ADDR:
			return regAddr;

		case IP:
			return regIP;

		case INSTR:
			return regInstr;

		case STATE:
			return regState;

		case KEY:
			return regKey;

		case MIP:
			return cu.getIP();

		case MINSTR:
			return cu.getInstr();
		}

		return null;
	}

	public int getRegValue(Reg reg) {
		return getRegister(reg).getValue();
	}

	public int getRegWidth(Reg reg) {
		return getRegister(reg).getWidth();
	}

	public int getStateValue(int startbit) {
		return regState.getValue(startbit);
	}

	public Memory getMemory() {
		return mem;
	}

	public int getMemoryValue(int addr) {
		return mem.getValue(addr);
	}

	public Memory getMicroMemory() {
		return cu.getMemory();
	}

	public int getMicroMemoryValue(int addr) {
		return cu.getMemoryValue(addr);
	}

	public synchronized void setRegKey(int value) {
		regKey.setValue(value);
	}

	public synchronized void setMicroMemory() {
		cu.setMemory(regKey.getValue());
	}

	public synchronized void invertRunState() {
		regState.invertBit(StateReg.FLAG_RUN);
	}

	public synchronized void setRunState(int state) {
		regState.setValue(state, StateReg.FLAG_RUN);
	}

	public synchronized void jump(int label) {
		cu.jump(label);
	}

	public synchronized void jump() {
		cu.setIP(regKey.getValue());
	}

	public synchronized void next() {
		cu.setIP(0);
	}

	public synchronized void cont() {
		regState.setValue(clock ? 1 : 0, StateReg.FLAG_PROG);
	}

	public synchronized boolean step() {
		cu.step();

		return regState.getValue(StateReg.FLAG_PROG) == 1;
	}

	public void start() throws Exception {
		int i = 0;

		cont();

		while (step())
			if ((++i) > runLimit)
				throw new Exception("Exceeded run limit");
	}

	public void startFrom(int label) throws Exception {
		jump(label);
		start();
	}

	public boolean getClockState() {
		return clock;
	}

	public void setClockState(boolean clock) {
		this.clock = clock;
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
		return mp.getMicroProgramName();
	}

	public int getIntrCycleStartAddr() {
		return cu.getIntrCycleStartAddr();
	}

	public Instruction[] getInstructionSet() {
		return mp.getInstructionSet();
	}
}
