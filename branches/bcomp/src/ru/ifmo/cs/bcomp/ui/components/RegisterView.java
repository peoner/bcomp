/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataSource;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RegisterView extends JComponent {
	private int width;
	private int height;
	private int formatWidth;
	private boolean hex;

	private DataSource reg;
	private JLabel title = new JLabel("", JLabel.CENTER);
	private JLabel value = new JLabel("", JLabel.CENTER);

	public RegisterView(DataSource reg) {
		this.reg = reg;

		title.setFont(FONT_COURIER_BOLD_21);
		title.setBackground(COLOR_MEM_TITLE);
		title.setOpaque(true);
		add(title);

		value.setFont(FONT_COURIER_BOLD_25);
		value.setBackground(COLOR_MEM_VALUE);
		value.setOpaque(true);
		add(value);
	}

	public void setProperties(String title, int x, int y, boolean hex, int regWidth) {
		this.hex = hex;
		this.formatWidth = hex ? ComponentManager.getHexWidth(regWidth) : regWidth;

		width = 2 + FONT_COURIER_BOLD_25_WIDTH * (1 + (hex ? this.formatWidth :
			ComponentManager.getBinWidth(regWidth)));
		height = 3 + 2 * CELL_HEIGHT;
		setBounds(x, y, width, height);

		this.title.setText(title);
		this.title.setBounds(1, 1, width - 2, CELL_HEIGHT);

		setValue();
		value.setBounds(1, 2 + CELL_HEIGHT, width - 2, CELL_HEIGHT);
	}

	public void setProperties(String title, int x, int y, boolean hex) {
		setProperties(title, x, y, hex, reg.getWidth());
	}

	protected int getRegWidth() {
		return reg.getWidth();
	}

	protected void setValue(String val) {
		value.setText(val);
	}

	public void setValue() {
		setValue(hex ?
			ComponentManager.toHex(reg.getValue(), formatWidth) :
			ComponentManager.toBin(reg.getValue(), formatWidth));
	}

	protected void setValueToolTip(String text) {
		value.setToolTipText(text);
	}

	protected void cleanValueMotionListeners() {
		for (MouseMotionListener l : value.getMouseMotionListeners()) {
			value.removeMouseMotionListener(l);
		}
	}

	protected void addValueMotionListener(MouseMotionListener l) {
		value.addMouseMotionListener(l);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(Color.BLACK);
		rs.drawRect(0, 0, width - 1, height - 1);
		rs.drawLine(1, CELL_HEIGHT + 1, width - 2, CELL_HEIGHT + 1);
	}
}
