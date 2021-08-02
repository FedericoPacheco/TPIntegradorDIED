package interfazGrafica.gestionarLineasDeTransporte;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import entidades.Estacion;
import entidades.LineaDeTransporte;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class ConsultarYModificarLineasDeTransporte extends JPanel implements TableModelListener
{
	@SuppressWarnings("serial")
	class ModeloTablaLineasDeTransporte extends AbstractTableModel
	{
		private String[] nombreColumnas = {"Id", "Nombre", "Color", "Estado"};
		private Object[][] datos = { {"", "", "", ""} };
		 
		public void setData(Object[][] datos) 			{ this.datos = datos; 					}
		public int getColumnCount() 					{ return nombreColumnas.length; 		}
		public int getRowCount() 						{ return datos.length; 					}
		public String getColumnName(int col) 			{ return nombreColumnas[col]; 			}
		public Object getValueAt(int row, int col) 		{ return datos[row][col]; 				}
		public Class getColumnClass(int c) 				{ return getValueAt(0, c).getClass(); 	}
		public boolean isCellEditable(int row, int col) { return (col > 0)? true : false;		}
		public void setValueAt(Object value, int row, int col) 	
		{
		    datos[row][col] = value;
		    fireTableCellUpdated(row, col);
		}
	 }
	
	private GridBagConstraints gbc;
	private JButton btn1;
	private JTable tabla;
	private ModeloTablaLineasDeTransporte modeloTabla;
	private JScrollPane sp;
	private JFrame ventana;
	private JPanel padre;
	private JComboBox<String> estado;
	
	private Object[][] datos;
	private List<LineaDeTransporte> lineasDeTransporte;
	private RedDeTransporte redDeTransporte;

	public ConsultarYModificarLineasDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		lineasDeTransporte = redDeTransporte.getAllLineasDeTransporte();
		datos = new Object[lineasDeTransporte.size()][4];
		
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
		modeloTabla = new ModeloTablaLineasDeTransporte();
		tabla = new JTable(modeloTabla);
	    sp = new JScrollPane(tabla);
		
		estado.addItem("Activa");
		estado.addItem("No Activa");
		
		tabla.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(estado));
		tabla.setPreferredScrollableViewportSize(new Dimension(600, 200));
	    tabla.getModel().addTableModelListener(this);
	    tabla.getColumnModel().getColumn(0).setPreferredWidth(20);
	    tabla.getColumnModel().getColumn(3).setPreferredWidth(120);
	    
	    this.recuperarDatos();
	    modeloTabla.setData(datos);
	
	    gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.ipady = 0;
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
	public void tableChanged(TableModelEvent e) {
		int i = e.getFirstRow();
        int j = e.getColumn();
        Object datoModificado = ((TableModel) e.getSource()).getValueAt(i, j);
        
        switch(j)
		{
        	case 1:
        		lineasDeTransporte.get(i).setNombre((String) datoModificado);
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
	
	public void recuperarDatos()
	{
		for (Integer i = 0; i < lineasDeTransporte.size(); i++)
		{
			datos[i][0] = lineasDeTransporte.get(i).getId();
			datos[i][1] = lineasDeTransporte.get(i).getNombre();
			datos[i][2] = lineasDeTransporte.get(i).getColor();
			datos[i][3] = (lineasDeTransporte.get(i).getEstado() == LineaDeTransporte.Estado.ACTIVA)? "Activa" : "Inactiva";
		}
	}
}
