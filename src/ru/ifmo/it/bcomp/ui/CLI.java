/*
 * $Id$
 */
package ru.ifmo.it.bcomp.ui;

import java.util.ArrayList;
import java.util.Scanner;
import ru.ifmo.it.elements.*;
import ru.ifmo.it.bcomp.*;

/**
 *
 * @author dima
 */

public class CLI {
	private CPU cpu;
	private boolean clock = true;
	private ArrayList<Integer> writelist = new ArrayList<Integer>();

	private class WriteHandler implements DataDestination {
		private WriteHandler()
		{
			cpu.addDestination(24, this);
		}

		public void setValue(int value)
		{
			int addr = cpu.getRegValue(CPU.Regs.ADDR);

			if (!writelist.contains(addr))
				writelist.add(addr);
		}
	}

	public CLI() throws Exception
	{
		cpu = new CPU();
	}

	private String getRegWidth(CPU.Regs reg)
	{		
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

	private String getFormatted(int value, String width)
	{
		return String.format("%1$0" + width + "x", value).toUpperCase();
	}

	private String getReg(CPU.Regs reg)
	{
		return getFormatted(cpu.getRegValue(reg), getRegWidth(reg));
	}

	private int getFlag(int flag)
	{
		return cpu.getRegValue(CPU.Regs.STATE, flag);
	}
	private String getFormattedFlag(int flag)
	{
		return getFormatted(getFlag(flag), "1");
	}

	private void printRegsTitle()
	{
		System.out.println(clock ?
			"Адр Знчн  СК  РА  РК   РД    А  C Адр Знчн" :
			"Адр МК   СК  РА  РК   РД    А  C   БР  N Z СчМК");
	}

	private String getRegs()
	{
		return getReg(CPU.Regs.IP) + " " +
			getReg(CPU.Regs.ADDR) + " " +
			getReg(CPU.Regs.INSTR) + " " +
			getReg(CPU.Regs.DATA) + " " +
			getReg(CPU.Regs.ACCUM) + " " +
			getFormattedFlag(StateReg.FLAG_C);
	}

	private void printRegs(int addr, String add)
	{
		System.out.println(clock?
			getFormatted(addr, "3") + " " +
				getFormatted(cpu.getMemory(addr), "4") + " " +
				getRegs() + add:
			getFormatted(addr, "2") + " " +
				getFormatted(cpu.getMicroMemory(addr), "4") + " " +
				getRegs() + " " +
				getReg(CPU.Regs.BUF) + " " +
				getFormattedFlag(StateReg.FLAG_N) + " " +
				getFormattedFlag(StateReg.FLAG_Z) + "  " +
				getReg(CPU.Regs.MIP));
	}

	private void printRegs(int addr)
	{
		printRegs(addr, "");
	}

	private int getIP()
	{
		return clock ? cpu.getRegValue(CPU.Regs.IP) : cpu.getRegValue(CPU.Regs.MIP);
	}

	private void cont(int count, boolean printtitle)
	{
		if (printtitle)
			printRegsTitle();

		for (int i = 0; i < count; i++) {
			int addr = getIP();
			String add;

			writelist.clear();
			while (cpu.step() && clock);

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
					getFormatted(writelist.get(0), "3") + " " +
					getFormatted(cpu.getMemory(writelist.get(0)), "4"));
		}
	}

	private void cont()
	{
		cont(1, true);
	}

	private boolean checkCmd(String[] cmd, String check)
	{
		return cmd[0].equals(check.substring(0, Math.min(check.length(), cmd[0].length())));
	}

	private int getCountFromCmd(String[] cmd)
	{
		int count = 1;

		if (cmd.length >= 2) {
			count = Integer.parseInt(cmd[1], 16);
		}

		return count;
	}

	private boolean getReqValue(String[] cmd, int i)
	{
		if (cmd.length <= i) {
			System.out.println("Value required");
			return true;
		}

		cpu.setRegKey(Integer.parseInt(cmd[i], 16));
		return false;
	}

	private boolean getReqValue(String[] cmd)
	{
		return getReqValue(cmd, 1);
	}

	private boolean isComment(String s)
	{
		return s.charAt(0) == '#';
	}

	private void printMicroRegs(int addr, boolean printtitle)
	{
		boolean clock = this.clock;
		this.clock = false;

		if (printtitle)
			printRegsTitle();

		printRegs(addr);
		this.clock = clock;
	}

	private void printHelp()
	{
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
			"\tВыполняет count тактов или команд или программ\n" +
			"\tПо умолчанию одну\n" +
			"run - Работа/Останов\n" +
			"\tПереключает режимы БЭВМ из режима Работа в режим Останов и обратно\n" +
			"clock - Потактовое выполнение\n" +
			"\tПереключает режимы БЭВМ в режим потактового выполнения и обратно\n" +
			"ma[ddress] value - Переход к микрокоманде\n" +
			"\tЗаписывает value в СчМК\n" +
			"mw[rite] value ... - Запись микрокоманды\n" +
			"\tЗаписывает value в память микрокоманд по адресу в СчМК\n" +
			"mr[ead] - Чтение микрокоманды\n" +
			"\tЧитает из памяти микрокоманд по адресу в СчМК\n" +
			"{?,help} - Вывод этой подсказки");
	}

	public void cli()
	{
		Scanner input = new Scanner(System.in);
		String line;
		WriteHandler writehandler = new WriteHandler();

		System.out.println("БЭВМ готова к работе. Используйте ? или help для получения справки");

		for (;;) {
			try {
				line = input.nextLine();
			} catch(Exception e) {
				break;
			}

			String[] cmd = line.split("[ \t]+");
			int value;

			if (cmd[0].equals(""))
				continue;

			if (checkCmd(cmd, "address")) {
				if (getReqValue(cmd))
					continue;

				cpu.jump(ControlUnit.LABEL_ADDR);
				cont();
				continue;
			}

			if (checkCmd(cmd, "write")) {
				for (int i = 1; i < cmd.length; i++) {
					if (isComment(cmd[i]))
						break;

					if (getReqValue(cmd, i))
						break;

					cpu.jump(ControlUnit.LABEL_WRITE);
					cont(1, i == 1);
				}
				continue;
			}

			if (checkCmd(cmd, "read")) {
				printRegsTitle();
				for (int i = 0; i < getCountFromCmd(cmd); i++) {
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
				cont(getCountFromCmd(cmd), true);
				continue;
			}

			if (checkCmd(cmd, "clock")) {
				clock = !clock;
				System.out.println("Такт: " + (clock ? "Нет" : "Да"));
				continue;
			}

			if (checkCmd(cmd, "run")) {
				cpu.invertRunState();
				System.out.println("Режим работы: " + (
					getFlag(StateReg.FLAG_RUN) == 1 ? "Работа" : "Останов"));
				continue;
			}

			if (checkCmd(cmd, "maddress")) {
				if (getReqValue(cmd))
					continue;

				cpu.jump();
				printMicroRegs(cpu.getRegValue(CPU.Regs.MIP), true);
				continue;
			}

			if (checkCmd(cmd, "mwrite")) {
				for (int i = 1; i < cmd.length; i++) {
					if (isComment(cmd[i]))
						break;

					if (getReqValue(cmd, i))
						break;

					int addr = cpu.getRegValue(CPU.Regs.MIP); 
					cpu.setMicroMemory();
					printRegsTitle();
					printMicroRegs(addr, i == 1);
				}
				continue;
			}

			if (checkCmd(cmd, "mread")) {
				printMicroRegs(cpu.getRegValue(CPU.Regs.MIP), true);
				continue;				
			}

			if (checkCmd(cmd, "?") || checkCmd(cmd, "help")) {
				printHelp();
				continue;				
			}

			if (isComment(cmd[0]))
				continue;

			System.out.println("Unknown command");
		}
	}

	public static void main(String[] args) throws Exception
	{
		CLI cli = new CLI();

		cli.cli();
	}
}
