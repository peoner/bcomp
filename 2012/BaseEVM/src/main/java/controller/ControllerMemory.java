package controller;

import  evm.cells.Cell;
import evm.cells.SimpleCell;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerMemory {

    private int MEMORY_SIZE = 2048;
    private int CELL_WIDTH = 32;
    private Cell address;

    protected ArrayList<Cell> memory = new ArrayList<Cell>(MEMORY_SIZE);

    public void load(String file) throws IOException {
       // Config.loadMemory(memory, file);
    }

    public void store(String file) throws IOException{
      //  Config.storeMemory(memory,file);
    }

    public ControllerMemory(Cell address){
        for (int i = 0; i < MEMORY_SIZE; i++){
            memory.set(i,new SimpleCell(CELL_WIDTH));
        }
        this.address = address;
    }

    public int get(){
        return memory.get(address.get()).get();
    }

    public void set(int data){
        memory.get(address.get()).set(data);
    }
}
