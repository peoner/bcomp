package evm.gates;

import evm.cells.Cell;

public class AdderGate extends CellGate {


    private Cell inputRight;

    public AdderGate(Cell input, Cell inputRight, Cell output, String name, int number) {
        super(input, output, name, number);
        this.inputRight = inputRight;
    }

    public void execute(int gateNumber, boolean open) {
        if (open == true) {
            output.set(input.get() * inputRight.get());
        } else {
            output.set(input.get() * inputRight.get());
        }
    }

}
