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
		CS25,	// Ввод-вывод
		CS26,	// Сброс всех ВУ
		CS27,	// DI
		CS28	// EI
	}

	private enum Decoder {
		LEFT_INPUT,
		RIGHT_INPUT,
		FLAG_C,
		BR_TO,
		CONTROL_CMD_REG
	}

	private MicroIP ip = new MicroIP(8);
	private Memory mem = new Memory(16, ip);
	private Valve mem2instr = new Valve(mem);
	private Register instr = new Register(16, mem2instr);
	private Bus aluOutput = new Bus(16);
	private HashMap<Decoder, DataHandler> decoders = new HashMap<Decoder, DataHandler>();
	private DataSource[] consts = new DataSource[2];
	private Valve vr00;
	private Valve valve4zero;

	public ControlUnit()
	{
		for (int i = 0; i < consts.length; i++)
			consts[i] = new DataConst(i, 1);

		Valve vr0 = new Valve(mem2instr, new Invertor(15, mem2instr));

		vr00 = new Valve(vr0, new Invertor(14, vr0));
		decoders.put(Decoder.LEFT_INPUT, new DataDecoder(vr00, 12, 2));
		decoders.put(Decoder.RIGHT_INPUT, new DataDecoder(vr00, 8, 2));

		Valve vr01 = new Valve(vr0, 14, vr0);
		decoders.put(Decoder.FLAG_C, new DataDecoder(vr01, 6, 2));
		decoders.put(Decoder.BR_TO, new DataDecoder(vr01, 0, 3));
		valve4zero = new Valve(consts[1], 7, decoders.get(Decoder.BR_TO));

		Valve vr1 = new Valve(mem2instr, 15, mem2instr);
		decoders.put(Decoder.CONTROL_CMD_REG, new DataDecoder(vr1, 12, 2));
		DataDecoder bitselector = new DataDecoder(vr1, 8, 4);
		Bus selectedbit = new Bus(1);
		for (int i = 0; i < 16; i++)
			selectedbit.addInput(new Valve(aluOutput, i, 1, i, bitselector));
		ForcedValve av = new ForcedValve(vr1, 0, 8,
			new Comparer(selectedbit, 14, vr1),
			new DummyValve(consts[0], vr0));
		av.addDestination(ip);
	}

	public DataHandler getValve(ControlSignal cs, DataSource input)
	{
		switch (cs) {
		case CS18:
			return new Valve(input,
				new Valve(consts[1], 1, decoders.get(Decoder.BR_TO)),
				valve4zero);

		case CS19:
			return new Valve(input,
				new Valve(consts[1], 2, decoders.get(Decoder.BR_TO)),
				valve4zero);

		case CS20:
			return new Valve(input,
				new Valve(consts[1], 3, decoders.get(Decoder.BR_TO)),
				valve4zero);

		case CS21:
			return new Valve(input, 4, decoders.get(Decoder.BR_TO));

		case CS22:
			return new Valve(input,
				new Valve(consts[1], 5, decoders.get(Decoder.BR_TO)),
				valve4zero);

		case CS23:
			return new Valve(input, 0, vr00);

		case CS24:
			return new Valve(input, 1, vr00);
		}

		return null;
	}

	public void step()
	{
		mem2instr.setValue(1);
	}

	public Bus getAluOuput()
	{
		return aluOutput;
	}

	public Memory getMemory()
	{
		return mem;
	}
}
