/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JComponent;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class ActivateblePanel extends JComponent {
	public abstract void panelActivated();
	public abstract String getPanelName();
	public abstract InputRegisterView getNextInputRegister();
}
