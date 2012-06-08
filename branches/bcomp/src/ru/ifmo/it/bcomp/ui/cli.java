/*
 * $Id$
 */
package ru.ifmo.it.bcomp.ui;

import java.io.*;
import ru.ifmo.it.bcomp.*;

/**
 *
 * @author dima
 */

public class cli {
	public static void main(String[] args)
	{
		CPU cpu = new CPU();

		cpu.setMemoryAt(0, 0xF200);

//		for (int i = 0; i < 19; i++) {
		for (int i = 0; i < 3; i++) {
			cpu.step();

			System.out.println("БР: " + Integer.toString(cpu.getRegValue(CPU.Regs.BUF), 16) +
				" А: " + Integer.toString(cpu.getRegValue(CPU.Regs.ACCUM), 16) +
				" РА: " + Integer.toString(cpu.getRegValue(CPU.Regs.ADDR), 16) +
				" РД: " + Integer.toString(cpu.getRegValue(CPU.Regs.DATA), 16) +
				" РК: " + Integer.toString(cpu.getRegValue(CPU.Regs.INSTR), 16) +
				" СК: " + Integer.toString(cpu.getRegValue(CPU.Regs.IP), 16) +
				" РС: " + Integer.toString(cpu.getRegValue(CPU.Regs.STATE), 16) +
				" СчМК: " + Integer.toString(cpu.getRegValue(CPU.Regs.MIP), 16) +
				" МК: " + Integer.toString(cpu.getRegValue(CPU.Regs.MINSTR), 16));
		}

//		Console c = System.console();
//		String[] str = c.readLine().split("[ \t]+");
//		System.out.println("Readed: " + str[0] + " # " + str[1] +  " # " + str[2]);
	}

}
