package interfazGrafica.lineaDeTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import entidades.Estacion;
import entidades.LineaDeTransporte;
import entidades.Tramo;
import entidades.Tramo.Estado;
import grafo.RedDeTransporte;

// https://stackoverflow.com/questions/3179136/jtable-how-to-refresh-table-model-after-insert-delete-or-update-the-data

@SuppressWarnings("serial")
public class AgregarTramosLineaDeTransporte extends JPanel 
{
	private ModeloTablaTramos mtt;
	private JTable tbl;
	private JScrollPane sp1;
	private JComboBox<String> cb1, cb2, cb3;
	private JButton btnAgregar, btnEliminar, btnAceptar, btnCompletarDatos;
	private JLabel lbl1, lbl2, lbl3;
	private GridBagConstraints gbc;
	private JFrame ventana;
	private JPanel padre;
	
	private List<Tramo> tramosLinea;
	private RedDeTransporte redDeTransporte;
	private Map<String, Estacion> estacionesCb;
	private Map<String, LineaDeTransporte> lineasCb;
	
	// Valores por defecto para los campos del tramo (Se hizo que en la db no pueda haber nulos)
	// (DFLT: default)
	private static final Double DISTANCIA_DFLT = Double.MAX_VALUE;
	private static final Integer DURACION_DFLT = Integer.MAX_VALUE;
	private static final Integer PASAJEROS_DFLT = 0;
	private static final Double COSTO_DFLT = Double.MAX_VALUE;
	
	public AgregarTramosLineaDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte) 
	{	
		this.ventana = ventana;
		this.padre = padre;
		this.redDeTransporte = redDeTransporte;
	
		tramosLinea = null;
		estacionesCb = new HashMap<String, Estacion>();
		lineasCb = new HashMap<String, LineaDeTransporte>();
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}

	private void armarPanel() 
	{
		cb1 = new JComboBox<String>();
		cb2 = new JComboBox<String>();
		cb3 = new JComboBox<String>();
		
		for (LineaDeTransporte l : redDeTransporte.getAllLineasDeTransporte())
		{
			lineasCb.put(l.getNombre() + " (id: " + l.getId() + ")", l);
			cb1.addItem(l.getNombre() + " (id: " + l.getId() + ")");
		}
		
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			estacionesCb.put(e.getNombre() + " (id: " + e.getId() + ")", e);
			cb2.addItem(e.getNombre() + " (id: " + e.getId() + ")");
			cb3.addItem(e.getNombre() + " (id: " + e.getId() + ")");
		}
		
		tramosLinea = redDeTransporte.getAllTramos(lineasCb.get(cb1.getSelectedItem()));
		
		btnAgregar = new JButton("Agregar");
		btnEliminar = new JButton("Eliminar");
		btnAceptar = new JButton("Aceptar");
		btnCompletarDatos = new JButton("Completar datos");
		
		lbl1 = new JLabel("Línea de transporte a completar: ");
		lbl2 = new JLabel("Tramos");
		
		lbl3 = new JLabel(">>>");
		
		mtt = new ModeloTablaTramos();
		tbl = new JTable(mtt);
		sp1 = new JScrollPane(tbl);
		mtt.setData(this.recuperarDatosEstacionesTramos());
		
	
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(lbl1, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.16;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(cb1, gbc);
		cb1.addActionListener(
			e -> { 
					tramosLinea = redDeTransporte.getAllTramos(lineasCb.get(cb1.getSelectedItem())); 
					mtt.setData(this.recuperarDatosEstacionesTramos());
					mtt.fireTableDataChanged();
				 } 
		);
		
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(lbl2, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(cb2, gbc);
		ActionListener a = 
			e -> {
					String cb2I = (String) cb2.getSelectedItem();
					String cb3I = (String) cb3.getSelectedItem();
					
					// Controlar que un tramo no tenga de origen y destino la misma estacion,
					// ni que haya tramos "repetidos"
					if(cb2I.equals(cb3I) || 
					   tramoFueAgregado(estacionesCb.get(cb2I), estacionesCb.get(cb3I)))
						btnAgregar.setEnabled(false);						
					else
						btnAgregar.setEnabled(true);
		 	     };
		a.actionPerformed(null);
		cb2.addActionListener(a);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(lbl3, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(cb3, gbc);
		cb3.addActionListener(a);
		
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 15;
		this.add(btnAgregar, gbc);
		btnAgregar.addActionListener(
			e -> {
					Tramo auxTramo = new Tramo(
						DISTANCIA_DFLT,
						DURACION_DFLT,
						PASAJEROS_DFLT,
						Tramo.Estado.INACTIVO,
						COSTO_DFLT,
						estacionesCb.get(cb2.getSelectedItem()).getId(),
						estacionesCb.get(cb3.getSelectedItem()).getId(),
						lineasCb.get(cb1.getSelectedItem()).getId()
					);
					
					tramosLinea.add(auxTramo);
					try {
						redDeTransporte.addTramo(auxTramo);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					mtt.setData(this.recuperarDatosEstacionesTramos());
					mtt.fireTableDataChanged();
					btnEliminar.setEnabled(true);
					btnCompletarDatos.setEnabled(true);
					//btn1.setEnabled(false);
					// Hacer la estacion destino de un tramo el origen de la siguiente:
					cb2.setSelectedItem(cb3.getSelectedItem());
					cb3.setSelectedIndex((cb3.getSelectedIndex() + 1) % cb3.getItemCount());
				 }
		);
		
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 15;
		if (tramosLinea.isEmpty())
			btnEliminar.setEnabled(false);
		else
			btnEliminar.setEnabled(true);
		this.add(btnEliminar, gbc);
		btnEliminar.addActionListener(
			e -> {
					ArrayList<Integer> filasSeleccionadas = arrayAArrayList(tbl.getSelectedRows());
					LinkedList<Tramo> nuevosTramos = new LinkedList<Tramo>();
					
					for (Integer i = 0; i < tramosLinea.size(); i++)
					{
						if (!filasSeleccionadas.contains(i))
							nuevosTramos.add(tramosLinea.get((int) i));
						else
						{
							try {
								redDeTransporte.deleteTramo(tramosLinea.get((int) i));
							} catch (ClassNotFoundException | SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
					
					tramosLinea = nuevosTramos;
					
					mtt.setData(this.recuperarDatosEstacionesTramos());
					mtt.fireTableDataChanged();
						
					if (tramosLinea.isEmpty())
						btnEliminar.setEnabled(false);
				 }
		);
		
		gbc.gridx = 5;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 15;
		if (tramosLinea.isEmpty())
			btnCompletarDatos.setEnabled(false);
		else
			btnCompletarDatos.setEnabled(true);
		this.add(btnCompletarDatos, gbc);
		btnCompletarDatos.addActionListener(
			e -> {
					ventana.setContentPane(
						new AgregarDatosTramo(tramosLinea.get(tbl.getSelectedRow()), this));
					ventana.pack();
					ventana.setVisible(true);		
				 }
		);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(sp1, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1.0;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 15;
		this.add(btnAceptar, gbc);
		btnAceptar.addActionListener(
			e -> {
					ventana.setContentPane(padre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);

		
	}
	
	private ArrayList<Integer> arrayAArrayList(int[] arr) 
	{
		ArrayList<Integer> arrL = new ArrayList<Integer>();
		for (int i = 0; i < arr.length; i++)
			arrL.add(Integer.valueOf(arr[i]));
		
		return arrL;
	}

	private Object[][] recuperarDatosEstacionesTramos()
	{
		Object[][] datos = new Object[tramosLinea.size()][2];
		Estacion estacionOrigen, estacionDestino;
		
		for (int i = 0; i < tramosLinea.size(); i++)
		{
			estacionOrigen = redDeTransporte.getEstacion(tramosLinea.get(i).getIdOrigen());
			estacionDestino = redDeTransporte.getEstacion(tramosLinea.get(i).getIdDestino());
			
			datos[i][0] = estacionOrigen.getNombre() + " (id: " + estacionOrigen.getId() + ")";
			datos[i][1] = estacionDestino.getNombre() + " (id: " + estacionDestino.getId() + ")";
		}
		
		return datos;
	}
	
	public Boolean tramoFueAgregado(Estacion estacionOrigen, Estacion estacionDestino)
	{
		for (Tramo t : tramosLinea)
			if (t.getIdOrigen().equals(estacionOrigen.getId()) && 
				t.getIdDestino().equals(estacionDestino.getId()))
				return true;
		return false;
	}
	
	class AgregarDatosTramo extends JPanel
	{
		GridBagConstraints gbc;
		JLabel lbl1, lbl2, lbl3, lbl4, lbl5, lbl6;
		JTextField txtf1, txtf2, txtf3, txtf4;
		JButton btn1;
		JComboBox<String> cb4;
		JPanel padre;
		
		Tramo tramo;
		
		public AgregarDatosTramo(Tramo tramo, JPanel padre) 
		{	
			this.tramo = tramo;
			this.padre = padre;
			gbc = new GridBagConstraints();
			this.setLayout(new GridBagLayout());
			this.armarPanel();
		}
		
		private void armarPanel()
		{
			lbl1 = new JLabel(
				redDeTransporte.getEstacion(tramo.getIdOrigen()).getNombre() + " (id: " + tramo.getIdOrigen() + ")  >>>  " + 
				redDeTransporte.getEstacion(tramo.getIdDestino()).getNombre() + " (id: " + tramo.getIdDestino() + ")"
			);
			lbl2 = new JLabel("Distancia [km]");
			lbl3 = new JLabel("Duración del viaje [min]");
			lbl4 = new JLabel("Cantidad máxima de pasajeros");
			lbl5 = new JLabel("Estado");
			lbl6 = new JLabel("Costo [$]");
		
			btn1 = new JButton("Aceptar");
			
			txtf1 = new JTextField(
				tramo.getDistanciaEnKm().equals(DISTANCIA_DFLT)? "" : tramo.getDistanciaEnKm().toString(),
				25
			);
			txtf2 = new JTextField(
				tramo.getDuracionViajeEnMin().equals(DURACION_DFLT)? "" : tramo.getDuracionViajeEnMin().toString(), 
				25
			);
			txtf3 = new JTextField(
				tramo.getCantidadMaximaPasajeros().equals(PASAJEROS_DFLT)? "" : tramo.getCantidadMaximaPasajeros().toString(), 
				25
			);
			txtf4 = new JTextField(
				tramo.getCosto().equals(COSTO_DFLT)? "" : tramo.getCosto().toString(), 
				25
			);
			
			cb4 = new JComboBox<String>();
			cb4.addItem("Activo");
			cb4.addItem("Inactivo");
			cb4.setSelectedItem((tramo.getEstado() == Tramo.Estado.ACTIVO)? "Activo" : "Inactivo");
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.weightx = 1.0;
			gbc.fill = GridBagConstraints.CENTER;
			gbc.insets = new Insets(10, 5, 5, 5);
			this.add(lbl1, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.weightx = 0.25;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(10, 5, 5, 5);
			this.add(lbl2, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(10, 5, 5, 5);
			this.add(txtf1, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 1;
			gbc.weightx = 0.25;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(5, 5, 5, 5);
			this.add(lbl3, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			this.add(txtf2, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 1;
			gbc.weightx = 0.25;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(5, 5, 5, 5);
			this.add(lbl4, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			this.add(txtf3, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.gridwidth = 1;
			gbc.weightx = 0.25;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(5, 5, 5, 5);
			this.add(lbl5, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			this.add(cb4, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.gridwidth = 1;
			gbc.weightx = 0.25;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(10, 5, 5, 5);
			this.add(lbl6, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 5;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(10, 5, 5, 5);
			this.add(txtf4, gbc);
			
			gbc.gridx = 2;
			gbc.gridy = 6;
			gbc.gridwidth = 1;
			gbc.weightx = 0.0;
			gbc.fill = GridBagConstraints.EAST;
			gbc.ipady = 15;
			gbc.insets = new Insets(30, 20, 10, 20);
			this.add(btn1, gbc);
			btn1.addActionListener(
				e -> {
						Tramo.Estado estado;
						if (((String) cb1.getSelectedItem()).equals("Activo")) 
							estado = Tramo.Estado.ACTIVO;
						else	
							estado = Tramo.Estado.INACTIVO;
						
						tramo.setDistanciaEnKm(Double.parseDouble(txtf1.getText()));
						tramo.setDuracionViajeEnMin(Integer.parseInt(txtf2.getText()));
						tramo.setCantidadMaximaPasajeros(Integer.parseInt(txtf3.getText()));
						tramo.setEstado(estado);
						tramo.setCosto(Double.parseDouble(txtf4.getText()));
					
						try {
							redDeTransporte.updateTramo(tramo);
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
						
						ventana.setContentPane(padre);
						ventana.pack();
						ventana.setVisible(true);
					 }			
			);  
		}
	}
	
	class ModeloTablaTramos extends AbstractTableModel
	{
		private String[] nombreColumnas = {"Origen", "Destino"};
		private Object[][] datos = {{"", ""}};
		 
		public void setData(Object[][] datos) 			{ this.datos = datos; 					}
		public int getColumnCount() 					{ return nombreColumnas.length; 		}
		public int getRowCount() 						{ return datos.length; 					}
		public String getColumnName(int col) 			{ return nombreColumnas[col]; 			}
		public Object getValueAt(int row, int col) 		{ return datos[row][col]; 				}
		public Class getColumnClass(int c) 				{ return getValueAt(0, c).getClass(); 	}
		public boolean isCellEditable(int row, int col) { return false;							}
		public void setValueAt(Object value, int row, int col) 	
		{
		    datos[row][col] = value;
		    fireTableCellUpdated(row, col);
		}
	 }
	
	/*
	class DuplaEstaciones
	{
		public Estacion origen;
		public Estacion destino;
		
		public DuplaEstaciones() { } 
		
		public DuplaEstaciones(Estacion origen, Estacion destino) 
		{
			this.origen = origen;
			this.destino = destino;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DuplaEstaciones other = (DuplaEstaciones) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (destino == null) {
				if (other.destino != null)
					return false;
			} else if (!destino.equals(other.destino))
				return false;
			if (origen == null) {
				if (other.origen != null)
					return false;
			} else if (!origen.equals(other.origen))
				return false;
			return true;
		}

		private AgregarTramosLineaDeTransporte getEnclosingInstance() {
			return AgregarTramosLineaDeTransporte.this;
		}
		
		public String toString()
		{
			return this.origen.toString() + " // " + this.destino.toString();
		}

	}
	*/
}
