/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class ComponentManager {
	// Frame dimentions
	public static final int FRAME_WIDTH = 852;
	public static final int FRAME_HEIGHT= 586;

	// Buttons coordinates
	private static final int buttonsHeight = 30;
	private static final int buttonsSpace = 2;
	private static final int buttonsY = 529;
	// Keyboards register
	private static final int regKeyX = 1;
	private static final int regKeyY = 470;

	private class ButtonsPanel extends JComponent {
		public ButtonsPanel() {
			setBounds(0, buttonsY, FRAME_WIDTH, buttonsHeight + 2 * buttonsSpace);

			JButton[] buttons = new JButton[] {
				new JButton("F4 - Ввод адреса"),
				new JButton("F5 - Запись"),
				new JButton("F6 - Чтение"),
				new JButton("F7 - Пуск"),
				new JButton("F8 - Продолжение")
			};
			int[] buttonsWidth = {153, 118, 118, 104, 153};
			int buttonsX = 1;

			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setForeground(Color.BLACK);
				buttons[i].setFont(FONT_COURIER_PLAIN_12);
				buttons[i].setBounds(buttonsX, 0, buttonsWidth[i], buttonsHeight);
				buttonsX += buttonsWidth[i] + buttonsSpace;
				buttons[i].setFocusable(false);
				add(buttons[i]);
			}

			/// XXX: Добавить actionListener() в JButtons
			buttons[0].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mem.tmp();
				}
			});
		}
	}
	private ButtonsPanel buttonsPanel = new ButtonsPanel();

	private GUI gui;
	private CPU cpu;
	private MemoryView mem;
	private EnumMap<CPU.Regs, RegisterView> regs = new EnumMap<CPU.Regs, RegisterView>(CPU.Regs.class);

	public ComponentManager(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();

		for (CPU.Regs reg : CPU.Regs.values()) {
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

		mem = new MemoryView(cpu.getMemory(), "Память", 1, 1);
	}

	public void addSubComponents(ActivateblePanel component) {
		component.add(mem);
		component.add(buttonsPanel);
		regs.get(CPU.Regs.KEY).setProperties("Клавишный регистр", regKeyX, regKeyY, false);

		InputRegisterView keyreg = (InputRegisterView)regs.get(CPU.Regs.KEY);
		keyreg.setActive(true);
		component.add(keyreg);
	}

	public RegisterView getRegisterView(CPU.Regs reg) {
		return regs.get(reg);
	}

	public static String toHex(int value, int width) {
		return String.format("%1$0" + width + "x", value).toUpperCase();
	}

	public static String toBin(int value, int width) {
		StringBuilder str = new StringBuilder(
			String.format("%" + width + "s",
				Integer.toBinaryString(value & ((1 << width) - 1))).replace(" ", "0"));

		if (width > 4) {
			str.insert(str.length() - 4, " ");

			if (width > 8) {
				str.insert(str.length() - 9, " ");

				if (width > 12)
					str.insert(str.length() - 14, " ");
			}
		}

		return str.toString();
	}

	public static int getHexWidth(int width) {
		return (width + 3) >> 2;
	}

	public static int getBinWidth(int width) {
		return width + ((width - 1)>> 2);
	}
}
