package interfazGrafica.gestionarEstaciones.abmc;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clasesUtiles.GUIAgregarEntidadGenerico;
import entidades.valueObjects.Estacion;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class AgregarEstacion extends JPanel
{
	private JFrame ventana;
	
	private RedDeTransporte redDeTransporte;
	private GUIAgregarEntidadGenerico agregarEstacion;
	
	public AgregarEstacion(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		
		agregarEstacion = new GUIAgregarEntidadGenerico(ventana, this, panelPadre);
		
		this.completarComponentes();
		agregarEstacion.armar();
	}
	
	private void completarComponentes() 
	{
		agregarEstacion
			.addEtiqueta("Nombre")
			.addEtiqueta("Horario de apertura")
			.addEtiqueta("Horario de cierre")
			.addEtiqueta("Estado");
		
		JTextField txtfNombre = new JTextField(25);     
		JTextField txtfHoraApertura = new JTextField(25);
		JTextField txtfHoraCierre = new JTextField(25);
		
		JComboBox<String> cbEstado = new JComboBox<String>();
		cbEstado.addItem("Operativa");
		cbEstado.addItem("En mantenimiento");
		
		agregarEstacion
			.addComponente(txtfNombre)
			.addComponente(txtfHoraApertura)
			.addComponente(txtfHoraCierre)
			.addComponente(cbEstado);
		
		agregarEstacion.setAccionAceptar(
			e -> { 
					if (txtfNombre.getText().equals("") || txtfHoraApertura.getText().equals("") || txtfHoraCierre.getText().equals(""))
						JOptionPane.showMessageDialog(ventana, "Complete los datos restantes, por favor.", "", JOptionPane.ERROR_MESSAGE);
					else
					{
						Estacion.Estado estado;
						if (((String) cbEstado.getSelectedItem()).equals("Operativa")) 
							estado = Estacion.Estado.OPERATIVA;
						else	
							estado = Estacion.Estado.EN_MANTENIMIENTO;						
						
						DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
						
						try
						{
							Estacion estacion = new Estacion(
								txtfNombre.getText(),
								LocalTime.parse(txtfHoraApertura.getText(), formatoHora),
								LocalTime.parse(txtfHoraCierre.getText(), formatoHora),
								estado
							);
							
							redDeTransporte.addEstacion(estacion);
							
							if (estado == Estacion.Estado.EN_MANTENIMIENTO)
								new ObservacionesMantenimiento(ventana, estacion, redDeTransporte);
							
							txtfNombre.setText("");
							txtfHoraApertura.setText("");
							txtfHoraCierre.setText("");
							cbEstado.setSelectedItem("Operativa");
						}
						catch(DateTimeException e1) 
						{
							JOptionPane.showMessageDialog(ventana, "Hora de apertura o cierre incorrectos.", "", JOptionPane.INFORMATION_MESSAGE);
							txtfHoraApertura.setText("");
							txtfHoraCierre.setText("");
						}
						catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				 }			
		);  
	}
}
