/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BorderedComponent extends JComponent {
	protected int width;
	protected final int height;

	protected BorderedComponent(int height) {
		this.height = height;
	}

	protected final JLabel addLabel(String value, Font font, Color color) {
		JLabel label = new JLabel(value, JLabel.CENTER);
		label.setFont(font);
		label.setBackground(color);
		label.setOpaque(true);
		add(label);
		return label;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
	}
}
