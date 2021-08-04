package interfazGrafica.gestionarLineasDeTransporte.abmc;

import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.LineaDeTransporte;
import grafo.RedDeTransporte;
import interfazGrafica.utilidades.AgregarEntidadGenerico;

@SuppressWarnings("serial")
public class AgregarLineaDeTransporte extends JPanel
{
	private RedDeTransporte redDeTransporte;
	private AgregarEntidadGenerico agregarLinea;
	
	public AgregarLineaDeTransporte(JFrame ventana, JPanel panePadre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		agregarLinea = new AgregarEntidadGenerico(ventana, this, panePadre);
		
		this.completarComponentes();
		agregarLinea.armar();
	}
	
	private void completarComponentes() 
	{
		agregarLinea
			.addEtiqueta("Nombre")
			.addEtiqueta("Color")
			.addEtiqueta("Estado");
			
		JTextField txtfNombre = new JTextField(25);
		JTextField txtfColor = new JTextField(25);
		
		JComboBox<String> cbEstado = new JComboBox<String>();
		cbEstado.addItem("Activa");
		cbEstado.addItem("No Activa");
		
		agregarLinea
			.addComponente(txtfNombre)
			.addComponente(txtfColor)
			.addComponente(cbEstado);
		
		agregarLinea.setAccionAceptar(
			e -> {
					LineaDeTransporte.Estado estado;
					if (((String) cbEstado.getSelectedItem()).equals("Activa")) 
						estado = LineaDeTransporte.Estado.ACTIVA;
					else	
						estado = LineaDeTransporte.Estado.INACTIVA;
				
					LineaDeTransporte linea = new LineaDeTransporte(				  
						txtfNombre.getText(),
						txtfColor.getText(),
						estado
					);
					
					try {
						redDeTransporte.addLineaDeTransporte(linea);
					} catch (SQLException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					
					txtfNombre.setText("");
					txtfColor.setText("");
					cbEstado.setSelectedItem("Activa");
				 }
			);
	}
}
