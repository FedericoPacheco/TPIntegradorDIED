package interfazGrafica.gestionarLineasDeTransporte;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import grafo.RedDeTransporte;
import interfazGrafica.gestionarLineasDeTransporte.abmc.AgregarLineaDeTransporte;
import interfazGrafica.gestionarLineasDeTransporte.abmc.ConsultarYModificarLineasDeTransporte;
import interfazGrafica.gestionarLineasDeTransporte.abmc.EliminarLineaDeTransporte;
import interfazGrafica.gestionarLineasDeTransporte.abmc.GestionarTramosLineaDeTransporte;
import interfazGrafica.utilidades.MenuGenerico;

@SuppressWarnings("serial")
public class MenuLineasDeTransporte extends JPanel
{
	private JFrame ventana;
	private MenuGenerico menu;
	
	private RedDeTransporte redDeTransporte;
	
	public MenuLineasDeTransporte(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		
		menu = new MenuGenerico(ventana, this, panelPadre);
		
		this.completarComponentes();
		menu.armar();
	}
	
	private void completarComponentes() 
	{
		JButton btnAgregar = new JButton("Agregar línea de transporte");
		btnAgregar.addActionListener(
			e -> { 
					ventana.setContentPane(new AgregarLineaDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 } 
		);
		
		JButton btnConsultarModificar = new JButton("Consultar y/o modificar datos básicos de líneas de transporte");
		btnConsultarModificar.addActionListener(
			e -> { 
					ventana.setContentPane(new ConsultarYModificarLineasDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 }		
		); 
		
		JButton btnEliminar = new JButton("Eliminar línea de transporte");
		btnEliminar.addActionListener(
			e -> { 
					ventana.setContentPane(new EliminarLineaDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
			 	 }
		);
		
		JButton btnGestionarTramos = new JButton("Gestionar tramos de línea de transporte");
		btnGestionarTramos.addActionListener(
			e -> { 
					ventana.setContentPane(new GestionarTramosLineaDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
			 	 }
		);
		
		
		menu
			.addComponente(btnAgregar)
			.addComponente(btnConsultarModificar)
			.addComponente(btnEliminar)
			.addComponente(btnGestionarTramos);
	}
}
