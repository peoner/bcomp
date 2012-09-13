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

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class ComponentManager {
	private static final FontRenderContext fr = new FontRenderContext(null, true, true);
	// Fonts
	public static final Font FONT_COURIER_PLAIN_12 = new Font("Courier New", Font.PLAIN, 12);
	public static final Font FONT_COURIER_BOLD_23 = new Font("Courier New", Font.BOLD, 23);
	public static final int FONT_COURIER_BOLD_23_WIDTH =
		(int)FONT_COURIER_BOLD_23.getStringBounds("0", fr).getWidth();
	public static final Font FONT_COURIER_BOLD_25 = new Font("Courier New", Font.BOLD, 25);
	public static final int FONT_COURIER_BOLD_25_WIDTH =
		(int)FONT_COURIER_BOLD_25.getStringBounds("0", fr).getWidth();
	// Colors
	public static final Color COLOR_MEM_BGADDR = new Color(157, 189, 165);
	public static final Color COLOR_MEM_BGVALUE = new Color(219, 249, 235);
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

		regKey = new RegisterView(gui, CPU.Regs.KEY, "Клавишный регистр", regKeyX, regKeyY, false);

		mem = new MemoryView("Память", 1, 1, new MemoryInterface() {
			public int getValue(int addr) {
				return cpu.getMemoryValue(addr);
			}

			public int getWidth() {
				return cpu.getRegWidth(CPU.Regs.ADDR);
			}
			});	
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
