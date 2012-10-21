/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.io.IOCtrl;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOView extends BCompPanel {
	private class FlagButtonListener implements ActionListener {
		private final IOCtrl ioctrl;

		public FlagButtonListener(IOCtrl ioctrl) {
			this.ioctrl = ioctrl;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ioctrl.setFlag();
		}
	}

	private class FlagListener implements DataDestination {
		private final JButton flag;

		public FlagListener(JButton flag) {
			this.flag = flag;
		}

		@Override
		public void setValue(int value) {
			flag.setForeground(value == 1 ? Color.RED : Color.BLACK);
		}
	}

	private IOCtrl[] ioctrls;
	private RegisterView[] ioregs = new RegisterView[3];
	private JButton[] flags = {
		new JButton("F1 ВУ1"),
		new JButton("F2 ВУ2"),
		new JButton("F3 ВУ3")		
	};

	public IOView(GUI gui) {
		super(gui.getComponentManager());

		ioctrls = gui.getIOCtrls();

		for (int i = 0; i < ioregs.length; i++) {
			ioregs[i] = i == 0 ?
				new RegisterView(ioctrls[i + 1].getRegData()) :
				(new InputRegisterView(cmanager, ioctrls[i + 1].getRegData()));
			ioregs[i].setProperties("ВУ" + Integer.toString(i + 1), IO_X + i * IO_DELIM, 300, false);
			add(ioregs[i]);

			flags[i].setFont(FONT_COURIER_PLAIN_12);
			flags[i].setBounds(350, 1 + i * 75, 100, CELL_HEIGHT);
			flags[i].setFocusable(false);
			add(flags[i]);
			flags[i].addActionListener(new FlagButtonListener(ioctrls[i + 1]));
			ioctrls[i + 1].addDestination(IOCtrl.ControlSignal.SETFLAG, new FlagListener(flags[i]));
		}

		setSignalListeners(new SignalListener[] {
			new SignalListener(ioregs[0], ControlSignal.IO1_OUT),
			new SignalListener(ioregs[2], ControlSignal.IO3_OUT)
		});
	}

	@Override
	public void panelActivate() {
		RegisterView reg = cmanager.getRegisterView(CPU.Reg.ADDR);
		reg.setProperties("РА", REG_ADDR_X_IO, REG_ADDR_Y_IO, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.IP);
		reg.setProperties("СК", REG_ADDR_X_IO, REG_IP_Y_IO, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.INSTR);
		reg.setProperties("РК", REG_DATA_X_IO, REG_INSTR_Y_IO, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.DATA);
		reg.setProperties("РД", REG_DATA_X_IO, REG_DATA_Y_IO, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.ACCUM);
		reg.setProperties("Акк", REG_DATA_X_IO, REG_ACCUM_Y_IO, true);
		add(reg);

		reg = cmanager.getRegisterView(CPU.Reg.STATE);
		reg.setProperties("C", REG_C_X_IO, REG_ACCUM_Y_IO, false);
		add(reg);

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		cmanager.panelDeactivate();
	}

	@Override
	public String getPanelName() {
		return "Работа с ВУ";
	}

	@Override
	public void stepFinish() {
		super.stepFinish();

		for (ControlSignal signal : cmanager.getActiveSignals())
			switch (signal) {
				case IO1_OUT:
					ioregs[0].setValue();
					break;

				case IO3_OUT:
					ioregs[2].setValue();
					break;
			}
	}
}
