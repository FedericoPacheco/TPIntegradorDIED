package interfazGrafica.utilidades;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import grafo.RedDeTransporte;

public class AgregarEntidad 
{
	List<String> etiquetas;
	List<JComponent> componentes;
	ActionListener accionAceptar;
	private GridBagConstraints gbc;
	
	private JFrame ventana;
	private JPanel panel;
	private JPanel panelPadre;
	
	public AgregarEntidad(JFrame ventana, JPanel panel, JPanel panelPadre, ActionListener accionAceptar)
	{
		this.ventana = ventana;
		this.panelPadre = panelPadre;
		this.panel = panel;
		this.accionAceptar = accionAceptar;
		
		componentes = new LinkedList<JComponent>();
		etiquetas = new LinkedList<String>();
		gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
	}
	
	public void armar()
	{
		int i = 0;
		for (JComponent c : componentes)
		{
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.gridwidth = 1;
			gbc.weightx = 0.25;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(5, 5, 5, 5);
			panel.add(new JLabel(etiquetas.get(i)), gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			panel.add(componentes.get(i), gbc);
			
			i++;
		}
		
		
		JButton btnAceptar = new JButton("Aceptar");
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.EAST;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		panel.add(btnAceptar, gbc);
		btnAceptar.addActionListener(accionAceptar);
		
		JButton btnVolver = new JButton("Volver");
		gbc.gridx = 1;
		gbc.gridy = i;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.WEST;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		panel.add(btnVolver, gbc);
		btnVolver.addActionListener(
			e -> {
					ventana.setContentPane(panelPadre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
	}
	
	public AgregarEntidad addEtiqueta(String etiqueta) {
		etiquetas.add(etiqueta);
		return this;
	}
	
	public AgregarEntidad addComponente(JComponent componente) {
		componentes.add(componente);
		return this;
	}
	
	
}
