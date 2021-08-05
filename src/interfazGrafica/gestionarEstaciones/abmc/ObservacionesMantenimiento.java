package interfazGrafica.gestionarEstaciones.abmc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import entidades.valueObjects.Estacion;
import entidades.valueObjects.TareaDeMantenimiento;
import grafo.RedDeTransporte;

public class ObservacionesMantenimiento
{
	public ObservacionesMantenimiento(JFrame ventanaPadre, Estacion estacion, RedDeTransporte redDeTransporte)
	{
		JFrame ventanaObservaciones = new JFrame();
		JPanel panelObservaciones = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		
		ventanaPadre.setEnabled(false);
		panelObservaciones.setLayout(new GridBagLayout());
		ventanaObservaciones.setTitle("Observaciones de tarea de mantenimiento"); 
		ventanaObservaciones.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		JTextArea txta1 = new JTextArea(20, 50);
		txta1.setEditable(true);
		JScrollPane sp1 = new JScrollPane(txta1);
		panelObservaciones.add(sp1, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 5, 5);
		JButton btnAceptar = new JButton("Aceptar");
		panelObservaciones.add(btnAceptar, gbc);
		btnAceptar.addActionListener(
			e -> { 
					TareaDeMantenimiento tareaDeMantenimiento = 
						new TareaDeMantenimiento(LocalDate.now(), null, txta1.getText());					
				
					try {
						redDeTransporte.addTareaDeMantenimiento(tareaDeMantenimiento, estacion);
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}
					
					estacion.addIdMantenimiento(tareaDeMantenimiento.getId());
					
					
					ventanaObservaciones.dispose(); 	
					ventanaPadre.setEnabled(true);
					ventanaPadre.setVisible(true);
				 }
		);
		
		ventanaObservaciones.setContentPane(panelObservaciones);
		ventanaObservaciones.pack();
		ventanaObservaciones.setLocationRelativeTo(ventanaPadre);
		ventanaObservaciones.setVisible(true);
	}
}
