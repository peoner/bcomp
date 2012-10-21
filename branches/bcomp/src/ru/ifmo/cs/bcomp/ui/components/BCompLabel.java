/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_TITLE;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.FONT_COURIER_BOLD_21;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BCompLabel extends BorderedComponent {
	public BCompLabel(String text, int x, int y, int width, int height) {
		super(height);

		this.width = width;
		setBounds(x, y, width, height);

		JLabel title = addLabel(text, FONT_COURIER_BOLD_21, COLOR_TITLE);
		title.setBounds(1, 1, width - 2, height - 2);
	}
}
