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
	private Valve vr00;
	private Valve vr01;
	private Valve valve4all;

	public ControlUnit()
	{
		for (int i = 0; i < consts.length; i++)
			consts[i] = new DataConst(i, 1);

		Valve vr0 = new Valve(mem2instr, new Invertor(15, mem2instr));

		vr00 = new Valve(vr0, new Invertor(14, vr0));
		decoders.put(Decoder.LEFT_INPUT, new DataDecoder(vr00, 12, 2));
		decoders.put(Decoder.RIGHT_INPUT, new DataDecoder(vr00, 8, 2));

		vr01 = new Valve(vr0, 14, vr0);
		decoders.put(Decoder.FLAG_C, new DataDecoder(vr01, 6, 2));
		decoders.put(Decoder.BR_TO, new DataDecoder(vr01, 0, 3));
		valve4all = new Valve(aluOutput, 7, decoders.get(Decoder.BR_TO));

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

	public DataHandler[] getValve(int cs, DataSource input)
	{
		switch (cs) {
		case 18:
			return new DataHandler[] {
				new Valve(input, 1, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 19:
			return new DataHandler[] {
				new Valve(input, 2, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 20:
			return new DataHandler[] {
				new Valve(input, 3, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 21:
			return new DataHandler[] {
				new Valve(input, 4, decoders.get(Decoder.BR_TO))
			};

		case 22:
			return new DataHandler[] {
				new Valve(input, 5, decoders.get(Decoder.BR_TO)),
				valve4all
			};

		case 23:
			return new DataHandler[] {
				new Valve(input, 0, vr00)
			};

		case 24:
			return new DataHandler[] {
				new Valve(input, 1, vr00)
			};
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
