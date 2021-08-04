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
import javax.swing.WindowConstants;

public class AgregarEntidad 
{
	private JButton btnAceptar, btnVolver;
	private List<String> etiquetas;
	private List<JComponent> componentes;
	private ActionListener accionVolver;
	private ActionListener accionAceptar;
	private List<ActionListener> accionesAuxiliares;
	private GridBagConstraints gbc;
	
	private JFrame ventana;
	private JPanel panel;
	private void init()
	{
		btnVolver = new JButton("Volver");
		btnAceptar = new JButton("Aceptar");
		
		accionAceptar = e -> { };
		accionesAuxiliares = new LinkedList<ActionListener>();
		
		componentes = new LinkedList<JComponent>();
		etiquetas = new LinkedList<String>();
		
		gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
	}
	
	public AgregarEntidad(JPanel panel, JFrame ventanaPadre, String tituloVentana) // Interfaz en una nueva ventana
	{
		this.panel = panel;
		this.init();
		
		ventana = new JFrame(tituloVentana);
		ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		ventana.setLocationRelativeTo(ventanaPadre);
		ventana.setContentPane(panel);
			
		accionVolver = 
			e -> {
					ventana.dispose();
					ventanaPadre.setEnabled(true);
					ventanaPadre.setVisible(true);
				 };
				 
		accionesAuxiliares.add(
			e -> {
					ventanaPadre.setEnabled(false);
					ventana.pack();
					ventana.setVisible(true);
				}
		);
	}
	
	public AgregarEntidad(JFrame ventanaPadre, JPanel panel, JPanel panelPadre) // Interfaz en la misma ventana
	{
		this.panel = panel;
		
		this.init();
		
		accionVolver = 
			e -> {
					ventanaPadre.setContentPane(panelPadre);
					ventanaPadre.pack();
					ventanaPadre.setVisible(true);
				 };
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
			gbc.gridy = i;
			gbc.gridwidth = 2;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			panel.add(c, gbc);
			
			i++;
		}
		
		// vtnAceptar
		gbc.gridx = 2;
		gbc.gridy = i;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.EAST;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		panel.add(btnAceptar, gbc);
		btnAceptar.addActionListener(accionAceptar);
		if (ventana != null) // Se esta en una nueva ventana
			btnAceptar.addActionListener(accionVolver);
		
		// btnVolver
		gbc.gridx = 1;
		gbc.gridy = i;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.WEST;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		panel.add(btnVolver, gbc);
		btnVolver.addActionListener(accionVolver);
		
		
		for (ActionListener a : accionesAuxiliares)
			a.actionPerformed(null);
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
