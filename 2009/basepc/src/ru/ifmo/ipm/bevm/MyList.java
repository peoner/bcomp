package ru.ifmo.ipm.bevm;

import java.awt.*;
import java.awt.event.*;

public class MyList extends Panel {
	private Label[] arr;
	private Panel pnlContent;

	public MyList (int len) {
		setLayout(null);
		setBackground(Color.gray);
		
		add(pnlContent = new Panel());
		pnlContent.setLayout(null);
		pnlContent.setBackground(Color.white);
		pnlContent.setBounds(1, 1, this.getWidth()-2, this.getHeight()-2);
		
		arr = new Label[len];
		
		for(int i = 0; i < len; i++ ){
			pnlContent.add(arr[i] = new Label("", Label.LEFT));
			arr[i].setBounds(0, i*15, this.getWidth()-2, 15);
		}
		
		this.addComponentListener(new ComponentListener(){
			public void componentResized(ComponentEvent arg0) {
				pnlContent.setSize(arg0.getComponent().getWidth()-2, arg0.getComponent().getHeight()-2);
				for(Label lbl : arr) {
					lbl.setSize(arg0.getComponent().getWidth()-2, 15);
				}
			}
			public void componentMoved(ComponentEvent arg0) {}
			public void componentShown(ComponentEvent arg0) {}
			public void componentHidden(ComponentEvent arg0) {}
		});
	}
	
	public void setItem(int n, String s) {
		arr[n].setText(s);
	}
}
