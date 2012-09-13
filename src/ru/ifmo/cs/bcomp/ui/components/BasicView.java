/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.*;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */

public class BasicView extends JComponent {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;

	public BasicView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		TableModel dataModel = new AbstractTableModel() {
			public int getColumnCount() { return 2; }
			public int getRowCount() { return 16;}
			public Object getValueAt(int row, int col) { return new Integer(row*col); }
		};
		JTable table = new JTable(dataModel);
		table.setBounds(200, 2, 200, 400);
		table.setShowHorizontalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setRowSelectionAllowed(false);
		table.setRowHeight(20);
		table.setGridColor(Color.BLACK);
		table.setFocusable(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//table.getColumnModel().getColumn(0).setWidth(20);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		//table.getColumnModel().getColumn(0).setPreferredWidth(20);
		//table.getColumnModel().getColumn(1).setWidth(40);
		table.getColumnModel().getColumn(1).setMaxWidth(80);
		table.getColumnModel().getColumn(1).setMinWidth(80);
		table.setBackground(ComponentManager.COLOR_MEM_BGVALUE);
		table.setFont(ComponentManager.FONT_COURIER_BOLD_23);
		//table.getColumnModel().getColumn(1).setPreferredWidth(40);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			Color[] colColors = new Color[] { Color.BLUE, Color.RED }; 

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(colColors[column]);
				return c;
				}
		});

		add(table);
	}

	@Override
	public void paintComponent(Graphics g) {

        Graphics2D rs = (Graphics2D) g;

		cmanager.paintComponent(this, rs);
	}
}
