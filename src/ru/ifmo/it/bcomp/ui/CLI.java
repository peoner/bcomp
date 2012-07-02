/*
 * $Id$
 */
package ru.ifmo.it.bcomp.ui;

import java.util.ArrayList;
import java.util.Scanner;
import ru.ifmo.it.bcomp.*;
import ru.ifmo.it.elements.DataDestination;
import ru.ifmo.it.io.IOCtrl;

/**
 *
 * @author dima
 */

public class CLI {
	private BasicComp bcomp;
	private CPU cpu;
	private IOCtrl[] ioctrls;
	private ArrayList<Integer> writelist = new ArrayList<Integer>();

	public CLI(MicroPrograms.Type mptype) throws Exception {
		bcomp = new BasicComp(mptype);

		cpu = bcomp.getCPU();
		cpu.addDestination(24, new DataDestination() {
			public void setValue(int value) {
				int addr = cpu.getRegValue(CPU.Regs.ADDR);

				if (!writelist.contains(addr))
					writelist.add(addr);
			}
		});

		ioctrls = bcomp.getIOCtrls();
	}

	private String getRegWidth(CPU.Regs reg) {
		switch (reg) {
		case ACCUM:
			return "4";

		case BUF:
			return "5";

		case DATA:
			return "4";

		case ADDR:
			return "3";

		case IP:
			return "3";

		case INSTR:
			return "4";

		case STATE:
			return "4";

		case KEY:
			return "4";

		case MIP:
			return "2";

		case MINSTR:
			return "4";
		}

		return null;
	}

	protected static String getFormatted(int value, String width) {
		return String.format("%1$0" + width + "x", value).toUpperCase();
	}

	private String getReg(CPU.Regs reg) {
		return getFormatted(cpu.getRegValue(reg), getRegWidth(reg));
	}

	private String getFormattedState(int flag) {
		return Integer.toString(cpu.getStateValue(flag));
	}

	private void printRegsTitle() {
		System.out.println(cpu.getClockState() ?
			"Адр Знчн  СК  РА  РК   РД    А  C Адр Знчн" :
			"Адр МК   СК  РА  РК   РД    А  C   БР  N Z СчМК");
	}

	private void printMicroMemoryTitle() {
		System.out.println("Адр МК");
	}

	private void printMicroMemory(int addr, int value) {
		System.out.println(getFormatted(addr, "2") + " " + getFormatted(value, "4"));
	}

	private String getRegs() {
		return getReg(CPU.Regs.IP) + " " +
			getReg(CPU.Regs.ADDR) + " " +
			getReg(CPU.Regs.INSTR) + " " +
			getReg(CPU.Regs.DATA) + " " +
			getReg(CPU.Regs.ACCUM) + " " +
			getFormattedState(StateReg.FLAG_C);
	}

	private void printRegs(int addr, String add) {
		System.out.println(cpu.getClockState() ?
			getFormatted(addr, "3") + " " +
				getFormatted(cpu.getMemory(addr), "4") + " " +
				getRegs() + add:
			getFormatted(addr, "2") + " " +
				getFormatted(cpu.getMicroMemory(addr), "4") + " " +
				getRegs() + " " +
				getReg(CPU.Regs.BUF) + " " +
				getFormattedState(StateReg.FLAG_N) + " " +
				getFormattedState(StateReg.FLAG_Z) + "  " +
				getReg(CPU.Regs.MIP));
	}

	private void printIO(int ioaddr) {
		System.out.println("ВУ" + ioaddr +
			": Флаг = " + ioctrls[ioaddr].getFlag() +
			" РДВУ = " + getFormatted(ioctrls[ioaddr].getData(), "2"));
	}

	private int getIP() {
		return cpu.getClockState() ? cpu.getRegValue(CPU.Regs.IP) : cpu.getRegValue(CPU.Regs.MIP);
	}

	private void cont(int count, boolean printtitle) {
		if (printtitle)
			printRegsTitle();

		for (int i = 0; i < count; i++) {
			int addr = getIP();
			String add;

			writelist.clear();

			try {
				cpu.start();
			} catch (Exception ex) {
				System.out.println("Программа не выполнена полностью: " + ex.getMessage());
			}

			if (writelist.isEmpty())
				add = "";
			else {
				add = " " + getFormatted(writelist.get(0), "3") + " " +
					getFormatted(cpu.getMemory(writelist.get(0)), "4");
				writelist.remove(0);
			}

			printRegs(addr, add);

			for (Integer wraddr : writelist)
				System.out.println(
					String.format("%1$34s", " ") +
					getFormatted(wraddr.intValue(), "3") + " " +
					getFormatted(cpu.getMemory(wraddr.intValue()), "4"));
		}
	}

	private void cont() {
		cont(1, true);
	}

	private boolean checkCmd(String[] cmd, String check) {
		return cmd[0].equals(check.substring(0, Math.min(check.length(), cmd[0].length())));
	}

	private int getReqValue(String[] cmd, int index) throws Exception {
		if (index >= cmd.length)
			throw new Exception("Value required");

		return Integer.parseInt(cmd[index], 16);
	}

	private int getReqValue(String[] cmd) throws Exception {
		return getReqValue(cmd, 1);
	}

	private int getCountFromCmd(String[] cmd) throws Exception {
		if (cmd.length >= 2)
			return getReqValue(cmd);

		return 1;
	}

	private void printHelp() {
		System.out.println("Пультовые команды:\n" +
			"a[ddress] value - Ввод адреса\n" +
			"\tЗаписывает value в СК\n" +
			"w[rite] value ... - Запись\n" +
			"\tЗаписывает value в ячейку памяти по адресу в СК\n" +
			"r[ead] [count] - Чтение\n" +
			"\tЧитает count ячеек памяти по адресу в СК\n" +
			"\tЕсли count не указан, читает одну ячейку\n" +
			"s[tart] - Пуск\n" +
			"c[continue] [count] - Продолжить\n" +
			"\tВыполняет 1 или count тактов или команд или программ\n" +
			"ru[n] - Работа/Останов\n" +
			"\tПереключает режимы БЭВМ из режима Работа в режим Останов и обратно\n" +
			"cl[ock] - Потактовое выполнение\n" +
			"\tПереключает режимы БЭВМ в режим потактового выполнения и обратно\n" +
			"ma[ddress] value - Переход к микрокоманде\n" +
			"\tЗаписывает value в СчМК\n" +
			"mw[rite] value ... - Запись микрокоманды\n" +
			"\tЗаписывает value в память микрокоманд по адресу в СчМК\n" +
			"mr[ead] [count] - Чтение одной или count микрокоманд по адресу в СчМК\n" +
			"io [addr [value]] - Вывод состояния всех ВУ/указанного ВУ/запись value в ВУ\n" +
			"flag addr - Установить флаг готовности указанного ВУ");
	}

	public void cli() {
		Scanner input = new Scanner(System.in);
		String line;

		bcomp.startTimer();

		System.out.println("Эмулятор Базовой ЭВМ. Версия r" + CLI.class.getPackage().getImplementationVersion() + "\n" +
			"Загружена " + cpu.getMicroProgramName() + " микропрограмма\n" +
			"Цикл прерывания начинается с адреса " + getFormatted(cpu.getIntrCycleStartAddr(), "2") + "\n" +
			"БЭВМ готова к работе.\n" +
			"Используйте ? или help для получения справки");

		for (;;) {
			try {
				line = input.nextLine();
			} catch(Exception ex) {
				break;
			}

			String[] cmd = line.split("[ \t]+");

			if ((cmd.length == 0) || cmd[0].equals(""))
				continue;

			if (cmd[0].charAt(0) == '#')
				continue;

			if (checkCmd(cmd, "?") || checkCmd(cmd, "help")) {
				printHelp();
				continue;
			}

			try {
				if (checkCmd(cmd, "address")) {
					cpu.setRegKey(getReqValue(cmd));
					cpu.jump(ControlUnit.LABEL_ADDR);
					cont();
					continue;
				}

				if (checkCmd(cmd, "write")) {
					for (int i = 1; i < cmd.length; i++) {
						cpu.setRegKey(getReqValue(cmd, i));
						cpu.jump(ControlUnit.LABEL_WRITE);
						cont(1, i == 1);
					}
					continue;
				}

				if (checkCmd(cmd, "read")) {
					int count = getCountFromCmd(cmd);

					printRegsTitle();

					for (int i = 0; i < count; i++) {
						cpu.jump(ControlUnit.LABEL_READ);
						cont(1, false);
					}
					continue;
				}

				if (checkCmd(cmd, "start")) {
					cpu.jump(ControlUnit.LABEL_START);
					cont();
					continue;
				}

				if (checkCmd(cmd, "continue")) {
					int count = getCountFromCmd(cmd);
					cont(count, true);
					continue;
				}

				if (checkCmd(cmd, "clock")) {
					System.out.println("Такт: " + (cpu.invertClockState() ? "Нет" : "Да"));
					continue;
				}

				if (checkCmd(cmd, "run")) {
					cpu.invertRunState();
					System.out.println("Режим работы: " + (
						cpu.getStateValue(StateReg.FLAG_RUN) == 1 ? "Работа" : "Останов"));
					continue;
				}

				if (checkCmd(cmd, "maddress")) {
					int addr = getReqValue(cmd);
					cpu.setRegKey(addr);
					cpu.jump();
					printMicroMemoryTitle();
					printMicroMemory(addr, cpu.getMicroMemory(addr));
					continue;
				}

				if (checkCmd(cmd, "mwrite")) {
					printMicroMemoryTitle();

					for (int i = 1; i < cmd.length; i++) {
						int addr = cpu.getRegValue(CPU.Regs.MIP); 
						cpu.setRegKey(getReqValue(cmd, i));
						cpu.setMicroMemory();
						printMicroMemory(addr, cpu.getMicroMemory(addr));
					}
					continue;
				}

				if (checkCmd(cmd, "mread")) {
					int count = getCountFromCmd(cmd);

					printMicroMemoryTitle();

					for (int i = 0; i < count; i++) {
						int mip = cpu.getRegValue(CPU.Regs.MIP);
						printMicroMemory(mip, cpu.getMicroMemory());
					}
					continue;
				}

				if (checkCmd(cmd, "io")) {
					if (cmd.length == 1) {
						for (int ioaddr = 0; ioaddr < 4; ioaddr++)
							printIO(ioaddr);
						continue;
					}

					int ioaddr = getReqValue(cmd);

					if (cmd.length == 3) {
						int value = getReqValue(cmd, 2);
						System.out.println("Value: " + value);
						ioctrls[ioaddr].setData(value);

					}

					printIO(ioaddr);
					continue;
				}

				if (checkCmd(cmd, "flag")) {
					int ioaddr = getReqValue(cmd);
					ioctrls[ioaddr].setFlag();
					printIO(ioaddr);
					continue;
				}
			} catch (Exception ex) {
				System.out.println("Ошибка: " + ex.getMessage());
				continue;
			}

			System.out.println("Unknown command");
		}

		bcomp.stopTimer();
	}

	public static void main(String[] args) throws Exception {
		MicroPrograms.Type mptype = MicroPrograms.Type.BASE;

		if (args.length == 1)
			if (args[0].equals("-o"))
				mptype = MicroPrograms.Type.OPTIMIZED;

		CLI cli = new CLI(mptype);

		cli.cli();
	}
}
