package ru.ifmo.ipm.bevm;

import java.awt.*;

public class DebugWnd extends Frame {
	
	List lstTrace;
	List lstMicroTrace;

	public DebugWnd () {
		super();
		setLayout(null);
		
		add(lstTrace = new List());
		lstTrace.setBounds(0, 0, 500, 350);
		
		add(lstMicroTrace = new List());
		lstMicroTrace.setBounds(0, 355, 500, 350);
		
		this.setSize(510, 710);
	}
}
