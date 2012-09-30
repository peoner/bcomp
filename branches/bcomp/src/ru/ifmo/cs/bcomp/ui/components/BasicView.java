/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.EnumMap;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends BCompPanel {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private ArrayList<BusView> openbuses = new ArrayList<BusView>();
	private static final ControlSignal[] bustypes = {
		ControlSignal.DATA_TO_ALU,
		ControlSignal.INSTR_TO_ALU,
		ControlSignal.IP_TO_ALU,
		ControlSignal.KEY_TO_ALU
	};
	private EnumMap<ControlSignal, BusView> buses = new EnumMap<ControlSignal, BusView>(ControlSignal.class);
	private SignalListener[] listeners = new SignalListener[bustypes.length + 1];

	public BasicView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		buses.put(bustypes[0], new BusView(openbuses, new int[][] {
			{550, 400},
			{550, 450}
		}));
		buses.put(bustypes[1], new BusView(openbuses, new int[][] {
			{650, 450},
			{650, 400}
		}));
		buses.put(bustypes[2], new BusView(openbuses, new int[][] {
			{580, 400},
			{630, 400}
		}));
		buses.put(bustypes[3], new BusView(openbuses, new int[][] {
			{630, 450},
			{580, 450}
		}));

		add(new ALUView(550, 200, 181, 90));

		for (int i = 0; i < bustypes.length; i++)
			listeners[i] = cmanager.createSignalListener(buses.get(bustypes[i]), bustypes[i]);

		listeners[bustypes.length] = cmanager.createSignalListener(CPU.Reg.STATE,
				ControlSignal.BUF_TO_STATE_C, ControlSignal.CLEAR_STATE_C, ControlSignal.SET_STATE_C);
	}

	@Override
	public void paintComponent(Graphics g) {
		for (ControlSignal bustype : bustypes)
			buses.get(bustype).draw(g, Color.GRAY);
	}

	@Override
	public void panelActivate() {
		RegisterView reg = cmanager.getRegisterView(CPU.Reg.ADDR);
		reg.setProperties("Регистр адреса", 200, 1, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.IP);
		reg.setProperties("Счётчик команд", 200, 75, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.INSTR);
		reg.setProperties("Регистр команд", 200, 150, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.DATA);
		reg.setProperties("Регистр данных", 200, 225, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.ACCUM);
		reg.setProperties("Аккумулятор", 200, 300, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.STATE);
		reg.setProperties("C", 169, 300, false);
		add(reg);

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cmanager.panelDeactivate();
	}

	@Override
	public String getPanelName() {
		return "Базовая ЭВМ";
	}

	@Override
	public InputRegisterView getNextInputRegister() {
		return (InputRegisterView)cmanager.getRegisterView(CPU.Reg.KEY);
	}

	private void drawOpenBuses(Color color) {
		Graphics g = getGraphics();

		for (BusView bus : openbuses)
			bus.draw(g, color);
	}

	@Override
	public void stepStart() {
		drawOpenBuses(Color.GRAY);
		openbuses.clear();
	}

	@Override
	public void stepFinish() {
		drawOpenBuses(Color.RED);
	}

	@Override
	public SignalListener[] getSignalListeners() {
		return listeners;
	}
}
