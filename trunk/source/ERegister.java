/*-----------------------------------------------------------------------------
  Регистр (битовая ячейка)
-----------------------------------------------------------------------------*/
public class ERegister implements IRegister
{
	ERegister(int width)
	{
		register_width = width;
		data = new boolean[width];
	}

	public boolean[] SendData()
	{
		return data;
	}
	public void GetData(boolean[] bits)
	{
		
	}
	
	int       register_width; // Разрядность 
	boolean[] data;           // Массив "битов"
}
