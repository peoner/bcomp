package memory;

import java.io.IOException;
import java.util.*;

import evm.cells.AutoIncrementCell;
import evm.cells.Cell;
import evm.cells.SimpleCell;

public class SimpleMemory extends Cell{
    
    
    private int MEMORY_SIZE = 2048;
    private int CELL_WIDTH = 16;
    private Cell address;

    protected  ArrayList<Cell> memory = new ArrayList<Cell>(MEMORY_SIZE);

    public SimpleMemory(Cell address){
        super(16); // No need,just for compiling
        for (int i = 0; i < MEMORY_SIZE; i++){
            memory.set(i,new SimpleCell(CELL_WIDTH));
        }
        for (int i = 0x8; i<= 0xF; i++){
            memory.set(i,new AutoIncrementCell(CELL_WIDTH));
        }
    }
    
    public void load(String file) throws IOException{
        // Config.loadMemory(memory,file);
    }

    public void store(String file) throws IOException{
        //Config.storeMemory(memory,file);
    }



    public int get(){
        return memory.get(address.get()).get();
    }
    
    public void set(int data){
        memory.get(address.get()).set(data);
    }

    public int onlyGet() {
        return data;
    }

    public int getBits(int start, int count) {
        return 0;
    }

    public void setBits(int start, int count, int value) {
    }
    
}
