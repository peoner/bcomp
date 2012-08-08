package evm;


import evm.gates.Gate;

import javax.xml.crypto.Data;

public interface EVM {
    void execute(int command);
    void registerGateListener(Gate Listener);
    void registerDataListener(Data Listener);
}
