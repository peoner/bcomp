package evm.gates;


import evm.cells.Cell;

public class RevertCodeGate extends CellGate {
    
    public RevertCodeGate(Cell input, Cell output, String name, int number) {
        super(input, output, name, number);
    }
    
    @Override 
    public void execute(boolean open){
        if (open) {
            output.set( revert(input.get()));
        }  else {
            output.set( input.get());
        }
    }

    private int revert(int arg){
        int buf = arg;
        buf = ~buf;
        buf += 1;
        return buf;
    }
    

}
