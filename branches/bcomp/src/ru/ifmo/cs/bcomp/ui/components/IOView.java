/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.io.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOView extends BCompPanel {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private RegisterView[] ioregs = new RegisterView[3];
	private InputRegisterView[] inputs = new InputRegisterView[3];
	private int lastInput;

	public IOView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		inputs[0] = (InputRegisterView)cmanager.getRegisterView(CPU.Regs.KEY);

		IOCtrl[] ioctrls = gui.getIOCtrls();

		for (int i = 0; i < ioregs.length; i++) {
			ioregs[i] = i == 0 ?
				new RegisterView(ioctrls[i + 1].getRegData()) :
				(inputs[i] = new InputRegisterView(ioctrls[i + 1].getRegData()));
			ioregs[i].setProperties("ВУ" + i, 500, 1 + i * 75, false);
			add(ioregs[i]);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;
	}

	@Override
	public void panelActivate() {
		RegisterView reg = cmanager.getRegisterView(CPU.Regs.ADDR);
		reg.setProperties("РА", 200, 1, true);
		add(reg);
		reg = cmanager.getRegisterView(CPU.Regs.IP);
		reg.setProperties("СК", 200, 75, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.INSTR);
		reg.setProperties("РК", 200, 150, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.DATA);
		reg.setProperties("РД", 200, 225, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.ACCUM);
		reg.setProperties("Акк", 200, 300, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.STATE);
		reg.setProperties("C", 169, 300, false);
		cpu.addDestination(13, reg);
		add(reg);

		((InputRegisterView)ioregs[1]).setActive(false);
		((InputRegisterView)ioregs[2]).setActive(false);

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cpu.addDestination(13, cmanager.getRegisterView(CPU.Regs.STATE));
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
}
