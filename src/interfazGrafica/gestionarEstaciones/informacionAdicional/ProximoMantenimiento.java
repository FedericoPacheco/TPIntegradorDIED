package interfazGrafica.gestionarEstaciones.informacionAdicional;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import entidades.Estacion;
import grafo.RedDeTransporte;
import interfazGrafica.utilidades.ModeloTablaGenerico;

@SuppressWarnings("serial")
public class ProximoMantenimiento extends JPanel 
{
	private ModeloTablaGenerico mTbl;
	private JTable tbl;
	private JScrollPane sp;
	private JButton btnVolver;
	
	private GridBagConstraints gbc;
	private JFrame ventana;
	private JPanel panelPadre;
	
	private RedDeTransporte redDeTransporte;
	List <Estacion> proximosMantenimientos;
	
	public ProximoMantenimiento(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte) 
	{	
		this.ventana = ventana;
		this.panelPadre = panelPadre;
		this.redDeTransporte = redDeTransporte;
	
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		
		this.armarPanel();
	}
	
	private void armarPanel() 
	{
		mTbl = new ModeloTablaGenerico(); 
		mTbl.addColumna("Id").addColumna("Estación");
		mTbl.setDatos(this.recuperarDatos());
		tbl = new JTable(mTbl);
		// https://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centrarDatos = new DefaultTableCellRenderer();
		centrarDatos.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centrarDatos);
		// -----------------------------------
		tbl.setPreferredScrollableViewportSize(new Dimension(250, 250));
		tbl.getColumnModel().getColumn(0).setPreferredWidth(50);
		tbl.getColumnModel().getColumn(1).setPreferredWidth(200);
		sp = new JScrollPane(tbl);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.ipady = 15;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		this.add(sp, gbc);
		
		
		btnVolver = new JButton("Volver");
		btnVolver.addActionListener(
			e -> {
					ventana.setContentPane(panelPadre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.0;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);
		this.add(btnVolver, gbc);
	}
	
	private Object[][] recuperarDatos()
	{
		proximosMantenimientos = redDeTransporte.proximosMantenimientos();
		
		Object[][] datos = new Object[proximosMantenimientos.size()][2];
		int i = 0;
		for (Estacion e : proximosMantenimientos)
		{
			datos[i][0] = e.getId();
			datos[i][1] = e.getNombre();
			
			i++;
		}
		
		return datos;
	}
}
