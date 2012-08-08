package ru.ifmo.ipm.bevm;


import java.applet.Applet;
import java.awt.*;

public class GUI extends Applet {

    public void init() {

        this.setSize(900,600);
     
        String[] adr=
        {
            "000",
            "001",
            "002",
            "003",
            "004",
            "005",
            "006",
            "007",
            "008",
            "009",
            "00A",
            "00B",
            "00C",
            "00D",
            "00E",
            "00F"
        };
        String[] val=
        {
            "F200",
            "0010",
            "3002",
            "0030",
            "0040",
            "0050",
            "0060",
            "F200",
            "0080",
            "0090",
            "00A0",
            "00B0",
            "00C0",
            "00D0",
            "00E0",
            "F000"
        };

        this.setLayout(new java.awt.BorderLayout());

        Memory memo=new Memory("ПАМЯТЬ",adr,val);
        memo.setSize(120, 580);
        this.add(memo, BorderLayout.WEST);


        GUIPanel baseView = new GUIPanel();
        this.add(baseView, BorderLayout.CENTER);

        Box RD=new Box("Регистр Данных");
        RD.setBounds(30, 90, 280, 70);
        Reg bRD=new Reg("0000111100001010");
        bRD.setBounds(5, 30, RD.getWidth()-10, RD.getHeight()-35);
        RD.add(bRD);

        Box RK=new Box("Регистр Команд");
        RK.setBounds(480, 110, 280, 70);
        Reg bRK=new Reg("0000111100001010");
        bRK.setBounds(5, 30, RK.getWidth()-10, RK.getHeight()-35);
        RK.add(bRK);

        Box RA=new Box("Регистр Адреса");
        RA.setBounds(200, 10, 210, 70);
        Reg bRA=new Reg("000000010000");
        bRA.setBounds(5, 30, RA.getWidth()-10, RA.getHeight()-35);
        RA.add(bRA);

        Box SK=new Box("Счётчик Команд");
        SK.setBounds(200, 180, 210, 70);
        Reg bSK=new Reg("000000010000");
        bSK.setBounds(5, 30, SK.getWidth()-10, SK.getHeight()-35);
        SK.add(bSK);

        Box A=new Box("Аккумулятор");
        A.setBounds(185, 440, 300, 70);
        Reg bA=new Reg("0111000000010000");
        bA.setBounds(30, 30, A.getWidth()-40, A.getHeight()-35);
        RegC bC=new RegC("1");
        bC.setBounds(0, 0, 30, A.getHeight()-5);
        A.add(bA);
        A.add(bC);
          
        ALU alu=new ALU();
        alu.setBounds(20, 290, 250, 101);

        Box yy = new Box("Устройство Управления");
        yy.setBounds(495, 220, 270, 280);
        YY viewYY=new YY();
        viewYY.setBounds(1, 30, yy.getWidth()-2, yy.getHeight()-31);
        yy.add(viewYY);

        
        baseView.add(RD);
        baseView.add(RK);
        baseView.add(RA);
        baseView.add(SK);
        baseView.add(alu);
        baseView.add(A);
        baseView.add(yy);

        baseView.InitArrows(new Arrow[]{
        new Arrow(new int[][]{{150,160},{150,265},{230,265},{230,280}}),      // 1 RD-ALU
        new Arrow(new int[][]{{200,220},{150,220},{150,265},{230,265},{230,280}}),  // 3 SK-ALU
        new Arrow(new int[][]{{185,480},{10,480},{10,265},{55,265},{55,280}}),  // 4 A-ALU
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

        viewYY.stage=2;
        baseView.arrowOpen(3,true);

        /*Panel down=new Panel();
        down.setSize(800, 500);
        down.setBackground(Color.PINK);
        this.add(down,BorderLayout.SOUTH);
         
         * тут будет нижняя панелька
         */
    }

    // TODO overwrite start(), stop() and destroy() methods
}
