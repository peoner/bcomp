package ru.ifmo.ipm.bevm;

import java.awt.*;

class Arrow                 // стрелка
{
public boolean Opened;
private int[][] coordinates;
private int endDirection; // 0=up, 1=down, 2=left, 3=right
private int numOfCoordinates;

public Arrow(int[][] coord)
{
    super();
    Opened=false;
    numOfCoordinates=coord.length;
    coordinates=new int[numOfCoordinates][2];
    for(int i=0; i<numOfCoordinates; i++)
    {
        coordinates[i][0]=coord[i][0];  //x coodinates
        coordinates[i][1]=coord[i][1];  //y coordinates
    }

    if(coordinates[numOfCoordinates-1][0]==coordinates[numOfCoordinates-2][0])
        endDirection=(coordinates[numOfCoordinates-1][1]>=coordinates[numOfCoordinates-2][1])?1:0;
    else
        endDirection=(coordinates[numOfCoordinates-1][0]>=coordinates[numOfCoordinates-2][0])?3:2;
}

public void draw(Graphics g)
{

    int[][][] TriangleRelativeCoords=
    {
       {{-10,0},{10,0},{0,-10}}, //  up
       {{-10,0},{10,0},{0,10}}, // down
       {{-10,0},{0,10},{0,-10}}, // left
       {{10,0},{0,10},{0,-10}} // right
    };
    Polygon p = new Polygon();
    for(int i=0; i<3; i++)
    p.addPoint(coordinates[numOfCoordinates-1][0]+TriangleRelativeCoords[endDirection][i][0],
            coordinates[numOfCoordinates-1][1]+TriangleRelativeCoords[endDirection][i][1]);

    g.setColor(Opened?Color.red:Color.lightGray);
    g.fillPolygon(p);
    for(int i=1; i<numOfCoordinates; i++)
    {
        g.fillRect(
                (coordinates[i][0]<coordinates[i-1][0]?coordinates[i][0]:coordinates[i-1][0])-5,
                (coordinates[i][1]<coordinates[i-1][1]?coordinates[i][1]:coordinates[i-1][1])-5,
                Math.abs(coordinates[i][0]-coordinates[i-1][0])+10,
                Math.abs(coordinates[i][1]-coordinates[i-1][1])+10);
    }
}
}

//##########################################################

class Box extends Panel   // панелька в которую пихаются элементы
{
    public String name;

    public Box(String name)
    {
        super();
        this.setLayout(null);
        this.setBackground(new Color(240, 240, 240));
        this.name=name;
    }

    public void paint(Graphics g)
    {
        int width=getSize().width;
        int height=getSize().height;

        g.setColor(Color.white);          //border
        g.drawLine(0, 0, width, 0);
        g.drawLine(0, 0, 0, height);
        g.setColor(Color.darkGray);
        g.drawLine(width-1, 0, width-1, height);
        g.drawLine(0, height-1, width, height-1);

        g.setColor(Color.black);                //name
        g.setFont(new Font(null, Font.BOLD,  20));
        g.drawString(name, width/2-name.length()*6, 25);
    }
}

//##########################################################

class ALU extends Canvas  // АЛУ для основного вида
{
    public ALU()
    {  super(); }

    public void paint(Graphics g)
    {
    Polygon ALU = new Polygon();
    ALU.addPoint(0, 0);
    ALU.addPoint(75, 0);
    ALU.addPoint(100, 30);
    ALU.addPoint(150, 30);
    ALU.addPoint(175, 0);
    ALU.addPoint(250, 0);
    ALU.addPoint(200, 100);
    ALU.addPoint(50, 100);

    g.setColor(Color.lightGray);
    g.fillPolygon(ALU);
    g.setColor(Color.black);
    g.drawPolygon(ALU);
    g.setFont(new Font(null, Font.BOLD,  35));
    g.drawString("АЛУ", 90, 80);
    }
}

//##########################################################

class YY extends Canvas  // таблица для Устройства Управления Основного вида
{
    private String[] stages={
    "Выборка команды",
    "Выборка Адреса",
    "Исполнение",
    "Прерывание",
    "Ввод-Вывод",
    "Состояние ВУ(Ф)",
    "Разр. прерывания",
    "Программа"
    };
    public int stage;

    public YY()
    {  super(); stage=-1; }

    public void paint(Graphics g)
    {
    g.setColor(Color.black);
    g.drawLine(0, 0, this.getWidth(), 0);
    g.drawLine(0, 1, this.getWidth(), 1);
    for(int i=0; i<stages.length; i++)
    {
    g.setColor(stage==i?Color.red:Color.black);
    g.setFont(new Font(null, Font.BOLD,  22));
    g.drawString(stages[i], 5, i*30+30);
    }
    }
}

//##########################################################

class GUIPanel extends Panel       // основная панель на которую всё выводится
{
public Arrow[] Arrows;

public GUIPanel()
{
    super();
    this.setLayout(null);
    this.setBackground(new Color(0,139,0));
}

public void InitArrows(Arrow[] initArrows)
{
    Arrows=initArrows;
}

public void arrowOpen(int index, boolean opened)
{
    Arrows[index].Opened=opened;
}

public void paint(Graphics g)
{
    int i;                                     //Arrows
    for(i=0;i<Arrows.length;i++)
        if(!Arrows[i].Opened) Arrows[i].draw(g);
    for(i=0;i<Arrows.length;i++)
        if(Arrows[i].Opened) Arrows[i].draw(g);


}
}

