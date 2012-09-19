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
import ru.ifmo.cs.bcomp.ui.components.*;
import ru.ifmo.cs.io.IOCtrl;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class GUI extends JApplet {
	private ComponentManager cmanager;
	private JTabbedPane tabs;
	private ActivateblePanel activePanel = null;
	private BasicComp bcomp;
	private CPU cpu;

	public GUI() throws Exception {
		bcomp = new BasicComp(MicroPrograms.Type.BASE);
		cpu = bcomp.getCPU();
		setFocusable(true);
	}

	@Override
	public void init() {
		cmanager = new ComponentManager(this);

		ActivateblePanel[] panes = new ActivateblePanel[] {
			new BasicView(this),
			new IOView(this),
			new MPView(this),
			new AssemblerView(this)
		};

		tabs = new JTabbedPane();
		tabs.setFocusable(false);

		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (activePanel != null)
					activePanel.panelDeactivate();

				activePanel = (ActivateblePanel)tabs.getSelectedComponent();
				activePanel.panelActivate();
			}
		});

		for (ActivateblePanel pane : panes) {
			pane.setFocusable(false);
			tabs.addTab(pane.getPanelName(), pane);
		}

		add(tabs);
	}

	public static void main(String[] args) throws Exception {
		GUI gui = new GUI();
		JFrame frame = new JFrame("БЭВМ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui);
		frame.getContentPane().setPreferredSize(
			new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.pack();
		frame.setResizable(false);
		gui.init();
		gui.start();
		frame.setVisible(true);
	}

	public CPU getCPU() {
		return cpu;
	}

	public IOCtrl[] getIOCtrls() {
		return bcomp.getIOCtrls();
	}

	public ComponentManager getComponentManager() {
		return cmanager;
	}
}
