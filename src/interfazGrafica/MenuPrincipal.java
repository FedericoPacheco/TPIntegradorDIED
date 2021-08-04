package interfazGrafica;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import grafo.RedDeTransporte;
import interfazGrafica.gestionarEstaciones.MenuEstaciones;
import interfazGrafica.gestionarLineasDeTransporte.MenuLineasDeTransporte;
import interfazGrafica.utilidades.MenuGenerico;
import interfazGrafica.ventaDeBoleto.VentaDeBoleto;

@SuppressWarnings("serial")
public class MenuPrincipal extends JPanel
{
	private JFrame ventana;
	private MenuGenerico menu;
	private RedDeTransporte redDeTransporte;
	
	public MenuPrincipal(JFrame ventana, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		
		menu = new MenuGenerico(ventana, this, null);
		
		this.completarComponentes();
		menu.armar();
	}
	
	private void completarComponentes()
	{	
		JLabel lblSistema = new JLabel("Sistema de gestión de transporte multimodal");
		lblSistema.setFont(new Font("Serif", Font.BOLD, 16));

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
		
		JButton btnVentaBoleto = new JButton("Venta de boleto");
		btnVentaBoleto.addActionListener(
			e -> {
					ventana.setContentPane(new VentaDeBoleto(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);
			 	 }
		);
		
		menu
			.addComponente(lblSistema)
			.addComponente(btnGestionarEstaciones)
			.addComponente(btnGestionarLineas)
			.addComponente(btnVentaBoleto);
	}
}
