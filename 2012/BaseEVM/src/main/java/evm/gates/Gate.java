package evm.gates;


import commons.listening.DataListener;
import commons.listening.GateListener;

import java.awt.*;
import java.util.Set;

public abstract class Gate {

    Set<GateListener> listeners;
    protected int number;

    abstract public void execute(boolean open);

    public int getNumber(){
        return number;
    }

    public void removeListener(GateListener listener){

    }

    public void addListener(GateListener listener){

    }

}
