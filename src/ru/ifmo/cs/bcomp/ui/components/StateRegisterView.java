/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import ru.ifmo.cs.bcomp.ui.Utils;
import ru.ifmo.cs.elements.DataSource;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class StateRegisterView extends RegisterView {
	private final int formattedWidth;
	private static final String[] tooltips = {
		"Перенос (C)",
		"Нуль (Z)",
		"Знак (N)",
		"0",
		"Разрешение прерывания",
		"Запрос прерывания",
		"Флаг ВУ",
		"Работа/останов",
		"Программа"
	};
	private MouseMotionAdapter listener = new MouseMotionAdapter() {
		private String tooltip = null;

		@Override
		public void mouseMoved(MouseEvent e) {
			int bitno = Utils.getBitNo(
				(e.getX() - FONT_COURIER_BOLD_25_WIDTH / 2) / FONT_COURIER_BOLD_25_WIDTH,
				formattedWidth);

			if (bitno < 0) {
				value.setToolTipText(tooltip = null);
				return;
			}

			String newtooltip = tooltips[bitno];
			if (newtooltip != tooltip)
				value.setToolTipText(tooltip = newtooltip);
		}
	};

	public StateRegisterView(DataSource reg) {
		super(reg);

		formattedWidth = Utils.getBinaryWidth(reg.getWidth());
	}

	@Override
	public void setProperties(String title, int x, int y, boolean fullView) {
		super.setProperties(title, x, y, false, fullView ? getRegWidth() : 1);

		if (fullView) {
			value.addMouseMotionListener(listener);
		} else {
			value.removeMouseMotionListener(listener);
			value.setToolTipText(null);
		}
	}
}
