package ru.ifmo.ipm.bevm;

public class MemoryArray {
	bin[] arr;
	
	public MemoryArray(int len, int blockSize) {
		arr = new bin[len];
		for (int i=0; i<len; i++) {
			arr[i] = new bin(blockSize);
		}
	}
	
	public void setVal(int addr, bin newval) {
		if(addr>=0 && addr<arr.length)
			arr[addr] = newval;
	}
	
	public void setVal(int addr, String newval) {
		if(addr>=0 && addr<arr.length)
			arr[addr] = new bin(arr[0].arr.length, newval);
	}
	
	public bin getVal(int addr) {
		if(addr>=0 && addr<arr.length)
			return arr[addr];
		else 
			return new bin(arr[0].arr.length);
	}
}
