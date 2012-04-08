package evm.gates;

import evm.cells.Bit;
import evm.cells.Cell;

/**
 * Created by IntelliJ IDEA.
 * User: niyaz
 * Date: 4/8/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class RotateGate extends CellGate {
    public RotateGate(Cell inputCell, Bit inputBit, Cell output, boolean left, String name, int number) {
        super(inputCell, output, name, number);
    }

    @Override
    public void execute(boolean open) {

    }
}
