/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.elements.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class SignalListener {
	public final DataDestination listener;
	public final ControlSignal[] signals;

	public SignalListener(DataDestination listener, ControlSignal ... signals) {
		this.listener = listener;
		this.signals = signals;
	}
}
