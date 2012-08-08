package ru.ifmo.basepc;

import java.applet.Applet;
import java.awt.event.*;

public class Main extends Applet implements KeyListener
{
    GUI gui;
    Processor proc;

    public void init() {

        proc=new Processor();
        proc.Memory[32]=Integer.parseInt("1111111111111111", 2);
        proc.Memory[33]=16416;


        this.setSize(900,600);  //гуй
        gui=new GUI();
        this.add(gui);
        this.addKeyListener(this);
        gui.addKeyListener(this);
        gui.procView.vyRDVY1.addKeyListener(this);
        gui.procView.vyRDVY2.addKeyListener(this);
        gui.procView.vyRDVY3.addKeyListener(this);
        gui.keyReg.addKeyListener(this);
        gui.isTakt.addKeyListener(this);
        gui.isMKediting.addKeyListener(this);
        gui.procView.vyFlag1.addKeyListener(this);
        gui.procView.vyFlag2.addKeyListener(this);
        gui.procView.vyFlag3.addKeyListener(this);
        this.setFocusable(true);
        gui.keyReg.requestFocus();
        Start();
    }

    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode()) {
        case KeyEvent.VK_F1:{
            gui.procView.vyFlag1.active=true;
            gui.procView.vyFlag1.repaint();
            break;
        }

        case KeyEvent.VK_F2:{
            gui.procView.vyFlag2.active=true;
            gui.procView.vyFlag2.repaint();
            break;
        }

        case KeyEvent.VK_F3:{
            gui.procView.vyFlag3.active=true;
            gui.procView.vyFlag3.repaint();
            break;
        }

       case KeyEvent.VK_F4:
       {
            //ввод
           Processor.KR=gui.getKeyReg();
           Processor.ChMK=153; //99
           do
              UpdateGUI(false);
           while(Processor.ChMK != 1);

           break;
        }

       case KeyEvent.VK_F5:{
            //запись
           Processor.KR=gui.getKeyReg();
           Processor.ChMK=161; //a1
           do
              UpdateGUI(false);
           while(Processor.ChMK != 1);

           break;
        }

       case KeyEvent.VK_F6:{
            //чтение
           Processor.KR=gui.getKeyReg();
           Processor.ChMK=156; //9c
           do
              UpdateGUI(false);
           while(Processor.ChMK != 1);

           break;
        }

       case KeyEvent.VK_F7:{
            //пуск
           Processor.ChMK=168; //a8
           do
              UpdateGUI(false);
           while(Processor.ChMK != 1);

            break;
        }

       case KeyEvent.VK_F8:{
            //продолжение
           //Processor.CK=new int[]{0,0,0,0,0,1,0,0,0,0,1};
           if(gui.isTakt.getState()) UpdateGUI(false);
           else
           do
              UpdateGUI(false);
           while(Processor.ChMK != 1);
           break;
        }

       case KeyEvent.VK_F9:{
            //раб/останов
           Start();
            break;
        }

		case KeyEvent.VK_F10:{
            //смена вида
            if(gui.procView.baseView.isVisible()){gui.procView.SetVYView();return;}
            if(gui.procView.VYView.isVisible()){gui.procView.SetMPView();return;}
            if(gui.procView.MPView.isVisible()){gui.procView.SetBaseView();return;}
            break;
            }

        }
    }

   public void DoOneTakt()
   {
          Processor.DecodeMK(proc.MemoryMK[proc.ChMK], proc.Memory);
   }

   public void Start()
   {
            Processor.A=new int[16];
            Processor.KR = new int[16];
            Processor.RA = new int[11];
            Processor.RC = new int[13];
            Processor.RD = new int[16];
            Processor.RK = new int[16];
            Processor.ChMK=1;

           UpdateGUI(true);
   }

   public void UpdateGUI(boolean isInnocence)
   {
           gui.setMKRD(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 16, Processor.MemoryMK[Processor.ChMK])),16));
           gui.updateMemoryMK(Processor.MemoryMK, proc.ChMK);
           if(!isInnocence) DoOneTakt();
           gui.setA(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 16, Processor.A)),16));
           gui.setRA(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 11, Processor.RA)),12));
           gui.setRD(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 16, Processor.RD)),16));
           gui.setRK(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 16, Processor.RK)),16));
           gui.setSK(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 11, Processor.CK)),12));
           gui.setC(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 1, Processor.C)),1));
           gui.setSMK(Processor.MinimazeSumm(Integer.toBinaryString(proc.ChMK),8));
           gui.setBR(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 17, Processor.ALY.bufregister)),17));
           gui.setRS(Processor.MinimazeSumm(Integer.toBinaryString(Processor.GetParam(0, 13, Processor.RC)), 13));
           gui.updateMemory(Processor.Memory, Processor.GetParam(0, 11, Processor.CK));

   }



    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
