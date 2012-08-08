package evm.cells;

public class AutoIncrementCell extends Cell{
    
    public AutoIncrementCell(int width){
        super(width);
    }   
    
    public int get(){
        int buf = super.get();
        super.set(++buf);
        return buf;
    }

}
