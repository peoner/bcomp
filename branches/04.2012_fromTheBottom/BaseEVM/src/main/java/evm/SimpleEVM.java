package evm;


import evm.cells.Cell;
import evm.cells.SimpleCell;
import evm.gates.*;


import javax.xml.crypto.Data;
import java.util.ArrayList;

public class SimpleEVM implements EVM{
    
    private ArrayList<Gate> gates = new ArrayList<Gate>();
    private ArrayList<Data> datas = new ArrayList<Data>();
/*

    private class Gate{
        public EVM.Gate gate;
        public int num;

        public Gate(EVM.Gate gate,int num){
            this.gate = gate;
            this.num = num;
        }
    }
*/

/*
    SimpleCell left = new SimpleCell(16);
    SimpleCell right = new SimpleCell(16);
*/

    public SimpleEVM(Cell controllerCell, int address, Cell memory){

/*
        SimpleCell addressRegister = new SimpleCell(11);
        addressRegister.set(address);
        SimpleCell dataRegister = new SimpleCell(16);
        SimpleCell commandRegister = new SimpleCell(16);
        SimpleCell commandPointer = new SimpleCell(11);

        SimpleCell beforeBufferRegister = new SimpleCell(17);
        SimpleCell bufferRegister = new SimpleCell(17);

        SimpleCell acc = new SimpleCell(16);
        SimpleCell stateRegister = new SimpleCell(13);

        SimpleCell keywordRegister = new SimpleCell(16);

        SimpleCell rightAdder = new SimpleCell(16);
        SimpleCell leftAdder = new SimpleCell(16);
        SimpleCell trueCell = new TrueCell();
        SimpleCell falseCell = new FalseCell();




        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(memory,dataRegister,"23"),23));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(dataRegister,memory,"24"),24));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(dataRegister,right,"1"),1));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(commandRegister,right,"2"),2));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(commandPointer,right,"3"),3));
        gates.add(new EVM.SimpleEVM.Gate(new RevertCodeGate(right,rightAdder,"8"),8));

        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(acc,left,"4"),4));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(stateRegister,left,"5"),5));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(keywordRegister,left,"6"),6));
        gates.add(new EVM.SimpleEVM.Gate(new RevertCodeGate(left,leftAdder,"7"),7));

        gates.add(new EVM.SimpleEVM.Gate(new AdderGate(leftAdder,rightAdder,beforeBufferRegister,"Adder_9"),9));
        gates.add(new EVM.SimpleEVM.Gate(new Plus1AdderGate(beforeBufferRegister,bufferRegister,"10"),10));


        gates.add(new EVM.SimpleEVM.Gate(new LeftRotateGate(acc,bufferRegister,"12"),12));
        gates.add(new EVM.SimpleEVM.Gate(new RightRotateGate(acc,bufferRegister,"11"),11));

        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(bufferRegister,acc,"22"),22));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(bufferRegister,commandPointer,"21"),21));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(bufferRegister,commandRegister,"20"),20));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(bufferRegister,dataRegister,"19"),19));
        gates.add(new EVM.SimpleEVM.Gate(new EVM.Gate(bufferRegister,addressRegister,"18"),18));
        gates.add(new EVM.SimpleEVM.Gate(new TransparentGate(bufferRegister,controllerCell,"30"),30));

        gates.add(new EVM.SimpleEVM.Gate(new OneBitGate(bufferRegister,stateRegister,"13",16,0),13));
        gates.add(new EVM.SimpleEVM.Gate(new OneBitGate(bufferRegister,stateRegister,"14",15,0),14));

        gates.add(new EVM.SimpleEVM.Gate(new OneBitGate(falseCell,stateRegister,"16",0,0),16));
        gates.add(new EVM.SimpleEVM.Gate(new OneBitGate(trueCell,stateRegister,"17",0,0),17));
        
        gates.add(new EVM.SimpleEVM.Gate(new ZGate(bufferRegister,stateRegister,"Z",2),15));
*/

    }

    public void execute(int command){
        /*left.set(0);
        right.set(0);
        for (EVM.SimpleEVM.Gate gate : gates){
            gate.gate.execute(checkBit(command,gate.num));
        }*/
    }

    public void registerGateListener(Gate Listener){

    }

    public void registerDataListener(Data Listener){

    }


}
