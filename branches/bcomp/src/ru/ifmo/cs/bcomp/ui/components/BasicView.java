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
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

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
		ControlSignal.ACCUM_TO_ALU,
		ControlSignal.KEY_TO_ALU,
		ControlSignal.BUF_TO_ACCUM,
		ControlSignal.BUF_TO_ADDR
	};
	private EnumMap<ControlSignal, BusView> buses = new EnumMap<ControlSignal, BusView>(ControlSignal.class);
	private SignalListener[] listeners = new SignalListener[bustypes.length + 1];

	public BasicView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		buses.put(ControlSignal.DATA_TO_ALU, new BusView(openbuses, new int[][] {
			{BUS_RIGHT_X1, BUS_FROM_DATA_Y},
			{BUS_RIGHT_X1, BUS_LEFT_INPUT_UP},
			{BUS_RIGHT_X, BUS_LEFT_INPUT_UP},
			{BUS_RIGHT_X, BUS_LEFT_INPUT_DOWN}
		}));
		buses.put(ControlSignal.INSTR_TO_ALU, new BusView(openbuses, new int[][] {
			{650, 450},
			{650, 400}
		}));
		buses.put(ControlSignal.IP_TO_ALU, new BusView(openbuses, new int[][] {
			{BUS_FROM_IP_X, BUS_FROM_IP_Y},
			{BUS_RIGHT_X1, BUS_FROM_IP_Y},
			{BUS_RIGHT_X1, BUS_LEFT_INPUT_UP},
			{BUS_RIGHT_X, BUS_LEFT_INPUT_UP},
			{BUS_RIGHT_X, BUS_LEFT_INPUT_DOWN}
		}));
		buses.put(ControlSignal.ACCUM_TO_ALU, new BusView(openbuses, new int[][] {
			{BUS_FROM_ACCUM_X, BUS_FROM_ACCUM_Y},
			{BUS_LEFT_INPUT_X1, BUS_FROM_ACCUM_Y},
			{BUS_LEFT_INPUT_X1, BUS_LEFT_INPUT_UP},
			{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_UP},
			{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_DOWN}
		}));
		buses.put(ControlSignal.KEY_TO_ALU, new BusView(openbuses, new int[][] {
			{BUS_LEFT_INPUT_X1, BUS_KEY_ALU},
			{BUS_LEFT_INPUT_X1, BUS_LEFT_INPUT_UP},
			{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_UP},
			{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_DOWN}
		}));
		buses.put(ControlSignal.BUF_TO_ACCUM, new BusView(openbuses, new int[][] {
			{FROM_ALU_X, FROM_ALU_Y},
			{FROM_ALU_X, TO_ACCUM_Y}
		}));
		buses.put(ControlSignal.BUF_TO_ADDR, new BusView(openbuses, new int[][] {
			{FROM_ALU_X, FROM_ALU_Y},
			{FROM_ALU_X, FROM_ALU_Y1},
			{FROM_ALU_X + 70, FROM_ALU_Y1}
		}));

		add(new ALUView(REG_C_X_BV, ALU_Y, ALU_WIDTH, ALU_HEIGHT));

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
		reg.setProperties("Регистр адреса", REG_ACCUM_X_BV, 1, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.DATA);
		reg.setProperties("Регистр данных", REG_ACCUM_X_BV, REG_DATA_Y_BV, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.IP);
		reg.setProperties("Счётчик команд", REG_IP_X_BV, REG_IP_Y_BV, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.INSTR);
		reg.setProperties("Регистр команд", 550, 10, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.ACCUM);
		reg.setProperties("Аккумулятор", REG_ACCUM_X_BV, REG_ACCUM_Y_BV, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.STATE);
		reg.setProperties("C", REG_C_X_BV, REG_ACCUM_Y_BV, false);
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
