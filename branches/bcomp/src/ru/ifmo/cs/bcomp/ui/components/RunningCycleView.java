/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.bcomp.StateReg;
import ru.ifmo.cs.bcomp.ui.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RunningCycleView extends JComponent {
	private CPU cpu;
	private int width;
	private int height;
	private static final String[] cycles = {
		"Выборка команды",
		"Выбора адреса",
		"Исполнение",
		"Прерывание",
		"Пультовая операция",
		"Программа"
	};
	private final JLabel[] labels = new JLabel[cycles.length];
	private RunningCycle lastcycle = RunningCycle.NONE;
	private int lastprogram = 0;

	public RunningCycleView(CPU cpu, int x, int y) {
		this.cpu = cpu;

		width = REG_16_WIDTH;
		height = 3 + CELL_HEIGHT * (cycles.length + 1);

		JLabel title = new JLabel("Устройство управления", JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_21);
		title.setBounds(1, 1, width - 2, CELL_HEIGHT);
		title.setBackground(COLOR_TITLE);
		title.setOpaque(true);
		add(title);

		for (int i = 0; i < cycles.length; i++) {
			labels[i] = new JLabel(cycles[i], JLabel.CENTER);
			labels[i].setFont(FONT_COURIER_BOLD_25);
			labels[i].setBounds(1, 2 + CELL_HEIGHT * (i + 1), width - 2, CELL_HEIGHT);
			labels[i].setBackground(COLOR_VALUE);
			labels[i].setOpaque(true);
			add(labels[i]);
		}

		setBounds(x, y, width, height);
	}

	public void update() {
		RunningCycle newcycle = cpu.getRunningCycle();
		int newprogram = cpu.getStateValue(StateReg.FLAG_PROG);

		if (newcycle != lastcycle) {
			if (lastcycle != RunningCycle.NONE)
				labels[lastcycle.ordinal()].setForeground(Color.BLACK);
			if (newcycle != RunningCycle.NONE)
				labels[newcycle.ordinal()].setForeground(Color.RED);
			lastcycle = newcycle;
		}

		if (newprogram != lastprogram) {
			labels[labels.length - 1].setForeground(newprogram == 0 ? Color.BLACK : Color.RED);
			lastprogram = newprogram;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		g.drawLine(1, CELL_HEIGHT + 1, width - 2, CELL_HEIGHT + 1);
	}
}
