package interfazGrafica.utilidades;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AgregarEntidad 
{
	private JButton btnAceptar;
	private List<String> etiquetas;
	private List<JComponent> componentes;
	private ActionListener accionAceptar;
	private List<ActionListener> accionesAuxiliares;
	private GridBagConstraints gbc;
	
	private JFrame ventana;
	private JPanel panel;
	private JPanel panelPadre;
	
	public AgregarEntidad(JFrame ventana, JPanel panel, JPanel panelPadre)
	{
		this.ventana = ventana;
		this.panelPadre = panelPadre;
		this.panel = panel;
		
		btnAceptar = new JButton("Aceptar");
		componentes = new LinkedList<JComponent>();
		etiquetas = new LinkedList<String>();
		accionAceptar = e -> { };
		accionesAuxiliares = new LinkedList<ActionListener>();
		gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
	}
	
	public void armar()
	{
		for (ActionListener a : accionesAuxiliares)
			a.actionPerformed(null);
		
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
			gbc.gridy = i;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			panel.add(c, gbc);
			
			i++;
		}
		
		// Aceptar
		gbc.gridx = 2;
		gbc.gridy = i;
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
	
	public void setEnabled(Boolean activado) {
		btnAceptar.setEnabled(activado);
	}

	
	public AgregarEntidad addEtiqueta(String etiqueta) {
		etiquetas.add(etiqueta);
		return this;
	}
	
	public AgregarEntidad addComponente(JComponent componente) {
		componentes.add(componente);
		return this;
	}
	
	public AgregarEntidad addAccionAuxiliar(ActionListener accion) {
		accionesAuxiliares.add(accion);
		return this;
	}
	
	public AgregarEntidad setAccionAceptar(ActionListener accionAceptar) {
		this.accionAceptar = accionAceptar;
		return this;
	}
}
