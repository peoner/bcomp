package ru.ifmo.ipm.bevm;

import java.awt.*;
import java.awt.event.*;

public class LogicGate extends Container { // Container
	
	public final class TextAlign {
		public static final int Top = 1;
		public static final int Bottom = 2;
		public static final int Left = 3;
		public static final int Right = 4;
		public static final int TopRight = 5; 
		public static final int TopLeft = 6;
		public static final int BottomRight = 7;
		public static final int BottomLeft = 8;
	}
	
	public Label lblName;
	
	public String  toolTipText;
	
	public Image imgActive;
	public Image imgInactive;
	boolean enabled;
	
	public void setEnabled(boolean val) {
		enabled = val;
		this.repaint();
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void paint(Graphics g) {
		if (enabled) 
			g.drawImage(imgActive, 17, 7, this);
		else 
			g.drawImage(imgInactive, 17, 7, this);
	}
	
	public LogicGate(Container parent, int x, int y, String name, String toolTipText, int nameAlign) {
		parent.add(this);
		this.setBounds(x, y, 70, 50);
		this.toolTipText = toolTipText;
		//this.setBackground(parent.getBackground());
		this.setLayout(null);
		parent.setComponentZOrder(this, 1);
		
		switch(nameAlign) {
		case TextAlign.Bottom:		lblName = new Label(name, Label.CENTER);lblName.setBounds(15, 40, 30, 10);break;
		case TextAlign.BottomLeft:	lblName = new Label(name, Label.LEFT);	lblName.setBounds(10, 40, 30, 10);break;
		case TextAlign.BottomRight:	lblName = new Label(name, Label.LEFT);	lblName.setBounds(40, 35, 30, 10);break;
		case TextAlign.Top:			lblName = new Label(name, Label.CENTER);lblName.setBounds(20, 0, 30, 10); break;
		case TextAlign.TopLeft:		lblName = new Label(name, Label.LEFT);	lblName.setBounds(3, 5, 23, 10); break;
		case TextAlign.TopRight:	lblName = new Label(name, Label.LEFT);	lblName.setBounds(40, 0, 30, 10); break;
		case TextAlign.Left:		lblName = new Label(name, Label.LEFT);	lblName.setBounds(0, 20, 23, 10);break;
		case TextAlign.Right:		lblName = new Label(name, Label.LEFT);	lblName.setBounds(47, 20, 30, 10);break;
		}
		add(lblName);
		
		
		enabled = false;
	}
	
	public void setBackground(Color c) {
		super.setBackground(c);
		lblName.setBackground(c);
	}
}
