package ru.ifmo.basepc;

import java.awt.*;

public class GUI extends Panel{
    
    public RegInput keyReg;
    public ViewPanel procView;
    public Memory memo;
    public Checkbox isTakt;
    public Checkbox isMKediting;

    public GUI() {     

        this.setLayout(new java.awt.BorderLayout());

        memo=new Memory("ПАМЯТЬ"); //память
        memo.setSize(120, 511);
        this.add(memo, BorderLayout.WEST);

        procView=new ViewPanel();    //главная панель
        this.add(procView,BorderLayout.CENTER);
        
        Panel down=new Panel();  //нижняя панель:
        down.setSize(900, 100);
        down.setBackground(new Color(240, 240, 240));
        down.setLayout(null);
        this.add(down,BorderLayout.SOUTH);
        
        keyReg=new RegInput("0000000000000000");  //клавишный регистр
        keyReg.setBounds(10, 5, 300, 40);
        down.add(keyReg);

        WorkingLabel workLabel=new WorkingLabel();  //состояние тумблера работа/остановка
        workLabel.setBounds(330, 5, 100, 40);
        down.add(workLabel);

        isTakt=new Checkbox("Такт", false);     //тактовый режим
        isTakt.setBounds(450, 5, 70, 40);
        isTakt.setFont(new Font(null,Font.PLAIN, 20));

        isMKediting=new Checkbox("Работа с памятью МК", false);   //переключатель работы с памятью МК
        isMKediting.setBounds(600, 5, 300, 40);
        isMKediting.setFont(new Font(null,Font.PLAIN, 20));

        Label helpText=new Label("F1,F2,F3 - Готовность ВУ,  F4 - Ввод,   F5 - Запись,  F6 - Чтение,        F7 - Пуск," +
                "     F8 - Продолжение,       F9 - Тумблер Работа/Остановка,      F10 - Смена вида");
        helpText.setBounds(5, 50, 900, 30);

        down.add(isTakt);
        down.add(isMKediting);
        down.add(helpText);
       }

       //public interface (gets, sets):
       public int[] getKeyReg()
       {
           int[] result=new int[this.keyReg.text.length()];
           for(int i=0; i<this.keyReg.text.length();i++)
              result[i]=this.keyReg.text.charAt(i)=='1'?1:0;
           return result;
       }

       public Boolean isTaktMode() { return isTakt.getState(); }
       public Boolean isEditingMK() { return isMKediting.getState(); }
       
       public String getRK()
       {
           return procView.baseRK.text;
       }
       public void setRK(String value)
       {
           procView.baseRK.text=value;
           procView.vyRK.text=BinStrtoHexStr(value,4);    //HEX
           procView.mpRK.text=BinStrtoHexStr(value,4);    //HEX
           procView.baseRK.repaint();
           procView.vyRK.repaint();
           procView.mpRK.repaint();

       }

       public String getRD() { return procView.baseRD.text;}
       public void setRD(String value)
       {
           procView.baseRD.text=value;
           procView.vyRD.text=BinStrtoHexStr(value,4);    //HEX
           procView.mpRD.text=BinStrtoHexStr(value,4);    //HEX
           procView.baseRD.repaint();
           procView.vyRD.repaint();
           procView.mpRD.repaint();
       }

       public String getRA() { return procView.baseRA.text;}
       public void setRA(String value)
       {
           procView.baseRA.text=value;
           procView.vyRA.text=BinStrtoHexStr(value,3);    //HEX
           procView.mpRA.text=BinStrtoHexStr(value,3);    //HEX
           procView.baseRA.repaint();
           procView.vyRA.repaint();
           procView.mpRA.repaint();
       }

       public String getA() { return procView.baseA.text;}
       public void setA(String value)
       {
           procView.baseA.text=value;
           procView.vyA.text=BinStrtoHexStr(value,4);    //HEX
           procView.mpA.text=BinStrtoHexStr(value,4);   //HEX
           procView.baseA.repaint();
           procView.vyA.repaint();
           procView.mpA.repaint();
       }

       public String getSK() { return procView.baseSK.text;}
       public void setSK(String value)
       {
           procView.baseSK.text=value;
           procView.vySK.text=BinStrtoHexStr(value,3);    //HEX
           procView.vySK.text=BinStrtoHexStr(value,3);    //HEX
           procView.mpSK.text=BinStrtoHexStr(value,3);
           procView.baseSK.repaint();
           procView.vySK.repaint();
           procView.mpSK.repaint();
       }

       public String getC() { return procView.baseC.text;}
       public void setC(String value)
       {
           procView.baseC.text=value;
           procView.vyAC.text=value;
           procView.mpAC.text=value;
           procView.baseC.repaint();
           procView.vyAC.repaint();
           procView.mpAC.repaint();
       }

       public String getMKRD() {return procView.mpRMK.text;}
       public void setMKRD(String value) {procView.mpRMK.text=value; procView.mpRMK.repaint();}

       public String getSMK() {return procView.mpMKC.text;}
       public void setSMK(String value) {procView.mpMKC.text=value;procView.mpMKC.repaint();}

       
       public String getBR() {return procView.mpBR.text;} // тут вместе с регистром переноса буферного регистра? + hex
       public void setBR(String value)
       {
           procView.mpBRC.text=String.valueOf(value.charAt(0));  //выделение переноса
           procView.mpBR.text=BinStrtoHexStr(value.substring(1),4);
           procView.mpBR.repaint();
           procView.mpBRC.repaint();
       }

       public String getRS() {return procView.mpRS.text;}
       public void setRS(String value) {procView.mpRS.text=value; procView.mpRS.repaint();}

       public void setYYStage(int value){procView.baseYY.stage=value; procView.baseYY.repaint(); }

       public String getRDVY(int num)  
       {
            switch(num)  //ввод только с ВУ-2 и ВУ-3
            {
                case 2:return procView.vyRDVY2.text.toString();
                case 3:return procView.vyRDVY3.text.toString();
                default: return "00000000";
            }
       }

       public void setRDVY(String value, int num) 
       {
            switch(num) //вывод только на ВУ-1 и ВУ-3
            {
                case 1:procView.vyRDVY1.text.append(value);procView.vyRDVY1.repaint();break;
                case 3:procView.vyRDVY3.text.append(value);procView.vyRDVY3.repaint();break;
            };
       }
       
       public Boolean getFlagVY(int num)  //чтение флагов
       {
            switch(num)
            {
                case 1:return procView.vyFlag1.active;
                case 2:return procView.vyFlag2.active;
                case 3:return procView.vyFlag3.active;
                default: return false;
            }
       }

       public void resetFlagVY(int num) //СБРОС флагов
       {
            switch(num)
            {
                case 1:procView.vyFlag1.active=false;procView.vyFlag1.repaint();break;
                case 2:procView.vyFlag2.active=false;procView.vyFlag2.repaint();break;
                case 3:procView.vyFlag3.active=false;procView.vyFlag3.repaint();break;
            };
       }

       public void updateMemory(int[] values, int currentAdress)
       {
           if(currentAdress>2031) currentAdress=2031;
           for(int i=0;i<16;i++)
           {
           memo.values[i]=BinStrtoHexStr(Integer.toBinaryString(values[i+currentAdress]),4);
           memo.adress[i]=BinStrtoHexStr(Integer.toBinaryString(i+currentAdress),3);
           }
           memo.repaint();
       }

       public void updateMemoryMK(int[][] values, int currentAdress)
       {
           if(currentAdress>239) currentAdress=239;
           for(int i=0;i<16;i++)
           {

           procView.MPmemo.values[i]=BinStrtoHexStr(Integer.toBinaryString(Processor.GetParam(0, 16, values[i+currentAdress])),4);
           procView.MPmemo.adress[i]=BinStrtoHexStr(Integer.toBinaryString(i+currentAdress),2);
           }
           procView.MPmemo.repaint();
       }

       //public void setRS(String value) {procView.mpRS.text=value;}

       private String BinStrtoHexStr(String str, int length)
       {
       String result = Integer.toHexString(Integer.parseInt(str,2)).toUpperCase();
       int resultLength=result.length();
       for(int i=length; i>resultLength; i--)
           result="0"+result;
       return result;
       }
       
}


