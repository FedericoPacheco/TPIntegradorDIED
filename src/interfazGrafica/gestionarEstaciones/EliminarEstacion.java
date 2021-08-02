package interfazGrafica.gestionarEstaciones;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entidades.Estacion;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class EliminarEstacion extends JPanel 
{
	private JFrame ventana;
	private JPanel padre;
	private GridBagConstraints gbc;
	private JComboBox<String> cb;
	private JButton btn1, btn2;
	private JLabel lbl1;
	
	Map<String, Estacion> estacionesCb;
	private RedDeTransporte redDeTransporte;
	
	public EliminarEstacion(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		estacionesCb = new HashMap<String, Estacion>();
		
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}

	private void armarPanel() 
	{
		btn1 = new JButton("Eliminar");
		btn2 = new JButton("Volver");
		lbl1 = new JLabel("Seleccione la estación que desea eliminar: ");
		cb = new JComboBox<String>();
	
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			estacionesCb.put(e.getNombre() + " (id: " + e.getId() + ")", e);
			cb.addItem(e.getNombre() + " (id: " + e.getId() + ")");
		}
		
		if(cb.getItemCount() == 0)
		{
			btn1.setEnabled(false);
			cb.setEnabled(false);
		}
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipady = 15;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(10, 20, 5, 20);
		this.add(lbl1, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipady = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 20, 5, 20);
		this.add(cb, gbc);
	
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipady = 15;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 20, 0, 20);
		this.add(btn1, gbc);
		btn1.addActionListener(
			e -> {
					if (cb.getItemCount() > 0)
					{
						Estacion auxEstacion = estacionesCb.get(cb.getSelectedItem());
						cb.removeItem(cb.getSelectedItem());
						
						try {
							if (auxEstacion != null) 
								redDeTransporte.deleteEstacion(auxEstacion);
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
						
						if (cb.getItemCount() == 0)
						{
							btn1.setEnabled(false);
							cb.setEnabled(false);
						}
					}
				 }
		);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.ipady = 15;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(30, 20, 10, 20);
		this.add(btn2, gbc);
		btn2.addActionListener(
			e -> {
					ventana.setContentPane(padre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
	}
}

