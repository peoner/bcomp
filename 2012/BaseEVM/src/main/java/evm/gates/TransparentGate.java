package evm.gates;


import evm.cells.Cell;

public class TransparentGate extends CellGate {



    public TransparentGate(Cell input, Cell output, String name, int number) {
        super(input, output, name, number);
    }

    public void execute(boolean open) {
            output.set(input.get());
    }

}
