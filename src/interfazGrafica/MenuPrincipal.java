package interfazGrafica;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.imgscalr.Scalr;

import clasesUtiles.GUIMenuGenerico;
import grafo.RedDeTransporte;
import interfazGrafica.gestionarEstaciones.MenuEstaciones;
import interfazGrafica.gestionarLineasDeTransporte.MenuLineasDeTransporte;
import interfazGrafica.venderUnBoleto.VentaDeBoleto;

@SuppressWarnings("serial")
public class MenuPrincipal extends JPanel
{
	private JFrame ventana;
	private GUIMenuGenerico menu;
	private RedDeTransporte redDeTransporte;
	
	public MenuPrincipal(JFrame ventana, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		
		menu = new GUIMenuGenerico(ventana, this, null);
		
		this.completarComponentes();
		menu.armar();
	}
	
	private void completarComponentes()
	{	
		JLabel lblSistema = new JLabel("Sistema de gestión de transporte multimodal");
		lblSistema.setFont(new Font("Serif", Font.BOLD, 20));

		
		/*
			* Como colocar imagen:
				https://stackoverflow.com/a/2706730
			* Cambiar tamanio imagen: 
				https://stackoverflow.com/a/21975099
				https://mvnrepository.com/artifact/org.imgscalr/imgscalr-lib/4.2
			* Imagen menu principal (fue modificada):
				https://www.pinclipart.com/downpngs/iibboho_transport-png-free-download-modes-of-transport-png/
			* Working directory: 
				https://stackoverflow.com/a/7603444
		*/
		BufferedImage imagen = null;
		JLabel lblImagen = null;
		try { 
			imagen = ImageIO.read(new File(System.getProperty("user.dir") + "\\imagenMenuPrincipal.png"));
			imagen = Scalr.resize(imagen, Scalr.Method.BALANCED, 200, 200);
		} catch (IOException e1) { 
			e1.printStackTrace();
		}
		if (imagen != null)
			lblImagen = new JLabel(new ImageIcon(imagen));
		else
			lblImagen = new JLabel("");
			
		
		JButton btnGestionarEstaciones = new JButton("Gestionar estaciones");
		btnGestionarEstaciones.addActionListener(
			e -> { 
					ventana.setContentPane(new MenuEstaciones(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 } 
		);
	
		JButton btnGestionarLineas = new JButton("Gestionar líneas de transporte");
		btnGestionarLineas.addActionListener(
			e -> {
					ventana.setContentPane(new MenuLineasDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		
		JButton btnVenderUnBoleto = new JButton("Vender un boleto");
		btnVenderUnBoleto.addActionListener(
			e -> {
					ventana.setContentPane(new VentaDeBoleto(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);
			 	 }
		);
		
		
		menu
			.addComponente(lblSistema)
			.addComponente(lblImagen)
			.addComponente(btnGestionarEstaciones)
			.addComponente(btnGestionarLineas)
			.addComponente(btnVenderUnBoleto);
	}
}
