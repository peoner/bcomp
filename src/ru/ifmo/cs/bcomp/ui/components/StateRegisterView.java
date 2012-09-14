/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.MouseEvent;
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

	private MouseMotionListener listener = new MouseMotionListener() {
		private String tooltip = null;

		@Override
		public void mouseDragged(MouseEvent e) { }

		@Override
		public void mouseMoved(MouseEvent e) {
			String newtooltip = null;

			switch ((e.getX() - FONT_COURIER_BOLD_25_WIDTH / 2) / FONT_COURIER_BOLD_25_WIDTH) {
				case 0:
					newtooltip = "Ввод-вывод";
					break;

				case 2:
					newtooltip = "Исполнение";
					break;

				case 3:
					newtooltip = "Выбора адреса";
					break;

				case 4:
					newtooltip = "Выбора команды";
					break;

				case 5:
					newtooltip = "Программа";
					break;

				case 7:
					newtooltip = "Работа/останов";
					break;

				case 8:
					newtooltip = "Флаг ВУ";
					break;

				case 9:
					newtooltip = "Запрос прерывания";
					break;

				case 10:
					newtooltip = "Разрешение прерывания";
					break;

				case 12:
					newtooltip = "0";
					break;

				case 13:
					newtooltip = "Знак (N)";
					break;

				case 14:
					newtooltip = "Нуль (Z)";
					break;

				case 15:
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
