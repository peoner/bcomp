/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import ru.ifmo.cs.bcomp.ControlSignal;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class BCompPanel extends ActivateblePanel {
	protected final ComponentManager cmanager;
	protected final BusView[] buses;
	private SignalListener[] listeners;

	public BCompPanel(ComponentManager cmanager, BusView ... buses) {
		this.cmanager= cmanager;
		this.buses = buses;
	}

	protected void setSignalListeners(SignalListener[] listeners) {
		this.listeners = listeners;
	}

	protected SignalListener[] getSignalListeners() {
		return listeners;
	}

	protected void drawBuses(Graphics g) {
		ArrayList<BusView> openbuses = new ArrayList<BusView>();
		ArrayList<ControlSignal> signals = cmanager.getActiveSignals();

		for (BusView bus : buses) {
			for (ControlSignal signal : bus.getSignals())
				if (signals.contains(signal)) {
					openbuses.add(bus);
					continue;
				}
			bus.draw(g, Color.GRAY);
		}

		for (BusView bus : openbuses)
			bus.draw(g, Color.RED);
	}

	private void drawOpenBuses(Color color) {
		Graphics g = getGraphics();
		ArrayList<ControlSignal> signals = cmanager.getActiveSignals();

		// XXX: Perfomance optimization required
//		for (ControlSignal active : signals)
//			for (BusView bus : buses)
//				for (ControlSignal bussignal : bus.getSignals())
//					if (active == bussignal)
//						bus.draw(g, color);
		for (BusView bus : buses)
			for (ControlSignal signal : bus.getSignals())
				if (signals.contains(signal))
					bus.draw(g, color);
	}

	public void stepStart() {
		drawOpenBuses(Color.GRAY);
	}

	public void stepFinish() {
		drawOpenBuses(Color.RED);
	}

	public abstract InputRegisterView getNextInputRegister();
}
