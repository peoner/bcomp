package ru.ifmo.ipm.bevm;

import java.awt.*;
import java.awt.event.*;

//import java.util.*;

public class Processor extends Panel {

	public interface DeviceListener {
		public void OnDevice1Ready();

		public void OnDevice2Ready();

		public void OnDevice3Ready();
	}

	public Panel pnlMK;

	DeviceListener devListener;

	Main parent;
	MouseListener currListener;

	Label lblToolTip;
	MyList lstState;

	MyList lstMemView;
	MyList lstMicroMemView;

	Register regData;
	Register regAddress;
	Register regCommand;
	Register regCommandCounter;
	Register regBuffer;
	Register regAccumulator;

	Register regMicroCommand;
	Register regMicroCounter;

	Register regState;
	InputRegister regKey;

	TextField txtZ;
	TextField txtN;
	TextField txtC;

	Panel pnlSetModeBase;
	Panel pnlSetModeExtDevices;
	Panel pnlSetModeMicroProgram;

	Panel pnlExtDevices;
	Panel pnlExtDevicesContent;

	Panel pnlMicroProgram;
	Panel pnlMicroProgramContent;

	Checkbox chkDev1Check;
	Checkbox chkDev2Check;
	Checkbox chkDev3Check;

	Register regDev1;
	Register regDev2;
	Register regDev3;

	public void initModExtDevices() {
		Panel pnl;
		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(100, 0, 490, 410);
		(pnlExtDevices = pnl).setVisible(false);
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 488, 408);
		pnlExtDevicesContent = pnl;
		pnl.setBackground(Color.white);

		pnl.add(new Picture(this, parent.image("2extdevs.png"), 180, 10, 330,
				380));

		regDev1 = new InputRegister(pnl, "�� ��1", 200, 300, 8);
		regDev2 = new InputRegister(pnl, "�� ��2", 300, 300, 8);
		regDev3 = new InputRegister(pnl, "�� ��3", 400, 300, 8);

		pnl.add(chkDev1Check = new Checkbox("", false));
		chkDev1Check.setBounds(231, 190, 12, 15);
		chkDev1Check.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (chkDev1Check.getState())
					if (devListener != null)
						devListener.OnDevice1Ready();
			}
		});
		pnl.add(chkDev2Check = new Checkbox("", false));
		chkDev2Check.setBounds(331, 190, 12, 15);
		chkDev2Check.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (chkDev2Check.getState())
					if (devListener != null)
						devListener.OnDevice2Ready();
			}
		});
		pnl.add(chkDev3Check = new Checkbox("", false));
		chkDev3Check.setBounds(431, 190, 12, 15);
		chkDev3Check.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (chkDev3Check.getState())
					if (devListener != null)
						devListener.OnDevice3Ready();
			}
		});
	}

	public void initModMicroProgram() {
		Panel pnl;
		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(100, 0, 490, 410);
		(pnlMicroProgram = pnl).setVisible(false);
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 488, 408);
		pnlMicroProgramContent = pnl;
		pnl.setBackground(Color.white);

		pnl.add(new Picture(this, parent.image("2mp.png"), 180, 10, 32, 380));
		pnl.add(new Picture(this, parent.image("2uu.png"), 250, 10, 200, 32));
		pnl.add(new Picture(this, parent.image("2decoder.png"), 252, 200, 130,
				32));

		regMicroCommand = new Register(pnl, "РМК", 250, 60, 16);
		regMicroCounter = new Register(pnl, "СчМК", 250, 320, 8);

		pnl.add(new Picture(pnl, parent.image("l-v.png"), 340, 99, 36, 70));
		pnl.add(new Picture(pnl, parent.image("arr-d.png"), 340, 164, 36, 36));

		pnl.add(new Picture(pnl, parent.image("l-v.png"), 340, 232, 36, 70));
		pnl.add(new Picture(pnl, parent.image("arr-d.png"), 340, 284, 36, 36));

		pnl.add(new Picture(pnl, parent.image("arr-l.png"), 180, 280, 36, 36));
		pnl.add(new Picture(pnl, parent.image("l-h.png"), 200, 280, 100, 36));
		pnl.add(new Picture(pnl, parent.image("corner-u-l.png"), 300, 280, 36,
				36));
		pnl.add(new Picture(pnl, parent.image("l-v.png"), 300, 232, 36, 50));

		pnl.add(new Picture(pnl, parent.image("arr-l.png"), 180, 240, 36, 36));
		pnl.add(new Picture(pnl, parent.image("l-h.png"), 200, 240, 60, 36));
		pnl.add(new Picture(pnl, parent.image("corner-u-l.png"), 260, 240, 36,
				36));
		pnl.add(new Picture(pnl, parent.image("l-v.png"), 260, 232, 36, 20));

		pnl.add(new Picture(pnl, parent.image("arr-l.png"), 388, 66, 36, 36));
		pnl.add(new Picture(pnl, parent.image("l-h.png"), 410, 66, 14, 36));

		MyList lv = new MyList(19);
		pnl.add(lv);
		lv.setBounds(425, 60, 60, 300);
		lstMicroMemView = lv;

		Label lbl;
		pnl.add(lbl = new Label("������ ��", Label.RIGHT));
		lbl.setBounds(420, 45, 65, 10);

		regState = new Register(pnl, "Регистр состояния", "U.XAKP.WFIE.0NZC",
				20, 320, 13);

		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(15, 250, 152, 60);
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 150, 58);
		pnl.setBackground(Color.white);
		regBuffer = new Register(pnl, "БР", 3, 16, 17);

		pnl.add(lbl = new Label("���", Label.CENTER));
		lbl.setBounds(1, 2, 150, 10);
	}

	public void setModeBase() {
		pnlSetModeBase.setLocation(-1, 360);
		pnlSetModeExtDevices.setLocation(-10, 400);
		pnlSetModeMicroProgram.setLocation(-10, 440);
		pnlExtDevices.setVisible(false);
		pnlMicroProgram.setVisible(false);

		add(regAddress);
		regAddress.setLocation(152, 0);
		add(regData);
		regData.setLocation(152, 114);
		add(regCommand);
		regCommand.setLocation(152, 164);
		add(regCommandCounter);
		regCommandCounter.setLocation(152, 214);
	}

	public void setModeExtDevices() {
		pnlSetModeBase.setLocation(-10, 360);
		pnlSetModeExtDevices.setLocation(-1, 400);
		pnlSetModeMicroProgram.setLocation(-10, 440);
		pnlExtDevices.setVisible(true);
		pnlMicroProgram.setVisible(false);

		pnlExtDevicesContent.add(regData);
		regData.setLocation(10, 80);
		pnlExtDevicesContent.add(regAddress);
		regAddress.setLocation(10, 140);
		pnlExtDevicesContent.add(regCommandCounter);
		regCommandCounter.setLocation(10, 200);
		pnlExtDevicesContent.add(regCommand);
		regCommand.setLocation(10, 260);
	}

	public void setModeMicroProgram() {
		pnlSetModeBase.setLocation(-10, 360);
		pnlSetModeExtDevices.setLocation(-10, 400);
		pnlSetModeMicroProgram.setLocation(-1, 440);
		pnlExtDevices.setVisible(false);
		pnlMicroProgram.setVisible(true);

		pnlMicroProgramContent.add(regData);
		regData.setLocation(20, 40);
		pnlMicroProgramContent.add(regAddress);
		regAddress.setLocation(20, 90);
		pnlMicroProgramContent.add(regCommandCounter);
		regCommandCounter.setLocation(20, 140);
		pnlMicroProgramContent.add(regCommand);
		regCommand.setLocation(20, 190);
	}

	public Processor(Main parent, DeviceListener devListener) {
		this.setLayout(null);
		this.setSize(600, 500);
		this.parent = parent;

		this.devListener = devListener;

		Panel pnl;
		Label lbl;

		initModExtDevices();
		initModMicroProgram();

		MouseListener listener1 = new MouseListener() {
			public void mouseEntered(MouseEvent ev) {
			}

			public void mouseExited(MouseEvent ev) {
			}

			public void mouseReleased(MouseEvent ev) {
			}

			public void mousePressed(MouseEvent ev) {
			}

			public void mouseClicked(MouseEvent ev) {
				setModeBase();
			}
		}, listener2 = new MouseListener() {
			public void mouseEntered(MouseEvent ev) {
			}

			public void mouseExited(MouseEvent ev) {
			}

			public void mouseReleased(MouseEvent ev) {
			}

			public void mousePressed(MouseEvent ev) {
			}

			public void mouseClicked(MouseEvent ev) {
				setModeExtDevices();
			}
		}, listener3 = new MouseListener() {
			public void mouseEntered(MouseEvent ev) {
			}

			public void mouseExited(MouseEvent ev) {
			}

			public void mouseReleased(MouseEvent ev) {
			}

			public void mousePressed(MouseEvent ev) {
			}

			public void mouseClicked(MouseEvent ev) {
				setModeMicroProgram();
			}
		};

		Functor<Container> setListener = new Functor<Container>() {
			public void F(Container arg) {
				for (Component comp : arg.getComponents()) {
					if (comp.getClass().equals(Panel.class))
						F((Container) comp);
					else
						comp.addMouseListener(currListener);
				}
			}
		};

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(-1, 360, 70, 35);
		pnlSetModeBase = pnl;
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 68, 33);
		pnl.setBackground(Color.orange);
		pnl.add(lbl = new Label("�������", Label.CENTER));
		lbl.setBounds(5, 5, 70, 10);
		pnl.add(lbl = new Label("���", Label.CENTER));
		lbl.setBounds(5, 17, 70, 10);
		currListener = listener1;
		setListener.F(pnl);

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(-10, 400, 70, 35);
		pnlSetModeExtDevices = pnl;
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 68, 33);
		pnl.setBackground(Color.orange);
		pnl.add(lbl = new Label("������", Label.CENTER));
		lbl.setBounds(5, 5, 70, 10);
		pnl.add(lbl = new Label("� ��", Label.CENTER));
		lbl.setBounds(5, 17, 70, 10);
		currListener = listener2;
		setListener.F(pnl);

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(-10, 440, 70, 35);
		pnlSetModeMicroProgram = pnl;
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 68, 33);
		pnl.setBackground(Color.orange);
		pnl.add(lbl = new Label("������", Label.CENTER));
		lbl.setBounds(5, 5, 70, 10);
		pnl.add(lbl = new Label("� ���", Label.CENTER));
		lbl.setBounds(5, 17, 70, 10);
		currListener = listener3;
		setListener.F(pnl);

		// [������]

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(5, 5, 76, 329);
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 74, 327);
		pnl.setBackground(Color.pink);
		pnl.add(lbl = new Label("������", Label.LEFT));
		lbl.setBounds(2, 4, 45, 10);
		MyList lv = new MyList(20);
		pnl.add(lv);
		lv.setBounds(2, 20, 70, 305);
		lstMemView = lv;

		// ��������

		// arr1
		add(new Picture(this, parent.image("arr-l.png"), 81, 6, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 100, 6, 47, 36));

		// arr2
		add(new Picture(this, parent.image("l-h.png"), 81, 42, 160, 36));
		add(new Picture(this, parent.image("corner-l-d.png"), 232, 42, 36, 36));
		add(new Picture(this, parent.image("arr-d.png"), 232, 78, 36, 36));

		// arr3
		add(new Picture(this, parent.image("arr-l.png"), 81, 78, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 100, 78, 74, 36));
		add(new Picture(this, parent.image("corner-l-d.png"), 172, 78, 36, 36));

		add(new Picture(this, parent.image("corner-d-r.png"), 80, 120, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 116, 120, 31, 36));
		add(new Picture(this, parent.image("l-h.png"), 104, 168, 42, 36));
		add(new Picture(this, parent.image("l-h.png"), 104, 216, 42, 36));
		add(new Picture(this, parent.image("l-v.png"), 80, 156, 36, 109));
		add(new Picture(this, parent.image("corner-u-r.png"), 80, 252, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 116, 252, 137, 36));
		add(new Picture(this, parent.image("corner-l-d.png"), 252, 252, 36, 36));
		// add(new Picture(this, parent.image("l-v.png"), 252, 288, 36, 37));

		add(new Picture(this, parent.image("corner-l-d.png"), 162, 270, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 110, 270, 77, 36));
		add(new Picture(this, parent.image("corner-d-r.png"), 80, 270, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 104, 435, 28, 36));
		add(new Picture(this, parent.image("l-v.png"), 80, 306, 36, 160));
		add(new Picture(this, parent.image("corner-u-r.png"), 80, 465, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 106, 465, 350, 36));
		add(new Picture(this, parent.image("corner-u-l.png"), 450, 465, 36, 36));

		add(new Picture(this, parent.image("l-h.png"), 229, 385, 129, 36));
		add(new Picture(this, parent.image("2alu.png"), 160, 302, 128, 128));
		add(new Picture(this, parent.image("arr-d.png"), 252, 288, 36, 36));
		add(new Picture(this, parent.image("arr-d.png"), 162, 288, 36, 36));
		add(new Picture(this, parent.image("arr-d.png"), 205, 390, 36, 36));

		add(new Picture(this, parent.image("arr-l.png"), 290, 432, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 308, 432, 50, 36));
		add(new Picture(this, parent.image("corner-u-l.png"), 345, 432, 36, 36));

		add(new Picture(this, parent.image("arr-l.png"), 290, 120, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 308, 120, 50, 36));
		add(new Picture(this, parent.image("arr-l.png"), 290, 168, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 308, 168, 50, 36));
		add(new Picture(this, parent.image("arr-l.png"), 257, 216, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 275, 216, 83, 36));
		add(new Picture(this, parent.image("l-v.png"), 345, 29, 36, 410));
		add(new Picture(this, parent.image("arr-l.png"), 257, 6, 36, 36));
		add(new Picture(this, parent.image("l-h.png"), 265, 6, 100, 36));
		add(new Picture(this, parent.image("corner-l-d.png"), 345, 6, 36, 36));

		add(lblToolTip = new Label("", Label.RIGHT));
		// lblToolTip.setBounds(0, getHeight()-15, 800, 15);
		lblToolTip.setBounds(303, 630, 424, 15);
		/*
		 * add(pnl = new Panel());pnl.setLayout(null);pnl.setBounds(635, 295,
		 * 85, 200); pnl.setBackground(Color.black); pnl.add(pnl = new
		 * Panel());pnl.setLayout(null);pnl.setBounds(1, 1, 83, 198);
		 * pnl.setBackground(new Color(0xF0F0F0));
		 */
		Image lgActive = parent.image("lv-g.png");
		Image lgInactive = parent.image("lv.png");

		LogicGate lg;
		Color c = new Color(0xC0FFFF);

		regAddress = new Register(this, "Регистра Адреса", 152, 0, 12);
		regData = new Register(this, "Регистр Данных", 152, 114, 16);
		regCommand = new Register(this, "Регистр Комманд", 152, 164, 16); // -2
		regCommandCounter = new Register(this, "Счётчик Комманд", 152, 214, 12); // -4
		regAccumulator = new Register(this, "Аккумулятор", 152, 426, 16);

		add(txtZ = new TextField());
		txtZ.setBounds(366, 549, 15, 20);
		txtZ.setEditable(false);
		txtZ.setText("0");
		txtZ.setEnabled(false);
		add(txtN = new TextField());
		txtN.setBounds(318, 549, 15, 20);
		txtN.setEditable(false);
		txtN.setText("0");
		txtN.setEnabled(false);
		// add(txtC = new TextField());txtC.setBounds(72, 549, 15,
		// 20);txtC.setEditable(false);txtC.setText("0");
		add(txtC = new TextField());
		txtC.setBounds(134, 443, 15, 20);
		txtC.setEditable(false);
		txtC.setText("0");
		txtC.setEnabled(false);
		add(lbl = new Label("C"));
		lbl.setBounds(134, 423, 20, 20);

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(380, 185, 212, 227);
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 210, 225);
		pnl.setBackground(new Color(0xF0F0F0));
		pnl.add(lbl = new Label("���������� ����������", Label.LEFT));
		lbl.setBounds(2, 2, 140, 15);
		pnl.add(lstState = new MyList(13));
		lstState.setBounds(5, 20, 200, 200);
		updateStateList();

		regKey = new InputRegister(this, "��������� �������", 400, 424, 16);

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(375, 440, 22, 22);
		pnl.setBackground(Color.black);
		pnlMK = pnl;
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 20, 20);
		pnl.setBackground(new Color(0xF0F0F0));
		pnl.add(lbl = new Label("��", Label.LEFT));
		lbl.setBounds(0, 1, 20, 20);
		pnlMK.setVisible(false);

		(new Functor<Container>() {
			public void F(Container arg) {
				for (Component comp : arg.getComponents()) {
					if (comp.getClass().equals(InputRegister.class))
						return;
					if (comp.getClass().equals(Panel.class)) {
						comp.addKeyListener(regKey.keyListener);
						F((Container) comp);
					} else
						comp.addKeyListener(regKey.keyListener);
				}
			}
		}).F(this);

		add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(380, 7, 212, 172);
		pnl.setBackground(Color.black);
		pnl.add(pnl = new Panel());
		pnl.setLayout(null);
		pnl.setBounds(1, 1, 210, 170);
		pnl.setBackground(new Color(0xF0F0F0));

		pnl.add(lbl = new Label("F1,F2,F3 - ���������� �� 1,2,3", Label.LEFT));
		lbl.setBounds(2, 2, 260, 15);
		pnl.add(lbl = new Label("F4 - ���� ������", Label.LEFT));
		lbl.setBounds(2, 17, 130, 15);
		pnl.add(lbl = new Label("F5 - ������", Label.LEFT));
		lbl.setBounds(2, 32, 130, 15);
		pnl.add(lbl = new Label("F6 - ������", Label.LEFT));
		lbl.setBounds(2, 47, 130, 15);
		pnl.add(lbl = new Label("F7 - ����", Label.LEFT));
		lbl.setBounds(2, 62, 130, 15);
		pnl.add(lbl = new Label("F8 - �����������", Label.LEFT));
		lbl.setBounds(2, 77, 130, 15);
		pnl.add(lbl = new Label("F9 - ������/�������", Label.LEFT));
		lbl.setBounds(2, 92, 130, 15);
		pnl.add(lbl = new Label("F10 - ������ � ��", Label.LEFT));
		lbl.setBounds(2, 107, 130, 15);
		pnl.add(lbl = new Label("F11 - ����", Label.LEFT));
		lbl.setBounds(2, 122, 130, 15);
		pnl.add(lbl = new Label("F12 - ����������", Label.LEFT));
		lbl.setBounds(2, 137, 130, 15);
		// pnl.add(lbl = new Label("Shift+F9 - ����", Label.LEFT));
		// lbl.setBounds(2, 137, 130, 15);
		// pnl.add(lbl = new Label("Tab - ������ � ��", Label.LEFT));
		// lbl.setBounds(2, 152, 130, 15);

	}

	public void updateStateList() {
		// lstState.clear();
		lstState.setItem(0, "0 - " + regState.val.getBit(0) + " - ������� (C)");
		lstState.setItem(1, "1 - " + regState.val.getBit(1) + " - ���� (Z)");
		lstState.setItem(2, "2 - " + regState.val.getBit(2) + " - ���� (N)");
		lstState.setItem(3, "3 - " + regState.val.getBit(3) + " - (���������������)");
		lstState.setItem(4, "4 - " + regState.val.getBit(4)
				+ " - ���������� ����������");
		lstState.setItem(5, "5 - " + regState.val.getBit(5) + " - ����������");
		lstState.setItem(6, "6 - " + regState.val.getBit(6) + " - ��������� ��");
		lstState.setItem(7, "7 - " + regState.val.getBit(7) + " - ������/�������");
		lstState.setItem(8, "8 - " + regState.val.getBit(8) + " - ���������");
		lstState.setItem(9, "9 - " + regState.val.getBit(9) + " - ������� �������");
		lstState.setItem(10, "10 - " + regState.val.getBit(10) + " - ������� ������");
		lstState.setItem(11, "11 - " + regState.val.getBit(11) + " - ����������");
		lstState.setItem(11, "12 - " + regState.val.getBit(12) + " - ����-�����");
	}

	public void regAccumSet(bin val) {
		regAccumulator.setValue(val);
	}

	public bin regAccumGet() {
		return regAccumulator.getValue();
	}

	public void regCCountSet(bin val) {
		regCommandCounter.setValue(val);
	}

	public bin regCCountGet() {
		return regCommandCounter.getValue();
	}

	public void regMicroCmdSet(bin val) {
		regMicroCommand.setValue(val);
	}

	public bin regMicroCmdGet() {
		return regMicroCommand.getValue();
	}

	public void regMicroCCountSet(bin val) {
		int n = val.toInt();
		bin b = regStGet();
		b.setBit(9, '0');
		b.setBit(10, '0');
		b.setBit(11, '0');
		b.setBit(12, '0');
		if (n > 0x00 && n < 0x0D) {
			b.setBit(9, '1');
		} else if (n >= 0x0D && n < 0x1D) {
			b.setBit(10, '1');
		} else if (n >= 0x1D && n < 0x8E) {
			b.setBit(11, '1');
			;
		} else if (n == 0x8E) {
			b.setBit(12, '1');
		} else if (n > 0x8E && n < 0x99) {

		} else {

		}
		regStSet(b);

		regMicroCounter.setValue(val);
		updateMemView(parent.mem);
	}

	public bin regMicroCCountGet() {
		return regMicroCounter.getValue();
	}

	public void regBuffSet(bin val) {
		regBuffer.setValue(val);
	}

	public bin regBuffGet() {
		return regBuffer.getValue();
	}

	public void regDataSet(bin val) {
		regData.setValue(val);
	}

	public bin regDataGet() {
		return regData.getValue();
	}

	public void regAddrSet(bin val) {
		regAddress.setValue(val);
		updateMemView(parent.mem);
	}

	public bin regAddrGet() {
		return regAddress.getValue();
	}

	public void regCmdSet(bin val) {
		regCommand.setValue(val);
	}

	public bin regCmdGet() {
		return regCommand.getValue();
	}

	public void regStSet(bin val) {
		regState.setValue(val);
		updateStateList();
		txtC.setText(Character.toString(val.getBit(0)));
		txtZ.setText(Character.toString(val.getBit(1)));
		txtN.setText(Character.toString(val.getBit(2)));
	}

	public bin regStGet() {
		return regState.getValue();
	}

	public void regKeySet(bin val) {
		regKey.setValue(val);
	}

	public bin regKeyGet() {
		return regKey.getValue();
	}

	public void setStBit(int n, char val) {
		regState.setBit(n, val);
		updateStateList();
	}

	public void updateMemView(MemoryArray mem) {
		//lstMemView.clear();
		for (int addr = Utils.alignBy16(regAddress.getValue().toInt()), i = 0; addr < mem.arr.length
				&& i < 20; addr++, i++) {
			lstMemView.setItem(i, Utils.AlignStr(Integer.toHexString(addr)
					.toUpperCase(), 3, '0')
					+ "|"
					+ Utils.AlignStr(Integer.toHexString(
							mem.getVal(addr).toInt()).toUpperCase(), 4, '0'));
		}
	}

	public void updateMicroMemView(MemoryArray mem) {
		//lstMicroMemView.clear();
		for (int addr = Utils.alignBy16(regMicroCounter.getValue().toInt()), i = 0; addr < mem.arr.length
				&& i < 19; addr++, i++) {
			lstMicroMemView.setItem(i, Utils.AlignStr(Integer.toHexString(addr)
					.toUpperCase(), 2, '0')
					+ "|"
					+ Utils.AlignStr(Integer.toHexString(
							mem.getVal(addr).toInt()).toUpperCase(), 4, '0'));
		}
	}

	public bin devGetFlag(int devN) {
		Checkbox chk;
		switch (devN) {
		case 1:
			chk = chkDev1Check;
			break;
		case 2:
			chk = chkDev2Check;
			break;
		case 3:
			chk = chkDev3Check;
			break;
		default:
			return new bin("00000000");
		}
		return new bin(1, ((chk.getState()) ? ("1") : ("0")));
	}

	public void devSetFlag(int devN, bin val) {
		Checkbox chk;
		switch (devN) {
		case 1:
			chk = chkDev1Check;
			break;
		case 2:
			chk = chkDev2Check;
			break;
		case 3:
			chk = chkDev3Check;
			break;
		default:
			return;
		}
		chk.setState((val.arr[0] == '0') ? (false) : (true));
	}

	public bin devDataGet(int devN) {
		switch (devN) {
		case 1:
			return regDev1.getValue();
		case 2:
			return regDev2.getValue();
		case 3:
			return regDev3.getValue();
		default:
			return new bin(8);
		}
	}

	public void devDataSet(int devN, bin val) {
		switch (devN) {
		case 1:
			regDev1.setValue(val);
			break;
		case 2:
			regDev2.setValue(val);
			break;
		case 3:
			regDev3.setValue(val);
			break;
		}
	}
}
