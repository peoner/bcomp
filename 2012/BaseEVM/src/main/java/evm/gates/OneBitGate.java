package evm.gates;


import evm.cells.Bit;

public class OneBitGate extends Gate {

    Bit bitNumberInput;
    Bit bitNumberOutput;
    String name;

    public OneBitGate(Bit input, Bit output, String name, int number) {

    }

    public int getNumber(){
        return number;
    }

    @Override
    public void execute(boolean open) {
    }
}
