/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.ArrayList;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Assembler {
	protected class Address {
		private final AsmLabel segment;
		private final int offset;
		private ArrayList<AsmLabel> labels = new ArrayList<AsmLabel>();

		private Address(AsmLabel segment, int offset) {
			this.segment = segment;
			this.offset = offset;
		}

		private Address(AsmLabel segment) {
			this(segment, 0);
		}

		private Address(int offset) {
			this(null, offset);
		}

		protected int getAddress() throws Exception {
			return segment == null ? offset : segment.getCommand() + offset;
		}

		private void addLabel(AsmLabel label) {
			labels.add(label);
			label.setAddr(this);
		}

		private Address addCommand(Command cmd) throws Exception {
			for (AsmLabel label : labels)
				label.setCommand(cmd);

			return new Address(segment, offset + cmd.getSize());
		}
	}

	protected class Command {
		private final AsmLabel arg;
		private Integer cmd;
		public final Address addr;
		public final AsmLabel size;

		private Command(Address addr, Integer cmd, AsmLabel arg, AsmLabel size) {
			this.addr = addr;
			this.cmd = cmd;
			this.arg = arg;
			this.size = size;
		}

		private Command(Address addr, int cmd) {
			this(addr, cmd, null, SIZE_1);
		}

		private Command(Address addr, int cmd, AsmLabel arg) {
			this(addr, cmd, arg, SIZE_1);
		}

		private Command(Address addr, AsmLabel size) {
			this(addr, null, null, size);
		}

		// XXX: rework this
		private Command(Address addr, String value) {
			AsmLabel arg;
			Integer cmd;

			try {
				cmd = Integer.parseInt(value, 16);
				arg = null;
			} catch (Exception e) {
				cmd = 0;
				arg = getLabel(value);
			}

			this.addr = addr;
			this.size = SIZE_1;
			this.cmd = cmd;
			this.arg = arg;
		}

		protected void setCommand(int cmd) {
			this.cmd = cmd;
		}

		protected int getCommand() throws Exception {
			if (cmd == null)
				throw new Exception("Использование неинициализированного значения");

			return arg == null ? cmd : cmd + arg.getAddress();
		}

		private int getAddress() throws Exception {
			return addr.getAddress();
		}

		private int getSize() throws Exception {
			return size.getCommand();
		}
	}

	private ArrayList<AsmLabel> labels;
	private ArrayList<AsmLabel> args;
	private ArrayList<Command> cmds;
	private Instruction[] instrset;
	private final AsmLabel SIZE_1 = new AsmLabel(null);

	public Assembler(Instruction[] instrset) {
		this.instrset = instrset;
		SIZE_1.setCommand(new Command(null, 1));
	}

	public void compileProgram(String program) throws Exception {
		String[] prog = program.replace("\r", "").toUpperCase().split("\n");
		Address addr = new Address(null, 0);
		int lineno = 0;

		labels = new ArrayList<AsmLabel>();
		args = new ArrayList<AsmLabel>();
		cmds = new ArrayList<Command>();

		try {
			for (String l : prog) {
				lineno++;

				String[] line = l.trim().split("[#;]+");

				if ((line.length == 0) || line[0].equals(""))
					continue;

				line = line[0].trim().split("[ \t]+");

				if ((line.length == 0) || (line[0].equals("")))
					continue;

				if (line[0].equals("ORG")) {
					if (line.length != 2)
						throw new Exception("Директива ORG требует один и только один аргумент");

					try {
						int offset = Integer.parseInt(line[1], 16);
						addr = new Address(offset);
					} catch (Exception e) {
						AsmLabel label = getLabel(line[1]);
						addr = new Address(label);
					}
					continue;
				}

				int col = 0;

				if (line[col].charAt(line[col].length() - 1) == ':') {
					String labelname = line[col].substring(0, line[col].length() - 1);

					if (labelname.equals(""))
						throw new Exception("Имя метки не может быть пустым");

					AsmLabel label = getLabel(labelname);

					if (label.hasAddress())
						throw new Exception("Метка " + labelname + " объявлена повторно");

					addr.addLabel(label);
					col++;
				}

				if (col == line.length)
					continue;

				if (line[col].equals("WORD")) {
					if (col++ == line.length - 1)
						throw new Exception("Директива WORD должна иметь аргументы");

					if ((line.length - col) == 3 && line[col + 1].equals("DUP")) {
						AsmLabel size;
						
						try {
							Command cmd = new Command(null, Integer.parseInt(line[col], 16));
							size = new AsmLabel(null);
							size.setCommand(cmd);
						} catch (Exception e) {
							size = findLabel(line[col]);
							if (size == null)
								throw new Exception("Метка " + line[col] + " должна быть уже определена");
						}

						col += 2;

						if (line[col].equals("(?)")) {
							if (!addr.labels.isEmpty())
								args.add(addr.labels.get(0));
							
							addr = addr.addCommand(new Command(addr, size));
							continue;
						}
						
						if (line[col].charAt(0) != '(' || line[col].charAt(line[col].length() - 1) != ')')
							throw new Exception("Значение после DUP должно быть в скобках");
						String value = line[col].substring(1, line[col].length() - 1);

						for (int i = 0; i < size.getCommand(); i++)
							addr = addCommand(addr, new Command(addr, value));

						continue;
					}

					String v;
					for (v = line[col++]; col < line.length; v = v.concat(" ").concat(line[col++]));
					String[] values = v.split(",");

					for (String value : values) {
						value = value.trim();

						if (value.equals("?")) {
							if (!addr.labels.isEmpty())
								args.add(addr.labels.get(0));
							addr = addr.addCommand(new Command(addr, SIZE_1));
						} else
							addr = addCommand(addr, new Command(addr, value));
					}
					continue;
				}

				Command cmd = null;
				Instruction instr = findInstruction(line[col]);

				if (instr == null)
					throw new Exception("Неизвестная команда " + line[col]);

				switch (instr.getType()) {
					case ADDR:
						if (col != line.length - 2)
							throw new Exception("Адресная команда " + line[col] + " требует один аргумент");

						String labelname = line[col + 1];
						int addrtype;

						if (labelname.charAt(0) == '(') {
							if (labelname.charAt(labelname.length() - 1) != ')')
								throw new Exception("Нет закрывающей скобки");

							labelname = labelname.substring(1, labelname.length() - 1);
							addrtype = 0x800;
						} else
							addrtype = 0;

						cmd = new Command(addr, instr.getInstr() + addrtype, getLabel(labelname));
						break;

					case NONADDR:
						if (col != line.length - 1)
							throw new Exception("Безадресная команда " + line[col] + " не требует аргументов");

						cmd = new Command(addr, instr.getInstr());
						break;

					case IO:
						if (col != line.length - 2)
							throw new Exception("Строка " + lineno + ": Команда ввода-вывода " + line[col] +
								" требует один и только один аргумент");

						cmd = new Command(addr, instr.getInstr() + Integer.parseInt(line[col + 1], 16));
						break;
				}

				addr = addCommand(addr, cmd);
			}

			for (AsmLabel label : labels)
				if (!label.hasAddress())
					throw new Exception("Ссылка на неопределённую метку " + label.label);
		} catch (Exception e) {
			throw new Exception("Строка " + lineno + ": " + e.getMessage());
		}
	}

	private Address addCommand(Address addr, Command cmd) throws Exception {
		cmds.add(cmd);
		return addr.addCommand(cmd);
	}

	private AsmLabel findLabel(String labelname) {
		for (AsmLabel label : labels)
			if (label.label.equals(labelname))
				return label;

		return null;
	}

	private AsmLabel getLabel(String labelname) {
		AsmLabel label = findLabel(labelname);

		if (label == null) {
			label = new AsmLabel(labelname);
			labels.add(label);
		}

		return label;
	}

	private Instruction findInstruction(String mnemonics) {
		for (Instruction instr : instrset)
			if (instr.getMnemonics().equals(mnemonics))
				return instr;

		return null;
	}

	public void loadProgram(CPU cpu) throws Exception {
		for (Command cmd : cmds) {
			cpu.setRegKey(cmd.getAddress());
			cpu.startFrom(ControlUnit.LABEL_ADDR);
			cpu.setRegKey(cmd.getCommand());
			cpu.startFrom(ControlUnit.LABEL_WRITE);
		}

		cpu.setRegKey(getBeginAddr());
		cpu.startFrom(ControlUnit.LABEL_ADDR);
	}

	public AsmLabel[] getArguments() {
		return args.toArray(new AsmLabel[args.size()]);
	}

	public int getLabelAddr(String labelname) throws Exception {
		AsmLabel label = getLabel(labelname);

		if (label == null)
			throw new Exception("Метка " + labelname + " не найдена");

		return label.getAddress();
	}

	public int getBeginAddr() throws Exception {
		return getLabelAddr("BEGIN");
	}

}
