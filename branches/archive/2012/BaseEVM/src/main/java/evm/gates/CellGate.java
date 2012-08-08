package evm.gates;


import evm.cells.Cell;

public class CellGate extends Gate{

    protected Cell input;
    protected Cell output;
    public String name;


    public CellGate(Cell input, Cell output, String name, int number) {
        this.input = input;
        this.output = output;
        this.name = name;
        this.number = number;
    }

    public int getNumber(){
        return number;
    }


    public void execute(boolean open) {
        if (open) {
            output.set(input.get());
        }
    }
}
