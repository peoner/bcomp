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
	private Register addrreg = new Register(11, cu.getValve(18, aluOutput));
	private Memory mem = new Memory(16, addrreg);
	private Register datareg = new Register(16,	cu.getValve(19, aluOutput));
	private Register instr = new Register(16, cu.getValve(20, aluOutput));
	private Register ip = new Register(11, cu.getValve(21, aluOutput));
	private Register accum = new Register(16, cu.getValve(22, aluOutput));
	private StateReg statereg = new StateReg(13);
	private Register keyreg = new Register(16);
	private Register bufreg = new Register(17);

	public CPU()
	{
		aluOutput.addInput(bufreg);
		DataHandler[] valves = cu.getValve(24, datareg);
		for (DataHandler valve : valves)
			valve.addDestination(mem);
		valves = cu.getValve(23, mem);
		for (DataHandler valve : valves)
			valve.addDestination(datareg);
		// 0 HLT
		// 1 РД -> Правый вход
		// 2 РК -> Правый вход
		// 3 СК -> Правый вход
		// 4 А -> Левый вход
		// 5 РС -> Левый вход
		// 6 КлР -> Левый вход
		// 7 Левый вход: инверсия
		// 8 Правый вход: инверсия
		// 9 АЛУ: + или &
		// 10 АЛУ: +1
		// 11 Сдвиг вправо
		// 12 Сдвиг влево
		// 13 БР(16) -> С
		// 14 БР(15) -> N
		// 15 БР == 0 -> Z
		// 16 0 -> С
		// 17 1 -> С
		// 18 БР -> РА
		// 19 БР -> РД
		// 20 БР -> РК
		// 21 БР -> СК
		// 22 БР -> А
		// 23 Память -> РД
		// 24 РД -> Память
		// 25 Ввод-вывод
		// 26 Сброс всех ВУ
		// 27 DI
		// 28 EI

	}

	public static void main(String[] args)
	{
		CPU cpu = new CPU();
		cpu.bufreg.setValue(0xc0de);
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
