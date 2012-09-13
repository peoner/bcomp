/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class RegisterView extends JComponent {
	private String name;
	private GUI gui;
	private CPU cpu;
	private CPU.Regs reg;
	private int width;
	private int height;
	private boolean hex;

	public RegisterView(GUI gui, CPU.Regs reg) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.reg = reg;
	}

	public RegisterView(GUI gui, CPU.Regs reg, String name, int x, int y, boolean hex) {
		this(gui, reg);
		activated(name, x, y, hex);
	}

	public final void activated(String name, int x, int y, boolean hex) {
		this.name = name;
		this.hex = hex;
		width = FONT_COURIER_BOLD_25_WIDTH * cpu.getRegWidth(reg);
		height = 2 * 25 + 2;

		setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(COLOR_BG_TITLE);
		rs.fillRect(1, 1, width - 1, height - 1);
		rs.setPaint(COLOR_BG_VALUE);
		rs.fillRect(10, 10, 100, 20);

		rs.setPaint(Color.BLACK);
		rs.drawRect(0, 0, width - 1, height - 1);
	}
}
