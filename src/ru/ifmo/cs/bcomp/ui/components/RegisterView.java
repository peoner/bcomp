/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataSource;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class RegisterView extends JComponent {
	private static final int separator = 4;
	private int width;
	private int height;
	private int valueWidth;
	private boolean hex;

	private DataSource reg;
	private JLabel title = new JLabel("", JLabel.CENTER);
	private JLabel value = new JLabel("", JLabel.CENTER);
	private JLabel comment = null;

	public RegisterView(DataSource reg) {
		this.reg = reg;

		title.setFont(FONT_COURIER_BOLD_20);
		add(title);

		value.setFont(FONT_COURIER_BOLD_21);
		value.setBackground(COLOR_REG_VALUE);
		value.setOpaque(true);
		add(value);
	}

	public RegisterView(DataSource reg, String comment) {
		this(reg);

		this.comment = new JLabel(comment, JLabel.CENTER);
		add(this.comment);
	}

	public void setProperties(String title, int x, int y, boolean hex, int regWidth) {
		this.hex = hex;

		valueWidth = hex ?
			ComponentManager.getHexWidth(regWidth) :
			ComponentManager.getBinWidth(regWidth);
		int valueComponentWidth = FONT_COURIER_BOLD_21_WIDTH * (1 + valueWidth);
		width = 2 + 2 * separator +	valueComponentWidth;
		height = 2 + 20 + 21 + separator + (comment == null ? separator : 21);
		setBounds(x, y, width, height);

		this.title.setText(title);
		this.title.setBounds(1, 1, width - 2, 20);

		value.setText(hex ?
			ComponentManager.toHex(reg.getValue(), valueWidth) :
			ComponentManager.toBin(reg.getValue(), valueWidth));
		value.setBounds(separator + 1, 1 + 20 + separator, valueComponentWidth, 21);

		if (comment != null)
			comment.setBounds(1, 20 + 21, width - 2, 21);
	}

	public void setProperties(String title, int x, int y, boolean hex) {
		setProperties(title, x, y, hex, reg.getWidth());
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(Color.BLACK);
		rs.drawRect(0, 0, width - 1, height - 1);

		rs.setPaint(COLOR_REG_TITLE);
		rs.fillRect(1, 1, width - 2, height - 2);
	}
}
