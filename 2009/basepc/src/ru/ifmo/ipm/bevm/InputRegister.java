package ru.ifmo.ipm.bevm;

import java.awt.*;
import java.awt.event.*;

public class InputRegister extends Register {
	
	int curr;
	KeyListener keyListener;
	
	public InputRegister(Container parent, String name, int x, int y, int valLen) {
		super(parent, name, x, y, valLen);
		
		txtBin.setEnabled(true);
		curr = 0;
		
		txtBin.addKeyListener(keyListener = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				fireKeyPressed(e);
			}
			
			public void keyTyped(KeyEvent e) { }
			public void keyReleased(KeyEvent e) { }
		});
	}
	
	private void updateSelection() {
		int n = curr + curr/4;
		txtBin.setSelectionStart(n);
		txtBin.setSelectionEnd(n+1);
	}
	
	public void fireKeyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: { // left
			curr--;
			if(curr<0)curr=val.arr.length-1;
			break;
		}
		case KeyEvent.VK_UP:{ // up
			val.arr[curr] = '1';
			break;
		}
		case KeyEvent.VK_RIGHT: { // right
			curr++;
			if(curr>=val.arr.length)curr=0;
			break;
		}
		case KeyEvent.VK_DOWN: { // down
			val.arr[curr] = '0';
			break;
		}
		case KeyEvent.VK_SPACE: {
			val.arr[curr] = (val.arr[curr] == '0')?('1'):('0');
			break;
		}
		case KeyEvent.VK_2:
		case KeyEvent.VK_0: { 
			val.arr[curr] = '0';
			curr++;
			if(curr>=val.arr.length)curr=0;
			break;
		}
		case KeyEvent.VK_1: {
			val.arr[curr] = '1';
			curr++;
			if(curr>=val.arr.length)curr=0;
			break;
		}
		default : {
			//txtBin.setText(Integer.toString(e.getKeyCode()));
			return;
		}
		}
		txtBin.requestFocus();
		e.consume();
		updateText();
		updateSelection();
	}
	
	public void addKeyListener(KeyListener l) {
		for(KeyListener ll : txtBin.getKeyListeners()) {
			if(ll.equals(l))return;
		}
		txtBin.addKeyListener(l);
	}
}