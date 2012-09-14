/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class MPView extends ActivateblePanel {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private MemoryView mem;

	public MPView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		mem = new MemoryView(cpu.getMicroMemory(), "Память МК", 711, 1);
		add(mem);

		RegisterView reg = cmanager.getRegisterView(CPU.Regs.MIP);
		reg.setProperties("Счётчик МК", 400, 1, false);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Regs.MINSTR);
		reg.setProperties("Регистр Микрокоманд", 400, 100, false);
		add(reg);
	}

	@Override
    public void paintComponent(Graphics g) {
        Graphics2D rs = (Graphics2D) g;

		//cmanager.paintComponent(this, rs);
		//mem.paintComponent(this, rs);
	}

	@Override
	public void panelActivated() {
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
		reg.setProperties("Регистр состояния", 169, 375, true);
		add(reg);
	}
}
