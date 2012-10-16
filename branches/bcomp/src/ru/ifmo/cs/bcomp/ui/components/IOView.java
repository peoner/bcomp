/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private ComponentManager cmanager;
	private RegisterView[] ioregs = new RegisterView[3];
	private InputRegisterView[] inputs = new InputRegisterView[3];
	private InputRegisterMouseListener[] inputlisteners = new InputRegisterMouseListener[3];
	private JButton[] flags = {
		new JButton("F1 ВУ1"),
		new JButton("F2 ВУ2"),
		new JButton("F3 ВУ3")		
	};
	private SignalListener[] listeners;
	private int lastInput;

	public IOView(GUI gui) {
		cmanager = gui.getComponentManager();

		inputs[0] = (InputRegisterView)cmanager.getRegisterView(CPU.Reg.KEY);

		ioctrls = gui.getIOCtrls();

		for (int i = 0; i < ioregs.length; i++) {
			ioregs[i] = i == 0 ?
				new RegisterView(ioctrls[i + 1].getRegData()) :
				(inputs[i] = new InputRegisterView(ioctrls[i + 1].getRegData()));
			ioregs[i].setProperties("ВУ" + Integer.toString(i + 1), 500, 1 + i * 75, false);
			add(ioregs[i]);
			inputlisteners[i] = new InputRegisterMouseListener(i);

			flags[i].setFont(FONT_COURIER_PLAIN_12);
			flags[i].setBounds(350, 1 + i * 75, 100, CELL_HEIGHT);
			flags[i].setFocusable(false);
			add(flags[i]);
			flags[i].addActionListener(new FlagButtonListener(ioctrls[i + 1]));
			ioctrls[i + 1].addFlagListener(new FlagListener(flags[i]));
		}

		listeners = new SignalListener[] {
			cmanager.createSignalListener(CPU.Reg.STATE,
				ControlSignal.BUF_TO_STATE_C, ControlSignal.CLEAR_STATE_C, ControlSignal.SET_STATE_C)
		};
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
		add(reg);

		((InputRegisterView)ioregs[1]).setActive(false);
		((InputRegisterView)ioregs[2]).setActive(false);

		ioctrls[1].addOutListener(ioregs[0]);
		ioctrls[3].addOutListener(ioregs[2]);

		for (int i = 0; i < inputs.length; i++) {
			inputs[i].addMouseListener(inputlisteners[i]);
		}

		cmanager.panelActivate(this);
	}

	@Override
	public void panelDeactivate() {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].removeMouseListener(inputlisteners[i]);
		}

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

	@Override
	public SignalListener[] getSignalListeners() {
		return listeners;
	}
}
