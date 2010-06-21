import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/*-----------------------------------------------------------------------------
				Компонент "Базовая ЭВМ". Вызывает методы отрисовки
							необходимых элементов.
-----------------------------------------------------------------------------*/	

public class EUIBasePC extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5587342628170024770L;
	public EUIBasePC (EUIBasePCFactory factory, EUIInputRegister input_register, JCheckBox movement_check, JCheckBox tact, JLabel work)
	{
		registers = factory.CreateClassicRegisters();
		channels = factory.CreateClassicChannels();
		memory = factory.CreateСlassicMemory();
		manager_device = factory.CreateManagerDevice();
		alu = factory.CreateClassicAlu();
		
		this.movement_check = movement_check;
		this.tact = tact;
		this.work = work;
		
		this.key_register = input_register;
	}
	
	public void paintComponent(Graphics g) 
	{	
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка каналов (сначала открытые)
		for (int i=0; i<channels.length; i++)
			if (channels[i].GetConnect() == false)
				channels[i].Draw(rs);
		for (int i=0; i<channels.length; i++)
			if (channels[i].GetConnect())
				channels[i].Draw(rs);
		
		//Отрисовка канала в устройство управления
		rs.setColor(Color.GRAY);
		rs.fillRect(713, 160, 40, 20);
		int[] mass1 = {703, 733, 763};		//Координаты x
		int[] mass2 = {180, 210, 180};		//Координаты у
		rs.fillPolygon(mass1, mass2, 3);
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
			registers[i].Draw(rs);
		
		//Отрисовка клавишного регистра
		key_register.Draw(rs);
		key_register.DrawPointer(rs);
		
		//Отрисовка номера бита
		rs.setFont(new Font("Courier New", Font.BOLD, 32));
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect(274, 465, 48, 50);
		rs.setColor(Color.BLACK);
		rs.drawRect(274, 465, 48, 50);
		int bit_number = 15 - key_register.GetPointerPosition();
		if(bit_number >= 10)
			rs.drawString("" + bit_number, 278, 500);
		else
			rs.drawString("" + bit_number, 287, 500);

		alu.Draw(rs);				//Отрисовка АЛУ
		memory.Draw(rs);			//Отрисовка Памяти
		manager_device.Draw(rs);	//Отрисовка Устройства Управления
		
		//Добавление чекбоксов и рамок
		add(movement_check);		//Проверка сдвига
		add(tact);					//"Такт"
		rs.drawRect(1, 436, 301, 20);
		rs.drawRect(329, 465, 101, 50);
		
		work.setLocation(437, 465);
		add(work);
	}
	
	
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIInputRegister			key_register;				//Клавишный регистр
	private EUIChannel[] 				channels;					//Массив каналов
	private	EUIMemory					memory;						//Память
	private EUIManagerDevice			manager_device;				//Устройство Управления
	private	EUIAlu						alu;						//АЛУ
	private JCheckBox					movement_check;				//Чекбокс установки сдвига указателя
	private JCheckBox					tact;						//Чекбокс режима "Такт"
	private JLabel						work;						//Лэйбл "Работа/Остановка"
}