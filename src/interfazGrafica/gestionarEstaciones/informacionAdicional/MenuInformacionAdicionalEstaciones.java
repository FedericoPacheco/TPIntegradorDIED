package interfazGrafica.gestionarEstaciones.informacionAdicional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import grafo.RedDeTransporte;
import interfazGrafica.utilidades.MenuGenerico;

@SuppressWarnings("serial")
public class MenuInformacionAdicionalEstaciones extends JPanel
{
	private JFrame ventana;
	private MenuGenerico menu;
	
	private RedDeTransporte redDeTransporte;
	
	public MenuInformacionAdicionalEstaciones(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte) 
	{
		this.ventana = ventana;
		this.redDeTransporte = redDeTransporte;
		
		menu = new MenuGenerico(ventana, this, panelPadre);
		
		this.completarComponentes();
		menu.armar();
	}
	
	private void completarComponentes()
	{
		JButton btnFlujoMaximo = new JButton("Flujo máximo entre dos estaciones");
		btnFlujoMaximo.addActionListener(
			e -> {
					ventana.setContentPane(new FlujoMaximo(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		
		JButton btnEstacionRank = new JButton("Estación-rank");
		btnEstacionRank.addActionListener(
			e -> {
					ventana.setContentPane(new EstacionRank(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		
		JButton btnProximoMantenimiento = new JButton("Próximas estaciones a realizar mantenimientos");
		btnProximoMantenimiento.addActionListener(
			e -> {
					ventana.setContentPane(new ProximoMantenimiento(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		
		
		menu
			.addComponente(btnFlujoMaximo)
			.addComponente(btnEstacionRank)
			.addComponente(btnProximoMantenimiento);
	}
}
