package evm.cells;

public class Cell extends AbstractData{
    protected int data = 0;
    protected int wight;

    private int mask;

    public Cell(int width) {
        this.wight = width;
        mask = 0;
        for (int i = 0; i < width; i++) {
            mask = (mask << 1) + 1;
        }
    }

    public int get() {
        return data;
    }

    public void set(int data) {
        this.data = (data & mask);
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
