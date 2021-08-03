package interfazGrafica.utilidades;

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
public class Menu extends JPanel
{
	private GridBagConstraints gbc;
	private List<JComponent> componentes; 
	
	private JFrame ventana;
	private JPanel panel;
	private JPanel panelPadre;

	public Menu(JFrame ventana, JPanel panel, JPanel panelPadre) 
	{
		this.ventana = ventana;
		this.panel = panel;
		this.panelPadre = panelPadre;
		
		componentes = new LinkedList<JComponent>();
		gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
	}
	
	public void armar()
	{
		gbc.gridx = 0;
		gbc.ipady = 15;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Insets ins = new Insets(5, 20, 5, 20);
		int i = 0;
		for (JComponent c : componentes)
		{
			if (i == 0)
				gbc.insets = new Insets(20, 20, 5, 20);
			else if ((i == componentes.size() - 1) && (panelPadre == null))
				gbc.insets = new Insets(5, 20, 20, 20);
			else 
				gbc.insets = ins;
			
			gbc.gridy = i;
			panel.add(c, gbc);
			i++;
		}
		
		if (panelPadre != null)
		{
			JButton btnVolver = new JButton("Volver");
			btnVolver.addActionListener(
				e -> {
						ventana.setContentPane(panelPadre);
						ventana.pack();
						ventana.setVisible(true);
					 }
			);
			gbc.gridy = i;
			gbc.insets = new Insets(45, 20, 20, 20);
			panel.add(btnVolver, gbc);
		}
	}
	
	public Menu addComponente(JComponent componente) {
		componentes.add(componente);
		return this;
	}
}
