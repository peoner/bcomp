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
	private DataHandler[][] valves = new DataHandler[cu.CONTROL_SIGNAL_COUNT][];
	private Register regAddr = new Register(11, getValves(18, aluOutput));
	private Memory mem = new Memory(16, regAddr);
	private Register regData = new Register(16,	getValves(19, aluOutput));
	private Register regInstr = new Register(16, getValves(20, aluOutput));
	private Register regIP = new Register(11, getValves(21, aluOutput));
	private Register regAccum = new Register(16, getValves(22, aluOutput));
	private StateReg regState = new StateReg(13);
	private Register regKey = new Register(16);
	private Register regBuf;
    private Bus busIOAddr = new Bus(8);
    private BusSplitter busIOOrder;
	private ControlUnit.Cycle cycle = ControlUnit.Cycle.PANEL;

	public CPU()
	{
		getValves(24, regData);
		addDestination(24, mem);

		getValves(23, mem);
		addDestination(23, regData);

		Bus aluRight = new Bus(getValves(1, regData));
		aluRight.addInput(getValves(2, regInstr));
		aluRight.addInput(getValves(3, regIP));

		Bus aluLeft = new Bus(getValves(4, regAccum));
		aluLeft.addInput(getValves(5, regState));
		aluLeft.addInput(getValves(6, regKey));

		DataSource notLeft = new DataInverter(aluLeft, getValves(7, consts[1]));
		DataSource notRight = new DataInverter(aluRight, getValves(8, consts[1]));
		DataSource adder = new DataAdder(notLeft, notRight, getValves(9, consts[1]));

		regBuf = new Register(17,
			new DataIncrement(adder, getValves(10, consts[1])),
			new DataRotateLeft(regAccum, regState, getValves(12, consts[1])),
			new DataRotateRight(regAccum, regState, getValves(11, consts[1])));
		aluOutput.addInput(regBuf);

		PseudoRegister regStateEI = new PseudoRegister(regState, StateReg.FLAG_EI, getValves(27, consts[0]));
		getValves(28, consts[1]);
		addDestination(28, regStateEI);

		PseudoRegister regStateC = new PseudoRegister(regState, StateReg.FLAG_C, getValves(13, regBuf));
		getValves(16, consts[0]);
		addDestination(16, regStateC);
		getValves(17, consts[1]);
		addDestination(17, regStateC);

		PseudoRegister regStateN = new PseudoRegister(regState, StateReg.FLAG_N, getValves(14, regBuf));
		PseudoRegister regStateZ = new PseudoRegister(regState, StateReg.FLAG_Z, getValves(15, regBuf));
		PseudoRegister regStateProg = new PseudoRegister(regState, StateReg.FLAG_PROG, getValves(0, consts[0]));

        busIOAddr.addInput(getValves(25, regData));
		busIOOrder = new BusSplitter(getValves(25, regData)[0], 8, 4);

		cu.compileMicroProgram(new BaseMicroProgram());
		cu.jump(ControlUnit.LABEL_HLT);
	}

	private DataHandler[] getValves(int cs, DataSource input)
	{
		if (valves[cs] == null)
			valves[cs] = cu.getValves(cs, input);

		return valves[cs];
	}

	public final void addDestination(int cs, DataDestination dest)
	{
		for (DataHandler valve : valves[cs])
			valve.addDestination(dest);
	}

	public void removeDestination(int cs, DataDestination dest)
	{
		for (DataHandler valve : valves[cs])
			valve.removeDestination(dest);
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

	public synchronized int getRegWidth(Regs reg)
	{
		return regGet(reg).getWidth();
	}

	public synchronized void setRegKey(int value)
	{
		regKey.setValue(value);
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
for (DataSource src : getValves(5, regState))
	System.out.println("XXX: " + Integer.toString(src.getValue(), 16));
for (DataSource src : getValves(1, regState))
	System.out.println("YYY: " + Integer.toString(src.getValue(), 16));
	
		return regState.getValue(StateReg.FLAG_PROG) == 1;
	}

	// XXX: TEMPORARY!!!
	public synchronized void setMemoryAt(int addr, int value)
	{
		mem.setValue(addr, value);
	}
	public synchronized void jump(int value)
	{
		regIP.setValue(value);
	}
}
