/**
 * $Id$
 */

package ru.ifmo.cs.bcomp;

public interface MicroProgram {
	public String[][] getMicroProgram();
	public String getMicroProgramName();
	public Instruction[] getInstructionSet();
}
