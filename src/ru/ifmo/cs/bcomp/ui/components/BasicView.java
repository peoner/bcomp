/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends BCompPanel {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;

	public BasicView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();
	}

	@Override
	public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;
	}

	@Override
	public void panelActivate() {
		RegisterView reg = cmanager.getRegisterView(CPU.Regs.ADDR);
		reg.setProperties("Регистр адреса", 200, 1, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.IP);
		reg.setProperties("Счётчик команд", 200, 75, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.INSTR);
		reg.setProperties("Регистр команд", 200, 150, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.DATA);
		reg.setProperties("Регистр данных", 200, 225, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.ACCUM);
		reg.setProperties("Аккумулятор", 200, 300, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.STATE);
		reg.setProperties("C", 169, 300, false);
		cpu.addDestination(13, reg);
		add(reg);

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cpu.removeDestination(13, cmanager.getRegisterView(CPU.Regs.STATE));
		cmanager.panelDeactivate();
	}

	@Override
	public String getPanelName() {
		return "Базовая ЭВМ";
	}

	@Override
	public InputRegisterView getNextInputRegister() {
		return (InputRegisterView)cmanager.getRegisterView(CPU.Regs.KEY);
	}

	@Override
	public void stepStart() { }

	@Override
	public void stepFinish() { }
}
