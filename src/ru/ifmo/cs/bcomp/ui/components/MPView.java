/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.elements.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MPView extends BCompPanel {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private MemoryView mem;
	private RegisterView regMIP;
	private RegisterView regMInstr;
	private RegisterView regBuf;
	private DataDestination stepHandler = new DataDestination() {
		@Override
		public void setValue(int value) {
			regMInstr.setValue();
			mem.eventRead();
		}
	};

	public MPView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		mem = new MemoryView(cpu.getMicroMemory(), "Память МК", 711, 1);
		add(mem);

		regMIP = cmanager.getRegisterView(CPU.Regs.MIP);
		regMIP.setProperties("Счётчик МК", 400, 1, false);
		add(regMIP);

		regMInstr = cmanager.getRegisterView(CPU.Regs.MINSTR);
		regMInstr.setProperties("Регистр Микрокоманд", 400, 100, false);
		add(regMInstr);

		regBuf = cmanager.getRegisterView(CPU.Regs.BUF);
		regBuf.setProperties("БР", 400, 200, true);
		add(regBuf);
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
		reg.setProperties("Регистр состояния", 169, 375, true);
		add(reg);

		mem.updateLastAddr();
		mem.updateMemory();

		regMIP.setValue();
		regMInstr.setValue();
		regBuf.setValue();

		cpu.addDestination(29, stepHandler);
		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cpu.addDestination(29, stepHandler);
		cmanager.panelDeactivate();
	}

	@Override
	public String getPanelName() {
		return "Работа с МПУ";
	}

	@Override
	public InputRegisterView getNextInputRegister() {
		return (InputRegisterView)cmanager.getRegisterView(CPU.Regs.KEY);
	}

	@Override
	public void stepDone() {
		regBuf.setValue();
		regMIP.setValue();
		cmanager.getRegisterView(CPU.Regs.STATE).setValue();
	}
}
