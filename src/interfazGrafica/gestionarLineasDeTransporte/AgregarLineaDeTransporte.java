package interfazGrafica.gestionarLineasDeTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.Estacion;
import entidades.LineaDeTransporte;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class AgregarLineaDeTransporte extends JPanel
{
	private GridBagConstraints gbc;
	private JButton btn1, btn2;
	private JLabel lbl1, lbl2, lbl3;
	private JTextField txtf1, txtf2;
	private JComboBox<String> cb1;
	private JFrame ventana;
	private JPanel padre;
	
	private RedDeTransporte redDeTransporte;
	
	private LineaDeTransporte lineaDeTransporte;
	
	public AgregarLineaDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		lineaDeTransporte = new LineaDeTransporte();
		
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
		gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}
	
	private void armarPanel() 
	{
		btn1 = new JButton("Aceptar");
		btn2 = new JButton("Volver");
		
		lbl1 = new JLabel("Nombre"); 
		lbl2 = new JLabel("Color");
		lbl3 = new JLabel("Estado");
		
		txtf1 = new JTextField(25);
		txtf2 = new JTextField(25);
		
		cb1 = new JComboBox<String>();
		cb1.addItem("Activa");
		cb1.addItem("No Activa");
		
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.25;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 5, 5, 5);
		this.add(lbl1, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 0.75;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 5, 5, 5);
		this.add(txtf1, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.25;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(lbl2, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.75;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(txtf2, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0.25;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(lbl3, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.weightx = 0.75;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(cb1, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.EAST;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		this.add(btn1, gbc);
		btn1.addActionListener(
			e -> {
					LineaDeTransporte.Estado estado;
					if (((String) cb1.getSelectedItem()).equals("Activa")) 
						estado = LineaDeTransporte.Estado.ACTIVA;
					else	
						estado = LineaDeTransporte.Estado.INACTIVA;
				
					agregarLineaDeTransporte
					(
						txtf1.getText(),
						txtf2.getText(),
						estado
					);
					
					txtf1.setText("");
					txtf2.setText("");
					cb1.setSelectedItem("Activa");
				 }
			);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.WEST;
		gbc.ipady = 15;
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
	
	public void agregarLineaDeTransporte(String nombre, String color, LineaDeTransporte.Estado estado)	
	{
		lineaDeTransporte.setNombre(nombre);
		lineaDeTransporte.setColor(color);
		lineaDeTransporte.setEstado(estado);
		
		try {
			redDeTransporte.addLineaDeTransporte(lineaDeTransporte);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		lineaDeTransporte = new LineaDeTransporte();
	}
}
