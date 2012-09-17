/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class AssemblerView extends ActivateblePanel {
	private GUI gui;
	private CPU cpu;
	private Assembler asm;
	private JTextArea text = new JTextArea(80, 24);

	public AssemblerView(GUI _gui) {
		this.gui = _gui;
		this.cpu = gui.getCPU();

		asm = new Assembler(cpu.getInstructionSet());

		text.setFont(FONT_COURIER_PLAIN_16);
		text.setBounds(10, 10, 500, 500);
		text.setBorder(new LineBorder(Color.BLACK));
		add(text);

		JButton button = new JButton("Компилировать");
		button.setForeground(Color.BLACK);
		button.setFont(FONT_COURIER_PLAIN_12);
		button.setBounds(1, 525, 200, 30);
		button.setFocusable(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					asm.compileProgram(text.getText());
					asm.loadProgram(cpu);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(gui, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(button);
	}

	@Override
	public void panelActivate() {
		text.requestFocusInWindow();
	}

	@Override
	public void panelDeactivate() { }

	@Override
	public String getPanelName() {
		return "Ассемблер";
	}
}
