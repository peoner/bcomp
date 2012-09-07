/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import javax.swing.*;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroPrograms;
import ru.ifmo.cs.bcomp.ui.components.BasicView;
import ru.ifmo.cs.bcomp.ui.components.ComponentManager;
import ru.ifmo.cs.bcomp.ui.components.IOView;
import ru.ifmo.cs.bcomp.ui.components.MPView;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class GUI extends JApplet {
	private BasicComp bcomp;
	private CPU cpu;

	public GUI() throws Exception {
		bcomp = new BasicComp(MicroPrograms.Type.BASE);
		cpu = bcomp.getCPU();
	}

	public void init() {
		ComponentManager cmanager = new ComponentManager(cpu);
		JComponent[] panes = new JComponent[] {
			new BasicView(cpu, cmanager),
			new IOView(cpu, cmanager),
			new MPView(cpu, cmanager)
		};
		String[] paneNames = new String[] {"Базовая ЭВМ", "Работа с ВУ", "Работа с МПУ"};

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);

		for (int i = 0; i < panes.length; i++)
			tabbedPane.addTab(paneNames[i], panes[i]);

		add(tabbedPane);
	}

	public static void main(String[] args) throws Exception {
		GUI applet = new GUI();
		JFrame frame = new JFrame("БЭВМ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(applet);
		frame.setSize(852, 586);
		applet.init();
		applet.start();
		frame.setVisible(true);
	}
}
