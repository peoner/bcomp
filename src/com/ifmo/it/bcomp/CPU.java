/**
* $Id$
*/

package com.ifmo.it.bcomp;

import java.util.HashMap;
import com.ifmo.it.elements.*;

public class CPU
{
	private ControlUnit cu = new ControlUnit();
	private Bus aluOutput = cu.getAluOuput();
	private Register addrreg = new Register(11, cu.getValve(ControlUnit.ControlSignal.CS18, aluOutput));
	private Memory mem = new Memory(16, addrreg);
	private Register datareg = new Register(16,
		cu.getValve(ControlUnit.ControlSignal.CS19, aluOutput),
		cu.getValve(ControlUnit.ControlSignal.CS23, mem));
	private Register instr = new Register(16, cu.getValve(ControlUnit.ControlSignal.CS20, aluOutput));
	private Register ip = new Register(11, cu.getValve(ControlUnit.ControlSignal.CS21, aluOutput));
	private Register accum = new Register(16, cu.getValve(ControlUnit.ControlSignal.CS22, aluOutput));
	private StateReg statereg = new StateReg(13);
	private Register keyreg = new Register(16);
	private Register bufreg = new Register(17);

	public CPU()
	{
		aluOutput.addInput(bufreg);
		DataHandler v = cu.getValve(ControlUnit.ControlSignal.CS24, datareg);
		v.addDestination(mem);
	}

	public static void main(String[] args)
	{
		CPU cpu = new CPU();
		cpu.accum.setValue(0xbeef);
		cpu.datareg.setValue(0xacdc);
		cpu.cu.getMemory().setValueAt(0, Integer.parseInt(args[0], 16));
		cpu.mem.setValueAt(0, Integer.parseInt(args[1], 16));
		cpu.cu.step();
		System.out.println("А : " + Integer.toString(cpu.accum.getValue(), 16));
		System.out.println("РА: " + Integer.toString(cpu.addrreg.getValue(), 16));
		System.out.println("РД: " + Integer.toString(cpu.datareg.getValue(), 16));
		System.out.println("РК: " + Integer.toString(cpu.instr.getValue(), 16));
		System.out.println("СК: " + Integer.toString(cpu.ip.getValue(), 16));
		System.out.println("Память[0]: " + Integer.toString(cpu.mem.getValueAt(0), 16));
	}
}
