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
import ru.ifmo.cs.bcomp.ui.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataSource;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RegisterView extends JComponent implements DataDestination {
	private int width;
	private int height;
	private int formatWidth;
	private boolean hex;

	private DataSource reg;
	private JLabel title = new JLabel("", JLabel.CENTER);
	private JLabel value = new JLabel("", JLabel.CENTER);

	public RegisterView(DataSource reg, Color colorTitleBG) {
		this.reg = reg;

		title.setFont(FONT_COURIER_BOLD_21);
		title.setBackground(colorTitleBG);
		title.setOpaque(true);
		add(title);

		value.setFont(FONT_COURIER_BOLD_25);
		value.setBackground(COLOR_VALUE);
		value.setOpaque(true);
		add(value);
	}

	public RegisterView(DataSource reg) {
		this(reg, COLOR_TITLE);
	}

	public void setProperties(String title, int x, int y, boolean hex, int regWidth) {
		this.hex = hex;
		this.formatWidth = regWidth;

		width = 2 + FONT_COURIER_BOLD_25_WIDTH * (1 + (hex ? Utils.getHexWidth(regWidth) :
			Utils.getBinaryWidth(regWidth)));
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
			Utils.toHex(reg.getValue(), formatWidth) :
			Utils.toBinary(reg.getValue(), formatWidth));
	}

	@Override
	public void setValue(int value) {
		setValue();
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

	protected void drawBorder(Graphics g, Color color) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(color);
		rs.drawRect(0, 0, width - 1, height - 1);
		rs.drawLine(1, CELL_HEIGHT + 1, width - 2, CELL_HEIGHT + 1);
	}

	@Override
	public void paintComponent(Graphics g) {
		drawBorder(g, Color.BLACK);
	}
}
