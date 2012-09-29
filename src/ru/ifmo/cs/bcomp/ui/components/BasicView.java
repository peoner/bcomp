/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.EnumMap;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends BCompPanel {
	private enum Buses { DATA2ALU, INSTR2ALU, IP2ALU, KEY2ALU };
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private ArrayList<BusView> openbuses = new ArrayList<BusView>();
	private EnumMap<Buses, BusView> buses = new EnumMap<Buses, BusView>(Buses.class);

	public BasicView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		buses.put(Buses.DATA2ALU, new BusView(openbuses, new int[][] {
			{550, 400},
			{550, 450}
		}));
		buses.put(Buses.INSTR2ALU, new BusView(openbuses, new int[][] {
			{650, 450},
			{650, 400}
		}));
		buses.put(Buses.IP2ALU, new BusView(openbuses, new int[][] {
			{580, 400},
			{630, 400}
		}));
		buses.put(Buses.KEY2ALU, new BusView(openbuses, new int[][] {
			{630, 450},
			{580, 450}
		}));

		add(new ALUView(550, 200, 181, 90));
	}

	@Override
	public void paintComponent(Graphics g) {
		for (Buses bustype : Buses.values())
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
		cpu.addDestination(13, reg);
		add(reg);

		cpu.addDestination(1, buses.get(Buses.DATA2ALU));
		cpu.addDestination(2, buses.get(Buses.INSTR2ALU));
		cpu.addDestination(3, buses.get(Buses.IP2ALU));
		cpu.addDestination(6, buses.get(Buses.KEY2ALU));

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cpu.removeDestination(13, cmanager.getRegisterView(CPU.Reg.STATE));

		cpu.removeDestination(1, buses.get(Buses.DATA2ALU));
		cpu.removeDestination(2, buses.get(Buses.INSTR2ALU));
		cpu.removeDestination(3, buses.get(Buses.IP2ALU));
		cpu.removeDestination(6, buses.get(Buses.KEY2ALU));

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
}
