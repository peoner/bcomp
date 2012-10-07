/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
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
	private EnumMap<ControlSignal, DataHandler> valves =
		new EnumMap<ControlSignal, DataHandler> (ControlSignal.class);
	private Register regAddr = new Register(11, getValve(ControlSignal.BUF_TO_ADDR, aluOutput));
	private Memory mem = new Memory("Память", 16, regAddr);
	private Register regData = new Register(16,
		getValve(ControlSignal.BUF_TO_DATA, aluOutput),
		getValve(ControlSignal.MEMORY_READ, mem));
	private Register regInstr = new Register(16, getValve(ControlSignal.BUF_TO_INSTR, aluOutput));
	private Register regIP = new Register(11, getValve(ControlSignal.BUF_TO_IP, aluOutput));
	private Register regAccum = new Register(16, getValve(ControlSignal.BUF_TO_ACCUM, aluOutput));
	private Register regKey = new Register(16);
	private Register regBuf;
	private CPU2IO cpu2io;
	private volatile boolean clock = true;
	private int runLimit = 4 * 1024 * 1024;
	private MicroProgram mp;

	public CPU(MicroProgram mp) throws Exception {
		getValve(ControlSignal.MEMORY_WRITE, regData).addDestination(mem);

		regState.setValue(2);

		Bus aluRight = new Bus(
			getValve(ControlSignal.DATA_TO_ALU, regData),
			getValve(ControlSignal.INSTR_TO_ALU, regInstr),
			getValve(ControlSignal.IP_TO_ALU, regIP));

		Bus aluLeft = new Bus(
			getValve(ControlSignal.ACCUM_TO_ALU, regAccum),
			getValve(ControlSignal.STATE_TO_ALU, regState),
			getValve(ControlSignal.KEY_TO_ALU, regKey));

		DataSource notLeft = getValve(ControlSignal.INVERT_LEFT, aluLeft);
		DataSource notRight = getValve(ControlSignal.INVERT_RIGHT, aluRight);

		DataSource aluplus1 = getValve(ControlSignal.ALU_PLUS_1, Consts.consts[1]);
		regBuf = new Register(17,
			getValve(ControlSignal.ALU_AND, notLeft, notRight, aluplus1),
			getValve(ControlSignal.SHIFT_RIGHT, regAccum, regState),
			getValve(ControlSignal.SHIFT_LEFT, regAccum, regState));
		aluOutput.addInput(regBuf);

		PseudoRegister regStateEI = new PseudoRegister(regState, StateReg.FLAG_EI,
			getValve(ControlSignal.DISABLE_INTERRUPTS, Consts.consts[0]),
			getValve(ControlSignal.ENABLE_INTERRUPTS, Consts.consts[1]));

		PseudoRegister regStateC = new PseudoRegister(regState, StateReg.FLAG_C,
			getValve(ControlSignal.BUF_TO_STATE_C, regBuf),
			getValve(ControlSignal.CLEAR_STATE_C, Consts.consts[0]),
			getValve(ControlSignal.SET_STATE_C, Consts.consts[1]));

		PseudoRegister regStateN =
			new PseudoRegister(regState, StateReg.FLAG_N, getValve(ControlSignal.BUF_TO_STATE_N, regBuf));
		PseudoRegister regStateZ =
			new PseudoRegister(regState, StateReg.FLAG_Z, getValve(ControlSignal.BUF_TO_STATE_Z, regBuf));
		PseudoRegister regStateProg =
			new PseudoRegister(regState, StateReg.FLAG_PROG, getValve(ControlSignal.HALT, Consts.consts[0]));

		DataAnd intrctrl = new DataAnd(regState, StateReg.FLAG_EI, intrReq,
			getValve(ControlSignal.DISABLE_INTERRUPTS), getValve(ControlSignal.ENABLE_INTERRUPTS));
		PseudoRegister intrwrite = new PseudoRegister(regState, StateReg.FLAG_INTR, intrctrl);

		cpu2io =
			new CPU2IO(regAccum, regState, intrReq, getValve(ControlSignal.INPUT_OUTPUT, regData), intrctrl);

		cu.compileMicroProgram(this.mp = mp);
		cu.jump(ControlUnit.LABEL_STP);
	}

	private DataHandler getValve(ControlSignal cs, DataSource ... inputs) {
		if (!valves.containsKey(cs))
			valves.put(cs, cu.createValve(cs, inputs));

		return valves.get(cs);
	}

	public CPU2IO getCPU2IO() {
		return cpu2io;
	}

	public synchronized final void addDestination(ControlSignal cs, DataDestination dest) {
		valves.get(cs).addDestination(dest);
	}

	public synchronized void removeDestination(ControlSignal cs, DataDestination dest) {
		valves.get(cs).removeDestination(dest);
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

	public RunningCycle getRunningCycle() {
		return cu.getCycle();
	}
}