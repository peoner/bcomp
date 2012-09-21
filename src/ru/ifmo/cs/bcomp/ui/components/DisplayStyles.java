/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class DisplayStyles {
	private static final FontRenderContext fr = new FontRenderContext(null, true, true);

	// Fonts
	public static final Font FONT_COURIER_PLAIN_12 = new Font("Courier New", Font.PLAIN, 12);
	public static final Font FONT_COURIER_PLAIN_16 = new Font("Courier New", Font.PLAIN, 16);
	public static final Font FONT_COURIER_BOLD_20 = new Font("Courier New", Font.BOLD, 20);
	public static final Font FONT_COURIER_BOLD_21 = new Font("Courier New", Font.BOLD, 21);
	public static final int FONT_COURIER_BOLD_21_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_21.getStringBounds("0", fr).getWidth());
	public static final Font FONT_COURIER_BOLD_23 = new Font("Courier New", Font.BOLD, 23);
	public static final int FONT_COURIER_BOLD_23_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_23.getStringBounds("0", fr).getWidth());
	public static final Font FONT_COURIER_BOLD_25 = new Font("Courier New", Font.BOLD, 25);
	public static final int FONT_COURIER_BOLD_25_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_25.getStringBounds("0", fr).getWidth());

	// Colors
	public static final Color COLOR_TITLE = new Color(157, 189, 165);
	public static final Color COLOR_VALUE = new Color(219, 249, 235);
	public static final Color COLOR_INPUT_TITLE = new Color(157 + 50, 189 + 50, 165 + 50);

	// Coordinates and dimentions
	// Memory cell height
	public static final int CELL_HEIGHT = 25;
	// Frame dimentions
	public static final int FRAME_WIDTH = 852;
	public static final int FRAME_HEIGHT= 586;
	// Buttons coordinates
	public static final int BUTTONS_HEIGHT = 30;
	public static final int BUTTONS_SPACE = 2;
	public static final int BUTTONS_Y = 529;
	// Keyboards register
	public static final int REG_KEY_X = 1;
	public static final int REG_KEY_Y = 470;
}
