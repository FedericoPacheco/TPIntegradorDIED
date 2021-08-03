package interfazGrafica.gestionarEstaciones;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import grafo.RedDeTransporte;
import interfazGrafica.gestionarEstaciones.informacionAdicional.MenuInformacionAdicionalEstaciones;
import interfazGrafica.utilidades.Menu;

@SuppressWarnings("serial")
public class MenuEstaciones extends JPanel
{
	private JFrame ventana;
	private Menu menu;
	private RedDeTransporte redDeTransporte;
	
	public MenuEstaciones(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		menu = new Menu(ventana, this, panelPadre);
		
		this.completarComponentes();
		menu.armar();
	}
	
	private void completarComponentes() 
	{
		JButton btnAgregar = new JButton("Agregar estación");
		btnAgregar.addActionListener(
			e -> { 
					ventana.setContentPane(new AgregarEstacion(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 } 
		);
		
		
		JButton btnConsultarModificar = new JButton("Consultar y/o modificar estaciones");
		btnConsultarModificar.addActionListener(
			e -> { 
					ventana.setContentPane(new ConsultarYModificarEstaciones(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 }		
		); 
		
		JButton btnEliminar = new JButton("Eliminar estación");
		btnEliminar.addActionListener(
			e -> { 
					ventana.setContentPane(new EliminarEstacion(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
			 	 }
		);
		
		JButton	btnInfoAdicional = new JButton("Información adicional de las estaciones");
		btnInfoAdicional.addActionListener(
			e -> { 
					ventana.setContentPane(new MenuInformacionAdicionalEstaciones(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
			 	 }
		);
		
		
		menu
			.addComponente(btnAgregar)
			.addComponente(btnConsultarModificar)
			.addComponente(btnEliminar)
			.addComponente(btnEliminar)
			.addComponente(btnInfoAdicional);
	}
}

