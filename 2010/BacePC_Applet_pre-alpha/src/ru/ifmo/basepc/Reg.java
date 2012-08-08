package ru.ifmo.basepc;

import java.awt.*;
import java.awt.event.*;

public class Reg extends Canvas       // регистр
{
    public String text;

public Reg(String text)
{
    super();
    this.setBackground(new Color(240, 240, 240));
    this.text=text;
    this.setFocusable(false);
}

public void paint(Graphics g)
{
    int width=getSize().width;
    int height=getSize().height;

    g.setColor(new Color(3,	168,158));    //inner panel
    g.fillRect(0, 0, width, height);

    g.setColor(Color.BLUE);              //text
   
    g.setFont(new Font(null, Font.PLAIN,  25));
    String tetrad="";
    
    if(text.length()%4!=0)
    {
    tetrad=text.substring(0,text.length()%4);
    g.setColor(Color.BLUE);
    g.drawString(tetrad, 5, height-5);
    }
    
    for(int i=0; 4*i<text.length()-1; i++)
    {
    tetrad=text.substring(4*i+text.length()%4, 4*i+4+text.length()%4);
    g.setColor(i%2==1?Color.BLUE:new Color(0,0,120));
    g.drawString(tetrad,(text.length()%4)*18+5+i*65, height-5);
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
    this.setFocusable(false);
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

class RegHex extends Canvas         //16-тиричный регистр без рамки
{
public String text;
public RegHex(String text)
{
    super();
    this.setBackground(new Color(240, 240, 240));
    this.text=text;
    this.setFocusable(false);
}

public void paint(Graphics g)
{
    g.setFont(new Font(null, Font.BOLD,  25));
    g.setColor(new Color(0,0,120));
    g.drawString(text, 10, getSize().height-5);
}
}

class RegInput extends Canvas implements FocusListener, KeyListener     // регистр ввода
{
    public StringBuffer text;
    public boolean active;
    public int position;

public RegInput(String text)
{
    super();
    this.setBackground(new Color(240, 240, 240));
    this.text=new StringBuffer(text);
    position=0;
    active=false;
    this.addFocusListener(this);
    this.addKeyListener(this);
}

public void paint(Graphics g)
{
    int width=getSize().width;
    int height=getSize().height;

    if(active)  //focus border
    {
    g.setColor(Color.red);
    g.drawRect(0,0,width-30, height-2);
    g.drawRect(1,1,width-30, height-2);
    }

    g.setColor(new Color(3,	168,158));    //inner panel
    g.fillRect(4, 4, width-35, height-8);

    g.setFont(new Font(null, Font.PLAIN,  23)); //position
    g.setColor(Color.black);
    g.drawString(Integer.toString(text.length()-position-1), width-25, height-8 );

    g.setFont(new Font(null, Font.PLAIN,  25)); //text
    String tetrad="";
    for(int i=0; 4*i<text.length()-1; i++)
    {
    tetrad=text.substring(4*i, 4*i+4);
    g.setColor(i%2==1?Color.BLUE:new Color(0,0,120));
    g.drawString(tetrad,7+i*65, height-8);
    }

    g.setColor(Color.red);
    g.drawString(text.substring(position,position+1),7+(position/4)*65+(position%4)*14, height-8);
}

    public void focusGained(FocusEvent e) {active=true; repaint();}
    public void focusLost(FocusEvent e) {active=false; repaint();}

    void replaceSymbol(String input, int index, char symbol)
    {
        char[] res=input.toCharArray();
        res[index]=symbol;
        input = String.copyValueOf(res);
    }

    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:{
            position--;
            if(position<0)position=text.length()-1;
            repaint();
            break;
        }
		case KeyEvent.VK_RIGHT: {
            position++;
            if(position>=text.length())position=0;
            repaint();
            break;
		}
        case KeyEvent.VK_UP:{
            text.setCharAt(position,text.charAt(position)=='1'?'0':'1');
            repaint();
            break;
		}
		case KeyEvent.VK_DOWN: {
            text.setCharAt(position,text.charAt(position)=='1'?'0':'1');
            repaint();
            break;
        }
		case KeyEvent.VK_0: {
            text.setCharAt(position,'0');
            position++;
            if(position>=text.length())position=0;
            repaint();
            break;
		}
		case KeyEvent.VK_1: {
            text.setCharAt(position,'1');
            position++;
            if(position>=text.length())position=0;
            repaint();
            break;
		}
        case KeyEvent.VK_NUMPAD0: {
            text.setCharAt(position,'0');
            position++;
            if(position>=text.length())position=0;
            repaint();
            break;
		}
		case KeyEvent.VK_NUMPAD1: {
            text.setCharAt(position,'1');
            position++;
            if(position>=text.length())position=0;
            repaint();
            break;
		}
		}
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}

class Flag extends Canvas implements FocusListener // флаг ВУ
{
    public boolean active;

public Flag()
{
    super();
    active=false;
    //this.addMouseListener(this);
    this.addFocusListener(this);
}

public void paint(Graphics g)
{
    int width=getSize().width;
    int height=getSize().height;

    g.setColor(Color.black);
    g.drawArc(0, 0, width, height, 0, 360);
    g.drawArc(1, 1, width-2, height-2, 0, 360);
    g.setColor(active?Color.red:Color.white);
    g.fillArc(2, 2, width-3, height-3, 0, 360);
}

public void 	focusGained(FocusEvent e)
{   active=true;    
    repaint();
}
public void 	focusLost(FocusEvent e) {}
}


