/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ControlUnit;
import ru.ifmo.cs.bcomp.StateReg;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.Register;
import ru.ifmo.cs.io.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ComponentManager {
	private class SignalHandler implements DataDestination {
		private final ControlSignal signal;
		private RegisterView reg = null;

		public SignalHandler(ControlSignal signal) {
			this.signal = signal;
		}

		public void setRegisterView(RegisterView reg) {
			this.reg = reg;
		}

		@Override
		public void setValue(int value) {
			activeSignals.add(signal);
			synchronized (lockActivePanel) {
				if (reg != null)
					reg.setValue();
			}
		}
	}

	private class ButtonProperties {
		private int width;
		private String[] texts;
		private ActionListener listener;

		public ButtonProperties(int width, String[] texts, ActionListener listener) {
			this.width = width;
			this.texts = texts;
			this.listener = listener;
		}

		public int getWidth() {
			return width;
		}

		public String[] getTexts() {
			return texts;
		}

		public ActionListener getListener() {
			return listener;
		}
	}

	private class ButtonsPanel extends JComponent {
		public ButtonsPanel() {
			setBounds(0, BUTTONS_Y, FRAME_WIDTH, BUTTONS_HEIGHT);

			int buttonsX = 1;

			buttons = new JButton[buttonProperties.length];

			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new JButton(buttonProperties[i].getTexts()[0]);
				buttons[i].setForeground(buttonColors[0]);
				buttons[i].setFont(FONT_COURIER_PLAIN_12);
				buttons[i].setBounds(buttonsX, 0, buttonProperties[i].getWidth(), BUTTONS_HEIGHT);
				buttonsX += buttonProperties[i].getWidth() + BUTTONS_SPACE;
				buttons[i].setFocusable(false);
				buttons[i].addActionListener(buttonProperties[i].getListener());
				add(buttons[i]);
			}
		}
	}

	private Color[] buttonColors = new Color[] { Color.BLACK, Color.RED };
	private ButtonProperties[] buttonProperties = {
		new ButtonProperties(135, new String[] { "F4 Ввод адреса" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdEnterAddr();
			}
		}),
		new ButtonProperties(115, new String[] { "F5 Запись" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdWrite();
			}
		}),
		new ButtonProperties(115, new String[] { "F6 Чтение" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdRead();
			}
		}),
		new ButtonProperties(90, new String[] { "F7 Пуск" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdStart();
			}
		}),
		new ButtonProperties(135, new String[] { "F8 Продолжение" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdContinue();
			}
		}),
		new ButtonProperties(110, new String[] { "F9 Останов", "F9 Работа" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdInvertRunState();
			}
		}),
		new ButtonProperties(130, new String[] { "Shift+F9 Такт" }, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdInvertClockState();
			}
		})
	};

	private static final int BUTTON_RUN = 5;
	private static final int BUTTON_CLOCK = 6;
	private JButton[] buttons;
	private ButtonsPanel buttonsPanel = new ButtonsPanel();

	private GUI gui;
	private CPU cpu;
	private IOCtrl[] ioctrls;
	private MemoryView mem;
	private MemoryView micromem;
	private EnumMap<CPU.Reg, RegisterView> regs = new EnumMap<CPU.Reg, RegisterView>(CPU.Reg.class);
	private volatile BCompPanel activePanel;
	private InputRegisterView activeInput;
	private final long[] delayPeriods = { 0, 1, 5, 10, 25, 50, 100, 1000 };
	private int currentDelay = 3;
	private volatile boolean running = false;
	private final Object lockRun = new Object();
	private final Object lockActivePanel = new Object();
	private JCheckBox cucheckbox;
	private volatile boolean cuswitch = false;
	private ArrayList<ControlSignal> activeSignals = new ArrayList<ControlSignal>();
	private EnumMap<ControlSignal, SignalHandler> signalHandlers =
		new EnumMap<ControlSignal, SignalHandler>(ControlSignal.class);
	private final RegistersSignals[] regsignals;
	private static final ControlSignal[] cpusignals = {
		ControlSignal.HALT,
		ControlSignal.DATA_TO_ALU,
		ControlSignal.INSTR_TO_ALU,
		ControlSignal.IP_TO_ALU,
		ControlSignal.ACCUM_TO_ALU,
		ControlSignal.STATE_TO_ALU,
		ControlSignal.KEY_TO_ALU,
		ControlSignal.ALU_AND,
		ControlSignal.SHIFT_RIGHT,
		ControlSignal.SHIFT_LEFT,
		ControlSignal.BUF_TO_STATE_C,
		ControlSignal.BUF_TO_STATE_N,
		ControlSignal.BUF_TO_STATE_Z,
		ControlSignal.CLEAR_STATE_C,
		ControlSignal.SET_STATE_C,
		ControlSignal.BUF_TO_ADDR,
		ControlSignal.BUF_TO_DATA,
		ControlSignal.BUF_TO_INSTR,
		ControlSignal.BUF_TO_IP,
		ControlSignal.BUF_TO_ACCUM,
		ControlSignal.MEMORY_READ,
		ControlSignal.MEMORY_WRITE,
		ControlSignal.INPUT_OUTPUT,
		ControlSignal.DISABLE_INTERRUPTS,
		ControlSignal.ENABLE_INTERRUPTS,
		ControlSignal.SET_RUN_STATE,
		ControlSignal.SET_PROGRAM,
		ControlSignal.SET_REQUEST_INTERRUPT
	};

	public ComponentManager(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.ioctrls = gui.getIOCtrls();

		cpu.addDestination(ControlSignal.MEMORY_READ, new DataDestination() {
			@Override
			public void setValue(int value) {
				if (activePanel != null)
					mem.eventRead();
				else
					mem.updateLastAddr();
			}
		});

		cpu.addDestination(ControlSignal.MEMORY_WRITE, new DataDestination() {
			@Override
			public void setValue(int value) {
				if (activePanel != null)
					mem.eventWrite();
				else
					mem.updateLastAddr();
			}
		});

		for (ControlSignal signal : cpusignals)
			cpu.addDestination(signal, createSignalHandler(signal));

		ioctrls[0].addListener(IOCtrl.ControlSignal.CHKFLAG, createSignalHandler(ControlSignal.IO0_TSF));
		ioctrls[1].addListener(IOCtrl.ControlSignal.CHKFLAG, createSignalHandler(ControlSignal.IO1_TSF));
		ioctrls[1].addListener(IOCtrl.ControlSignal.OUT, createSignalHandler(ControlSignal.IO1_OUT));
		ioctrls[2].addListener(IOCtrl.ControlSignal.CHKFLAG, createSignalHandler(ControlSignal.IO2_TSF));
		ioctrls[2].addListener(IOCtrl.ControlSignal.IN, createSignalHandler(ControlSignal.IO2_IN));
		ioctrls[3].addListener(IOCtrl.ControlSignal.CHKFLAG, createSignalHandler(ControlSignal.IO3_TSF));
		ioctrls[3].addListener(IOCtrl.ControlSignal.IN, createSignalHandler(ControlSignal.IO3_IN));
		ioctrls[3].addListener(IOCtrl.ControlSignal.OUT, createSignalHandler(ControlSignal.IO3_OUT));

		gui.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						activeInput.moveLeft();
						break;

					case KeyEvent.VK_RIGHT:
						activeInput.moveRight();
						break;

					case KeyEvent.VK_UP:
						activeInput.invertBit();
						break;

					case KeyEvent.VK_0:
					case KeyEvent.VK_NUMPAD0:
						activeInput.setBit(0);
						break;

					case KeyEvent.VK_1:
					case KeyEvent.VK_NUMPAD1:
						activeInput.setBit(1);
						break;

					// XXX: Must be corrected to support Tab key
					case KeyEvent.VK_N:
						InputRegisterView newInput = activePanel.getNextInputRegister();
						if (activeInput != newInput) {
							activeInput.setActive(false);
							(activeInput = newInput).setActive(true);
						}

					case KeyEvent.VK_F1:
						if (e.isShiftDown())
							cmdAbout();
						else
							cmdSetIOFlag(1);
						break;

					case KeyEvent.VK_F2:
						cmdSetIOFlag(2);
						break;

					case KeyEvent.VK_F3:
						cmdSetIOFlag(3);
						break;

					case KeyEvent.VK_F4:
						cmdEnterAddr();
						break;

					case KeyEvent.VK_F5:
						cmdWrite();
						break;

					case KeyEvent.VK_F6:
						cmdRead();
						break;

					case KeyEvent.VK_F7:
						cmdStart();
						break;

					case KeyEvent.VK_F8:
						cmdContinue();
						break;

					case KeyEvent.VK_F9:
						if (e.isShiftDown())
							cmdInvertClockState();
						else
							cmdInvertRunState();
						break;

					case KeyEvent.VK_F10:
						System.exit(0);
						break;

					case KeyEvent.VK_F11:
						cmdPrevDelay();
						break;

					case KeyEvent.VK_F12:
						cmdNextDelay();
						break;
				}
			}
		});

		for (CPU.Reg reg : CPU.Reg.values()) {
			switch (reg) {
				case KEY:
					regs.put(reg, new InputRegisterView((Register)cpu.getRegister(reg)));
					break;

				case STATE:
					regs.put(reg, new StateRegisterView(cpu.getRegister(reg)));
					break;

				default:
					regs.put(reg, new RegisterView(cpu.getRegister(reg)));
			}
		}

		regsignals = new RegistersSignals[] {
			new RegistersSignals(regs.get(CPU.Reg.STATE),
				ControlSignal.BUF_TO_STATE_C, ControlSignal.CLEAR_STATE_C, ControlSignal.SET_STATE_C),
			new RegistersSignals(regs.get(CPU.Reg.ADDR), ControlSignal.BUF_TO_ADDR),
			new RegistersSignals(regs.get(CPU.Reg.DATA), ControlSignal.BUF_TO_DATA),
			new RegistersSignals(regs.get(CPU.Reg.INSTR), ControlSignal.BUF_TO_INSTR),
			new RegistersSignals(regs.get(CPU.Reg.IP), ControlSignal.BUF_TO_IP),
			new RegistersSignals(regs.get(CPU.Reg.ACCUM),
				ControlSignal.BUF_TO_ACCUM, ControlSignal.IO2_IN, ControlSignal.IO3_IN)		
		};

		mem = new MemoryView(cpu.getMemory(), MEM_X, MEM_Y);
		micromem = new MemoryView(cpu.getMicroMemory(), 711, MEM_Y);

		cucheckbox = new JCheckBox("Работа с МПУУ");
		cucheckbox.setFocusable(false);
		cucheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				cuswitch = e.getStateChange() == ItemEvent.SELECTED;
			}
		});
	}

	private SignalHandler createSignalHandler(ControlSignal cs) {
		SignalHandler sighandler = new SignalHandler(cs);
		signalHandlers.put(cs, sighandler);
		return sighandler;
	}

	public void startBComp() {
		Thread bcomp = new Thread(new Runnable() {
			@Override
			public void run() {
				for (;;) {
					synchronized (lockRun) {
						try {
							lockRun.wait();
						} catch (Exception e) {
							continue;
						}
					}

					running = true;
					cpu.cont();

					for (;;) {
						synchronized (lockActivePanel) {
							if (activePanel != null)
								activePanel.stepStart();
						}

						activeSignals.clear();
						boolean run = cpu.step();

						synchronized (lockActivePanel) {
							if (activePanel != null)
								activePanel.stepFinish();
						}

						if (!run)
							break;
						try {
							Thread.sleep(delayPeriods[currentDelay]);
						} catch (Exception e) {	}
					}

					running = false;
				}
			}
		});
		bcomp.start();
	}

	public void panelActivate(BCompPanel component) {
		synchronized (lockActivePanel) {
			activePanel = component;
			setRegistersHandlers(activePanel.getRegistersSignals());
			setRegistersHandlers(regsignals);
		}

		activePanel.add(mem);
		activePanel.add(buttonsPanel);

		activeInput = (InputRegisterView)regs.get(CPU.Reg.KEY);
		activeInput.setProperties("Клавишный регистр", REG_KEY_X, REG_KEY_Y, false);
		activeInput.setActive(true);
		component.add(activeInput);

		mem.updateMemory();

		cuswitch = false;

		gui.requestFocusInWindow();
	}

	public void panelDeactivate() {
		synchronized (lockActivePanel) {
			clearRegistersHandlers(regsignals);
			clearRegistersHandlers(activePanel.getRegistersSignals());
			activePanel = null;
		}
	}

	public void setRegistersHandlers(RegistersSignals[] regsignals) {
		for (RegistersSignals regsig : regsignals)
			for (ControlSignal signal : regsig.signals)
				signalHandlers.get(signal).setRegisterView(regsig.register);
	}

	public void clearRegistersHandlers(RegistersSignals[] regsignals) {
		for (RegistersSignals regsig : regsignals)
			for (ControlSignal signal : regsig.signals)
				signalHandlers.get(signal).setRegisterView(null);
	}

	public RegisterView getRegisterView(CPU.Reg reg) {
		return regs.get(reg);
	}

	public void cmdContinue() {
		if (running)
			return;
		synchronized (lockRun) {
			lockRun.notifyAll();
		}
	}

	private void cmdCPUjump(int label) {
		if (running)
			return;
		cpu.jump(label);
		cmdContinue();
	}

	public void cmdEnterAddr() {
		if (cuswitch) {
			cpu.jump();
			regs.get(CPU.Reg.MIP).setValue();
		} else
			cmdCPUjump(ControlUnit.LABEL_ADDR);
	}

	public void cmdWrite() {
		if (cuswitch) {
			micromem.updateLastAddr();
			cpu.setMicroMemory();
			micromem.updateMemory();
			regs.get(CPU.Reg.MIP).setValue();
		} else
			cmdCPUjump(ControlUnit.LABEL_WRITE);
	}

	public void cmdRead() {
		if (cuswitch) {
			micromem.eventRead();
			cpu.readMInstr();
			regs.get(CPU.Reg.MIP).setValue();
			regs.get(CPU.Reg.MINSTR).setValue();
		} else
			cmdCPUjump(ControlUnit.LABEL_READ);
	}

	public void cmdStart() {
		cmdCPUjump(ControlUnit.LABEL_START);
	}

	public void cmdInvertRunState() {
		cpu.invertRunState();
		int state = cpu.getStateValue(StateReg.FLAG_RUN);
		buttons[BUTTON_RUN].setForeground(buttonColors[state]);
		buttons[BUTTON_RUN].setText(buttonProperties[BUTTON_RUN].getTexts()[state]);
	}

	public void cmdInvertClockState() {
		cpu.invertClockState();
		boolean state = cpu.getClockState();
		buttons[BUTTON_CLOCK].setForeground(buttonColors[state ? 0 : 1]);
		if (!state)
			cpu.cont();
	}

	public void cmdSetIOFlag(int dev) {
		ioctrls[dev].setFlag();
	}

	public void cmdNextDelay() {
		currentDelay = currentDelay < delayPeriods.length - 1 ? currentDelay + 1 : 0;
	}

	public void cmdPrevDelay() {
		currentDelay = (currentDelay > 0 ? currentDelay : delayPeriods.length) - 1;
	}

	public void cmdAbout() {
		JOptionPane.showMessageDialog(gui,
			"Эмулятор Базовой ЭВМ. Версия r" + GUI.class.getPackage().getImplementationVersion() +
			"\n\nЗагружена " + gui.getMicroProgramName() + " микропрограмма",
			"О программе", JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean getRunningState() {
		return running;
	}

	public void activeInputSwitch(InputRegisterView input) {
		if (activeInput == input)
			return;

		activeInput.setActive(false);
		(activeInput = input).setActive(true);
	}

	public MemoryView getMicroMemory() {
		return micromem;
	}

	public JCheckBox getMPCheckBox() {
		return cucheckbox;
	}

	public ArrayList<ControlSignal> getActiveSignals() {
		return activeSignals;
	}

	public void clearActiveSignals() {
		activeSignals.clear();
	}
}
