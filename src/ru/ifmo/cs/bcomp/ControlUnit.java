/**
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.elements.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ControlUnit {
	public static final int CONTROL_SIGNAL_COUNT = 29;

	public enum Cycle {
		INSTRFETCH, ADDRFETCH, EXECUTION, INTERRUPT, PANEL
	}

	private enum Decoders {
		LEFT_INPUT, RIGHT_INPUT, FLAG_C, BR_TO, CONTROL_CMD_REG
	}

	private MicroIP ip = new MicroIP(8);
	private Memory mem = new Memory(16, ip);
	private Valve instr = new Valve(mem);
	private EnumMap<Decoders, DataHandler> decoders = new EnumMap<Decoders, DataHandler>(Decoders.class);
	private DataHandler vr00;
	private DataHandler vr01;
	private DataHandler valve4ctrlcmd;
	private static final String[] labels = new String[] {
		"ADDRGET", "EXEC", "INTR", "EXECCNT", "ADDR", "READ", "WRITE", "START", "STP"
	};
	private int[] labelsaddr = new int[labels.length];
	private static final int LABEL_CYCLE_ADDR = 0;
	private static final int LABEL_CYCLE_EXEC = 1;
	private static final int LABEL_CYCLE_INTR = 2;
	private static final int LABEL_CYCLE_EXECCNT = 3;
	public static final int LABEL_ADDR = 4;
	public static final int LABEL_READ = 5;
	public static final int LABEL_WRITE = 6;
	public static final int LABEL_START = 7;
	public static final int LABEL_STP = 8;

	public ControlUnit(Bus aluOutput) {
		Valve vr0 = new Valve(instr, new Inverter(15, instr));

		vr00 = new Valve(vr0, new Inverter(14, vr0));
		decoders.put(Decoders.LEFT_INPUT, new DataDecoder(vr00, 12, 2));
		decoders.put(Decoders.RIGHT_INPUT, new DataDecoder(vr00, 8, 2));

		vr01 = new Valve(vr0, 14, vr0);
		decoders.put(Decoders.FLAG_C, new DataDecoder(vr01, 6, 2));
		decoders.put(Decoders.BR_TO, new DataDecoder(vr01, 0, 3));

		Valve vr1 = new Valve(instr, 15, instr);
		decoders.put(Decoders.CONTROL_CMD_REG, new DataDecoder(vr1, 12, 2));
		valve4ctrlcmd = new DummyValve(Consts.consts[0], vr1);
		DataDecoder bitselector = new DataDecoder(vr1, 8, 4);
		Valve[] bits = new Valve[16];
		for (int i = 0; i < 16; i++)
			bits[i] = new Valve(aluOutput, i, 1, i, bitselector);
		ForcedValve av = new ForcedValve(vr1, 8,
			new Comparer(vr1, 14, bits),
			new DummyValve(Consts.consts[0], vr0));
		av.addDestination(ip);
	}

	public DataHandler getValve(int cs, DataSource ... inputs) {
		// Not used: 26 Сброс всех ВУ

		switch (cs) {
			case 0:
				// HLT
				return new Valve(inputs[0], 3, vr01);

			case 1:
				// РД -> Правый вход
				return new ValveOnce(inputs[0], 1,
					decoders.get(Decoders.RIGHT_INPUT),
					decoders.get(Decoders.CONTROL_CMD_REG)
				);

			case 2:
				// РК -> Правый вход
				return new ValveOnce(inputs[0], 2,
					decoders.get(Decoders.RIGHT_INPUT),
					decoders.get(Decoders.CONTROL_CMD_REG)
				);

			case 3:
				// СК -> Правый вход
				return new ValveOnce(inputs[0], 3, decoders.get(Decoders.RIGHT_INPUT));

			case 4:
				// А -> Левый вход
				return new ValveOnce(inputs[0],
					new DataPart(1, decoders.get(Decoders.LEFT_INPUT)),
					new DataPart(3, decoders.get(Decoders.CONTROL_CMD_REG))
				);

			case 5:
				// РС -> Левый вход
				return new ValveOnce(inputs[0],
					new DataPart(2, decoders.get(Decoders.LEFT_INPUT)),
					new DataPart(0, decoders.get(Decoders.CONTROL_CMD_REG))
				);

			case 6:
				// КлР -> Левый вход
				return new ValveOnce(inputs[0], 3, decoders.get(Decoders.LEFT_INPUT));

			case 7:
				// Левый вход: инверсия
				return new DataInverter(inputs[0],
					new DataPart(6, vr00),
					valve4ctrlcmd
				);

			case 8:
				// Правый вход: инверсия
				return new DataInverter(inputs[0],
					new DataPart(7, vr00),
					valve4ctrlcmd
				);

			case 9:
				// АЛУ: + или &
				return new DataAdder(inputs[0], inputs[1], inputs[2],
					new DataPart(5, vr00),
					valve4ctrlcmd
				);

			case 10:
				// АЛУ: +1
				return new ValveOnce(inputs[0], 4, vr00);

			case 11:
				// Сдвиг вправо
				return new DataRotateRight(inputs[0], inputs[1], 2, vr00);

			case 12:
				// Сдвиг влево
				return new DataRotateLeft(inputs[0], inputs[1], 3, vr00);

			case 13:
				// БР(16) -> С
				return new Valve(inputs[0], 16, 1, 1, decoders.get(Decoders.FLAG_C));

			case 14:
				// БР(15) -> N
				return new Valve(inputs[0], 15, 1, 5, vr01);

			case 15:
				// БР == 0 -> Z
				return new DataCheckZero(inputs[0], 16, 4, vr01);

			case 16:
				// 0 -> С
				return new Valve(inputs[0], 2, decoders.get(Decoders.FLAG_C));

			case 17:
				// 1 -> С
				return new Valve(inputs[0], 3, decoders.get(Decoders.FLAG_C));

			case 18:
				// БР -> РА
				return new Valve(inputs[0], 1, decoders.get(Decoders.BR_TO));

			case 19:
				// БР -> РД
				return new Valve(inputs[0], 2, decoders.get(Decoders.BR_TO));

			case 20:
				// БР -> РК
				return new Valve(inputs[0], 3, decoders.get(Decoders.BR_TO));

			case 21:
				// БР -> СК
				return new Valve(inputs[0], 4, decoders.get(Decoders.BR_TO));

			case 22:
				// БР -> А
				return new Valve(inputs[0], 5, decoders.get(Decoders.BR_TO));

			case 23:
				// Память -> РД
				return new Valve(inputs[0], 0, vr00);

			case 24:
				// РД -> Память
				return new Valve(inputs[0], 1, vr00);

			case 25:
				// Ввод-вывод
				return new Valve(inputs[0], 8, vr01);

			case 27:
				// DI
				return new Valve(inputs[0], 10, vr01);

			case 28:
				// EI
				return new Valve(inputs[0], 11, vr01);
		}

		return null;
	}

	private int getLabelAddr(String[][] mp, String label) {
		for (int i = 0; i < mp.length; i++)
			if (mp[i][0] != null)
				if (mp[i][0].equals(label))
					return i;

		return -1;
	}

	public void compileMicroProgram(MicroProgram mpsrc) throws Exception {
		String[][] mp = mpsrc.getMicroProgram();

		for (int i = 0; i < labelsaddr.length; labelsaddr[i++] = 0);

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

	public DataSource getIP() {
		return ip;
	}

	public int getIPValue() {
		return ip.getValue();
	}

	public void setIP(int value) {
		ip.setValue(value);
	}

	public void jump(int label) {
		ip.setValue(labelsaddr[label]);
	}

	public DataSource getInstr() {
		return instr;
	}

	public int getInstrValue() {
		return instr.getValue();
	}

	public Memory getMemory() {
		return mem;
	}

	public int getMemoryValue(int addr) {
		return mem.getValue(addr);
	}

	public int getMemoryValue() {
		int value = mem.getValue(ip.getValue());
		ip.setValue(0);
		return value;
	}

	public void setMemory(int value) {
		mem.setValue(value);
		ip.setValue(0);
	}

	public void step() {
		instr.setValue(1);
	}

	public Cycle getCycle() {
		int ipvalue = ip.getValue();

		if (ipvalue < labelsaddr[LABEL_CYCLE_ADDR])
			return Cycle.INSTRFETCH;

		if (ipvalue < labelsaddr[LABEL_CYCLE_EXEC])
			return Cycle.ADDRFETCH;

		if (ipvalue < labelsaddr[LABEL_CYCLE_INTR])
			return Cycle.EXECUTION;

		if (ipvalue < labelsaddr[LABEL_ADDR])
			return Cycle.INTERRUPT;

		if (ipvalue < labelsaddr[LABEL_CYCLE_EXECCNT])
			return Cycle.PANEL;

		return Cycle.EXECUTION;
	}

	public int getIntrCycleStartAddr() {
		return labelsaddr[LABEL_CYCLE_INTR];
	}
}
