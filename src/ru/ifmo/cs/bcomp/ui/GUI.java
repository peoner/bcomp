/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import java.awt.Dimension;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.MicroPrograms;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.FRAME_HEIGHT;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.FRAME_WIDTH;
import ru.ifmo.cs.bcomp.ui.components.*;
import ru.ifmo.cs.io.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class GUI extends JApplet {
	private ComponentManager cmanager;
	private JTabbedPane tabs;
	private ActivateblePanel activePanel = null;
	private final BasicComp bcomp;
	private final CPU cpu;

	public GUI(MicroProgram mp) throws Exception {
		bcomp = new BasicComp(mp);
		cpu = bcomp.getCPU();
	}

	public GUI() throws Exception {
		this(MicroPrograms.getMicroProgram(MicroPrograms.DEFAULT_MICROPROGRAM));
	}

	@Override
	public void init() {
		cmanager = new ComponentManager(this);
		cmanager.startBComp();
		bcomp.startTimer();

		ActivateblePanel[] panes = {
			new BasicView(this),
			new IOView(this),
			new MPView(this),
			new AssemblerView(this)
		};

		tabs = new JTabbedPane();
		tabs.addKeyListener(cmanager.getKeyListener());

		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (activePanel != null)
					activePanel.panelDeactivate();

				activePanel = (ActivateblePanel)tabs.getSelectedComponent();
				activePanel.panelActivate();
			}
		});

		for (ActivateblePanel pane : panes)
			tabs.addTab(pane.getPanelName(), pane);

		add(tabs);
	}

	@Override
	public void start() {
		cmanager.switchFocus();
	}

	public void gui() throws Exception {
		JFrame frame = new JFrame("БЭВМ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.getContentPane().setPreferredSize(
			new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.pack();
		frame.setResizable(false);
		init();
		start();
		frame.setVisible(true);
	}

	public BasicComp getBasicComp() {
		return bcomp;
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

	public String getMicroProgramName() {
		return cpu.getMicroProgramName();
	}
}
