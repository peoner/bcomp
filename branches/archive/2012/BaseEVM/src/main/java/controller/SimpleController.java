package controller;


import evm.cells.AutoIncrementCell;
import evm.cells.Cell;
import evm.cells.SimpleCell;
import evm.EVM;


public class SimpleController {

    AutoIncrementCell mkcomPointer = new AutoIncrementCell(11);
    ControllerMemory memory;
    Cell evmCell;
    EVM evm;
    Cell mkcommandRegister = new SimpleCell(32);

    public SimpleController(ControllerMemory memory, EVM evm, Cell evmCell) {
        mkcomPointer.set(0);
        this.memory = memory;
        this.evm = evm;
        this.evmCell = evmCell;

    }

    public void goTo(int address){

    }

    public void clk() {
     /*   mkcommandRegister.set(memory.get());
        int buf = mkcommandRegister.get();
        if (checkBit(buf, 31)) {
            SimpleCell command = new SimpleCell(32);
            if (checkBit(buf, 28)) {
                command.setBit(4, true);
            }
            if (checkBit(buf, 27)) {
                command.setBit(2, true);
            }
            if (checkBit(buf, 26)) {
                command.setBit(1, true);
            }
            if (checkBit(buf, 25)) {
                command.setBit(5, true);
            }
            int buf2 = buf;
            int mask = 1;
            for (int i = 0; i < 31; i++) {
                buf = (mask << 1) + 1;
            }
            buf2 &= mask;

            evm.execute(command.get());

            if (((buf2 & evmCell.get()) != 0) == (checkBit(buf, 24))) {
                int address = 0;
                for (int i = 16; i <= 23; i++)
                    if ((checkBit(buf, i))) {
                        address <<= 1;
                        address += 1;
                    }
                mkcomPointer.set(address);
            }
        } else {
            evm.execute(buf);
        }*/
    }
}
