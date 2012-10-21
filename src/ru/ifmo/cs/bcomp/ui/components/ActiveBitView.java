/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataSource;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ActiveBitView extends BCompComponent {
	private final JLabel value = addValueLabel();

	public ActiveBitView(int x, int y) {
		super("Бит", COLOR_INPUT_TITLE);

		setBounds(x, y, getValueWidth(8, true));
		setTitleBounds();
		value.setBounds(1, getValueY(), width - 2, CELL_HEIGHT);
	}

	public void setValue(int value) {
		this.value.setText(Utils.toHex(value, 1));
	}
}
