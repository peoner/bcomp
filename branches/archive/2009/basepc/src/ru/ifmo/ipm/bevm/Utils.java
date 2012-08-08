package ru.ifmo.ipm.bevm;

public class Utils {
	
	public static int alignBy16 ( int size )
	{
		return	( (size%16 == 0) ? (size) : ( (size/16)*16 ) );
	}

	private static String HexChar2BinStr(char ch) {
		switch(ch) {
		case '0':return "0000";
		case '1':return "0001";
		case '2':return "0010";
		case '3':return "0011";
		case '4':return "0100";
		case '5':return "0101";
		case '6':return "0110";
		case '7':return "0111";
		case '8':return "1000";
		case '9':return "1001";
		case 'a':
		case 'A':return "1010";
		case 'b':
		case 'B':return "1011";
		case 'c':
		case 'C':return "1100";
		case 'd':
		case 'D':return "1101";
		case 'e':
		case 'E':return "1110";
		case 'f':
		case 'F':return "1111";
		default: return "0000";
		}
	}
	
	public static String Int2BinStr(int n) {
		String ret = "";
		for(char ch : Integer.toHexString(n).toCharArray()) {
			ret += HexChar2BinStr(ch);
		}
		return ret;
	}
	
	public static String AlignStr(String str, int len, char ch) {
		String ret = str;
		
		if (ret.length() > len)
			return ret.substring(ret.length() - len);
			
		
		for ( int i = ret.length(); i < len; i++ ) {
			ret = ch + ret;
		}
		
		return ret;
	}

	public static String AlignStrEnd(String str, int len, char ch) {
		String ret = str;
		
		for ( int i = ret.length(); i < len; i++ ) {
			ret += ch;
		}
		
		return ret;
	}

	public static String FormatStr(String str, int step ) {
		int i = (str.length())%step;
		if ( i == 0 ) i = step;
		String ret = str.substring(0,i);
		for ( ; i < str.length(); i+=step ) {
			ret += '.' + str.substring(i, i+step);
		}
		return ret;
	}


	public static bin MicroVertical2Horizontal(bin bin_cmd ) {
		bin ret = new bin(32);
		
		switch(bin_cmd.getBit(15))
		{
		case '0':{ // операционная команда
			ret.setBit(31, '0');
		
			switch(bin_cmd.getBit(14)) {
			case '0':{ // ОМК0 
				switch ((new bin(Character.toString(bin_cmd.getBit(13))+
								 Character.toString(bin_cmd.getBit(12)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(4,'1');break;
				case 2:ret.setBit(5,'1');break;
				case 3:ret.setBit(6,'1');break;
				}
				
				switch ((new bin(Character.toString(bin_cmd.getBit(9))+
						 		 Character.toString(bin_cmd.getBit(8)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(1,'1');break;
				case 2:ret.setBit(2,'1');break;
				case 3:ret.setBit(3,'1');break;
				}
				
				switch ((new bin(Character.toString(bin_cmd.getBit(7))+
				 		 		 Character.toString(bin_cmd.getBit(6)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(7,'1');break;
				case 2:ret.setBit(8,'1');break;
				case 3:break;
				}
				
				switch ((new bin(Character.toString(bin_cmd.getBit(5))+
								 Character.toString(bin_cmd.getBit(4)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(10,'1');break;
				case 2:ret.setBit(9,'1');break;
				case 3:break;
				}
				
				switch ((new bin(Character.toString(bin_cmd.getBit(3))+
								 Character.toString(bin_cmd.getBit(2)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(11,'1');break;
				case 2:ret.setBit(12,'1');break;
				case 3:break;
				}
				
				switch ((new bin(Character.toString(bin_cmd.getBit(1))+
								 Character.toString(bin_cmd.getBit(0)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(23,'1');break;
				case 2:ret.setBit(24,'1');break;
				case 3:break;
				}
				
				break; // ОМК0
			}
			case '1': { // ОМК1 
				//'Mid$(bin_cmd, 5, 4) ' - управление обменом с ВУ
				ret.setBit(28,bin_cmd.getBit(11));
				ret.setBit(27,bin_cmd.getBit(10));
				ret.setBit(26,bin_cmd.getBit(9));
				ret.setBit(25,bin_cmd.getBit(8));
				
				switch ((new bin(Character.toString(bin_cmd.getBit(7))+
								 Character.toString(bin_cmd.getBit(6)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(13,'1');break;
				case 2:ret.setBit(16,'1');break;
				case 3:ret.setBit(17,'1');break;
				}
				
				if ( bin_cmd.getBit(5) == '1' ) ret.setBit(14, '1');
				if ( bin_cmd.getBit(4) == '1' ) ret.setBit(15, '1');
				if ( bin_cmd.getBit(3) == '1' ) ret.setBit(0, '1');
	
				switch ((new bin(Character.toString(bin_cmd.getBit(2))+
						 		 Character.toString(bin_cmd.getBit(1))+
								 Character.toString(bin_cmd.getBit(0)))).toInt()) {
				case 0:break;
				case 1:ret.setBit(18,'1');break;
				case 2:ret.setBit(19,'1');break;
				case 3:ret.setBit(20,'1');break;
				case 4:ret.setBit(21,'1');break;
				case 5:ret.setBit(22,'1');break;
				case 6:break;
				case 7:
					ret.setBit(18,'1');
					ret.setBit(19,'1');
					ret.setBit(20,'1');
					ret.setBit(22,'1');
					break;
				}
				
				break; // ОМК1
			}
			}
			break; // операционная команда
		}
		case '1': { // управляющая команда
			ret.setBit(31, '1');
			ret.setBit(24, bin_cmd.getBit(14)); // поле сравнения
			
			switch ((new bin(Character.toString(bin_cmd.getBit(13))+
							 Character.toString(bin_cmd.getBit(12)))).toInt()) {
			case 0:ret.setBit(25,'1');break;
			case 1:ret.setBit(26,'1');break;
			case 2:ret.setBit(27,'1');break;
			case 3:ret.setBit(28,'1');break;
			}
	
			ret.setBit((new bin(Character.toString(bin_cmd.getBit(11))+
								Character.toString(bin_cmd.getBit(10))+
								Character.toString(bin_cmd.getBit(9))+
								Character.toString(bin_cmd.getBit(8)))).toInt(),'1'); // поле выбора бита
			
			ret.setBit((new bin(Character.toString(bin_cmd.getBit(11))+
					Character.toString(bin_cmd.getBit(10))+
					Character.toString(bin_cmd.getBit(9))+
					Character.toString(bin_cmd.getBit(8)))).toInt(),'1');

			//ret = Left$(ret, 8) & Mid$(bin_cmd, 9, 8) & Right$(ret, 16) 
			
			ret.setBit(23,bin_cmd.getBit(7));
			ret.setBit(22,bin_cmd.getBit(6));
			ret.setBit(21,bin_cmd.getBit(5));
			ret.setBit(20,bin_cmd.getBit(4));
			ret.setBit(19,bin_cmd.getBit(3));
			ret.setBit(18,bin_cmd.getBit(2));
			ret.setBit(17,bin_cmd.getBit(1));
			ret.setBit(16,bin_cmd.getBit(0));

	
			break; // управляющая команда
		}
		}
		
		return ret;
	}
}
