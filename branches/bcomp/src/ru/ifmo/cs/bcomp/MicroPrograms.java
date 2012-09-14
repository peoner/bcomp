/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroPrograms {
	public enum Type {
		BASE, OPTIMIZED, EXTENDED
	}

	public static MicroProgram getMicroProgram(Type mptype) {
		switch (mptype) {
		case BASE:
			return new BaseMicroProgram();

		case OPTIMIZED:
			return new OptimizedMicroProgram();
		}

		return null;
	}
}
