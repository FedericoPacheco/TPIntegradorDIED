package interfazGrafica.gestionarEstaciones.informacionAdicional;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import clasesUtiles.Dupla;
import clasesUtiles.ModeloTablaGenerico;
import entidades.valueObjects.Estacion;
import entidades.valueObjects.Tramo;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class FlujoMaximo extends JPanel 
{
	private ModeloTablaGenerico mTbl;
	private JTable tbl;
	private JScrollPane sp;
	private JComboBox<String> cbOrigen, cbDestino;
	private JLabel lbl1, lbl2;
	private JButton btnVolver, btnCalcular;
	
	private GridBagConstraints gbc;
	private JFrame ventana;
	private JPanel panelPadre;
	
	private RedDeTransporte redDeTransporte;
	private Map<String, Estacion> estacionesCb;
	List<Dupla<LinkedList<Tramo>, Integer>> caminosYFlujosMaximos;
	
	public FlujoMaximo(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte) 
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
		gbc.gridy = 0;
		gbc.weightx = 0.25;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		cbOrigen = new JComboBox<String>();
		gbc.gridx = 0;
		gbc.ipady = 0;
		this.add(cbOrigen, gbc);
		
		lbl1 = new JLabel(">>>");
		gbc.gridx = 1;
		gbc.ipady = 0;
		this.add(lbl1, gbc);
		
		cbDestino = new JComboBox<String>();
		gbc.gridx = 2;
		gbc.ipady = 0;
		this.add(cbDestino, gbc);
		
		btnCalcular = new JButton("Calcular");
		gbc.gridx = 3;
		gbc.ipady = 15;
		btnCalcular.addActionListener(
			e -> {
					mTbl.setDatos(this.recuperarDatos());
					mTbl.fireTableDataChanged();
				 }
		);
		this.add(btnCalcular, gbc);
		
		estacionesCb = new HashMap<String, Estacion>();
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			estacionesCb.put(e.getNombre() + " (id: " + e.getId() + ")", e);
			cbOrigen.addItem(e.getNombre() + " (id: " + e.getId() + ")");
			cbDestino.addItem(e.getNombre() + " (id: " + e.getId() + ")");
		}
		
		
		mTbl = new ModeloTablaGenerico(); 
		mTbl.addColumna("Camino").addColumna("Flujo máximo");
		tbl = new JTable(mTbl);
		// https://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centrarDatos = new DefaultTableCellRenderer();
		centrarDatos.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centrarDatos);
		// -----------------------------------
		tbl.setPreferredScrollableViewportSize(new Dimension(600, 300));
		tbl.getColumnModel().getColumn(0).setPreferredWidth(500);
		tbl.getColumnModel().getColumn(1).setPreferredWidth(100);
		sp = new JScrollPane(tbl);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		this.add(sp, gbc);
		
		lbl2 = new JLabel();//"Notación: (idEstaciónOrigen, idEstaciónDestino): idLíneaDeTransporteDelTramo");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 10, 10, 10);
		this.add(lbl2, gbc);
		
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
		caminosYFlujosMaximos = 
			redDeTransporte.flujoMaximo(
				estacionesCb.get(cbOrigen.getSelectedItem()),
				estacionesCb.get(cbDestino.getSelectedItem())
			);
		
		Object datos[][] = new Object[caminosYFlujosMaximos.size() + 1][2];
		int flujoTotal = 0;
		int i;
		for (i = 0; i < caminosYFlujosMaximos.size(); i++)
		{
			datos[i][0] = this.recuperarCamino(caminosYFlujosMaximos.get(i).primero);
			datos[i][1] = caminosYFlujosMaximos.get(i).segundo; 
			
			flujoTotal += caminosYFlujosMaximos.get(i).segundo;
		}	
		datos[i][0] = "Total";
		datos[i][1] = flujoTotal;
		
		return datos;
	}
	
	private String recuperarCamino(List<Tramo> camino)
	{
		String caminoStr = "[ ";
		
		for (int i = 0; i < camino.size(); i++)
		{
			caminoStr += 
				"(" + camino.get(i).getIdOrigen() + ", " + 
					  camino.get(i).getIdDestino() + "): " + 
					  camino.get(i).getIdLineaDeTransporte() + ((i == camino.size() - 1)? "" : "; ");
		}
		
		return caminoStr + " ]";
	}
}
