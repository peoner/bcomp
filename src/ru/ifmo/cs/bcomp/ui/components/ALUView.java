/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ALUView extends JComponent {
	private int x[];
	private int y[];

	public ALUView(int x, int y, int width, int height) {
		int half = width / 2;
		int offset = height / 3;
		int soffset = offset / 3;

		this.x = new int[] {
			0, half - soffset, half, half + soffset, width - 1, width - 1 - offset, offset
		};
		this.y = new int[] {
			0, 0, offset, 0, 0, height - 1, height - 1
		};

		JLabel title = new JLabel("АЛУ", JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_45);
		title.setBounds(0, offset, width, height - offset);
		add(title);
		
		setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D rs = (Graphics2D) g;

		rs.setPaint(COLOR_TITLE);
        rs.fillPolygon(x, y, x.length);
        //rs.setStroke(new BasicStroke(1.0f));
        rs.setPaint(Color.BLACK);
        rs.drawPolygon(x, y, x.length);

	}
}
