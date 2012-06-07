/**
* $Id$
*/

package com.ifmo.it.bcomp;

import com.ifmo.it.elements.*;

public class CPU
{
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
	}

	private DataHandler[] getValves(int cs, DataSource input)
	{
		if (valves[cs] == null)
			valves[cs] = cu.getValves(cs, input);

		return valves[cs];
	}

	public void addDestination(int cs, DataDestination dest)
	{
		for (DataHandler valve : valves[cs])
			valve.addDestination(dest);
	}

	public void removeDestination(int cs, DataDestination dest)
	{
		for (DataHandler valve : valves[cs])
			valve.removeDestination(dest);
	}

	public static void main(String[] args)
	{
		CPU cpu = new CPU();
		cpu.regBuf.setValue(0x11111);
		cpu.regAccum.setValue(0x2222);
		cpu.regAddr.setValue(0x3333);
		cpu.regData.setValue(0x4444);
		cpu.regInstr.setValue(0x5555);
		cpu.regIP.setValue(0x6666);
		cpu.regState.setValue(0x7777);
		cpu.regKey.setValue(0x8888);

		cpu.cu.getMemory().setValueAt(0, Integer.parseInt(args[0], 16));
		cpu.mem.setValueAt(0, Integer.parseInt(args[1], 16));

		cpu.cu.step();

		System.out.println("БР: " + Integer.toString(cpu.regBuf.getValue(), 16));
		System.out.println("А : " + Integer.toString(cpu.regAccum.getValue(), 16));
		System.out.println("РА: " + Integer.toString(cpu.regAddr.getValue(), 16));
		System.out.println("РД: " + Integer.toString(cpu.regData.getValue(), 16));
		System.out.println("РК: " + Integer.toString(cpu.regInstr.getValue(), 16));
		System.out.println("СК: " + Integer.toString(cpu.regIP.getValue(), 16));
		System.out.println("РС: " + Integer.toString(cpu.regState.getValue(), 16));
		System.out.println("КЛ: " + Integer.toString(cpu.regKey.getValue(), 16));
		System.out.println("Память[0]: " + Integer.toString(cpu.mem.getValueAt(0), 16));
	}
}
