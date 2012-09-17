/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class BCompPanel extends ActivateblePanel {
	public abstract InputRegisterView getNextInputRegister();
	public abstract void stepStart();
	public abstract void stepFinish();
}
