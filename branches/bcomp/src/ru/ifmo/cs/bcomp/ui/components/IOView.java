/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.io.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOView extends BCompPanel {
	private class InputRegisterMouseListener extends MouseAdapter {
		private int input;

		public InputRegisterMouseListener(int input) {
			this.input = input;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			activeInputSwitch(input);
		}
	}

	private GUI gui;
	private CPU cpu;
	private IOCtrl[] ioctrls;
	private ComponentManager cmanager;
	private RegisterView[] ioregs = new RegisterView[3];
	private InputRegisterView[] inputs = new InputRegisterView[3];
	private InputRegisterMouseListener[] listeners = new InputRegisterMouseListener[3];
	private int lastInput;

	public IOView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		inputs[0] = (InputRegisterView)cmanager.getRegisterView(CPU.Reg.KEY);

		ioctrls = gui.getIOCtrls();

		for (int i = 0; i < ioregs.length; i++) {
			ioregs[i] = i == 0 ?
				new RegisterView(ioctrls[i + 1].getRegData()) :
				(inputs[i] = new InputRegisterView(ioctrls[i + 1].getRegData()));
			ioregs[i].setProperties("ВУ" + Integer.toString(i + 1), 500, 1 + i * 75, false);
			add(ioregs[i]);
			listeners[i] = new InputRegisterMouseListener(i);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
	}

	@Override
	public void panelActivate() {
		RegisterView reg = cmanager.getRegisterView(CPU.Reg.ADDR);
		reg.setProperties("РА", 200, 1, true);
		add(reg);
		reg = cmanager.getRegisterView(CPU.Reg.IP);
		reg.setProperties("СК", 200, 75, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.INSTR);
		reg.setProperties("РК", 200, 150, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.DATA);
		reg.setProperties("РД", 200, 225, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.ACCUM);
		reg.setProperties("Акк", 200, 300, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.STATE);
		reg.setProperties("C", 169, 300, false);
		cpu.addDestination(ControlSignal.BUF_TO_STATE_C, reg);
		cpu.addDestination(ControlSignal.CLEAR_STATE_C, reg);
		cpu.addDestination(ControlSignal.SET_STATE_C, reg);
		add(reg);

		((InputRegisterView)ioregs[1]).setActive(false);
		((InputRegisterView)ioregs[2]).setActive(false);

		ioctrls[1].addOutListener(ioregs[0]);
		ioctrls[3].addOutListener(ioregs[2]);

		for (int i = 0; i < inputs.length; i++) {
			inputs[i].addMouseListener(listeners[i]);
		}

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].removeMouseListener(listeners[i]);
		}

		RegisterView reg = cmanager.getRegisterView(CPU.Reg.STATE);
		cpu.removeDestination(ControlSignal.BUF_TO_STATE_C, reg);
		cpu.removeDestination(ControlSignal.CLEAR_STATE_C, reg);
		cpu.removeDestination(ControlSignal.SET_STATE_C, reg);

		ioctrls[1].removeOutListener(ioregs[0]);
		ioctrls[3].removeOutListener(ioregs[2]);

		cmanager.panelDeactivate();
	}

	@Override
	public String getPanelName() {
		return "Работа с ВУ";
	}

	@Override
	public InputRegisterView getNextInputRegister() {
		lastInput = lastInput < inputs.length - 1 ? lastInput + 1 : 0;
		return inputs[lastInput];
	}

	@Override
	public void stepStart() { }

	@Override
	public void stepFinish() { }

	private void activeInputSwitch(int input) {
		cmanager.activeInputSwitch(inputs[lastInput = input]);
	}
}
