/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JComponent;
import ru.ifmo.cs.bcomp.CPU;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ComponentManager {
	// Buttons coordinates
	private static final int buttonsY = 495;
	private static final int buttonsHeight = 30;
	private static final int buttonsSpace = 2;

	private JButton[] buttons;
	private CPU cpu;
	private MemoryView mem = new MemoryView("Память", 1, 1, new MemoryInterface() {
		public int getValue(int addr) {
			return cpu.getMemory(addr);
		}
	});

	public ComponentManager(CPU cpu) {
		this.cpu = cpu;

		buttons = new JButton[] {
			new JButton("F4 - Ввод адреса"),
			new JButton("F5 - Запись"),
			new JButton("F6 - Чтение"),
			new JButton("F7 - Пуск"),
			new JButton("F8 - Продолжение")
		};
		int[] buttonsWidth = {153, 118, 118, 104, 153};
		int buttonsX = 1;

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setBounds(buttonsX, buttonsY, buttonsWidth[i], buttonsHeight);
			buttonsX += buttonsWidth[i] + buttonsSpace;
			buttons[i].setFocusable(false);
			buttons[i].setForeground(Color.BLACK);
			buttons[i].setFont(new Font("Courier New", Font.PLAIN, 12));
		}

		/// XXX: Добавить actionListener() в JButtons
	}

	public void paintComponent(JComponent component, Graphics2D rs) {
		for (JButton button : buttons)
			component.add(button);

		mem.paintComponent(rs);
	}
}
