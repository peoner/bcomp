/*
 * $Id$
 */
package ru.ifmo.it.bcomp;

/**
 *
 * @author dima
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
			return  new OptimizedMicroProgram();
		}

		return null;
	}
}
