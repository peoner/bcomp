package ru.ifmo.ipm.bevm;

import java.awt.*;

class Reg extends Canvas       // регистр
{
    public String text;

public Reg(String text)
{
    super();
    this.setBackground(new Color(240, 240, 240));
    this.text=text;
}

public void paint(Graphics g)
{
    int width=getSize().width;
    int height=getSize().height;

    g.setColor(new Color(3,	168,158));    //inner panel
    g.fillRect(0, 0, width, height);

    g.setColor(Color.BLUE);              //text
   
    g.setFont(new Font(null, Font.PLAIN,  25));
    String tetrad;
    for(int i=0; 4*i<text.length(); i++)
    {
    tetrad=text.substring(4*i, 4*i+4);
    g.setColor(i%2==1?Color.BLUE:new Color(0,0,120));
    g.drawString(tetrad, 5+i*65, height-5);
    }
}
}

class RegC extends Canvas       // регистр переноса
{
   public String text;

public RegC(String text)
{
    super();
    this.text=text;
}

public void paint(Graphics g)
{
    int width=getSize().width;
    int height=getSize().height;

    g.setColor(Color.black);                //name
    g.setFont(new Font(null, Font.BOLD,  20));
    g.drawString("С", 10, 25);

    g.setColor(Color.white);    //inner panel
    g.fillRect(5, 30, width-10, height-5);

    g.setColor(Color.black);              //text

    g.setFont(new Font(null, Font.PLAIN,  25));
    g.drawString(text, 10, height-5);
}
}

