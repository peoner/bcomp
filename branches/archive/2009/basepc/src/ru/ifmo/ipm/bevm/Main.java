package ru.ifmo.ipm.bevm;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;


public class Main extends Applet {

	MediaTracker  mTracker;
	URL docBase;
	int trackerCount;

	Processor proc;

	MemoryArray mem;
	MemoryArray micromem;

	LinkedList<LogicGate> LogicGates = new LinkedList<LogicGate>();

	Label lblToolTip;

	Timer tmrMain;
	boolean working;
	TimerTask tmrTask;

	boolean step = false;

	DebugWnd dbg;

	public void init()
    {
		this.setLayout(null);
		this.setSize(600, 500);

		trackerCount = 0;
		mTracker = new MediaTracker(this);
		docBase = getDocumentBase();

		add(proc = new Processor(this, new Processor.DeviceListener() {
			public void OnDevice1Ready() {
				proc.devSetFlag(1, new bin("1"));
				//proc.setStBit(5, '1');
				proc.setStBit(6, '1');
			}

			public void OnDevice2Ready(){
				proc.devSetFlag(2, new bin("1"));
				//proc.setStBit(5, '1');
				proc.setStBit(6, '1');
			}

			public void OnDevice3Ready(){
				proc.devSetFlag(3, new bin("1"));
				//proc.setStBit(5, '1');
				proc.setStBit(6, '1');
			}
		}));
		proc.setLocation(0, 0);

		mem = new MemoryArray(0x800, 16);
		micromem = new MemoryArray(0x100, 16);

		final KeyListener listener2 = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_F1:{
					proc.devSetFlag(1, new bin("1"));
					//proc.setStBit(5, '1');
					proc.setStBit(6, '1');
					break;
				}
				case KeyEvent.VK_F2:{
					proc.devSetFlag(2, new bin("1"));
					//proc.setStBit(5, '1');
					proc.setStBit(6, '1');
					break;
				}
				case KeyEvent.VK_F3:{
					proc.devSetFlag(3, new bin("1"));
					//proc.setStBit(5, '1');
					proc.setStBit(6, '1');
					break;
				}
				case KeyEvent.VK_F4:{
					bin t = proc.regKeyGet();
					proc.regBuffSet(t);
					proc.regCCountSet(t);
					break;
				}
				case KeyEvent.VK_F5:{ //
					if( proc.pnlMK.isVisible() ){
						bin t = proc.regKeyGet(),
							at = proc.regCCountGet();
						proc.regDataSet(t);
						micromem.setVal(at.toInt(),t);
						at=at.inc();
						proc.regBuffSet(at);
						proc.regCCountSet(at);
					}else{
						bin t = proc.regKeyGet(),
							at = proc.regCCountGet();
						//proc.regAddrSet(at); // -->
						proc.regDataSet(t);
						mem.setVal(at.toInt(),t);
						proc.regAddrSet(at);// <--
						at=at.inc();
						proc.regBuffSet(at);
						proc.regCCountSet(at);
					}
					break;
				}
				case KeyEvent.VK_F6:{
					bin at = proc.regCCountGet();
					//proc.regAddrSet(at); // -->
					proc.regDataSet(mem.getVal(at.toInt()));
					proc.regAddrSet(at);// <--
					at=at.inc();
					proc.regBuffSet(at);
					proc.regCCountSet(at);
					break;
				}
				case KeyEvent.VK_F7:{ //
					proc.regAddrSet(new bin(1));
					proc.regAccumSet(new bin(1));
					proc.regCmdSet(new bin(1));
					proc.regBuffSet(new bin(1));
					proc.regDataSet(new bin(1));
					if(proc.regState.val.getBit(7)=='1')working = true;
					break;
				}
				case KeyEvent.VK_F8:{ //
					working = true;
					break;
				}
				case KeyEvent.VK_F9:{ //
					proc.setStBit(7, ((proc.regState.val.getBit(7)=='1')?('0'):('1')));
					break;
				}
				case KeyEvent.VK_F10:{
					proc.pnlMK.setVisible(!proc.pnlMK.isVisible());
					break;
				}
				case KeyEvent.VK_F11: { //
					step = !step;
					break;
				}
				case KeyEvent.VK_F12:{
					proc.setStBit(5, '1');
					break;
				}
				}
			}

			public void keyTyped(KeyEvent e) { }
			public void keyReleased(KeyEvent e) { }
		};

		Functor<Container> F = new Functor<Container>() {
			public void F(Container arg) {
				for ( Component comp : arg.getComponents()) {
					if(comp.getClass().equals(Panel.class))
					{
						comp.addKeyListener(listener2);
						F((Container)comp);
					}
					else
						comp.addKeyListener(listener2);
				}
			}
		};
		F.F(proc);

		try { mTracker.waitForAll(); } catch(InterruptedException e) {}
		proc.regStSet(new bin(13,"0000000000010"));
		proc.regMicroCCountSet(new bin(8, "1"));


		this.InitMicroMem();

		proc.updateMemView(mem);
		proc.updateMicroMemView(micromem);

		working = false;
		tmrMain = new Timer();
		tmrTask = new TimerTask(){
			public void run() {
				if(working) {
					bin cmc = proc.regMicroCCountGet();
					bin ccmd = micromem.getVal(cmc.toInt());
					bin chcmd = Utils.MicroVertical2Horizontal(ccmd);
					proc.regMicroCmdSet(ccmd);
					//proc.SetLogicGates(chcmd);
					proc.regMicroCCountSet(cmc.inc());
					ExecuteMicroCommand(chcmd);
					System.out.print(Integer.toHexString(
							cmc.toInt())+"\t"+
							Utils.FormatStr(chcmd.toString(), 4)+"\t"+
							Utils.FormatStr(ccmd.toString(), 4)+"\t"+
							Utils.FormatStr(proc.regBuffGet().toString(), 4)+"\n");
					dbg.lstMicroTrace.add(Integer.toHexString(cmc.toInt()) + " " +
							Integer.toHexString(ccmd.toInt()) + " " +
							Integer.toHexString(proc.regCCountGet().toInt()) + " " +
							Integer.toHexString(proc.regAddrGet().toInt()) + " " +
							Integer.toHexString(proc.regCmdGet().toInt())+ " " +
							Integer.toHexString(proc.regDataGet().toInt()) + " " +
							Integer.toHexString(proc.regAccumGet().toInt()) + " " +
							proc.regStGet().getBit(0) + " " +
							Integer.toHexString(proc.regBuffGet().toInt()) + " " +
							proc.regStGet().getBit(2) + " " +
							proc.regStGet().getBit(1) + " " +
							Integer.toHexString(proc.regMicroCCountGet().toInt())
							);
					//dbg.lstTrace.add();
				}
				if(step) working = false;
			}
		};
		tmrMain.schedule(tmrTask, 50, 50);

		//(dbg = new DebugWnd()).show();
		dbg = new DebugWnd();
	}

	public Image image(String name) {
		Image img = getImage(docBase, name);
		mTracker.addImage(img, trackerCount);
		return img;
	}
/*
	public void update(Graphics g) {

	}

	public void paint(Graphics g) {

	}
*/
	/*
	Button btnFind;
	Button btnBreak;
	Button btnClear;
	TextField txtStartPath;
	TextField txt_sline;
	TextField txtFileMask;
	Label lblStartPath;
	Label lblFileNameMask;
	Label lblCurrFile;
	Checkbox chkSubFolders;
	Checkbox chkScanFiles;
	JTable tblTable;
	JScrollPane scrTablePane;
	//SearchResultDataModel tblData;
	MenuBar menuBar;
	*/

	public boolean handleEvent(Event event) {
		if (event.id == Event.WINDOW_DESTROY)
			System.exit(0);
		return super.handleEvent(event);
	}

	public Main() {
		this.setSize(800, 600);
	}

	private void InitMicroMem() {
		micromem.setVal(1, "0000001100000000");
		micromem.setVal(2, "0100000000000001");
		micromem.setVal(3, "0000001100010001");
		micromem.setVal(4, "0100000000000100");
		micromem.setVal(5, "0000000100000000");
		micromem.setVal(6, "0100000000000011");
		micromem.setVal(7, "1010111100001100");
		micromem.setVal(8, "1010111000001100");
		micromem.setVal(9, "1010110100001100");
		micromem.setVal(10, "1110110001011110");
		micromem.setVal(11, "1000001110001110");
		micromem.setVal(12, "1010101100011101");
		micromem.setVal(13, "0000000100000000");
		micromem.setVal(14, "0100000000000001");
		micromem.setVal(15, "0000000000000001");
		micromem.setVal(16, "1010001100011101");
		micromem.setVal(17, "1110010000011101");
		micromem.setVal(18, "1110010100011101");
		micromem.setVal(19, "1110011000011101");
		micromem.setVal(20, "1110011100011101");
		micromem.setVal(21, "1110100000011101");
		micromem.setVal(22, "1110100100011101");
		micromem.setVal(23, "1110101000011101");
		micromem.setVal(24, "0000000100010000");
		micromem.setVal(25, "0100000000000010");
		micromem.setVal(26, "0000000000000010");
		micromem.setVal(27, "0000000101000000");
		micromem.setVal(28, "0100000000000010");
		micromem.setVal(29, "1110111100101101");
		micromem.setVal(30, "0000000100000000");
		micromem.setVal(31, "0100000000000001");
		micromem.setVal(32, "1110111000100111");
		micromem.setVal(33, "1010110100100100");
		micromem.setVal(34, "1010110001010111");
		micromem.setVal(35, "1000001100111000");
		micromem.setVal(36, "0000000000000001");
		micromem.setVal(37, "1010110001010000");
		micromem.setVal(38, "1000001100110101");
		micromem.setVal(39, "0000000000000001");
		micromem.setVal(40, "1010110100101011");
		micromem.setVal(41, "1010110001000011");
		micromem.setVal(42, "1000001110110000");
		micromem.setVal(43, "1010110000111100");
		micromem.setVal(44, "1000001100111111");
		micromem.setVal(45, "1010111000110000");
		micromem.setVal(46, "1010110001000111");
		micromem.setVal(47, "1000001111010000");
		micromem.setVal(48, "1010110100110011");
		micromem.setVal(49, "1010110001001100");
		micromem.setVal(50, "1000001101001110");
		micromem.setVal(51, "1010110001000110");
		micromem.setVal(52, "1000001101001010");
		micromem.setVal(53, "0001000100100000");
		micromem.setVal(54, "0100000000110101");
		micromem.setVal(55, "1000001110001111");
		micromem.setVal(56, "0001000000000000");
		micromem.setVal(57, "0100000000000010");
		micromem.setVal(58, "0000000000000010");
		micromem.setVal(59, "1000001110001111");
		micromem.setVal(60, "0001000100000000");
		micromem.setVal(61, "0100000001110101");
		micromem.setVal(62, "1000001110001111");
		micromem.setVal(63, "1000000000111100");
		micromem.setVal(64, "0001000100010000");
		micromem.setVal(65, "0100000001110101");
		micromem.setVal(66, "1000001110001111");
		micromem.setVal(67, "0001000110010000");
		micromem.setVal(68, "0100000001110101");
		micromem.setVal(69, "1000001110001111");
		micromem.setVal(70, "1000000010001111");
		micromem.setVal(71, "0000000100000000");
		micromem.setVal(72, "0100000000000100");
		micromem.setVal(73, "1000001110001111");
		micromem.setVal(74, "1100001010001111");
		micromem.setVal(75, "1000001101000111");
		micromem.setVal(76, "1000001010001111");
		micromem.setVal(77, "1000001101000111");
		micromem.setVal(78, "1000000110001111");
		micromem.setVal(79, "1000001101000111");
		micromem.setVal(80, "0000000100010000");
		micromem.setVal(81, "0100000000000010");
		micromem.setVal(82, "0000000000000010");
		micromem.setVal(83, "1101111110001111");
		micromem.setVal(84, "0000001100010000");
		micromem.setVal(85, "0100000000000100");
		micromem.setVal(86, "1000001110001111");
		micromem.setVal(87, "0000000100010000");
		micromem.setVal(88, "0100000000000011");
		micromem.setVal(89, "0000001100000000");
		micromem.setVal(90, "0100000000000010");
		micromem.setVal(91, "0000001000000010");
		micromem.setVal(92, "0100000000000100");
		micromem.setVal(93, "1000001110001111");
		micromem.setVal(94, "1010101101100001");
		micromem.setVal(95, "1010101001101100");
		micromem.setVal(96, "1000001111100000");
		micromem.setVal(97, "1010101001100111");
		micromem.setVal(98, "1010100101100101");
		micromem.setVal(99, "1010100010000010");
		micromem.setVal(100, "1000001110000101");
		micromem.setVal(101, "1010100001111011");
		micromem.setVal(102, "1000001101111110");
		micromem.setVal(103, "1010100101101010");
		micromem.setVal(104, "1010100001110110");
		micromem.setVal(105, "1000001101111001");
		micromem.setVal(106, "1010100010001000");
		micromem.setVal(107, "1000001110000111");
		micromem.setVal(108, "1010100101101111");
		micromem.setVal(109, "1010100010001010");
		micromem.setVal(110, "1000001110001100");
		micromem.setVal(111, "1010100001110011");
		micromem.setVal(112, "0001000010000000");
		micromem.setVal(113, "0100000001110101");
		micromem.setVal(114, "1000001110001111");
		micromem.setVal(115, "0001000000010000");
		micromem.setVal(116, "0100000001110101");
		micromem.setVal(117, "1000001110001111");
		micromem.setVal(118, "0000000000100000");
		micromem.setVal(119, "0100000000110101");
		micromem.setVal(120, "1000001110001111");
		micromem.setVal(121, "0100000010000000");
		micromem.setVal(122, "1000001110001111");
		micromem.setVal(123, "0001000001000000");
		micromem.setVal(124, "0100000000110101");
		micromem.setVal(125, "1000001110001111");
		micromem.setVal(126, "1000000010000000");
		micromem.setVal(127, "1000001101111001");
		micromem.setVal(128, "0100000011000000");
		micromem.setVal(129, "1000001110001111");
		micromem.setVal(130, "0000000000001000");
		micromem.setVal(131, "0100000001110101");
		micromem.setVal(132, "1000001110001111");
		micromem.setVal(133, "0000000000000100");
		micromem.setVal(134, "0100000001110101");
		micromem.setVal(135, "1000001110001111");
		micromem.setVal(136, "0100000000001000");
		micromem.setVal(137, "1000001100000001");
		micromem.setVal(138, "0100100000000000");
		micromem.setVal(139, "1000001100000001");
		micromem.setVal(140, "0100010000000000");
		micromem.setVal(141, "1000001100000001");
		micromem.setVal(142, "0100000100000000");
		micromem.setVal(143, "1000011110001000");
		micromem.setVal(144, "1000010100000001");
		micromem.setVal(145, "0000000000100000");
		micromem.setVal(146, "0100000000000001");
		micromem.setVal(147, "0000001100000000");
		micromem.setVal(148, "0100000000000010");
		micromem.setVal(149, "0000000000010010");
		micromem.setVal(150, "0100000000000100");
		micromem.setVal(151, "0100010000000000");
		micromem.setVal(152, "1000001100000001");
		micromem.setVal(153, "0011000000000000");
		micromem.setVal(154, "0100000000000100");
		micromem.setVal(155, "1000001110001111");
		micromem.setVal(156, "0000001100000000");
		micromem.setVal(157, "0100000000000001");
		micromem.setVal(158, "0000001100010001");
		micromem.setVal(159, "0100000000000100");
		micromem.setVal(160, "1000001110001111");
		micromem.setVal(161, "0000001100000000");
		micromem.setVal(162, "0100000000000001");
		micromem.setVal(163, "0011000000000000");
		micromem.setVal(164, "0100000000000010");
		micromem.setVal(165, "0000001100010010");
		micromem.setVal(166, "0100000000000100");
		micromem.setVal(167, "1000001110001111");
		micromem.setVal(168, "0000000000100000");
		micromem.setVal(169, "0100000001110111");
		micromem.setVal(170, "0100001000000000");
		micromem.setVal(171, "0100010000000000");
		micromem.setVal(172, "1000001110001111");
		micromem.setVal(173, "0000000000000000");
		micromem.setVal(174, "0000000000000000");
		micromem.setVal(175, "0000000000000000");
		micromem.setVal(176, "0000000000000000");
		micromem.setVal(177, "0000000000000000");
		micromem.setVal(178, "0000000000000000");
		micromem.setVal(179, "0000000000000000");
		micromem.setVal(180, "0000000000000000");
		micromem.setVal(181, "0000000000000000");
		micromem.setVal(182, "0000000000000000");
		micromem.setVal(183, "0000000000000000");
		micromem.setVal(184, "0000000000000000");
		micromem.setVal(185, "0000000000000000");
		micromem.setVal(186, "0000000000000000");
		micromem.setVal(187, "0000000000000000");
		micromem.setVal(188, "0000000000000000");
		micromem.setVal(189, "0000000000000000");
		micromem.setVal(190, "0000000000000000");
		micromem.setVal(191, "0000000000000000");
		micromem.setVal(192, "0000000000000000");
		micromem.setVal(193, "0000000000000000");
		micromem.setVal(194, "0000000000000000");
		micromem.setVal(195, "0000000000000000");
		micromem.setVal(196, "0000000000000000");
		micromem.setVal(197, "0000000000000000");
		micromem.setVal(198, "0000000000000000");
		micromem.setVal(199, "0000000000000000");
		micromem.setVal(200, "0000000000000000");
		micromem.setVal(201, "0000000000000000");
		micromem.setVal(202, "0000000000000000");
		micromem.setVal(203, "0000000000000000");
		micromem.setVal(204, "0000000000000000");
		micromem.setVal(205, "0000000000000000");
		micromem.setVal(206, "0000000000000000");
		micromem.setVal(207, "0000000000000000");
		micromem.setVal(208, "0000000000000000");
		micromem.setVal(209, "0000000000000000");
		micromem.setVal(210, "0000000000000000");
		micromem.setVal(211, "0000000000000000");
		micromem.setVal(212, "0000000000000000");
		micromem.setVal(213, "0000000000000000");
		micromem.setVal(214, "0000000000000000");
		micromem.setVal(215, "0000000000000000");
		micromem.setVal(216, "0000000000000000");
		micromem.setVal(217, "0000000000000000");
		micromem.setVal(218, "0000000000000000");
		micromem.setVal(219, "0000000000000000");
		micromem.setVal(220, "0000000000000000");
		micromem.setVal(221, "0000000000000000");
		micromem.setVal(222, "0000000000000000");
		micromem.setVal(223, "0000000000000000");
		micromem.setVal(224, "0000000000000000");
		micromem.setVal(225, "0000000000000000");
		micromem.setVal(226, "0000000000000000");
		micromem.setVal(227, "0000000000000000");
		micromem.setVal(228, "0000000000000000");
		micromem.setVal(229, "0000000000000000");
		micromem.setVal(230, "0000000000000000");
		micromem.setVal(231, "0000000000000000");
		micromem.setVal(232, "0000000000000000");
		micromem.setVal(233, "0000000000000000");
		micromem.setVal(234, "0000000000000000");
		micromem.setVal(235, "0000000000000000");
		micromem.setVal(236, "0000000000000000");
		micromem.setVal(237, "0000000000000000");
		micromem.setVal(238, "0000000000000000");
		micromem.setVal(239, "0000000000000000");
		micromem.setVal(240, "0000000000000000");
		micromem.setVal(241, "0000000000000000");
		micromem.setVal(242, "0000000000000000");
		micromem.setVal(243, "0000000000000000");
		micromem.setVal(244, "0000000000000000");
		micromem.setVal(245, "0000000000000000");
		micromem.setVal(246, "0000000000000000");
		micromem.setVal(247, "0000000000000000");
		micromem.setVal(248, "0000000000000000");
		micromem.setVal(249, "0000000000000000");
		micromem.setVal(250, "0000000000000000");
		micromem.setVal(251, "0000000000000000");
		micromem.setVal(252, "0000000000000000");
		micromem.setVal(253, "0000000000000000");
		micromem.setVal(254, "0000000000000000");
		micromem.setVal(255, "0000000000000000");
	}

	private void ExecuteMicroCommand(bin h_bin_cmd) {
		bin aluLeft = null;
		bin aluRight = null;
		boolean f = false;
		bin sd;
		int i;
		int bn = 0;
		bin r = null;

		if(h_bin_cmd.getBit(31)=='0') {

			if(h_bin_cmd.getBit(0) == '1')
				working = false;

if ( Long.parseLong(String.copyValueOf(h_bin_cmd.arr).substring(18, 31)) > 0 ||
	 Long.parseLong(String.copyValueOf(h_bin_cmd.arr).substring(7, 8)) > 0 ) {
			//ОМК1

			if(h_bin_cmd.getBit(1) == '1') aluRight = proc.regDataGet();
			if(h_bin_cmd.getBit(2) == '1') aluRight = proc.regCmdGet();
			if(h_bin_cmd.getBit(3) == '1') aluRight = proc.regCCountGet();
			if(h_bin_cmd.getBit(4) == '1') aluLeft = proc.regAccumGet();
			if(h_bin_cmd.getBit(5) == '1') aluLeft = proc.regStGet();
			if(h_bin_cmd.getBit(6) == '1') aluLeft = proc.regKeyGet();

			if(aluLeft == null) aluLeft = new bin(16);
			if(aluRight == null) aluRight = new bin(16);

			if(h_bin_cmd.getBit(7) == '1') aluLeft.invert();
			if(h_bin_cmd.getBit(8) == '1') aluRight.invert();

			if(h_bin_cmd.getBit(9) == '1') {
				r = aluRight.and(aluLeft); //'&
			} else {
				r = aluRight.add(aluLeft); //'+
			}

			if(h_bin_cmd.getBit(10) == '1') r = r.inc();

			proc.regBuffSet(r);

			if(h_bin_cmd.getBit(11) == '1') {
				bin acc = proc.regAccumGet();
				proc.regBuffSet(new bin(Character.toString(acc.getBit(0)) +
						Character.toString(proc.regStGet().getBit(0)) +
						Character.toString(acc.getBit(15)) +
						Character.toString(acc.getBit(14)) +
						Character.toString(acc.getBit(13)) +
						Character.toString(acc.getBit(12)) +
						Character.toString(acc.getBit(11)) +
						Character.toString(acc.getBit(10)) +
						Character.toString(acc.getBit(9)) +
						Character.toString(acc.getBit(8)) +
						Character.toString(acc.getBit(7)) +
						Character.toString(acc.getBit(6)) +
						Character.toString(acc.getBit(5)) +
						Character.toString(acc.getBit(4)) +
						Character.toString(acc.getBit(3)) +
						Character.toString(acc.getBit(2)) +
						Character.toString(acc.getBit(1))
						));
			}

			if(h_bin_cmd.getBit(12) == '1') {
				proc.regBuffSet(new bin(proc.regAccumGet().toString() + Character.toString(proc.regStGet().getBit(0))));
			}

			if(h_bin_cmd.getBit(23) == '1') {
				proc.regDataSet(mem.getVal(proc.regAddrGet().toInt()));
			}
			if(h_bin_cmd.getBit(24) == '1') {
				mem.setVal(proc.regAddrGet().toInt(),proc.regDataGet());
			}

//Else
} else {

			if(h_bin_cmd.getBit(13) == '1') proc.setStBit(4, proc.regBuffGet().getBit(16));
			if(h_bin_cmd.getBit(14) == '1') proc.setStBit(4, proc.regBuffGet().getBit(15));
			if(h_bin_cmd.getBit(15) == '1')
				if(proc.regBuffGet().toInt() == 0)	proc.setStBit(1, '1');
				else								proc.setStBit(1, '0');

			if(h_bin_cmd.getBit(16) == '1') proc.setStBit(2, '0');
			if(h_bin_cmd.getBit(17) == '1') proc.setStBit(2, '1');

			if(h_bin_cmd.getBit(18) == '1') proc.regAddrSet(proc.regBuffGet());
			if(h_bin_cmd.getBit(19) == '1') proc.regDataSet(proc.regBuffGet());
			if(h_bin_cmd.getBit(20) == '1') proc.regCmdSet(proc.regBuffGet());
			if(h_bin_cmd.getBit(21) == '1') proc.regCCountSet(proc.regBuffGet());
			if(h_bin_cmd.getBit(22) == '1') proc.regAccumSet(proc.regBuffGet());

			if(h_bin_cmd.getBit(25) == '1') {
				String scmd = proc.regDataGet().toString();
				int devN = (new bin(scmd.substring(8, 16))).toInt();

				switch((new bin(scmd.substring(4, 8))).toInt()) {
				case 0: proc.devSetFlag(devN, new bin("0")); break;// CLF
				case 1: {
					if( proc.devGetFlag(devN).toInt() > 0 )
						proc.regCCountSet(proc.regCommandCounter.val.inc());
				} break;// TSF
				case 2: proc.regAccumSet(proc.devDataGet(devN)); break;// IN
				case 3: proc.devDataSet(devN, proc.regAccumGet()); break;// OUT
				}
			}
			if(h_bin_cmd.getBit(26) == '1') {
				proc.devSetFlag(1, new bin("0"));
				proc.devSetFlag(2, new bin("0"));
				proc.devSetFlag(3, new bin("0"));
			}
			if(h_bin_cmd.getBit(27) == '1') {
				proc.setStBit(4, '0');
				proc.setStBit(5, '0');
			}

}
//End If
			if(h_bin_cmd.getBit(28) == '1') proc.setStBit(4, '1');

		}else{
			// управляющая

			sd = null;
			if(h_bin_cmd.getBit(25) == '1')sd = proc.regStGet();
			if(h_bin_cmd.getBit(26) == '1')sd = proc.regDataGet();
			if(h_bin_cmd.getBit(27) == '1')sd = proc.regCmdGet();
			if(h_bin_cmd.getBit(28) == '1')sd = proc.regAccumGet();
			if(sd == null)sd = new bin(16);

			for(i = 0; i < 16; i++)	{
				if(h_bin_cmd.getBit(i) == '1') bn = i;
			}
			if(h_bin_cmd.getBit(24) == sd.getBit(bn)) {
				proc.regMicroCCountSet(new bin(Character.toString(h_bin_cmd.getBit(23))+
											Character.toString(h_bin_cmd.getBit(22))+
											Character.toString(h_bin_cmd.getBit(21))+
											Character.toString(h_bin_cmd.getBit(20))+
											Character.toString(h_bin_cmd.getBit(19))+
											Character.toString(h_bin_cmd.getBit(18))+
											Character.toString(h_bin_cmd.getBit(17))+
											Character.toString(h_bin_cmd.getBit(16))));
			}
		}

	}
}
