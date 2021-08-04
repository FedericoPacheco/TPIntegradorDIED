package interfazGrafica.gestionarLineasDeTransporte.abmc;

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
import javax.swing.table.DefaultTableCellRenderer;

import entidades.valueObjects.Estacion;
import entidades.valueObjects.LineaDeTransporte;
import entidades.valueObjects.Tramo;
import grafo.RedDeTransporte;
import interfazGrafica.utilidades.GUIAgregarEntidadGenerico;
import interfazGrafica.utilidades.ModeloTablaGenerico;

// https://stackoverflow.com/questions/3179136/jtable-how-to-refresh-table-model-after-insert-delete-or-update-the-data

@SuppressWarnings("serial")
public class GestionarTramosLineaDeTransporte extends JPanel 
{
	private ModeloTablaGenerico mt;
	private JTable tbl;
	private JScrollPane sp;
	private JComboBox<String> cbLinea, cbOrigen, cbDestino;
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
	private static final Double DISTANCIA_DFLT = (double) (Integer.MAX_VALUE - 1);
	private static final Integer DURACION_DFLT = Integer.MAX_VALUE - 1;
	private static final Integer PASAJEROS_DFLT = 0;
	private static final Double COSTO_DFLT = (double) (Integer.MAX_VALUE - 1);
	
	public GestionarTramosLineaDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte) 
	{	
		this.ventana = ventana;
		this.padre = padre;
		this.redDeTransporte = redDeTransporte;
	
		estacionesCb = new HashMap<String, Estacion>();
		lineasCb = new HashMap<String, LineaDeTransporte>();
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}

	private void armarPanel() 
	{
		cbLinea = new JComboBox<String>();
		cbOrigen = new JComboBox<String>();
		cbDestino = new JComboBox<String>();
		
		for (LineaDeTransporte l : redDeTransporte.getAllLineasDeTransporte())
		{
			lineasCb.put(l.getNombre() + " (id: " + l.getId() + ")", l);
			cbLinea.addItem(l.getNombre() + " (id: " + l.getId() + ")");
		}
		
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			estacionesCb.put(e.getNombre() + " (id: " + e.getId() + ")", e);
			cbOrigen.addItem(e.getNombre() + " (id: " + e.getId() + ")");
			cbDestino.addItem(e.getNombre() + " (id: " + e.getId() + ")");
		}
	
		tramosLinea = new ArrayList<Tramo>();
		for (Integer idTramo : lineasCb.get(cbLinea.getSelectedItem()).getIdsTramos())
			tramosLinea.add(redDeTransporte.getTramo(idTramo));
		
		
		btnAgregar = new JButton("Agregar");
		btnEliminar = new JButton("Eliminar");
		btnAceptar = new JButton("Volver");
		btnCompletarDatos = new JButton("Completar datos");
		
		lbl1 = new JLabel("Seleccione una línea de transporte: ");
		lbl2 = new JLabel("Tramos");
		lbl3 = new JLabel(">>>");
		
		mt = new ModeloTablaGenerico();
		mt.addColumna("Estación origen").addColumna("Estación destino");
		tbl = new JTable(mt);
		// https://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centrarDatos = new DefaultTableCellRenderer();
		centrarDatos.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centrarDatos);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centrarDatos);
		// -----------------------------------
		// https://stackoverflow.com/a/32942079
		tbl.getSelectionModel().addListSelectionListener( // Activa los botones cuando se selecciona una columna											  
			e -> {	
					if (!e.getValueIsAdjusting()) // Metodo magico que evita que el cuerpo del if se ejecute dos veces						  
					{
						btnEliminar.setEnabled(true);
						btnCompletarDatos.setEnabled(true);
					}
				 }
		);
		
		sp = new JScrollPane(tbl);
		mt.setDatos(this.recuperarDatosEstacionesTramos());
		
	
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
		this.add(cbLinea, gbc);
		cbLinea.addActionListener(
			e -> { 
					tramosLinea = new ArrayList<Tramo>();
					for (Integer idTramo : lineasCb.get(cbLinea.getSelectedItem()).getIdsTramos())
						tramosLinea.add(redDeTransporte.getTramo(idTramo));
				
					mt.setDatos(this.recuperarDatosEstacionesTramos());
					mt.fireTableDataChanged();
					
					btnEliminar.setEnabled(false);
					btnCompletarDatos.setEnabled(false);
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
		this.add(cbOrigen, gbc);
		ActionListener a = 
			e -> {
					String cb2I = (String) cbOrigen.getSelectedItem();
					String cb3I = (String) cbDestino.getSelectedItem();
					
					// Controlar que un tramo no tenga de origen y destino la misma estacion,
					// ni que haya tramos "repetidos"
					if(cb2I.equals(cb3I) || 
					   tramoFueAgregado(estacionesCb.get(cb2I), estacionesCb.get(cb3I)))
						btnAgregar.setEnabled(false);						
					else
						btnAgregar.setEnabled(true);
		 	     };
		a.actionPerformed(null);
		cbOrigen.addActionListener(a);
		
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
		this.add(cbDestino, gbc);
		cbDestino.addActionListener(a);
		
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
						estacionesCb.get(cbOrigen.getSelectedItem()).getId(),
						estacionesCb.get(cbDestino.getSelectedItem()).getId(),
						lineasCb.get(cbLinea.getSelectedItem()).getId()
					);
					
					tramosLinea.add(auxTramo);
					try {
						redDeTransporte.addTramo(auxTramo);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					mt.setDatos(this.recuperarDatosEstacionesTramos());
					mt.fireTableDataChanged();
					btnEliminar.setEnabled(false);
					btnCompletarDatos.setEnabled(false);
	
					// Hacer la estacion destino de un tramo el origen de la siguiente:
					cbOrigen.setSelectedItem(cbDestino.getSelectedItem());
					cbDestino.setSelectedIndex((cbDestino.getSelectedIndex() + 1) % cbDestino.getItemCount());
				 }
		);
		
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 15;
		btnEliminar.setEnabled(false);
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
					
					mt.setDatos(this.recuperarDatosEstacionesTramos());
					mt.fireTableDataChanged();
						
					btnEliminar.setEnabled(false);
					btnCompletarDatos.setEnabled(false);
				 }
		);
		
		gbc.gridx = 5;
		gbc.gridy = 2;
		gbc.weightx = 0.16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 15;
		btnCompletarDatos.setEnabled(false);
		this.add(btnCompletarDatos, gbc);
		btnCompletarDatos.addActionListener(
			e -> {
					int[] filasSeleccionadas = tbl.getSelectedRows();
					
					for (int i = 0; i < filasSeleccionadas.length; i++)
						new AgregarDatosTramo(tramosLinea.get(i));	
				}
		);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.ipady = 0;
		this.add(sp, gbc);
		
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
	
	private class AgregarDatosTramo extends JPanel
	{
		private Tramo tramo;
		
		private GUIAgregarEntidadGenerico agregarTramo;
		
		public AgregarDatosTramo(Tramo tramo) 
		{	
			this.tramo = tramo;
			
			agregarTramo = new GUIAgregarEntidadGenerico(
					this, 
					ventana,
					redDeTransporte.getEstacion(tramo.getIdOrigen()).getNombre() + 
					" (id: " + tramo.getIdOrigen() + ")  >>>  " + 
					redDeTransporte.getEstacion(tramo.getIdDestino()).getNombre() + 
					" (id: " + tramo.getIdDestino() + ")"
			);
			
			this.completarComponentes();
			agregarTramo.armar();
		}
		
		private void completarComponentes()
		{
			agregarTramo
				.addEtiqueta("Distancia [km]")
				.addEtiqueta("Duración del viaje [min]")
				.addEtiqueta("Cantidad máxima de pasajeros")
				.addEtiqueta("Estado")
				.addEtiqueta("Costo [$]");
				
			
			JTextField txtfDistancia = new JTextField(
				tramo.getDistanciaEnKm().equals(DISTANCIA_DFLT)? "" : tramo.getDistanciaEnKm().toString(),
				25
			);
			JTextField txtfDuracion = new JTextField(
				tramo.getDuracionViajeEnMin().equals(DURACION_DFLT)? "" : tramo.getDuracionViajeEnMin().toString(), 
				25
			);
			JTextField txtfPasajeros = new JTextField(
				tramo.getCantidadMaximaPasajeros().equals(PASAJEROS_DFLT)? "" : tramo.getCantidadMaximaPasajeros().toString(), 
				25
			);
			JTextField txtfCosto = new JTextField(
				tramo.getCosto().equals(COSTO_DFLT)? "" : tramo.getCosto().toString(), 
				25
			);
			
			JComboBox<String> cbEstado = new JComboBox<String>();
			cbEstado.addItem("Activo");
			cbEstado.addItem("Inactivo");
			if(redDeTransporte.getLineaDeTransporte(tramo.getIdLineaDeTransporte()).getEstado() == LineaDeTransporte.Estado.INACTIVA)
			{
				cbEstado.setSelectedItem("Inactivo");
				cbEstado.setEnabled(false);
			}
			else
				cbEstado.setSelectedItem((tramo.getEstado() == Tramo.Estado.ACTIVO)? "Activo" : "Inactivo");
			
			
			agregarTramo.setAccionAceptar(
				e -> {
						Tramo.Estado estado;
						if (((String) cbEstado.getSelectedItem()).equals("Activo")) 
							estado = Tramo.Estado.ACTIVO;
						else	
							estado = Tramo.Estado.INACTIVO;
						
						tramo.setDistanciaEnKm(Double.parseDouble(txtfDistancia.getText()));
						tramo.setDuracionViajeEnMin(Integer.parseInt(txtfDuracion.getText()));
						tramo.setCantidadMaximaPasajeros(Integer.parseInt(txtfPasajeros.getText()));
						tramo.setEstado(estado);
						tramo.setCosto(Double.parseDouble(txtfCosto.getText()));
					
						try {
							redDeTransporte.updateTramo(tramo);
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
						
						lineasCb.get(cbLinea.getSelectedItem()).addIdTramo(tramo.getId());;
					 }			
			);  
			

			agregarTramo
				.addComponente(txtfDistancia)
				.addComponente(txtfDuracion)
				.addComponente(txtfPasajeros)
				.addComponente(cbEstado)
				.addComponente(txtfCosto);
		}
	}
}
