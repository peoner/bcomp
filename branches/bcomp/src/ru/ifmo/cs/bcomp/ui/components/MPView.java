/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MPView extends BCompPanel {
	private final MemoryView mem;
	private final RegisterView regMIP;
	private final RegisterView regMInstr;
	private final RegisterView regBuf;
	private final RegisterView regState;
	private final JCheckBox cucheckbox;
	private static final ControlSignal[] statesignals = {
	};

	public MPView(GUI gui) {
		super(gui.getComponentManager());

		add(mem = cmanager.getMicroMemory());

		regMIP = cmanager.getRegisterView(CPU.Reg.MIP);
		regMIP.setProperties("Счётчик МК", 400, 1, false);
		add(regMIP);

		regMInstr = cmanager.getRegisterView(CPU.Reg.MINSTR);
		regMInstr.setProperties("Регистр Микрокоманд", 400, 100, false);
		add(regMInstr);

		regBuf = cmanager.getRegisterView(CPU.Reg.BUF);
		regBuf.setProperties("БР", 400, 200, true);
		add(regBuf);

		regState = cmanager.getRegisterView(CPU.Reg.STATE);

		setSignalListeners(new SignalListener[] {
			new SignalListener(regState, 
				ControlSignal.HALT,
				ControlSignal.BUF_TO_STATE_N,
				ControlSignal.BUF_TO_STATE_Z,
				ControlSignal.DISABLE_INTERRUPTS,
				ControlSignal.ENABLE_INTERRUPTS,
				ControlSignal.IO0_TSF,
				ControlSignal.IO1_TSF,
				ControlSignal.IO2_TSF,
				ControlSignal.IO3_TSF,
				ControlSignal.SET_RUN_STATE,
				ControlSignal.SET_PROGRAM,
				ControlSignal.SET_REQUEST_INTERRUPT),
			new SignalListener(regBuf,
				ControlSignal.ALU_AND,
				ControlSignal.SHIFT_RIGHT,
				ControlSignal.SHIFT_LEFT)
		});

		cucheckbox = cmanager.getMPCheckBox();
		cucheckbox.setBounds(450, 400, 200, 30);
		cucheckbox.setOpaque(false);
		add(cucheckbox);
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
		reg.setProperties("РС", 169, 375, true);
		add(reg);

		mem.updateLastAddr();
		mem.updateMemory();

		regMIP.setValue();
		regMInstr.setValue();
		regBuf.setValue();

		cucheckbox.setSelected(false);

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cmanager.panelDeactivate();
	}

	@Override
	public String getPanelName() {
		return "Работа с МПУ";
	}

	@Override
	public void stepStart() {
		mem.eventRead();
	}

	@Override
	public void stepFinish() {
		ArrayList<ControlSignal> signals = cmanager.getActiveSignals();

		regMIP.setValue();
		regMInstr.setValue();
	}
}
