/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroPrograms;
import ru.ifmo.cs.bcomp.ui.components.*;
import ru.ifmo.cs.io.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class GUI extends JApplet {
	private ComponentManager cmanager;
	JTabbedPane tabbedPane;
	ActivateblePanel activePanel;
	InputRegisterView activeInput;
	private BasicComp bcomp;
	private CPU cpu;

	public GUI() throws Exception {
		bcomp = new BasicComp(MicroPrograms.Type.BASE);
		cpu = bcomp.getCPU();
	}

	@Override
	public void init() {
		cmanager = new ComponentManager(this);

		ActivateblePanel[] panes = new ActivateblePanel[] {
			new BasicView(this),
			new IOView(this),
			new MPView(this)
		};

		tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				activePanel = (ActivateblePanel)tabbedPane.getSelectedComponent();
				cmanager.addSubComponents(activePanel);
				activePanel.panelActivated();
				activeInput = activePanel.getNextInputRegister();
			}
		});

		for (ActivateblePanel pane : panes)
			tabbedPane.addTab(pane.getPanelName(), pane);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						activeInput.moveLeft();
						break;

					case KeyEvent.VK_RIGHT:
						activeInput.moveRight();
						break;

					case KeyEvent.VK_UP:
						activeInput.invertBit();
						break;

					case KeyEvent.VK_0:
					case KeyEvent.VK_NUMPAD0:
						activeInput.setBit(0);
						break;

					case KeyEvent.VK_1:
					case KeyEvent.VK_NUMPAD1:
						activeInput.setBit(1);
						break;

					// XXX: Must be corrected to support Tab key
					case KeyEvent.VK_N:
						System.out.println("shit");
						InputRegisterView newInput = activePanel.getNextInputRegister();
						if (activeInput != newInput) {
							activeInput.setActive(false);
							(activeInput = newInput).setActive(true);
						}
				}
			}
		});

		add(tabbedPane);
		requestFocusInWindow();
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

	public IOCtrl[] getIOCtrls() {
		return bcomp.getIOCtrls();
	}

	public ComponentManager getComponentManager() {
		return cmanager;
	}
}
