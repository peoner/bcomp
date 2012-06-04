/**
* $Id$
*/

package com.ifmo.it.bcomp;

import java.util.HashMap;
import com.ifmo.it.elements.*;

public class ControlUnit
{
	public enum ControlSignal {
		CS0,	// HLT
		CS1,	// РД -> Правый вход
		CS2,	// РК -> Правый вход
		CS3,	// СК -> Правый вход
		CS4,	// А -> Левый вход
		CS5,	// РС -> Левый вход
		CS6,	// КлР -> Левый вход
		CS7,	// Левый вход: инверсия
		CS8,	// Правый вход: инверсия
		CS9,	// АЛУ: + или &
		CS10,	// АЛУ: +1
		CS11,	// Сдвиг вправо
		CS12,	// Сдвиг влево
		CS13,	// БР(16) -> С
		CS14,	// БР(15) -> N
		CS15,	// БР == 0 -> Z
		CS16,	// 0 -> С
		CS17,	// 1 -> С
		CS18,	// БР -> РА
		CS19,	// БР -> РД
		CS20,	// БР -> РК
		CS21,	// БР -> СК
		CS22,	// БР -> А
		CS23,	// Память -> РД
		CS24,	// РД -> Память
		CS25,
		CS26,
		CS27,
		CS28,
		CS29,	// Ввод-вывод
		CS30,	// БР -> РА, РД, РК, А
		CS31,	// Сброс В3 и В6 для УМК
		CS32,	// УМК: РС
		CS33,	// УМК: РД
		CS34,	// УМК: РК
		CS35,	// УМК: А
		CS36,	// УМК: АЛУ +
		CS37	// УМК: АЛУ без +1
	}

	private Register ip;
	private Register instr;
	private Memory mem;
	private ValveSource mem2instr;
	private HashMap<ControlSignal, DataHandler> CS = new HashMap<ControlSignal, DataHandler>();
	private Bus aluOutput;

	public ControlUnit()
	{
		ip = new MicroIP(8);
		mem = new Memory(16, ip);
		mem2instr = new ValveSource(mem);
		instr = new Register(16, mem2instr);
		aluOutput = new Bus(16);

		DataPart instr15 = new DataPart(mem2instr, 15, 1);
		Invertor notinstr15 = new Invertor(instr15);
		ValveSource vr0 = new ValveSource(mem2instr, notinstr15);
		Valve0 ipinc = new Valve0(notinstr15);
		ipinc.addDestination(ip);

		DataPart vr014 = new DataPart(vr0, 14, 1);
		ValveSource vr00 = new ValveSource(vr0, new Invertor(vr014));
		Decoder leftinput = new Decoder(new DataPart(vr00, 12, 2));
		CS.put(ControlSignal.CS4, new DataPart(leftinput, 1, 1));
		CS.put(ControlSignal.CS5, new DataPart(leftinput, 2, 1));
		CS.put(ControlSignal.CS6, new DataPart(leftinput, 3, 1));
		Decoder rightinput = new Decoder(new DataPart(vr00, 8, 2));
		CS.put(ControlSignal.CS1, new DataPart(rightinput, 1, 1));
		CS.put(ControlSignal.CS2, new DataPart(rightinput, 2, 1));
		CS.put(ControlSignal.CS3, new DataPart(rightinput, 3, 1));
		CS.put(ControlSignal.CS8, new DataPart(vr00, 7, 1));
		CS.put(ControlSignal.CS7, new DataPart(vr00, 6, 1));
		CS.put(ControlSignal.CS9, new DataPart(vr00, 5, 1));
		CS.put(ControlSignal.CS10, new DataPart(vr00, 4, 1));
		CS.put(ControlSignal.CS12, new DataPart(vr00, 3, 1));
		CS.put(ControlSignal.CS11, new DataPart(vr00, 2, 1));
		CS.put(ControlSignal.CS24, new DataPart(vr00, 1, 1));
		CS.put(ControlSignal.CS23, new DataPart(vr00, 0, 1));

		ValveSource vr01 = new ValveSource(vr0, vr014);
		CS.put(ControlSignal.CS29, new DataPart(vr01, 8, 1));
		Decoder c = new Decoder(new DataPart(vr01, 6, 2));
		CS.put(ControlSignal.CS13, new DataPart(c, 1, 1));
		CS.put(ControlSignal.CS16, new DataPart(c, 2, 1));
		CS.put(ControlSignal.CS17, new DataPart(c, 3, 1));
		CS.put(ControlSignal.CS14, new DataPart(vr01, 5, 1));
		CS.put(ControlSignal.CS15, new DataPart(vr01, 4, 1));
		CS.put(ControlSignal.CS0, new DataPart(vr01, 3, 1));
		Decoder aluout = new Decoder(new DataPart(vr01, 0, 3));
		CS.put(ControlSignal.CS18, new DataPart(aluout, 1, 1));
		CS.put(ControlSignal.CS19, new DataPart(aluout, 2, 1));
		CS.put(ControlSignal.CS20, new DataPart(aluout, 3, 1));
		CS.put(ControlSignal.CS21, new DataPart(aluout, 4, 1));
		CS.put(ControlSignal.CS22, new DataPart(aluout, 5, 1));
		CS.put(ControlSignal.CS30, new DataPart(aluout, 7, 1));

		CS.put(ControlSignal.CS31, new Valve0(instr15));
		ValveSource vr1 = new ValveSource(mem2instr, instr15);
		Decoder reg = new Decoder(new DataPart(vr1, 12, 2));
		CS.put(ControlSignal.CS32, new DataPart(reg, 0, 1));
		CS.put(ControlSignal.CS33, new DataPart(reg, 1, 1));
		CS.put(ControlSignal.CS34, new DataPart(reg, 2, 1));
		CS.put(ControlSignal.CS35, new DataPart(reg, 3, 1));
		DummyValve dummy = new DummyValve(reg);
		Invertor notdummy = new Invertor(dummy);
		CS.put(ControlSignal.CS36, notdummy);
		CS.put(ControlSignal.CS37, notdummy);
		Decoder choosebit = new Decoder(new DataPart(vr1, 8, 4));
		BusSplitter[] splitter = new BusSplitter[16];
		ValveSource[] cb = new ValveSource[16];
		Bus cbb = new Bus(1);
		for (int i = 0; i < cb.length; i++) {
			splitter[i] = new BusSplitter(aluOutput, i, 1);
			cb[i] = new ValveSource(splitter[i], new DataPart(choosebit, i, 1));
			cbb.addInput(cb[i]);
		}
		Comparer comparer = new Comparer(cbb, new DataPart(vr1, 14, 1));
		ValveActive va = new ValveActive(new BusSplitter(vr1, 0, 8), comparer);
		va.addDestination(ip);
	}

	public void step()
	{
		mem2instr.setValue(1);
	}

	public static void main(String[] args)
	{
		ControlUnit cu = new ControlUnit();

		cu.mem.setValueAt(0, Integer.parseInt(args[0], 16));
		cu.step();
	}
}
