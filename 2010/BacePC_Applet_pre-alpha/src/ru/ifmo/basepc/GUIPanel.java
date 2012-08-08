package ru.ifmo.basepc;

import java.awt.*;
import java.awt.event.*;

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
        this.setFocusable(false);
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
        g.drawString(name, width/2-name.length()*5, 25);
    }
}

//##########################################################

class ALU extends Canvas  // АЛУ для основного вида
{
    public ALU()
    {  super(); this.setFocusable(false); }

    public void paint(Graphics g)
    {
    Polygon ALU = new Polygon();
    ALU.addPoint(0, 0);
    ALU.addPoint(75, 0);
    ALU.addPoint(90, 30);
    ALU.addPoint(105, 0);
    ALU.addPoint(180, 0);
    ALU.addPoint(130, 100);
    ALU.addPoint(50, 100);

    g.setColor(Color.lightGray);
    g.fillPolygon(ALU);
    g.setColor(Color.black);
    g.drawPolygon(ALU);
    g.setFont(new Font(null, Font.BOLD,  35));
    g.drawString("АЛУ", 60, 80);
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
    {  super(); stage=-1; this.setFocusable(false); }

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

//######################################################

class VYDecoder extends Box   // панелька в которую пихаются элементы
{
    public VYDecoder()
    {
        super("");
        this.setLayout(null);
        this.setSize(180, 100);
        Font textFont=new Font(null,Font.PLAIN, 22);

         Label decoderText1=new Label("   Дешифратор");
         Label decoderText2=new Label("      адреса и");
         Label decoderText3=new Label("      приказов");
         decoderText1.setFont(textFont);
         decoderText2.setFont(textFont);
         decoderText3.setFont(textFont);
         decoderText1.setBounds(5, 5, this.getWidth()-10, 25);
         decoderText2.setBounds(5, 35, this.getWidth()-10, 25);
         decoderText3.setBounds(5, 65, this.getWidth()-10, 25);
         this.add(decoderText1);
         this.add(decoderText2);
         this.add(decoderText3);
         this.setFocusable(false);
    }
}


//###########################################################

class WorkingLabel extends Label   // тумблер работа/остановка
{
    public WorkingLabel()
    {
        super();
        this.setFont(new Font(null,Font.PLAIN, 22));
        setWorking(false);
        this.setFocusable(false);
    }

    public void setWorking(boolean isworking)
    {
        if(isworking)
        {
        this.setText("Работа");
        this.setForeground(Color.red);
        }
        else
        {
        this.setText("Останов.");
        this.setForeground(Color.black);
        }
    }
}

//##########################################################

class GUIPanel extends Panel       // основная панель на которую всё выводится
{
public Arrow[] Arrows;

public GUIPanel(Color background)
{
    super();
    this.setLayout(null);
    this.setBackground(background);
}

public void InitArrows(Arrow[] initArrows)
{
    Arrows=initArrows;
}

public void arrowOpen(int index, boolean opened)
{
    Arrows[index].Opened=opened;
}

public void arrowsOpen(int[] toOpen)
{
    for(int i=0;i<Arrows.length;i++)
    Arrows[i].Opened=false;

    for(int i=0;i<toOpen.length;i++)
    Arrows[toOpen[i]].Opened=true;
}

public void paint(Graphics g)
{
    int i;                                     //
    if(Arrows!=null)
    {
    for(i=0;i<Arrows.length;i++)
        if(!Arrows[i].Opened) Arrows[i].draw(g);
    for(i=0;i<Arrows.length;i++)
        if(Arrows[i].Opened) Arrows[i].draw(g);
    }
}
}

class ViewPanel extends Panel       // основная панель на которую всё выводится
{
GUIPanel baseView;
GUIPanel VYView;
GUIPanel MPView;

Reg baseRD;  //регистры главного вида
Reg baseRK;
Reg baseRA;
Reg baseSK;
Reg baseA;
RegC baseC;
YY baseYY;

RegHex mpRA;  //регистры вида МП
RegHex mpRD;
RegHex mpRK;
RegHex mpSK;
RegHex mpBR;
Reg mpRS;
RegHex mpA;
RegC mpBRC;
RegC mpAC;
Reg mpRMK;
Reg mpMKC;


RegInput vyRDVY1; // регистры вида ВУ
RegInput vyRDVY2;
RegInput vyRDVY3;
RegHex vyRA;  //регистры вида МП
RegHex vyRD;
RegHex vyRK;
RegHex vySK;
RegHex vyA;
RegC vyAC;
Flag vyFlag1;
Flag vyFlag2;
Flag vyFlag3;
Memory MPmemo;

public ViewPanel()
{
    super();
    this.setLayout(new java.awt.CardLayout());
    initBaseView();
    initVYView();
    initMPView();
    this.add(baseView, "BaseView");
    this.add(VYView, "VYView");
    this.add(MPView, "MPView");
    SetBaseView();
}

public void initBaseView()
{
        baseView = new GUIPanel(new Color(0,128,0));

        Box RD=new Box("Регистр Данных");
        RD.setBounds(30, 90, 280, 70);
        baseRD=new Reg("0000000000000000");
        baseRD.setBounds(5, 30, RD.getWidth()-10, RD.getHeight()-35);
        RD.add(baseRD);

        Box RK=new Box("Регистр Команд");
        RK.setBounds(480, 110, 280, 70);
        baseRK=new Reg("0000000000000000");
        baseRK.setBounds(5, 30, RK.getWidth()-10, RK.getHeight()-35);
        RK.add(baseRK);

        Box RA=new Box("Регистр Адреса");
        RA.setBounds(200, 10, 210, 70);
        baseRA=new Reg("000000000000");
        baseRA.setBounds(5, 30, RA.getWidth()-10, RA.getHeight()-35);
        RA.add(baseRA);

        Box SK=new Box("Счётчик Команд");
        SK.setBounds(200, 180, 210, 70);
        baseSK=new Reg("000000000000");
        baseSK.setBounds(5, 30, SK.getWidth()-10, SK.getHeight()-35);
        SK.add(baseSK);

        Box A=new Box("Аккумулятор");
        A.setBounds(185, 440, 300, 70);
        baseA=new Reg("0000000000000000");
        baseA.setBounds(30, 30, A.getWidth()-40, A.getHeight()-35);
        baseC=new RegC("0");
        baseC.setBounds(0, 0, 30, A.getHeight()-5);
        A.add(baseA);
        A.add(baseC);

        ALU alu=new ALU();
        alu.setBounds(50, 290, 250, 101);

        Box yy = new Box("Устройство Управления");
        yy.setBounds(495, 220, 270, 280);
        baseYY=new YY();
        baseYY.setBounds(1, 30, yy.getWidth()-2, yy.getHeight()-31);
        yy.add(baseYY);

        baseView.add(RD);
        baseView.add(RK);
        baseView.add(RA);
        baseView.add(SK);
        baseView.add(alu);
        baseView.add(A);
        baseView.add(yy);

        baseView.InitArrows(new Arrow[]{
        new Arrow(new int[][]{{150,160},{150,265},{200,265},{200,280}}),      // 1 RD-ALU
        new Arrow(new int[][]{{200,220},{150,220},{150,265},{200,265},{200,280}}),  // 3 SK-ALU
        new Arrow(new int[][]{{185,480},{10,480},{10,265},{85,265},{85,280}}),  // 4 A-ALU
        new Arrow(new int[][]{{140,390},{140,415},{455,415},{455,40},{420,40}}),  // 18 ALU-RA
        new Arrow(new int[][]{{140,390},{140,415},{455,415},{455,125},{320,125}}),  // 19 ALU-RD
        new Arrow(new int[][]{{140,390},{140,415},{455,415},{455,80},{600,80},{600,100}}),  // 20 ALU-RK
        new Arrow(new int[][]{{140,390},{140,415},{455,415},{455,215},{420,215}}),  // 21 ALU-SK
        new Arrow(new int[][]{{140,390},{140,415},{455,415},{455,430}}),  // 22 ALU-A
        new Arrow(new int[][]{{0,60},{60,60},{60,80}}),  // 23 OP->RD
        new Arrow(new int[][]{{60,160},{60,190},{10,190}}),// 24 RD->OP
        new Arrow(new int[][]{{200,30},{10,30}}),// RA - OP
        new Arrow(new int[][]{{600,180},{600,210}})// RK - YY
        });
}

public void initVYView()
{
       VYView = new GUIPanel(new Color(0,128,128));

        VYView.InitArrows(new Arrow[]{
        new Arrow(new int[][]{{175,800},{175,0}}),      // 0 vertical line
        new Arrow(new int[][]{{180,30},{300,30},{300,40}}),  //1 cpu-command decoder1
        new Arrow(new int[][]{{180,30},{450,30},{450,40}}),  //2 cpu-command decoder2
        new Arrow(new int[][]{{180,30},{650,30},{650,40}}),  //3 cpu-command decoder3
        new Arrow(new int[][]{{180,170},{300,170},{300,160}}),  //4 cpu-adress decoder1
        new Arrow(new int[][]{{180,170},{450,170},{450,160}}),  //5 cpu-adress decoder2
        new Arrow(new int[][]{{180,170},{650,170},{650,160}}),  //6 cpu-adress decoder3
        new Arrow(new int[][]{{295,240},{295,230},{190,230}}),  //7 interruption1-cpu
        new Arrow(new int[][]{{485,240},{485,230},{190,230}}),  //8 interruption2-cpu
        new Arrow(new int[][]{{665,240},{665,230},{190,230}}),  //9 interruption3-cpu
        new Arrow(new int[][]{{295,270},{295,290},{190,290}}),  //10 flag1-cpu
        new Arrow(new int[][]{{485,270},{485,290},{190,290}}),  //11 flag2-cpu
        new Arrow(new int[][]{{665,270},{665,290},{190,290}}),  //12 flag3-cpu
        new Arrow(new int[][]{{450,370},{450,350},{190,350}}),  //13 rdvy2-cpu
        new Arrow(new int[][]{{650,370},{650,350},{190,350}}),  //14 rdvy3-cpu
        new Arrow(new int[][]{{180,460},{300,460},{300,450}}),  //15 cpu-rdvy1
        new Arrow(new int[][]{{180,460},{650,460},{650,450}})  //16 cpu-rdvy3
        });


        Box vyRDbox=new Box("РД");
        vyRDbox.setBounds(10, 10, 100, 60);
        vyRD=new RegHex("0000");
        vyRD.setBounds(15, 30, vyRDbox.getWidth()-20, vyRDbox.getHeight()-35);
        vyRDbox.add(vyRD);

        Box vyRAbox=new Box("РА");
        vyRAbox.setBounds(10, 100, 100, 60);
        vyRA=new RegHex("000");
        vyRA.setBounds(15, 30, vyRAbox.getWidth()-20, vyRAbox.getHeight()-35);
        vyRAbox.add(vyRA);

        Box vySKbox=new Box("СК");
        vySKbox.setBounds(10, 200, 100, 60);
        vySK=new RegHex("000");
        vySK.setBounds(15, 30, vySKbox.getWidth()-20, vySKbox.getHeight()-35);
        vySKbox.add(vySK);

        Box vyRKbox=new Box("РК");
        vyRKbox.setBounds(10, 300, 100, 60);
        vyRK=new RegHex("0000");
        vyRK.setBounds(15, 30, vyRKbox.getWidth()-20, vyRKbox.getHeight()-35);
        vyRKbox.add(vyRK);

        Box vyAbox=new Box("А");
        vyAbox.setBounds(10, 400, 150, 70);
        vyA=new RegHex("0000");
        vyA.setBounds(30, 30, vyAbox.getWidth()-35, vyAbox.getHeight()-35);
        vyAC = new RegC("0");
        vyAC.setBounds(0, 0, 30, vyAbox.getHeight()-5);
        vyAbox.add(vyA);
        vyAbox.add(vyAC);

         VYDecoder vyDecoder1=new VYDecoder();
         vyDecoder1.setBounds(200,50,180,100);

         VYDecoder vyDecoder2=new VYDecoder();
         vyDecoder2.setBounds(390,50,180,100);

         VYDecoder vyDecoder3=new VYDecoder();
         vyDecoder3.setBounds(580,50,180,100);

         vyFlag1=new Flag();
         vyFlag1.setBounds(280, 245, 30, 30);
        
         vyFlag2=new Flag();
         vyFlag2.setBounds(470, 245, 30, 30);

         vyFlag3=new Flag();
         vyFlag3.setBounds(650, 245, 30, 30);
       
          Box vyRDVY1box=new Box("РД ВУ1");
         vyRDVY1box.setBounds(200, 370, 170, 70);
         vyRDVY1=new RegInput("00000000");
         vyRDVY1.setBounds(5, 30, vyRDVY1box.getWidth()-10, vyRDVY1box.getHeight()-35);
         vyRDVY1box.add(vyRDVY1);

         Box vyRDVY2box=new Box("РД ВУ2");
         vyRDVY2box.setBounds(390, 370, 170, 70);
         vyRDVY2=new RegInput("00000000");
         vyRDVY2.setBounds(5, 30, vyRDVY2box.getWidth()-10, vyRDVY2box.getHeight()-35);
         vyRDVY2box.add(vyRDVY2);

         Box vyRDVY3box=new Box("РД ВУ3");
         vyRDVY3box.setBounds(580, 370, 170, 70);
         vyRDVY3=new RegInput("00000000");
         vyRDVY3.setBounds(5, 30, vyRDVY3box.getWidth()-10, vyRDVY3box.getHeight()-35);
         vyRDVY3box.add(vyRDVY3);

         Font textLabel=new Font(null,Font.PLAIN,20);
         Label vyCommand=new Label("Приказ на ввод-вывод");
         vyCommand.setFont(textLabel);
         vyCommand.setBounds(200, 0, 300, 20);

         Label vyAdress=new Label("Адрес ВУ");
         vyAdress.setFont(textLabel);
         vyAdress.setBounds(200, 180, 300, 20);

         Label vyInterruption=new Label("Запрос прерывания");
         vyInterruption.setFont(textLabel);
         vyInterruption.setBounds(200, 200, 300, 20);

         Label vyFlags=new Label("Состояние флагов ВУ");
         vyFlags.setFont(textLabel);
         vyFlags.setBounds(200, 300, 300, 20);
         
         Label vyIn=new Label("Шина Ввода");
         vyIn.setFont(textLabel);
         vyIn.setBounds(200, 320, 300, 20);
         
         Label vyOut=new Label("Шина Вывода");
         vyOut.setFont(textLabel);
         vyOut.setBounds(200, 470, 300, 20);

         VYView.add(vyRDbox);
         VYView.add(vyRAbox);
         VYView.add(vySKbox);
         VYView.add(vyRKbox);
         VYView.add(vyAbox);
         VYView.add(vyRDVY1box);
         VYView.add(vyRDVY2box);
         VYView.add(vyRDVY3box);
         VYView.add(vyFlag1);
         VYView.add(vyFlag2);
         VYView.add(vyFlag3);
         VYView.add(vyDecoder1);
         VYView.add(vyDecoder2);
         VYView.add(vyDecoder3);
         VYView.add(vyCommand);
         VYView.add(vyAdress);
         VYView.add(vyInterruption);
         VYView.add(vyFlags);
         VYView.add(vyIn);
         VYView.add(vyOut);
}

public void initMPView()
{
      //  MPView = new GUIPanel(new Color(128,128,0));
        MPView = new GUIPanel(new Color(255,163,177));


        MPView.InitArrows(new Arrow[]{
        new Arrow(new int[][]{{640,55},{450,55},{450,90}}),      // MemoryMK - RMK
        new Arrow(new int[][]{{450,170},{450,220}}),  // RMK - decoder
        new Arrow(new int[][]{{480,270},{480,390}}),  // decoder - MK counter
        new Arrow(new int[][]{{340,270},{340,320},{260,320}}),  // decoder - y0
        new Arrow(new int[][]{{380,270},{380,360},{260,360}}),  // decoder - y28
        new Arrow(new int[][]{{245,800},{245,0}})  // vertical line ^^
        });

        Box mpRDbox=new Box("РД");
        mpRDbox.setBounds(10, 80, 100, 60);
        mpRD=new RegHex("0000");
        mpRD.setBounds(15, 30, mpRDbox.getWidth()-20, mpRDbox.getHeight()-35);
        mpRDbox.add(mpRD);

        Box mpRAbox=new Box("РА");
        mpRAbox.setBounds(10, 10, 100, 60);
        mpRA=new RegHex("000");
        mpRA.setBounds(15, 30, mpRAbox.getWidth()-20, mpRAbox.getHeight()-35);
        mpRAbox.add(mpRA);

        Box mpSKbox=new Box("СК");
        mpSKbox.setBounds(120, 10, 100, 60);
        mpSK=new RegHex("000");
        mpSK.setBounds(15, 30, mpSKbox.getWidth()-20, mpSKbox.getHeight()-35);
        mpSKbox.add(mpSK);

        Box mpRKbox=new Box("РК");
        mpRKbox.setBounds(120, 80, 100, 60);
        mpRK=new RegHex("0000");
        mpRK.setBounds(15, 30, mpRKbox.getWidth()-20, mpRKbox.getHeight()-35);
        mpRKbox.add(mpRK);

        Box mpALU=new Box("АЛУ");
        mpALU.setBounds(10, 160, 150, 100);
        Box mpBRbox=new Box("БР");
        mpBRbox.setBounds(5, 30, mpALU.getWidth()-10, mpALU.getHeight()-35);
        mpBR=new RegHex("0000");
        mpBR.setBounds(30, 30, mpBRbox.getWidth()-40, mpBRbox.getHeight()-35);
        mpBRC=new RegC("0");
        mpBRC.setBounds(0, 0, 30, mpBRbox.getHeight()-5);
        mpBRbox.add(mpBR);
        mpBRbox.add(mpBRC);
        mpALU.add(mpBRbox);

        Box mpAbox=new Box("А");
        mpAbox.setBounds(10, 280, 150, 70);
        mpA=new RegHex("0000");
        mpA.setBounds(30, 30, mpAbox.getWidth()-35, mpAbox.getHeight()-35);
        mpAC = new RegC("0");
        mpAC.setBounds(0, 0, 30, mpAbox.getHeight()-5);
        mpAbox.add(mpA);
        mpAbox.add(mpAC);
    
        Box mpRSbox=new Box("Регистр Состояния");
        mpRSbox.setBounds(10, 370, 230, 100);
        Label RSText=new Label("U XAKP WFIE 0NZC");
        RSText.setBounds(5, 65, mpRSbox.getWidth()-10, 30);
        RSText.setFont(new Font(null,Font.PLAIN,22));
        mpRSbox.add(RSText);
        mpRS=new Reg("0000000000000");
        mpRS.setBounds(5, 35, mpRSbox.getWidth()-10, 30);
        mpRSbox.add(mpRS);

        Box mpRMKbox=new Box("Регистр Микро Команд");
        mpRMKbox.setBounds(300, 100, 280, 70);
        mpRMK=new Reg("0000000000000000");
        mpRMK.setBounds(5, 30, mpRMKbox.getWidth()-10, mpRMKbox.getHeight()-35);
        mpRMKbox.add(mpRMK);

        Box CMDDecoder=new Box("Дешифратор МК ");
        CMDDecoder.setBounds(300, 230, 220, 40);

        Box mpMKCbox=new Box("Сч МК");
        mpMKCbox.setBounds(400, 400, 150, 70);
        mpMKC=new Reg("00000000");
        mpMKC.setBounds(5, 30, mpMKCbox.getWidth()-10, mpMKCbox.getHeight()-35);
        mpMKCbox.add(mpMKC);

        Label YYname=new Label("Устройство Управления");
        YYname.setFont(new Font(null,Font.BOLD,25));
        YYname.setBounds(300, 10, 300, 30);

        Label MKCinc=new Label("+1");
        MKCinc.setFont(new Font(null,Font.PLAIN,25));
        MKCinc.setBounds(350, 400, 30, 30);

        Label Y0=new Label("У0");
        Y0.setFont(new Font(null,Font.PLAIN,22));
        Y0.setBounds(250, 280, 50, 30);

        Label Y28=new Label("У28");
        Y28.setFont(new Font(null,Font.PLAIN,22));
        Y28.setBounds(250, 380, 50, 30);

        MPmemo=new Memory("ПАМЯТЬ МК");
        MPmemo.setBounds(640, 0, 140, 550);

        MPView.add(mpRDbox);
        MPView.add(mpRAbox);
        MPView.add(mpSKbox);
        MPView.add(mpRKbox);
        MPView.add(mpALU);
        MPView.add(mpAbox);
        MPView.add(mpRSbox);
        MPView.add(mpRMKbox);
        MPView.add(CMDDecoder);
        MPView.add(mpMKCbox);
        MPView.add(YYname);
        MPView.add(MKCinc);
        MPView.add(Y0);
        MPView.add(Y28);
        MPView.add(MPmemo);
}

public void SetBaseView()
{
    VYView.setVisible(false);
    MPView.setVisible(false);
    baseView.setVisible(true);
}

public void SetVYView()
{
    MPView.setVisible(false);
    baseView.setVisible(false);
    VYView.setVisible(true);
}

public void SetMPView()
{
    VYView.setVisible(false);
    baseView.setVisible(false);
    MPView.setVisible(true);
}

}



