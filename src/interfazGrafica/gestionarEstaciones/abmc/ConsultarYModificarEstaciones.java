package interfazGrafica.gestionarEstaciones.abmc;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import entidades.valueObjects.Estacion;
import entidades.valueObjects.TareaDeMantenimiento;
import grafo.RedDeTransporte;
import interfazGrafica.utilidades.ModeloTablaGenerico;

// https://stackoverflow.com/questions/3029079/how-to-disable-main-jframe-when-open-new-jframe
// https://stackoverflow.com/questions/29807260/how-to-close-current-jframe

@SuppressWarnings("serial")
public class ConsultarYModificarEstaciones extends JPanel implements TableModelListener
{	
	private GridBagConstraints gbc;
	private JButton btn1;
	private JTable tbl;
	private ModeloTablaGenerico mTbl;
	private JScrollPane sp;
	private JFrame ventana;
	private JPanel padre;
	private JComboBox<String> estado;
	
	private DateTimeFormatter formatoHora;
	private List<Estacion> estaciones;
	private RedDeTransporte redDeTransporte;

	public ConsultarYModificarEstaciones(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		estaciones = redDeTransporte.getAllEstaciones();
		
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
		
	    this.setLayout(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    this.armarPanel();
	}
	
	private void armarPanel()
	{
		mTbl = new ModeloTablaGenerico();
		mTbl.addColumna("Id").addColumna("Nombre").addColumna("Horario de apertura")
			.addColumna("Horario de cierre").addColumna("Estado");
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
		tbl.getColumnModel().getColumn(4).setCellRenderer(centrarDatos);
		// -----------------------------------
	    sp = new JScrollPane(tbl);
		
	    estado = new JComboBox<String>();
		estado.addItem("Operativa");
		estado.addItem("En mantenimiento");
		
		tbl.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(estado));
		tbl.setPreferredScrollableViewportSize(new Dimension(600, 200));
	    tbl.getModel().addTableModelListener(this);
	    tbl.getColumnModel().getColumn(0).setPreferredWidth(20);
	    tbl.getColumnModel().getColumn(4).setPreferredWidth(120);
	    gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.ipady = 0;
		this.add(sp, gbc);
		
		btn1 = new JButton("Volver");
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
	
	public Object[][] recuperarDatos()
	{
		Object[][] datos = new Object[estaciones.size()][5];
		for (int i = 0; i < estaciones.size(); i++)
		{
			datos[i][0] = estaciones.get(i).getId();
			datos[i][1] = estaciones.get(i).getNombre();
			datos[i][2] = estaciones.get(i).getHoraApertura().toString();
			datos[i][3] = estaciones.get(i).getHoraCierre().toString();
			datos[i][4] = (estaciones.get(i).getEstado() == Estacion.Estado.OPERATIVA)? "Operativa" : "En Mantenimiento";
		}
		
		return datos;
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
