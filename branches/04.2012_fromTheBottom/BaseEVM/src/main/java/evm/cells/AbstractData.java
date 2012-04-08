package evm.cells;

import commons.listening.DataListener;

import java.util.List;
import java.util.Set;


public abstract class AbstractData {
    Set<DataListener> listener;

    public void removeListener(DataListener listener){

    }

    public void addListener(DataListener listener){

    }

}
