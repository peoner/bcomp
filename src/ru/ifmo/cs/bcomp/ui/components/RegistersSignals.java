/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.ControlSignal;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RegistersSignals {
	public final RegisterView register;
	public final ControlSignal[] signals;

	public RegistersSignals(RegisterView register, ControlSignal ... signals) {
		this.register = register;
		this.signals = signals;
	}
}
