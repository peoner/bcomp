package evm.gates;


import evm.cells.Cell;

public class Plus1AdderGate extends CellGate {
    public Plus1AdderGate(Cell input, Cell output, String name, int number) {
        super(input, output, name, number);
    }

    public void execute(boolean open) {
        if (open) {
            output.set(input.get() + 1);
        } else {
            output.set(input.get());

        }
    }

}
