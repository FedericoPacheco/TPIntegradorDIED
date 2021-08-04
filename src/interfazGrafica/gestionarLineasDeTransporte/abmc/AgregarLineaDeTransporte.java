package interfazGrafica.gestionarLineasDeTransporte.abmc;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.valueObjects.LineaDeTransporte;
import grafo.RedDeTransporte;
import interfazGrafica.utilidades.GUIAgregarEntidadGenerico;

@SuppressWarnings("serial")
public class AgregarLineaDeTransporte extends JPanel
{
	private RedDeTransporte redDeTransporte;
	private GUIAgregarEntidadGenerico agregarLinea;
	
	private static String colorHex = "#FFFFFF";
	
	public AgregarLineaDeTransporte(JFrame ventana, JPanel panePadre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		agregarLinea = new GUIAgregarEntidadGenerico(ventana, this, panePadre);
		
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
		
		//https://stackoverflow.com/a/26565256
		JButton btnSeleccionColor = new JButton("Seleccionar");
		btnSeleccionColor.addActionListener(
			e -> {
					Color colorSeleccionado = JColorChooser.showDialog(null, "", Color.BLACK);
					
					if (colorSeleccionado != null)
					{
						colorHex = "#" + 
									Integer.toHexString(colorSeleccionado.getRed()) + 
									Integer.toHexString(colorSeleccionado.getGreen()) + 
									Integer.toHexString(colorSeleccionado.getBlue());
					}
				 }
		); 
		
		JComboBox<String> cbEstado = new JComboBox<String>();
		cbEstado.addItem("Activa");
		cbEstado.addItem("No Activa");
		
		agregarLinea
			.addComponente(txtfNombre)
			//.addComponente(txtfColor)
			.addComponente(btnSeleccionColor)
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
						colorHex,
						estado
					);
					
					try {
						redDeTransporte.addLineaDeTransporte(linea);
					} catch (SQLException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					
					txtfNombre.setText("");
					cbEstado.setSelectedItem("Activa");
				 }
			);
	}
}
