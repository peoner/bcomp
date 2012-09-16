/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.*;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends ActivateblePanel {
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

		//cmanager.paintComponent(this, rs);
	}

	@Override
	public InputRegisterView panelActivated() {
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

		StateRegisterView statereg = (StateRegisterView)cmanager.getRegisterView(CPU.Regs.STATE);
		statereg.setProperties("C", 169, 300, false);
		add(statereg);

		cmanager.addSubComponents(this);

		return getNextInputRegister();
	}

	@Override
	public String getPanelName() {
		return "Базовая ЭВМ";
	}

	@Override
	public InputRegisterView getNextInputRegister() {
		return (InputRegisterView)cmanager.getRegisterView(CPU.Regs.KEY);
	}
}
