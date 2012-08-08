package ru.ifmo.ipm.bevm;

import java.awt.*;

public class Picture extends Component{
	Image img;
	
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	public Picture(Container parent, Image img, int x, int y, int w, int h) {
		//parent.add(this);
		this.img = img;
		this.setBounds(x, y, w, h);
	}

}
