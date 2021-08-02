package interfazGrafica.gestionarLineasDeTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class MenuLineasDeTransporte extends JPanel
{
	private GridBagConstraints gbc;
	private JButton btn1, btn2, btn3, btn4, btn5;
	private JFrame ventana;
	private JPanel padre;
	
	private RedDeTransporte redDeTransporte;
	
	public MenuLineasDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}
	
	private void armarPanel() 
	{
		btn1 = new JButton("Agregar línea de transporte");
		btn2 = new JButton("Consultar y/o modificar datos básicos de líneas de transporte");
		btn3 = new JButton("Eliminar línea de transporte");
		btn4 = new JButton("Gestionar tramos de línea de transporte");
		btn5 = new JButton("Volver");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 20, 5, 20);
		this.add(btn1, gbc);
		btn1.addActionListener(
			e -> { 
					ventana.setContentPane(new AgregarLineaDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 } 
		);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 20, 5, 20);
		this.add(btn2, gbc);
		btn2.addActionListener(
			e -> { 
					ventana.setContentPane(new ConsultarYModificarLineasDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
				 }		
		); 
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 20, 5, 20);
		this.add(btn3, gbc);
		btn3.addActionListener(
			e -> { 
					ventana.setContentPane(new EliminarLineaDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
			 	 }
		);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 20, 5, 20);
		this.add(btn4, gbc);
		btn4.addActionListener(
			e -> { 
					ventana.setContentPane(new GestionarTramosLineaDeTransporte(ventana, this, redDeTransporte));
					ventana.pack();
					ventana.setVisible(true);	
			 	 }
		); 
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(30, 20, 10, 20);
		this.add(btn5, gbc);
		btn5.addActionListener(
			e -> {
					ventana.setContentPane(padre);
					ventana.pack();
					ventana.setVisible(true);
				 } 
		);
	}
}
