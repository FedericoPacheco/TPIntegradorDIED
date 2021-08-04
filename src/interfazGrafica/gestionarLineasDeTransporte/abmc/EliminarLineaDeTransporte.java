package interfazGrafica.gestionarLineasDeTransporte.abmc;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entidades.valueObjects.LineaDeTransporte;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class EliminarLineaDeTransporte extends JPanel 
{
	private JFrame ventana;
	private JPanel padre;
	private GridBagConstraints gbc;
	private JComboBox<String> cb;
	private JButton btnEliminar, btnVolver;
	private JLabel lbl1;
	
	Map<String, LineaDeTransporte> lineasDeTransporteCb;
	private RedDeTransporte redDeTransporte;
	
	public EliminarLineaDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		lineasDeTransporteCb = new HashMap<String, LineaDeTransporte>();
		
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}

	private void armarPanel() 
	{
		btnEliminar = new JButton("Eliminar");
		btnVolver = new JButton("Volver");
		lbl1 = new JLabel("Seleccione la línea de transporte que desea eliminar: ");
		cb = new JComboBox<String>();
	
		for (LineaDeTransporte l : redDeTransporte.getAllLineasDeTransporte())
		{
			lineasDeTransporteCb.put(l.getNombre() + " (id: " + l.getId() + ")", l);
			cb.addItem(l.getNombre() + " (id: " + l.getId() + ")");
		}
		
		if(cb.getItemCount() == 0)
		{
			btnEliminar.setEnabled(false);
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
		this.add(btnEliminar, gbc);
		btnEliminar.addActionListener(
			e -> {
					if (cb.getItemCount() > 0)
					{
						if (JOptionPane.showConfirmDialog(ventana, "¿Está seguro?", "", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) 
							== JOptionPane.YES_OPTION)
						{
							LineaDeTransporte auxLineaDeTransporte = lineasDeTransporteCb.get(cb.getSelectedItem());
							cb.removeItem(cb.getSelectedItem());
							
							try {
								if (auxLineaDeTransporte != null) 
									redDeTransporte.deleteLineaDeTransporte(auxLineaDeTransporte);
							} catch (ClassNotFoundException | SQLException e1) {
								e1.printStackTrace();
							}
							
							if (cb.getItemCount() == 0)
							{
								btnEliminar.setEnabled(false);
								cb.setEnabled(false);
							}
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
		this.add(btnVolver, gbc);
		btnVolver.addActionListener(
			e -> {
					ventana.setContentPane(padre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
	}
}
