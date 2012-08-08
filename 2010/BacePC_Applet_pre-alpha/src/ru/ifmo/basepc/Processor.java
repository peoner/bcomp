package ru.ifmo.basepc;

public class Processor {
	public Processor(int[] A, int[] CK, int[] KR, int[] RA, int[] RC, int[] RD, int[] RK, int[] Memory, int[][] MemoryMK)
	{
		this.A = A;
		this.CK = CK;
		this.KR = KR;
		this.RA = RA;
		this.RC = RC;
		this.RD = RD;
		this.RK = RK;
		this.Memory = Memory;
		this.MemoryMK = MemoryMK;

               

		if (GetParam(0, A.length, A) == 0) { Z[0] = 1; RC[11] = 1; }
		else if (A[0] == 1) { N[0] = 1; RC[10] = 1; }
	}

        public Processor()
        {
            A=new int[16];//{1,0,1,1,0,1,0,0,1,0,1,1,0,1,1,1};
            CK = new int[11];
            KR = new int[16];
            RA = new int[11];
            RC = new int[13];
            RD = new int[16];
            RK = new int[16];
            Memory = new int[2048];
            MemoryMK = new int[256][16];

            ChMK=1;

            MicroCommands MCs=new MicroCommands();
            int j;
             for (int i = 0; i < MCs.MKValues.length; ++i)
		{
			int[] MicroCommand = ConvertToBit((MCs.MKValues[i]), 0);
			for (j = 0; j < 16; ++j)
                            MemoryMK[i][j] = MicroCommand[j];
		}

            if (GetParam(0, A.length, A) == 0) { Z[0] = 1; RC[11] = 1; }
            else if (A[0] == 1) { N[0] = 1; RC[10] = 1; }
            else{ RC[11] = 0;Z[0] = 0; N[0] = 0; RC[10] = 0;}
        }

	public static int GetParam(int x0, int x1, int[] binaryMK)
	{
		String s_param = new String();
		for (int i = x0; i < x1; ++i)
			s_param += (binaryMK[i]);
		return Integer.parseInt(s_param, 2);
	}
	public static int[] GetParam(int[] reg0, int[] reg1, int[] reg2, int[] reg3,
		int x0, int x1, int[] binaryMK)
	{
		return binaryMK[x0] * binaryMK[x1] == 1 ? 
                    reg0 :
                    binaryMK[x0] + binaryMK[x1] == 0 ?
		reg1:
                        binaryMK[x0] == 1 ?
                            reg2 :
                            reg3;
	}
	public static int[] MinimazeSumm(int[] i_right, int iright, int length, int dopl1, int dopl2, int dopl3)
	{
		if (length == 16)
			return iright < dopl1 ? ConvertToBit("000" + Integer.toString(iright, 16), 0) :
			iright < dopl2 ? ConvertToBit("00" + Integer.toString(iright, 16), 0) :
			iright < dopl3 ?ConvertToBit("0" + Integer.toString(iright, 16), 0):
			ConvertToBit(Integer.toString(iright, 16), 0);

		else if (length < 16)
		{
			return length < dopl2 ? ConvertToBit("00" + Integer.toString(iright, 16), 0) :
				length < dopl3 ? ConvertToBit("0" + Integer.toString(iright, 16), 0) :
				ConvertToBit(Integer.toString(iright, 16), 0);
				}
		else if (length > 16) return ConvertToBit(Integer.toString(iright, 16), 3);
		return null;
	}
	public static String MinimazeSumm(String s_RMK){
	return s_RMK.length() < 2?"000" + s_RMK:s_RMK.length() < 3?
		"00" + s_RMK:s_RMK.length() < 4?"0" + s_RMK:s_RMK;
	}

        public static String MinimazeSumm(String s_RMK, int length)
        {

            /*return s_RMK.length() < 2?"000" + s_RMK:s_RMK.length() < 3?
		"00" + s_RMK:s_RMK.length() < 4?"0" + s_RMK:s_RMK;
	*/
            String result=s_RMK;
            int resultLength=result.length();
            for(int i=length; i>resultLength; i--)
            result="0"+result;
            return result;
        }

	
	public static class ALY { 
		public ALY(int[] left, int[] right) {
			this.right = right;
			this.left = left;
		}
		
	    public int[] SummRegister(boolean ifOpen) 
		{
			int summ = Processor.GetParam(0, left.length, left) + Processor.GetParam(0, right.length, right);

			if (ifOpen) ++summ;
			char[] ch_summ = Integer.toBinaryString(summ).toCharArray();
			int max = right.length < left.length ? left.length : right.length;
			if (ch_summ.length == max + 1)
				ALY.bufregister[0] = 1;
			int[] i_summ = new int[16];
			for (int i = ch_summ.length - 1, j = i_summ.length - 1; i > -1 && j > -1; --i, --j)
				i_summ[j] = Integer.parseInt(String.valueOf(ch_summ[i]));
			return i_summ;
		}
		public void ReturnCode(int[] register)
		{
			for (int i = 0; i < register.length; ++i)
			{
				if (register[i] == 0)
					register[i] = 1;
				else if (register[i] == 1)
					register[i] = 0;
			}
		}
		int[] right, left;
		public static int[] bufregister = new int[17];
	}
	public static class YY
	{
		public static void DecodeOMK0(int[] binaryMK, int[] mem)
		{
			int n = 0;
			ALY alu = new ALY(GetParam(KR, null, RC, A, 2, 3, binaryMK), GetParam(CK, null, RK, RD, 6, 7, binaryMK));

			if (alu.left != null && alu.right != null)
				n = alu.left.length > alu.right.length ? alu.left.length : alu.right.length;
			else if (alu.left == null && alu.right != null)
				n = alu.right.length;
			else if (alu.right == null && alu.left != null)
				n = alu.left.length;

			int[] result = new int[n];

			if (binaryMK[9] == 1 && binaryMK[8] == 0)
			{
				if(alu.left!=null)alu.ReturnCode(alu.left);
				else alu.ReturnCode(alu.right);
			}
			else if (binaryMK[8] == 1 && binaryMK[9] == 0)
			{
				if (alu.right != null) alu.ReturnCode(alu.right);
				else alu.ReturnCode(alu.left);
			}
			if (binaryMK[10] == 0 && binaryMK[11] == 0)
			{
				if (alu.left != null && alu.right != null)
					result = alu.SummRegister(false);
				else if (alu.left == null && alu.right != null)
					result = alu.right;
				else if (alu.left != null && alu.right == null)
					result = alu.left;
				
				if (binaryMK[12] == 1 || binaryMK[13] == 1)
					result = A;
			}
			else if (binaryMK[10] == 0 && binaryMK[11] == 1)
			{
				if (alu.left != null && alu.right != null)
					result = alu.SummRegister(true);
				else if (alu.left == null && alu.right != null)
				{
					int iright = GetParam(0, alu.right.length, alu.right);
					++iright;
					result = MinimazeSumm(alu.right, iright, alu.right.length, 16 , 16*16, 16*16*16);
					}
					else if (alu.left != null && alu.right == null)
				{
					int ileft = GetParam(0, alu.left.length, alu.left);
					++ileft;
					result = MinimazeSumm(alu.left, ileft, alu.left.length, 16, 16*16 , 16*16*16 );
					}
			}
			else if (binaryMK[10] == 1 && binaryMK[11] == 0){
				for (int i = 0; i < 16; ++i){
					if (alu.left != null && alu.right != null)
					{
						result[i] = alu.left[i] * alu.right[i]; 
					}
					else if (alu.left == null && alu.right == null)
						for (int j = 0; j < alu.bufregister.length; ++j) alu.bufregister[j] = 0;
					}
					}

			if (binaryMK[12] == 0 && binaryMK[13] == 1)
			{
				for (int i = 0; i < 15; ++i)
					alu.bufregister[i + 1] = result[i];
				alu.bufregister[0] = result[15];
				alu.bufregister[16] = result[14];
			}
			else if (binaryMK[12] == 1 && binaryMK[13] == 0)
			{
				for (int i = 0; i < 15; ++i)
					alu.bufregister[i] = result[i];
				alu.bufregister[16] = result[0]; 
				alu.bufregister[15] = result[15];
			}
			else
				for (int i = 15; i > 15 - result.length; --i)
					alu.bufregister[i + 1] = 
						result[result.length + i - 15 - 1];
			if (binaryMK[14] == 1 && binaryMK[15] == 0)
			{
				int i_RD = GetParam(0, 16, RD);
				int i_RA = GetParam(0, 11, RA);
				mem[i_RA] = i_RD;

			}
			else if (binaryMK[15] == 1 && binaryMK[14] == 0)
			{
				int i_RA = GetParam(0, 11, RA);
				int i_Mem =mem[i_RA];
	//			System.Console.WriteLine(Integer.toHexString(i_Mem).ToUpper() + "->��");
				RD = MinimazeSumm(mem, i_Mem, 16, 16, 16*16, 16*16*16);
			}

				int i_result = GetParam(0, 17, alu.bufregister);
//				System.Console.WriteLine(Integer.toHexString(i_result).ToUpper() + "->��");
		}
		public static void ExitBR(int[] register, int what/*���, ��� � ���*/)
		{
			for (int i = register.length - 1; i > -1; --i)
				register[i] = what == 1 ? ALY.bufregister[i + 6] : ALY.bufregister[i + 1];
		}
		public static void DecodeOMK1(int[] binaryMK)
		{
			int exitBR = GetParam(13, 16, binaryMK);
			switch (exitBR)
			{
				case 1: ExitBR(RA, 1); break;
				case 2: ExitBR(RD, 0); break;
				case 3: ExitBR(RK, 0); break;
				case 4: ExitBR(CK, 1); break;
				case 5: ExitBR(A, 0); break;
				case 6: break;
				case 7: ExitBR(RA, 1); ExitBR(RK, 0); ExitBR(A, 0); ExitBR(RD, 0); break;
				default: break;
			} 
			if (binaryMK[8] == 0 && binaryMK[9] == 1)
			{
				if (ALY.bufregister[0] == 1) { C[0] = 1; RC[12] = 1; }
			}
			if (binaryMK[8] == 1 && binaryMK[9] == 0) 
			{ C[0] = 0;  RC[12] = 0; } 
			if (binaryMK[8] == 1 && binaryMK[9] == 1) 
			{ C[0] = 1; RC[12] = 1; }
			if (binaryMK[10] == 1)
			{
				if (A[0] == 1) { N[0] = 1; RC[10] = 1; }
                                else{
                                    N[0] = 0; RC[10] = 0;}
			}
			if (binaryMK[11] == 1) { 
				int Ak = GetParam(0, 16, A);
                                if (Ak == 0)
			{ Z[0] = 1; RC[11] = 1; }
                                else {
                                    Z[0] = 0; RC[11] = 0;} }
			if (binaryMK[12] == 1) HLT = true;

		}
		public static void DecodeOMK(int[] binaryMK, int[] mem)
		{
			if (binaryMK[1] == 0) DecodeOMK0(binaryMK, mem);
			else DecodeOMK1(binaryMK);
			++ChMK;
		}
		public static void DecodeUMK(int[] binaryMK)
		{
			int[] register = new int[16];
			register = GetParam(A, RC, RK, RD, 2, 3, binaryMK);
			for (int i = register.length - 1; i > -1; --i)
			{
				if (register.length == 16) ALY.bufregister[i + 1] = register[i];
				else if (register.length == 13)
				{
					ALY.bufregister[i + 4] = register[i];
					for (int j = 0; j < 4; ++j) ALY.bufregister[i] = 0;
				}
				else if (register.length == 11)
				{
					ALY.bufregister[i + 6] = register[i];
					for (int j = 0; j < 6; ++j) ALY.bufregister[i] = 0;
				}
			}
			int numbBit = GetParam(4, 8, binaryMK);
			int addrPereh = GetParam(8, 16, binaryMK);

		//	System.Console.WriteLine(Integer.toHexString(GetParam(0, 17, ALY.bufregister)).ToUpper() + "->��");
			
			if (register[register.length - numbBit - 1] == binaryMK[1])
			{
//				System.Console.WriteLine(Integer.toHexString(addrPereh).ToUpper() + "->C���");
				ChMK = addrPereh;
			}
			else
				++ChMK;
		}
		
		
	}
	public static void DecodeMK(int[] i_binaryMK, int[] mem)
	{
		if (i_binaryMK[0] == 0)
			YY.DecodeOMK(i_binaryMK, mem);
		else
			YY.DecodeUMK(i_binaryMK);
	}
	public static int ChMK = 1;
	static public boolean HLT = false;
	static public int[] A, CK, RA, RC, RD, RK, KR,/* bufreg, */N = new int[1], Z = new int[1], C = new int[1], Memory;
	static int[][] MemoryMK;

        public static int[] ConvertToBit(String buffer, int what)
	{
		int mk = 0; int n = 0;

		char[] ch_buffer = buffer.toCharArray();
		int length = what == 0 ? 16 : what == 1 ? 12 : 17;//17 ��� 3

		int[] i_binaryMK = new int[length];

		for (int z = 0; z < ch_buffer.length; ++z)
		{

			mk = Integer.parseInt(String.valueOf(ch_buffer[z]), 16); //16 - ��� ����� � �����������������
			char[] ch_binaryMK = new char[4];
			char[] ch_binaryMK_k = Integer.toBinaryString(mk).toCharArray();
			for (int q = 3, q1 = 0; q > -1 && q1 < ch_binaryMK_k.length; --q, ++q1)
			{
				ch_binaryMK[q] = ch_binaryMK_k[ch_binaryMK_k.length - q1 - 1];
			}
			for (int i = n, j = 0; (i < 4 + n) && (j < 4); ++i, ++j)
			{
				if (ch_binaryMK[j] != 0) i_binaryMK[i] =
                                        Integer.parseInt(String.valueOf(ch_binaryMK[j]));
				else i_binaryMK[i] = 0;
			}
			n += 4;

		}
		return i_binaryMK;
	}


}

/*
public class Program
{
	public static int[] ConvertToBit(String buffer, int what)
	{
		int mk = 0; int n = 0;

		char[] ch_buffer = buffer.ToCharArray();
		int length = what == 0 ? 16 : what == 1 ? 12 : 17;//17 ��� 3

		int[] i_binaryMK = new int[length];

		for (int z = 0; z < ch_buffer.length; ++z)
		{
			mk = System.Convert.ToInt32(Integer.toString(ch_buffer[z]), 16); //16 - ��� ����� � �����������������
			char[] ch_binaryMK = new char[4];
			char[] ch_binaryMK_k = Integer.toBinaryString(mk).toCharArray();
			for (int q = 3, q1 = 0; q > -1 && q1 < ch_binaryMK_k.length; --q, ++q1)
			{
				ch_binaryMK[q] = ch_binaryMK_k[ch_binaryMK_k.length - q1 - 1];
			}
			for (int i = n, j = 0; (i < 4 + n) && (j < 4); ++i, ++j)
			{
				if (ch_binaryMK[j] != 0) i_binaryMK[i] = System.Convert.ToInt32(ch_binaryMK[j]) - 48;
				else i_binaryMK[i] = 0;
			}
			n += 4;

		}
		return i_binaryMK;
	}

	public static Processor Read()
	{
		int[] bufreg = new int[17],A= new int[16],RA = new int[11], 
			RD = new int[16], RK = new int[16], CK = new int[11],
		Memory = new int[2048], KR = new int[8], RC = new int[13];
		
		int[][] MemoryMK = new int[256][16]; 
		
		String path = "C:\\Users\\User\\Desktop\\BasePCLogicV0\\MicroCommands.txt";
		String buf = System.IO.File.ReadAllText(path);
		String s_plit[] = buf.Split(new char[] { '\n' });
		String[][] mas = new String[s_plit.length][];
		mas[3] = s_plit[3].Split(new char[] { '|' });

		RD = ConvertToBit(mas[3][0], 0);
		int[] long_RA = ConvertToBit(mas[3][1], 1);
		for (int i = RA.length - 1; i > -1; --i) RA[i] = long_RA[i + 1];

		int[] long_CK = ConvertToBit(mas[3][2], 1);
		for (int i = CK.length - 1; i > -1; --i)
			CK[i] = long_CK[i + 1];
		A = ConvertToBit(mas[3][3], 0);
		RK = ConvertToBit(mas[3][4].Trim(new char[] { '\r' }), 0);

		for (int i = 4; i < 7; ++i)
		{
			mas[i] = s_plit[i].Split(new char[] { '|' });
			int AddrInMem = System.Convert.ToInt32(Integer.toString(mas[i][0]), 16);
			int operand = System.Convert.ToInt32(Integer.toString(mas[i][1].Trim(new char[] { '\r' })), 16);
			Memory[AddrInMem] = operand;
		}

		for (int i = 7; i < s_plit.length; ++i)
		{
			mas[i] = s_plit[i].Split(new char[] { '\t' });
			int[] MicroCommand = ConvertToBit(Integer.toString(mas[i][0].Trim(new char[] { '\r' })), 0);
			for (int j = 0; j < 16; ++j) MemoryMK[i - 6][j] = MicroCommand[j];
		}
		return new Processor(A, CK, KR, RA, RC, RD, RK,  Memory, MemoryMK);
	}
	public static void main(String[] args)
	{
		Processor processor = Read();
		System.IO.StreamWriter writer = new System.IO.StreamWriter("C:\\Users\\User\\Desktop\\BasePCLogicV0\\Trass.txt");

		do
		{
			int[] RMK = processor.MemoryMK[processor.ChMK]; int i_RMK = Processor.GetParam(0, RMK.length, RMK);
			
			String s_RMK = Integer.toHexString(i_RMK).ToUpper();
			s_RMK = Processor.MinimazeSumm(s_RMK);
			Processor.DecodeMK(processor.MemoryMK[processor.ChMK], processor.Memory);

			int i_BR = processor.GetParam(0, 17, Processor.ALY.bufregister);
			int i_MK = processor.GetParam(0, 16, processor.MemoryMK[processor.ChMK]),
				i_CK = processor.GetParam(0, 11, processor.CK),
				i_RA = processor.GetParam(0, 11, processor.RA),
				i_RD = processor.GetParam(0, 16, processor.RD),
				i_A = processor.GetParam(0, 16, processor.A),
				i_RK = processor.GetParam(0, 16, processor.RK);
            String s_RD =  Processor.MinimazeSumm(Integer.toHexString(i_RD).ToUpper());
			
			String s_RK = Processor.MinimazeSumm(Integer.toHexString(i_RK).ToUpper());
			
			String s_A = Processor.MinimazeSumm(Integer.toHexString(i_A).ToUpper());
			
			String s_CK = Integer.toHexString(i_CK).ToUpper();
			if (s_CK.length() < 2) s_CK = "00" + s_CK;
			else if (s_CK.length() < 3) s_CK = "0" + s_CK;

			String s_RA = Integer.toHexString(i_RA).ToUpper();
			if (s_RA.length() < 2) s_RA = "00" + s_RA;
			else if (s_RA.length() < 3) s_RA = "0" + s_RA;

			String s_ChMK = Integer.toHexString(processor.ChMK).ToUpper();
			if (s_ChMK.length() < 2) s_ChMK = "0" + s_ChMK;

			writer.WriteLine("��� " + s_RMK + "   |�� " + s_CK + "   |�� " + s_RA +
				"   |�� " + s_RK + "   |�� " + s_RD + "   |� " + s_A + "   |���� " + s_ChMK
				+ "   |�� " + Integer.toHexString(i_BR).ToUpper());

		} while (processor.ChMK != 1);
		writer.Close();
	}
}
*/