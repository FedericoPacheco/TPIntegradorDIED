package interfazGrafica.gestionarEstaciones.informacionAdicional;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import clasesUtiles.Dupla;
import clasesUtiles.ModeloTablaGenerico;
import entidades.valueObjects.Estacion;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class EstacionRank extends JPanel 
{
	private static final Double FACTOR_AMORTIGUACION = 0.15;
	private static final Double ERROR = 1.0 / 1000;
	
	private ModeloTablaGenerico mTbl;
	private JTable tbl;
	private JScrollPane sp;
	private JButton btnVolver;
	
	private GridBagConstraints gbc;
	private JFrame ventana;
	private JPanel panelPadre;
	
	private RedDeTransporte redDeTransporte;
	List<Dupla<Estacion, Double>> estacionesRankOrdenado;
	
	public EstacionRank(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte) 
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
		mTbl.addColumna("Id").addColumna("Estación").addColumna("EstaciónRank");
		mTbl.setDatos(this.recuperarDatos());
		tbl = new JTable(mTbl);
		// https://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centrarDatos = new DefaultTableCellRenderer();
		centrarDatos.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(2).setCellRenderer(centrarDatos);
		// -----------------------------------
		tbl.setPreferredScrollableViewportSize(new Dimension(500, 250));
		tbl.getColumnModel().getColumn(0).setPreferredWidth(50);
		tbl.getColumnModel().getColumn(1).setPreferredWidth(200);
		tbl.getColumnModel().getColumn(2).setPreferredWidth(250);
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
		this.ordenarPorEstacionRank(redDeTransporte.estacionRank(FACTOR_AMORTIGUACION, ERROR));
		
		Object[][] datos = new Object[estacionesRankOrdenado.size()][3];
		int i = 0;
		for (Dupla<Estacion, Double> d : estacionesRankOrdenado)
		{
			datos[i][0] = d.primero.getId();
			datos[i][1] = d.primero.getNombre();
			datos[i][2] = d.segundo;
			
			i++;
		}
		
		return datos;
	}

	private void ordenarPorEstacionRank(Map<Estacion, Double> estacionesRank) 
	{
		estacionesRankOrdenado = new LinkedList<Dupla<Estacion, Double>>();
		
		for (Map.Entry<Estacion, Double> pr : estacionesRank.entrySet())
			estacionesRankOrdenado.add(new Dupla<Estacion, Double>(pr.getKey(), pr.getValue()));
		
		Collections.sort(
			estacionesRankOrdenado, 
			(d1, d2) -> d2.segundo.compareTo(d1.segundo)
		);
	}
}