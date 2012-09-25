/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import ru.ifmo.cs.elements.DataSource;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class StateRegisterView extends RegisterView {
	public StateRegisterView(DataSource reg) {
		super(reg);
	}

	private MouseMotionAdapter listener = new MouseMotionAdapter() {
		private String tooltip = null;

		@Override
		public void mouseMoved(MouseEvent e) {
			String newtooltip = null;

			switch ((e.getX() - FONT_COURIER_BOLD_25_WIDTH / 2) / FONT_COURIER_BOLD_25_WIDTH) {
				case 0:
					newtooltip = "Программа";
					break;

				case 2:
					newtooltip = "Работа/останов";
					break;

				case 3:
					newtooltip = "Флаг ВУ";
					break;

				case 4:
					newtooltip = "Запрос прерывания";
					break;

				case 5:
					newtooltip = "Разрешение прерывания";
					break;

				case 7:
					newtooltip = "0";
					break;

				case 8:
					newtooltip = "Знак (N)";
					break;

				case 9:
					newtooltip = "Нуль (Z)";
					break;

				case 10:
					newtooltip = "Перенос (C)";
					break;
			}

			if (newtooltip != null)
				if (newtooltip != tooltip)
					setValueToolTip(tooltip = newtooltip);
		}
	};

	@Override
	public void setProperties(String title, int x, int y, boolean fullView) {
		super.setProperties(title, x, y, false, fullView ? getRegWidth() : 1);

		if (fullView)
			addValueMotionListener(listener);
		else {
			cleanValueMotionListeners();
			setValueToolTip(null);
		}
	}
}
