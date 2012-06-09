/**
* $Id$
*/

package ru.ifmo.it.bcomp;

import ru.ifmo.it.elements.*;

public class CPU
{
	public enum Regs {
		ACCUM, BUF, DATA, ADDR, IP, INSTR, STATE, KEY, MIP, MINSTR
	}

	private ControlUnit cu = new ControlUnit();
	private Bus aluOutput = cu.getALUOuput();
	private DataSource[] consts = cu.getConsts();
	private DataHandler[] valves = new DataHandler[cu.CONTROL_SIGNAL_COUNT];
	private Register regAddr = new Register(11, getValve(18, aluOutput));
	private Memory mem = new Memory(16, regAddr);
	private Register regData = new Register(16,	getValve(19, aluOutput), getValve(23, mem));
	private Register regInstr = new Register(16, getValve(20, aluOutput));
	private Register regIP = new Register(11, getValve(21, aluOutput));
	private Register regAccum = new Register(16, getValve(22, aluOutput));
	private StateReg regState = new StateReg(13);
	private Register regKey = new Register(16);
	private Register regBuf;
    private Bus busIOAddr = new Bus(8);
    private BusSplitter busIOOrder;
	private ControlUnit.Cycle cycle = ControlUnit.Cycle.PANEL;

	public CPU() throws Exception
	{
		getValve(24, regData);
		addDestination(24, mem);

		regState.setValue(2);

		Bus aluRight = new Bus(getValve(1, regData), getValve(2, regInstr), getValve(3, regIP));

		Bus aluLeft = new Bus(getValve(4, regAccum), getValve(5, regState), getValve(6, regKey));

		DataSource notLeft = new DataInverter(aluLeft, getValve(7, consts[1]));
		DataSource notRight = new DataInverter(aluRight, getValve(8, consts[1]));
		DataSource adder = new DataAdder(notLeft, notRight, getValve(9, consts[1]));

		regBuf = new Register(17,
			new DataIncrement(adder, getValve(10, consts[1])),
			new DataRotateLeft(regAccum, regState, getValve(12, consts[1])),
			new DataRotateRight(regAccum, regState, getValve(11, consts[1])));
		aluOutput.addInput(regBuf);

		PseudoRegister regStateEI =	new PseudoRegister(regState, StateReg.FLAG_EI,
			getValve(27, consts[0]),
			getValve(28, consts[1]));

		PseudoRegister regStateC = new PseudoRegister(regState, StateReg.FLAG_C,
			getValve(13, regBuf),
			getValve(16, consts[0]),
			getValve(17, consts[1]));

		PseudoRegister regStateN = new PseudoRegister(regState, StateReg.FLAG_N, getValve(14, regBuf));
		PseudoRegister regStateZ = new PseudoRegister(regState, StateReg.FLAG_Z, getValve(15, regBuf));
		PseudoRegister regStateProg = new PseudoRegister(regState, StateReg.FLAG_PROG, getValve(0, consts[0]));

        busIOAddr.addInput(getValve(25, regData));
		busIOOrder = new BusSplitter(getValve(25, regData), 8, 4);

		cu.compileMicroProgram(new BaseMicroProgram());
		cu.jump(ControlUnit.LABEL_HLT);
	}

	private DataHandler getValve(int cs, DataSource input)
	{
		if (valves[cs] == null)
			valves[cs] = cu.getValve(cs, input);

		return valves[cs];
	}

	public final void addDestination(int cs, DataDestination dest)
	{
		valves[cs].addDestination(dest);
	}

	public void removeDestination(int cs, DataDestination dest)
	{
		valves[cs].removeDestination(dest);
	}

	private DataSource regGet(Regs reg)
	{
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

	public synchronized int getRegValue(Regs reg)
	{
		return regGet(reg).getValue();
	}

	public synchronized int getRegValue(Regs reg, int startbit)
	{
		return ((Register)regGet(reg)).getValue(startbit);
	}

	public synchronized int getMemory(int addr)
	{
		return mem.getValue(addr);
	}

	public synchronized int getMicroMemory(int addr)
	{
		return cu.getMemoryCell(addr);
	}

	public synchronized void setRegKey(int value)
	{
		regKey.setValue(value);
	}

	public synchronized void invertRunState()
	{
		regState.invertBit(StateReg.FLAG_RUN);
	}

	public synchronized void jump(int label)
	{
		cu.jump(label);
	}

	public synchronized void jump()
	{
		cu.setIP(regKey.getValue());
	}

	public synchronized void setMicroMemory()
	{
		cu.setMemoryCell(regKey.getValue());
		cu.setIP(0);
	}

	public synchronized boolean step()
	{
		ControlUnit.Cycle cycle = cu.getCycle();
		int value;

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
}
