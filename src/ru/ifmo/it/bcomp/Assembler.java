/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.it.bcomp;

import java.util.ArrayList;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Assembler {
	private interface AddressWaiter {
	}

	private class Label {
		private String label;
		private Integer addr;
		private ArrayList<Command> cmds = new ArrayList<Command>();

		public Label(String label) {
			this.label = label;
		}

		public Label(String label, int addr) {
			this(label);
			this.addr = addr;
		}

		public String getLabel() {
			return label;
		}

		public Integer getAddr() {
			return addr;
		}

		public void addCommand(Command cmd) {
			cmds.add(cmd);
		}

		public void setAddr(int addr) {
			this.addr = addr;

			for (Command cmd : cmds)
				cmd.setArgAddr(addr);
		}
	}

	private class Command {
		private int addr;
		private int cmd;

		public Command(int addr, int cmd) {
			this.addr = addr;
			this.cmd = cmd;
		}

		public Command(int addr, int cmd, Label arg) {
			this(addr, cmd);
			this.cmd += arg.getAddr();
		}

		public int getAddr() {
			return addr;
		}

		public int getCommand() {
			return cmd;
		}

		public void setArgAddr(int addr) {
			cmd += addr;		
		}

	}

	private ArrayList<Label> labels;
	private ArrayList<Label> args;
	private ArrayList<Command> cmds;
	private Instruction[] instrset;

	public Assembler(Instruction[] instrset) {
		this.instrset = instrset;
	}

	public void compileProgram(String program) throws Exception {
		String[] prog = program.toUpperCase().split("[\r\n]+");
		int addr = 0;
		int value;

		labels = new ArrayList<Label>();
		args = new ArrayList<Label>();
		cmds = new ArrayList<Command>();

		for (String l : prog) {
			String[] line = l.trim().split("[#;]+");

			if ((line.length == 0) || line[0].equals(""))
				continue;

			line = line[0].trim().split("[ \t]+");

			if ((line.length == 0) || (line[0].equals("")))
				continue;

			if (line[0].equals("ORG")) {
				if (line.length != 2)
					throw new Exception("Директива ORG требует один и только один аргумент");

				addr = Integer.parseInt(line[1], 16);
				continue;
			}

			int col = 0;
			Label label = null;

			if (line[0].charAt(line[0].length() - 1) == ':') {
				String labelname = line[0].substring(0, line[0].length() - 1);
				label = getLabel(labelname);

				if (label == null)
					labels.add(label = new Label(labelname, addr));
				else
					label.setAddr(addr);

				col++;
			}

			if (col == line.length)
				continue;

			if (line[col].equals("WORD")) {
				if (col != line.length - 1)
					throw new Exception("Директива WORD не требует аргументов");

				if (label != null)
					args.add(label);

				addr++;
				continue;
			}

			Instruction instr = findInstruction(line[col]);

			if (instr != null) {
				switch (instr.getType()) {
					case ADDR:
						if (col != line.length - 2)
							throw new Exception("Адресная команда " + line[col] +
								" требует один и только один аргумент");

						String labelname = line[col + 1];
						int addrtype;

						if (labelname.charAt(0) == '(') {
							if (labelname.charAt(labelname.length() - 1) != ')')
								throw new Exception("Нет закрывающей скобки");

							labelname = labelname.substring(1, labelname.length() - 1);
							addrtype = 0x800;
						} else
							addrtype = 0;

						if ((label = getLabel(labelname)) == null)
							labels.add(label = new Label(labelname));

						if (label.getAddr() == null) {
							Command cmd = new Command(addr, instr.getInstr() + addrtype);
							cmds.add(cmd);
							label.addCommand(cmd);
						} else 
							cmds.add(new Command(addr, instr.getInstr() + addrtype, label));

						break;

					case NONADDR:
						if (col != line.length - 1)
							throw new Exception("Безадресная команда " + line[col] +
								" не требует аргументов");

						cmds.add(new Command(addr, instr.getInstr()));
						break;

					case IO:
						if (col != line.length - 2)
							throw new Exception("Команда ввода-вывода " + line[col] +
								" требует один и только один аргумент");

						cmds.add(new Command(addr, instr.getInstr() + Integer.parseInt(line[col + 1], 16)));
						break;
				}

				addr++;
				continue;
			}

			try {
				value = Integer.parseInt(line[col], 16);
			} catch (Exception ex) {
				throw new Exception("Неизвестная команда " + line[col]);
			}

			if (col != line.length - 1)
				throw new Exception("Константа " + line[col] + " не требует аргументов");

			cmds.add(new Command(addr++, value));
		}

		for (Label label : labels)
			if (label.getAddr() == null)
				throw new Exception("Не найдена метка " + label.getLabel());
	}

	private Label getLabel(String labelname) {
		for (Label label : labels)
			if (label.getLabel().equals(labelname))
				return label;

		return null;
	}

	private Instruction findInstruction(String mnemonics) {
		for (Instruction instr : instrset)
			if (instr.getMnemonics().equals(mnemonics))
				return instr;

		return null;
	}

    public void loadProgram(CPU cpu) throws Exception {
		for (Command cmd : cmds) {
			cpu.setRegKey(cmd.getAddr());
			cpu.jump(ControlUnit.LABEL_ADDR);
			cpu.start();
			cpu.setRegKey(cmd.getCommand());
			cpu.jump(ControlUnit.LABEL_WRITE);
			cpu.start();
		}

		cpu.setRegKey(getBeginAddr());
		cpu.jump(ControlUnit.LABEL_ADDR);
		cpu.start();
	}

	public int getBeginAddr() throws Exception {
		Label label = getLabel("BEGIN");

		if (label == null)
			throw new Exception("Метка BEGIN не найдена");

		return label.getAddr();
	}

	// XXX Отдача информации о аргументах
	// XXX Подумать о переделке loadProgram, дабы не передавать сюда cpu
}
