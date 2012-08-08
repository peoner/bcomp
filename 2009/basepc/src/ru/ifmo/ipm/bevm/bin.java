package ru.ifmo.ipm.bevm;

public class bin {
	char[] arr;
	
	public String toString() {
		return String.copyValueOf(arr);
	}
	
	public bin(int len) {
		arr = Utils.AlignStr("", len, '0').toCharArray();
	}
	
	public bin(int len, String str) {
		arr = Utils.AlignStr(str, len, '0').toCharArray();
	}
	
	public bin(String str) {
		arr = str.toCharArray();
	}
	
	public bin(bin b) {
		arr = b.arr.clone();
	}
	
	public bin inc() {
		int n = toInt() + 1;
		/*while(n>65535){
			n -= 65536;
		}*/
		return new bin(Utils.Int2BinStr(n));
	}
	
	public int toInt() {
		int ret = 0;
		for(int i = 0; i<arr.length; i++)
			if(arr[i]=='1') ret += Math.pow(2, arr.length-i-1);
		return ret;
	}
	
	public bin add(bin arg) {
		int n = toInt() + arg.toInt();
		/*while(n>65535){
			n -= 65536;
		}*/
		return new bin(Utils.Int2BinStr(n));
	}
	
	public bin clone() {
		return new bin(this);
	}
	
	public bin and(bin arg) {
		String s = "";
		for(int i = 0; i<arr.length; i++)
			s = ((getBit(i)=='1' && arg.getBit(i)=='1')?('1'):('0')) + s;
		return new bin(s);
	}
	
	public char getBit(int n) {
		if(n>=0 && n<arr.length)
			return arr[arr.length-n-1];
		else 
			return 0;
	}
	
	public void setBit(int n, char val) {
		if(n>=0 && n<arr.length)
			arr[arr.length-n-1] = val;
	}
	
	public void invert(){
		for(int i = 0; i<arr.length; i++)
			arr[i]=((arr[i]=='1')?('0'):('1')); 
	}
}
