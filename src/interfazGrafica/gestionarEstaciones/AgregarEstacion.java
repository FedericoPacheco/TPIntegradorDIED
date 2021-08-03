package interfazGrafica.gestionarEstaciones;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.Estacion;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class AgregarEstacion extends JPanel
{
	private GridBagConstraints gbc;
	private JButton btn1, btn2;
	private JLabel lbl1, lbl2, lbl3, lbl4;
	private JTextField txtf1, txtf2, txtf3;  // Podria haberse usado JFormattedTextField
	private JComboBox<String> cb1;
	private JFrame ventana;
	private JPanel padre;
	
	private RedDeTransporte redDeTransporte;
	
	private Estacion estacion;
	private DateTimeFormatter formatoHora;
	
	public AgregarEstacion(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		
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
		lbl2 = new JLabel("Horario de apertura");
		lbl3 = new JLabel("Horario de cierre");
		lbl4 = new JLabel("Estado");
		
		txtf1 = new JTextField(25);
		txtf2 = new JTextField(25);
		txtf3 = new JTextField(25);
		
		cb1 = new JComboBox<String>();
		cb1.addItem("Operativa");
		cb1.addItem("En mantenimiento");
		
		
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
		this.add(txtf3, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.25;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(lbl4, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 0.75;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(cb1, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.EAST;
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		this.add(btn1, gbc);
		btn1.addActionListener(
			e -> {
					Estacion.Estado estado;
					if (((String) cb1.getSelectedItem()).equals("Operativa")) 
						estado = Estacion.Estado.OPERATIVA;
					else	
						estado = Estacion.Estado.EN_MANTENIMIENTO;						
					
					estacion = new Estacion(
						txtf1.getText(),
						LocalTime.parse(txtf2.getText(), formatoHora),
						LocalTime.parse(txtf3.getText(), formatoHora),
						estado
					);
					
					try 
					{
						redDeTransporte.addEstacion(estacion);
						new ObservacionesMantenimiento(ventana, estacion, redDeTransporte);
					} 
					catch (SQLException e1) {
						e1.printStackTrace();
					}
						
					txtf1.setText("");
					txtf2.setText("");
					txtf3.setText("");
					cb1.setSelectedItem("Operativa");
				 }			
		);  
		
		gbc.gridx = 1;
		gbc.gridy = 4;
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
}
