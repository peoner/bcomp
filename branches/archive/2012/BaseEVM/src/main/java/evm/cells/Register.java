package evm.cells;

public class Register extends SimpleCell{

    public String name;

    public Register(int width,String name){
        super(width);
        this.name = name;
    }

}
