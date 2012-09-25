/**
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class StateReg extends Register {
	public static final int FLAG_C = 0;
	public static final int FLAG_Z = 1;
	public static final int FLAG_N = 2;
	public static final int FLAG_0 = 3;
	public static final int FLAG_EI = 4;
	public static final int FLAG_INTR = 5;
	public static final int FLAG_READY = 6;
	public static final int FLAG_RUN = 7;
	public static final int FLAG_PROG = 8;
	private static final int REGISTER_WIDTH = FLAG_PROG + 1;

	public StateReg(DataSource ... inputs) {
		super(REGISTER_WIDTH, inputs);
	}
}
