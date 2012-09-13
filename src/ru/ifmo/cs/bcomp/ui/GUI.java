/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
	private ComponentManager cmanager;
	JTabbedPane tabbedPane;
	private BasicComp bcomp;
	private CPU cpu;

	public GUI() throws Exception {
		bcomp = new BasicComp(MicroPrograms.Type.BASE);
		cpu = bcomp.getCPU();
	}

	@Override
	public void init() {
		cmanager = new ComponentManager(this);

		JComponent[] panes = new JComponent[] {
			new BasicView(this),
			new IOView(this),
			new MPView(this)
		};
		String[] paneNames = new String[] {"Базовая ЭВМ", "Работа с ВУ", "Работа с МПУ"};

		tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				cmanager.addSubComponents((JComponent)tabbedPane.getSelectedComponent());
			}
		});

		for (int i = 0; i < panes.length; i++)
			tabbedPane.addTab(paneNames[i], panes[i]);

		add(tabbedPane);
	}

	public static void main(String[] args) throws Exception {
		GUI applet = new GUI();
		JFrame frame = new JFrame("БЭВМ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(applet);
		//frame.setSize(852, 586);
		frame.getContentPane().setPreferredSize(
			new Dimension(ComponentManager.FRAME_WIDTH, ComponentManager.FRAME_HEIGHT));
		frame.pack();
		frame.setResizable(false);
		applet.init();
		applet.start();
		frame.setVisible(true);
	}

	public CPU getCPU() {
		return cpu;
	}

	public ComponentManager getComponentManager() {
		return cmanager;
	}
}
