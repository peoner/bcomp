/**
* $Id$
*/

package ru.ifmo.it.bcomp;

import java.util.HashMap;
import ru.ifmo.it.elements.*;

public class ControlUnit
{
	public static final int CONTROL_SIGNAL_COUNT = 29;

	public enum Cycle {
		INSTRFETCH, ADDRFETCH, EXECUTION, INTERRUPT, PANEL
	}

	private enum Decoder {
		LEFT_INPUT, RIGHT_INPUT, FLAG_C, BR_TO, CONTROL_CMD_REG
	}

	private MicroIP ip = new MicroIP(8);
	private Memory mem = new Memory(16, ip);
	private Valve instr = new Valve(mem);
	private Bus aluOutput = new Bus(16);
	private HashMap<Decoder, DataHandler> decoders = new HashMap<Decoder, DataHandler>();
	private DataSource[] consts = new DataSource[2];
	private DataHandler vr00;
	private DataHandler vr01;
	private DataHandler valve2all;
	private DataHandler valve4ctrlcmd;
	private static final String[] labels = new String[] {
		"ADDRFETCH", "EXEC", "INTR", "EXECCONT", "ADDR", "READ", "WRITE", "START", "HLT"
	};
	private int[] labelsaddr = new int[labels.length];
	private static final int LABEL_CYCLE_ADDR = 0;
	private static final int LABEL_CYCLE_EXEC = 1;
	private static final int LABEL_CYCLE_INTR = 2;
	private static final int LABEL_CYCLE_EXECCONT = 3;
	public static final int LABEL_ADDR = 4;
	public static final int LABEL_READ = 5;
	public static final int LABEL_WRITE = 6;
	public static final int LABEL_START = 7;
	public static final int LABEL_HLT = 8;

	public ControlUnit()
	{
		for (int i = 0; i < consts.length; i++)
			consts[i] = new DataConst(i, 1);

		Valve vr0 = new Valve(instr, new Inverter(15, instr));

		vr00 = new Valve(vr0, new Inverter(14, vr0));
		decoders.put(Decoder.LEFT_INPUT, new DataDecoder(vr00, 12, 2));
		decoders.put(Decoder.RIGHT_INPUT, new DataDecoder(vr00, 8, 2));

		vr01 = new Valve(vr0, 14, vr0);
		decoders.put(Decoder.FLAG_C, new DataDecoder(vr01, 6, 2));
		decoders.put(Decoder.BR_TO, new DataDecoder(vr01, 0, 3));
		valve2all = new Valve(consts[1], 7, decoders.get(Decoder.BR_TO));

		Valve vr1 = new Valve(instr, 15, instr);
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

	public DataHandler getValve(int cs, DataSource input)
	{
		// Not used: 26 Сброс всех ВУ

		switch (cs) {
		case 0:
			// HLT
			return new Valve(input, 3, vr01);

		case 1:
			// РД -> Правый вход
			return new Valve(input,
				new ForcedValve(consts[1], 1, decoders.get(Decoder.RIGHT_INPUT)),
				new ForcedValve(consts[1], 1, decoders.get(Decoder.CONTROL_CMD_REG))
			);

		case 2:
			// РК -> Правый вход
			return new Valve(input,
				new ForcedValve(consts[1], 2, decoders.get(Decoder.RIGHT_INPUT)),
				new ForcedValve(consts[1], 2, decoders.get(Decoder.CONTROL_CMD_REG))
			);

		case 3:
			// СК -> Правый вход
			return new Valve(input,
				new ForcedValve(consts[1], 3, decoders.get(Decoder.RIGHT_INPUT)),
				valve4ctrlcmd
			);

		case 4:
			// А -> Левый вход
			return new Valve(input,
				new ForcedValve(consts[1], 1, decoders.get(Decoder.LEFT_INPUT)),
				new ForcedValve(consts[1], 3, decoders.get(Decoder.CONTROL_CMD_REG))
			);

		case 5:
			// РС -> Левый вход
			return new Valve(input,
				new ForcedValve(consts[1], 2, decoders.get(Decoder.LEFT_INPUT)),
				new ForcedValve(consts[1], 0, decoders.get(Decoder.CONTROL_CMD_REG))
			);

		case 6:
			// КлР -> Левый вход
			return new Valve(input,
				new ForcedValve(consts[1], 3, decoders.get(Decoder.LEFT_INPUT)),
				valve4ctrlcmd
			);

		case 7:
			// Левый вход: инверсия
			return new ForcedValve(input,
				new ForcedValve(consts[1], 6, vr00),
				valve4ctrlcmd
			);

		case 8:
			// Правый вход: инверсия
			return new ForcedValve(input,
				new ForcedValve(consts[1], 7, vr00),
				valve4ctrlcmd
			);

		case 9:
			// АЛУ: + или &
			return new ForcedValve(input,
				new ForcedValve(consts[1], 5, vr00),
				valve4ctrlcmd
			);

		case 10:
			// АЛУ: +1
			return new ForcedValve(input,
				new ForcedValve(consts[1], 4, vr00),
				valve4ctrlcmd
			);

		case 11:
			// Сдвиг вправо
			return new Valve(input, 2, vr00);

		case 12:
			// Сдвиг влево
			return new Valve(input, 3, vr00);

		case 13:
			// БР(16) -> С
			return new Valve(input, 16, 1, 1, decoders.get(Decoder.FLAG_C));

		case 14:
			// БР(15) -> N
			return new Valve(input, 15, 1, 5, vr01);

		case 15:
			// БР == 0 -> Z
			return new DataCheckZero(input, 16, 4, vr01);

		case 16:
			// 0 -> С
			return new Valve(input, 2, decoders.get(Decoder.FLAG_C));

		case 17:
			// 1 -> С
			return new Valve(input, 3, decoders.get(Decoder.FLAG_C));

		case 18:
			// БР -> РА
			return new Valve(input,
				new Valve(consts[1], 1, decoders.get(Decoder.BR_TO)),
				valve2all
			);

		case 19:
			// БР -> РД
			return new Valve(input,
				new Valve(consts[1], 2, decoders.get(Decoder.BR_TO)),
				valve2all
			);

		case 20:
			// БР -> РК
			return new Valve(input,
				new Valve(consts[1], 3, decoders.get(Decoder.BR_TO)),
				valve2all
			);

		case 21:
			// БР -> СК
			return new Valve(input, 4, decoders.get(Decoder.BR_TO));

		case 22:
			// БР -> А
			return new Valve(input,
				new Valve(consts[1], 5, decoders.get(Decoder.BR_TO)),
				valve2all
			);

		case 23:
			// Память -> РД
			return new Valve(input, 0, vr00);

		case 24:
			// РД -> Память
			return new Valve(input, 1, vr00);

		case 25:
			// Ввод-вывод
			return new Valve(input, 8, vr00);

		case 27:
			// DI
			return new Valve(input, 10, vr01);

		case 28:
			// EI
			return new Valve(input, 11, vr01);
		}

		return null;
	}

	public Bus getALUOuput()
	{
		return aluOutput;
	}

	public DataSource[] getConsts()
	{
		return consts;
	}

	private int getLabelAddr(String[][] mp, String label)
	{
		for (int i = 0; i < mp.length; i++)
			if (mp[i][0].equals(label))
				return i;

		return -1;
	}

	public void compileMicroProgram(MicroProgram mpsrc) throws Exception
	{
		String[][] mp = mpsrc.getMicroProgram();

		for (int i = 0; i < mp.length; i++) {
			int cmd = Integer.parseInt(mp[i][1], 16);

			if (mp[i][0] != null)
				for (int j = 0; j < labels.length; j++)
					if (mp[i][0].equals(labels[j]))
						labelsaddr[j] = i;

			if (mp[i][2] != null) {
				int label = getLabelAddr(mp, mp[i][2]);
				if (label < 0)
					throw new Exception("Label " + mp[i][2] + " not found!");

				cmd += label;
			}
			mem.setValue(i, cmd);
		}

		for (int i = 0; i < labels.length; i++)
			if (labelsaddr[i] == 0)
				throw new Exception("Required label '" + labels[i] + "' not found");
	}

	public DataSource getIP()
	{
		return ip;
	}

	public synchronized void setIP(int value)
	{
		ip.setValue(value);
	}

	public synchronized void jump(int label)
	{
		ip.setValue(labelsaddr[label]);
	}

	public DataSource getInstr()
	{
		return instr;
	}

	public int getMemoryCell(int addr)
	{
		return mem.getValue(addr);
	}

	public synchronized void setMemoryCell(int value)
	{
		mem.setValue(value);
		instr.setValue(0);
	}

	public synchronized void step()
	{
		instr.setValue(1);
	}

	public Cycle getCycle()
	{
		int ipvalue = ip.getValue();

		if (ipvalue < labelsaddr[LABEL_CYCLE_ADDR])
			return Cycle.INSTRFETCH;

		if (ipvalue < labelsaddr[LABEL_CYCLE_EXEC])
			return Cycle.ADDRFETCH;

		if (ipvalue < labelsaddr[LABEL_CYCLE_INTR])
			return Cycle.EXECUTION;

		if (ipvalue < labelsaddr[LABEL_ADDR])
			return Cycle.INTERRUPT;

		if (ipvalue < labelsaddr[LABEL_CYCLE_EXECCONT])
			return Cycle.PANEL;

		return Cycle.EXECUTION;
	}
}
