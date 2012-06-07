/**
* $Id$
*/

package com.ifmo.it.bcomp;

import java.util.HashMap;
import com.ifmo.it.elements.*;

public class ControlUnit
{
	public static final int CONTROL_SIGNAL_COUNT = 29;

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
	private DataHandler vr00;
	private DataHandler vr01;
	private DataHandler valve4all;
	private DataHandler valve4ctrlcmd;

	public ControlUnit()
	{
		for (int i = 0; i < consts.length; i++)
			consts[i] = new DataConst(i, 1);

		Valve vr0 = new Valve(mem2instr, new Inverter(15, mem2instr));

		vr00 = new Valve(vr0, new Inverter(14, vr0));
		decoders.put(Decoder.LEFT_INPUT, new DataDecoder(vr00, 12, 2));
		decoders.put(Decoder.RIGHT_INPUT, new DataDecoder(vr00, 8, 2));

		vr01 = new Valve(vr0, 14, vr0);
		decoders.put(Decoder.FLAG_C, new DataDecoder(vr01, 6, 2));
		decoders.put(Decoder.BR_TO, new DataDecoder(vr01, 0, 3));
		valve4all = new Valve(aluOutput, 7, decoders.get(Decoder.BR_TO));

		Valve vr1 = new Valve(mem2instr, 15, mem2instr);
		decoders.put(Decoder.CONTROL_CMD_REG, new DataDecoder(vr1, 12, 2));
		valve4ctrlcmd = new DummyValve(consts[0], vr1);
		DataDecoder bitselector = new DataDecoder(vr1, 8, 4);
		Bus selectedbit = new Bus(1);
		for (int i = 0; i < 16; i++)
			selectedbit.addInput(new Valve(aluOutput, i, 1, i, bitselector));
		ForcedValve av = new ForcedValve(vr1, 0, 8,
			new Comparer(selectedbit, 14, vr1),
			new DummyValve(consts[0], vr0));
		av.addDestination(ip);
	}

	public DataHandler[] getValves(int cs, DataSource input)
	{
		// Not used: 26 Сброс всех ВУ

		switch (cs) {
		case 0:
			// HLT
			return new DataHandler[] {
				new Valve(input, 3, vr01)
			};

		case 1:
			// РД -> Правый вход
			return new DataHandler[] {
				new Valve(input, 1, decoders.get(Decoder.RIGHT_INPUT)),
				new Valve(input, 1, decoders.get(Decoder.CONTROL_CMD_REG))
			};

		case 2:
			// РК -> Правый вход
			return new DataHandler[] {
				new Valve(input, 2, decoders.get(Decoder.RIGHT_INPUT)),
				new Valve(input, 2, decoders.get(Decoder.CONTROL_CMD_REG))
			};

		case 3:
			// СК -> Правый вход
			return new DataHandler[] {
				new Valve(input, 3, decoders.get(Decoder.RIGHT_INPUT)),
				valve4ctrlcmd
			};

		case 4:
			// А -> Левый вход
			return new DataHandler[] {
				new Valve(input, 1, decoders.get(Decoder.LEFT_INPUT)),
				new Valve(input, 3, decoders.get(Decoder.CONTROL_CMD_REG))
			};

		case 5:
			// РС -> Левый вход
			return new DataHandler[] {
				new Valve(input, 2, decoders.get(Decoder.LEFT_INPUT)),
				new Valve(input, 0, decoders.get(Decoder.CONTROL_CMD_REG))
			};

		case 6:
			// КлР -> Левый вход
			return new DataHandler[] {
				new Valve(input, 3, decoders.get(Decoder.LEFT_INPUT)),
				valve4ctrlcmd
			};

		case 7:
			// Левый вход: инверсия
			return new DataHandler[] {
				new ForcedValve(input, 6, vr00),
				valve4ctrlcmd
			};

		case 8:
			// Правый вход: инверсия
			return new DataHandler[] {
				new ForcedValve(input, 7, vr00),
				valve4ctrlcmd
			};

		case 9:
			// АЛУ: + или &
			return new DataHandler[] {
				new ForcedValve(input, 5, vr00),
				valve4ctrlcmd
			};

		case 10:
			// АЛУ: +1
			return new DataHandler[] {
				new ForcedValve(input, 4, vr00),
				valve4ctrlcmd
			};

		case 11:
			// Сдвиг вправо
			return new DataHandler[] {
				new Valve(input, 2, vr00)
			};

		case 12:
			// Сдвиг влево
			return new DataHandler[] {
				new Valve(input, 3, vr00)
			};

		case 13:
			// БР(16) -> С
			return new DataHandler[] {
				new Valve(input, 16, 1, 1, decoders.get(Decoder.FLAG_C))
			};

		case 14:
			// БР(15) -> N
			return new DataHandler[] {
				new Valve(input, 15, 1, 5, vr01)
			};

		case 15:
			// БР == 0 -> Z
			return new DataHandler[] {
				new DataCheckZero(input, 16, 4, vr01)
			};

		case 16:
			// 0 -> С
			return new DataHandler[] {
				new Valve(input, 2, decoders.get(Decoder.FLAG_C))
			};

		case 17:
			// 1 -> С
			return new DataHandler[] {
				new Valve(input, 3, decoders.get(Decoder.FLAG_C))
			};

		case 18:
			// БР -> РА
			return new DataHandler[] {
				new Valve(input, 1, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 19:
			// БР -> РД
			return new DataHandler[] {
				new Valve(input, 2, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 20:
			// БР -> РК
			return new DataHandler[] {
				new Valve(input, 3, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 21:
			// БР -> СК
			return new DataHandler[] {
				new Valve(input, 4, decoders.get(Decoder.BR_TO))
			};

		case 22:
			// БР -> А
			return new DataHandler[] {
				new Valve(input, 5, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 23:
			// Память -> РД
			return new DataHandler[] {
				new Valve(input, 0, vr00)
			};

		case 24:
			// РД -> Память
			return new DataHandler[] {
				new Valve(input, 1, vr00)
			};

		case 25:
			// Ввод-вывод
			return new DataHandler[] {
				new Valve(input, 8, vr00)
			};

		case 27:
			// DI
			return new DataHandler[] {
				new Valve(input, 10, vr01)
			};

		case 28:
			// EI
			return new DataHandler[] {
				new Valve(input, 11, vr01)
			};
		}

		return null;
	}

	public void step()
	{
		mem2instr.setValue(1);
	}

	public Bus getALUOuput()
	{
		return aluOutput;
	}

	public DataSource[] getConsts()
	{
		return consts;
	}

	public Memory getMemory()
	{
		return mem;
	}
}
