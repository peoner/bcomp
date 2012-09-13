/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

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
	private RegisterView regKey;

	public ComponentManager(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();

		regKey = new RegisterView(cpu.getRegister(CPU.Regs.ACCUM), "Клавишный регистр", regKeyX, regKeyY, false);

		mem = new MemoryView(cpu.getMemory(), "Память", 1, 1);
	}

	public void addSubComponents(JComponent component) {
		component.add(mem);
		component.add(buttonsPanel);
		component.add(regKey);
	}

	public void paintComponent(JComponent component, Graphics2D rs) {
		
		// Slowest
		//rs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


		//mem.repaint();
	}

	public static String toHex(int value, int width) {
		return String.format("%1$0" + width + "x", value).toUpperCase();
	}
}
