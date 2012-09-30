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
	public static final Font FONT_COURIER_BOLD_45 = new Font("Courier New", Font.BOLD, 45);

	// Colors
	public static final Color COLOR_TITLE = new Color(157, 189, 165);
	public static final Color COLOR_VALUE = new Color(219, 249, 235);
	public static final Color COLOR_INPUT_TITLE = new Color(157 + 50, 189 + 50, 165 + 50);
	public static final Color COLOR_ACTIVE_INPUT = new Color(192, 0, 0);
	public static final String COLOR_ACTIVE_BIT = "<font color=\"#FF0000\">";

	// Coordinates and dimentions
	// Bus width
	public static final int BUS_WIDTH = 4;
	private static final int ELEMENT_DELIM = 4 * BUS_WIDTH;
	private static final int ARROW = BUS_WIDTH * 3 + 1;
	// Memory cell height
	public static final int CELL_HEIGHT = 25;
	private static final int REG_HEIGHT = 2 * CELL_HEIGHT + 3;
	// Frame dimentions
	public static final int FRAME_WIDTH = 881;
	public static final int FRAME_HEIGHT= 571;
	// JComponents dimentions
	private static final int COMPONENT_HEIGHT = FRAME_HEIGHT - 27;
	// Buttons coordinates
	public static final int BUTTONS_HEIGHT = 30;
	public static final int BUTTONS_SPACE = 2;
	public static final int BUTTONS_Y = COMPONENT_HEIGHT - BUTTONS_HEIGHT;
	// Keyboards register
	public static final int REG_KEY_X = 8;
	public static final int REG_KEY_Y = BUTTONS_Y - REG_HEIGHT - ELEMENT_DELIM;

	// Basic view
	private static final int REG_16_HALF = 151;
	private static final int REG_HEIGHT_HALF = CELL_HEIGHT + 1;
	// ALU: Left input buses
	public static final int BUS_LEFT_INPUT_X1 = REG_KEY_X + REG_16_HALF;
	public static final int BUS_KEY_ALU = REG_KEY_Y - BUS_WIDTH - 1;
	// Accum
	public static final int REG_C_X_BV = BUS_LEFT_INPUT_X1 + 5 * BUS_WIDTH + 1;
	public static final int REG_ACCUM_X_BV = REG_C_X_BV + 31;
	public static final int REG_ACCUM_Y_BV = BUS_KEY_ALU - ELEMENT_DELIM - REG_HEIGHT + 5;
	// From ALU
	public static final int FROM_ALU_X = REG_ACCUM_X_BV + 61;
	public static final int TO_ACCUM_Y = REG_ACCUM_Y_BV - 3 * BUS_WIDTH - 2;
	public static final int FROM_ALU_Y1 = TO_ACCUM_Y - 2 * BUS_WIDTH + 1;
	public static final int FROM_ALU_Y = FROM_ALU_Y1 - 4 * BUS_WIDTH;
	// ALU
	public static final int ALU_WIDTH = 181;
	public static final int ALU_HEIGHT = 90;
	public static final int ALU_Y = FROM_ALU_Y - ALU_HEIGHT - BUS_WIDTH;
	public static final int BUS_LEFT_INPUT_X = BUS_LEFT_INPUT_X1 + ALU_WIDTH / 3 + 1;
	public static final int BUS_LEFT_INPUT_DOWN = ALU_Y - ARROW - 1;
	public static final int BUS_LEFT_INPUT_UP = BUS_LEFT_INPUT_DOWN - 2 * BUS_WIDTH + 1;
	public static final int BUS_FROM_ACCUM_X = REG_C_X_BV - BUS_WIDTH - 1;
	public static final int BUS_FROM_ACCUM_Y = REG_ACCUM_Y_BV + REG_HEIGHT_HALF;
	public static final int REG_IP_X_BV = REG_ACCUM_X_BV + 6 * FONT_COURIER_BOLD_25_WIDTH;
	public static final int REG_IP_Y_BV = BUS_LEFT_INPUT_UP - ELEMENT_DELIM - REG_HEIGHT;
	public static final int BUS_FROM_IP_X = REG_IP_X_BV - BUS_WIDTH - 1;
	public static final int BUS_FROM_IP_Y = REG_IP_Y_BV + REG_HEIGHT_HALF;
	public static final int BUS_RIGHT_X1 = BUS_FROM_IP_X - ELEMENT_DELIM;
	public static final int BUS_RIGHT_X = REG_C_X_BV + ALU_WIDTH - ALU_WIDTH / 4 + 2;
	public static final int REG_DATA_Y_BV = REG_IP_Y_BV - ELEMENT_DELIM - REG_HEIGHT;
	public static final int BUS_FROM_DATA_Y = REG_IP_Y_BV - ELEMENT_DELIM + BUS_WIDTH;

	public static final int TEXTAREA_X = 1;
	public static final int TEXTAREA_Y = 1;
	public static final int TEXTAREA_WIDTH = 600;
	public static final int TEXTAREA_HEIGHT = COMPONENT_HEIGHT - 2;
}
