package interfazGrafica.gestionarEstaciones;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import entidades.Estacion;
import entidades.TareaDeMantenimiento;
import grafo.RedDeTransporte;

// https://stackoverflow.com/questions/3029079/how-to-disable-main-jframe-when-open-new-jframe
// https://stackoverflow.com/questions/29807260/how-to-close-current-jframe

@SuppressWarnings("serial")
public class ConsultarYModificarEstaciones extends JPanel implements TableModelListener
{
	class ModeloTablaEstaciones extends AbstractTableModel
	{
		private String[] nombreColumnas = {"Id", "Nombre", "Horario de apertura", "Horario de cierre", "Estado"};
		private Object[][] datos = { {"", "", "", "", ""} };
		 
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
	private ModeloTablaEstaciones modeloTabla;
	private JScrollPane sp;
	private JFrame ventana;
	private JPanel padre;
	private JComboBox<String> estado;
	
	private DateTimeFormatter formatoHora;
	private List<Estacion> estaciones;
	private Object[][] datos;
	private RedDeTransporte redDeTransporte;

	public ConsultarYModificarEstaciones(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		estaciones = redDeTransporte.getAllEstaciones();
		datos = new Object[estaciones.size()][5];
		
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
	    this.setLayout(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    this.armarPanel();
	}
	
	@SuppressWarnings("unchecked")
	private void armarPanel()
	{
		btn1 = new JButton("Volver");
		estado = new JComboBox<String>();
		modeloTabla = new ModeloTablaEstaciones();
		tabla = new JTable(modeloTabla);
	    sp = new JScrollPane(tabla);
		
		estado.addItem("Operativa");
		estado.addItem("En mantenimiento");
		
		tabla.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(estado));
		tabla.setPreferredScrollableViewportSize(new Dimension(600, 200));
	    tabla.getModel().addTableModelListener(this);
	    tabla.getColumnModel().getColumn(0).setPreferredWidth(20);
	    tabla.getColumnModel().getColumn(4).setPreferredWidth(120);
	
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
	public void tableChanged(TableModelEvent e) 
	{
		int i = e.getFirstRow();
        int j = e.getColumn();
        Object datoModificado = ((TableModel) e.getSource()).getValueAt(i, j);
        
        try 
        {
        	switch(j)
     		{
            	case 1:
            		estaciones.get(i).setNombre((String) datoModificado);
             		break;
             	case 2: 
             		estaciones.get(i).setHoraApertura(LocalTime.parse((String) datoModificado, formatoHora));
             		break;
             	case 3: 
             		estaciones.get(i).setHoraCierre(LocalTime.parse((String) datoModificado, formatoHora));
             		break;
             	case 4: 
             		if (estado.getSelectedItem().equals("Operativa"))
             		{
             			if (estaciones.get(i).getEstado() == Estacion.Estado.EN_MANTENIMIENTO)
             			{
             				this.finalizarTareaDeMantenimiento(estaciones.get(i));
             				estaciones.get(i).setEstado(Estacion.Estado.OPERATIVA);
             			}
             		}
             		else
             		{
             			if (estaciones.get(i).getEstado() == Estacion.Estado.OPERATIVA)
     					{
     						new ObservacionesMantenimiento(ventana, estaciones.get(i), redDeTransporte);
     						estaciones.get(i).setEstado(Estacion.Estado.EN_MANTENIMIENTO);
     					}
             		}
             		break;
     		}
        	
        	redDeTransporte.updateEstacion(estaciones.get(i));
        }
        catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void recuperarDatos()
	{
		for (Integer i = 0; i < estaciones.size(); i++)
		{
			datos[i][0] = estaciones.get(i).getId();
			datos[i][1] = estaciones.get(i).getNombre();
			datos[i][2] = estaciones.get(i).getHoraApertura().toString();
			datos[i][3] = estaciones.get(i).getHoraCierre().toString();
			datos[i][4] = (estaciones.get(i).getEstado() == Estacion.Estado.OPERATIVA)? "Operativa" : "En Mantenimiento";
		}
	}
	
	public void finalizarTareaDeMantenimiento(Estacion estacion) throws ClassNotFoundException, SQLException 
	{
		Integer idUltitmoMantenimiento = 
			estacion.getIdsMantenimientosRealizados().get(estacion.getIdsMantenimientosRealizados().size() - 1);
		TareaDeMantenimiento ultimoMantenimiento = redDeTransporte.getTareaDeMantenimiento(idUltitmoMantenimiento);
		ultimoMantenimiento.setFechaFin(LocalDate.now());
		redDeTransporte.updateTareaDeMantenimiento(ultimoMantenimiento, estacion);
	}
}
