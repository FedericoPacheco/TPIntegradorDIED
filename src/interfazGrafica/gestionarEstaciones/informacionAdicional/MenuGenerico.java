package interfazGrafica.gestionarEstaciones.informacionAdicional;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MenuGenerico extends JPanel
{
	private GridBagConstraints gbc;
	private List<JComponent> componentes; 
	
	private JFrame ventana;
	private JPanel panel;
	private JPanel panelPadre;

	public MenuGenerico(JFrame ventana, JPanel panel, JPanel panelPadre) 
	{
		this.ventana = ventana;
		this.panel = panel;
		this.panelPadre = panelPadre;
		
		componentes = new LinkedList<JComponent>();
		gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
	}
	
	public void armarMenu()
	{
		gbc.gridx = 0;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 20, 0, 20);
		int i = 0;
		for (JComponent c : componentes)
		{
			gbc.gridy = i;
			panel.add(c, gbc);
			i++;
		}
		
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(
			e -> {
					ventana.setContentPane(panelPadre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		gbc.gridy = i;
		gbc.insets = new Insets(35, 20, 10, 20);
		panel.add(btnVolver, gbc);
	}
	
	public MenuGenerico addComponente(JComponent componente) {
		componentes.add(componente);
		return this;
	}
}
