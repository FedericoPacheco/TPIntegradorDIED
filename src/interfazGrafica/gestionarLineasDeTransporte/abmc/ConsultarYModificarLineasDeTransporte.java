package interfazGrafica.gestionarLineasDeTransporte.abmc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import entidades.LineaDeTransporte;
import grafo.RedDeTransporte;
import interfazGrafica.utilidades.ModeloTablaGenerico;

@SuppressWarnings("serial")
public class ConsultarYModificarLineasDeTransporte extends JPanel implements TableModelListener
{
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
		mTbl.setIsCellEditable((row, col) -> (col > 0)? true : false);
		tbl = new JTable(mTbl);
		// https://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centrarDatos = new DefaultTableCellRenderer();
		centrarDatos.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(2).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(3).setCellRenderer(centrarDatos);
		// -----------------------------------
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
