package ru.ifmo.ipm.bevm;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Register extends Panel {
	protected bin val;
	
	Label lblCaption;
	protected TextField txtBin;
   // protected Label txtBin;
	
	Panel content;

	public Register(Container parent, String name, int x, int y, int valLen) {
		this.setLayout(null);
		parent.add(this);
		this.setBackground(Color.gray);
		
		val = new bin(valLen);
		int l = val.toString().length();
		int w1 = 13 * l + l / 4;
		
		l = name.toString().length();
		int w2 = 9 * l;
		int w  = (w2>w1)?(w2):(w1);
		
		Panel pnl;

		add(content = pnl = new Panel());
		pnl.setBounds(1, 1, w+2, 70);
		pnl.setLayout(null);
		pnl.setBackground(Color.white);

        //pnl.add(txtBin= new Label(val.toString()));
		pnl.add(txtBin = new TextField(val.toString()));

        txtBin.setBounds(1+((w2>w1)?((w2-w1)/2):(0)), 16, w1, 50);
		txtBin.setText(Utils.FormatStr(val.toString(), 4));
		txtBin.setEditable(false);
	//	txtBin.setEnabled(false);
        txtBin.setBackground(Color.lightGray);
        txtBin.setFont(new Font(null,0,20));
        txtBin.setForeground(Color.BLUE);
		pnl.add(lblCaption = new Label(name, Label.LEFT));
		lblCaption.setBounds(1, 1, w, 15);
		
		this.setBounds(x, y, w+4, 39);
	}
	
	public Register(Container parent, String name, String bottomText, int x, int y, int valLen) {
		this(parent, name, x, y, valLen);
	
		Label lbl;
		content.add(lbl = new Label(bottomText, Label.LEFT));
		lbl.setBounds(4, 37, this.getWidth()-2, 15);
		
		this.setSize(this.getWidth(), this.getHeight() + 15);
		content.setSize(content.getWidth(), content.getHeight() + 15);
	}
	
	public bin getValue() {
		return val.clone();
	}
	
	public void setValue(bin value) {
		if(value.arr.length==val.arr.length)
			val = value;
		else 
			val = new bin(val.arr.length, Utils.AlignStr(value.toString(), val.arr.length, '0'));
		updateText();
	}
	
	public void updateText() {
		txtBin.setText(Utils.FormatStr(val.toString(), 4));
	}
	
	public void addKeyListener(KeyListener l) {
		super.addKeyListener(l);
		txtBin.addKeyListener(l);
		lblCaption.addKeyListener(l);
	}
	
	public void requestFocus() {
		txtBin.requestFocus();
	}
	
	public void setBit(int n, char val) {
		this.val.setBit(n, val);
		updateText();
	}
}
