package interfazGrafica.gestionarLineasDeTransporte.abmc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import clasesUtiles.ModeloTablaGenerico;
import entidades.valueObjects.LineaDeTransporte;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class ConsultarYModificarLineasDeTransporte extends JPanel implements TableModelListener
{
	private static final int COLUMNA_COLOR = 2;
	
	private GridBagConstraints gbc;
	private JButton btn1;
	private JTable tbl;
	private ModeloTablaGenerico mTbl;
	private JScrollPane sp;
	private JFrame ventana;
	private JPanel padre;
	private JComboBox<String> estado;
	
	private List<LineaDeTransporte> lineasDeTransporte;
	private RedDeTransporte redDeTransporte;

	public ConsultarYModificarLineasDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		lineasDeTransporte = redDeTransporte.getAllLineasDeTransporte();
		
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
	    this.setLayout(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    this.armarPanel();
	}
	
	private void armarPanel()
	{
		btn1 = new JButton("Volver");
		estado = new JComboBox<String>();
		mTbl = new ModeloTablaGenerico();
		mTbl.addColumna("Id").addColumna("Nombre").addColumna("Color").addColumna("Estado");
		mTbl.setDatos(this.recuperarDatos());
		mTbl.setIsCellEditable((row, col) -> (col == 0 || col == 2)? false : true);
		tbl = new JTable(mTbl);
		// https://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centrarDatos = new DefaultTableCellRenderer();
		centrarDatos.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(2).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(3).setCellRenderer(centrarDatos);
		// -----------------------------------
		
		// Adaptado de: https://www.tutorialspoint.com/how-can-we-detect-the-double-click-events-of-a-jtable-row-in-java
		tbl.addMouseListener(
			new MouseAdapter() 
			{
				public void mouseClicked(MouseEvent me) // Permite re-seleccionar el color haciendo click en la casilla del mismo
				{	
					if (me.getClickCount() > 1) // Si isCellEditable == true no funciona :(
					{
						int i = ((JTable) me.getSource()).getSelectedRow();
						int j = ((JTable) me.getSource()).getSelectedColumn();
						
						if (j == COLUMNA_COLOR) 
						{
							Color colorSeleccionado = JColorChooser.showDialog(null, "", Color.decode(lineasDeTransporte.get(i).getColor()));
							
							if (colorSeleccionado != null)
							{
								String colorHex = "#" + 
										  Integer.toHexString(colorSeleccionado.getRed()) + 
										  Integer.toHexString(colorSeleccionado.getGreen()) + 
										  Integer.toHexString(colorSeleccionado.getBlue());
								
								tbl.setValueAt(colorHex, i, j);
							}
						}
					}
	         }
	      }
		);
		// -----------------------------------------
		
	    sp = new JScrollPane(tbl);
		
		estado.addItem("Activa");
		estado.addItem("No Activa");
		
		tbl.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(estado));
		tbl.setPreferredScrollableViewportSize(new Dimension(600, 200));
	    tbl.getModel().addTableModelListener(this);
	    tbl.getColumnModel().getColumn(0).setPreferredWidth(20);
	    tbl.getColumnModel().getColumn(3).setPreferredWidth(120);
	    gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.ipady = 0;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
		this.add(sp, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		gbc.fill = GridBagConstraints.CENTER;
	    this.add(btn1, gbc);
	    btn1.addActionListener(
	    	e -> {
	    			ventana.setContentPane(padre);
	    			ventana.pack();
	    			ventana.setVisible(true);
				 }	 
	    );
	}
	
	@Override
	public void tableChanged(TableModelEvent e) 
	{
		int i = e.getFirstRow();
        int j = e.getColumn();
        Object datoModificado = ((TableModel) e.getSource()).getValueAt(i, j);
        
        switch(j)
		{
        	case 1:
        		if (!((String) datoModificado).equals(""))
        			lineasDeTransporte.get(i).setNombre((String) datoModificado);
        		else
        		{
        			JOptionPane.showMessageDialog(ventana, "El nombre no puede ser vacío.", "", JOptionPane.ERROR_MESSAGE);
        			tbl.setValueAt(lineasDeTransporte.get(i).getNombre(), i, j);
        		}
        		break;
        	case 2: 
        		lineasDeTransporte.get(i).setColor((String) datoModificado);
        		break;
        	case 3: 
        		if (estado.getSelectedItem().equals("Activa"))
        			lineasDeTransporte.get(i).setEstado(LineaDeTransporte.Estado.ACTIVA);
        		else
        			lineasDeTransporte.get(i).setEstado(LineaDeTransporte.Estado.INACTIVA);
        		break;
		}
        
        try {
			redDeTransporte.updateLineaDeTransporte(lineasDeTransporte.get(i));
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public Object[][] recuperarDatos()
	{
		Object[][] datos = new Object[lineasDeTransporte.size()][4];
		for (Integer i = 0; i < lineasDeTransporte.size(); i++)
		{
			datos[i][0] = lineasDeTransporte.get(i).getId();
			datos[i][1] = lineasDeTransporte.get(i).getNombre();
			datos[i][2] = lineasDeTransporte.get(i).getColor();
			datos[i][3] = (lineasDeTransporte.get(i).getEstado() == LineaDeTransporte.Estado.ACTIVA)? "Activa" : "Inactiva";
		}
		
		return datos;
	}
}
