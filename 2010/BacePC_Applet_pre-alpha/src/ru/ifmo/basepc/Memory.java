package ru.ifmo.basepc;

import java.awt.*;

public class Memory extends Panel
{
    public String name;
    public String[] adress;
    public String[] values;

public Memory(String name)
{
    super();
    this.setLayout(null);
    this.setBackground(new Color(240, 240, 240));
    this.name=name;
    adress=new String[16];
    values=new String[16];
    for(int i=0;i<16;i++)
    {adress[i]="00";values[i]="0000";}
        
}
    
public Memory(String name, String[] adress, String[] values)
{
    super();
    this.setLayout(null);
    this.setBackground(new Color(240, 240, 240));
    this.name=name;
    this.adress=adress;
    this.values=values;
    this.setFocusable(false);
}

public void paint(Graphics g)
{
    int width=getSize().width;
    int height=getSize().height;

    g.setColor(Color.black);                //name
    g.setFont(new Font(null, Font.BOLD,  20));
    g.drawString(name, width/2-name.length()*6, 25);

    g.setFont(new Font(Font.MONOSPACED, Font.BOLD,  22));

    g.setColor(new Color(192, 220, 192));
    g.fillRect(50, 30, width, 480);


    for(int i=0;i<16;i++)
    {
    g.setColor(Color.black);
    g.drawRect(1, i*30+30, 50, 30);
    g.drawRect(50, i*30+30, width-51, 30);
    g.setColor(new Color(0,0,120));
    g.drawString(adress[i], 5, i*30+53);
    g.drawString(values[i], 55, i*30+53);
    }
    
    }
}

