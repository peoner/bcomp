/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.util.ArrayList;
import javax.swing.JCheckBox;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

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
		super(gui.getComponentManager(),
			new RegisterProperties[] {
				new RegisterProperties(CPU.Reg.ADDR, "РА", 200, 1, true),
				new RegisterProperties(CPU.Reg.IP, "СК", 200, 75, true),
				new RegisterProperties(CPU.Reg.INSTR, "РК", 200, 150, true),
				new RegisterProperties(CPU.Reg.DATA, "РД", 200, 225, true),
				new RegisterProperties(CPU.Reg.ACCUM, "Акк", 200, 300, true),
				new RegisterProperties(CPU.Reg.STATE, "РС", 169, 375, true)
			},
			new BusView[] { }
		);

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
		cucheckbox.setBounds(450, 400, 300, 30);
		add(cucheckbox);

		add(new ALUView(400, 300, ALU_WIDTH, ALU_HEIGHT));
	}

	@Override
	public void panelActivate() {
		mem.updateLastAddr();
		mem.updateMemory();

		regMIP.setValue();
		regMInstr.setValue();
		regBuf.setValue();

		cucheckbox.setSelected(false);

		super.panelActivate();
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
