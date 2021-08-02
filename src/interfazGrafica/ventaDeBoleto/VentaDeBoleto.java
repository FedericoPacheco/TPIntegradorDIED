package interfazGrafica.ventaDeBoleto;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.Boleto;
import entidades.Estacion;
import entidades.Tramo;
import grafo.DuplaCostoCamino;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class VentaDeBoleto extends JPanel 
{
	private GridBagConstraints gbc;
	private JButton btnAceptar, btnVolver;
	private JLabel lblNombre, lblCorreo, lblOrigen, lblDestino, lblCamino;
	private JTextField txtfNombre, txtfCorreo;  
	private JComboBox<String> cbOrigen, cbDestino, cbCamino;
	private JFrame ventana;
	private JPanel padre;
	
	private RedDeTransporte redDeTransporte;
	private Map<String, Estacion> estacionCb;
	
	public VentaDeBoleto(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = padre;
		
		gbc = new GridBagConstraints();
		estacionCb = new HashMap<String, Estacion>();
		this.setLayout(new GridBagLayout());
		this.armarPanel();
	}
	
	private void armarPanel() 
	{
		btnAceptar = new JButton("Aceptar");
		btnVolver = new JButton("Volver");
		
		lblNombre = new JLabel("Nombre del cliente"); 
		lblCorreo = new JLabel("Correo electrónico");
		lblOrigen = new JLabel("Estación origen");
		lblDestino = new JLabel("Estación destino");
		lblCamino = new JLabel("Camino a elegir");
		
		txtfNombre = new JTextField(25);
		txtfCorreo = new JTextField(25);
		
		cbOrigen = new JComboBox<String>();
		cbDestino = new JComboBox<String>();
		
		String auxString;
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			auxString = e.getNombre() + " (id :" + e.getId() + ")";
			
			estacionCb.put(auxString, e);
			cbOrigen.addItem(auxString);
			cbDestino.addItem(auxString);
		}
		
		cbCamino = new JComboBox<String>();
		cbCamino.addItem("El más corto");
		cbCamino.addItem("El más rápido");
		cbCamino.addItem("El más barato");
	
	 
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25; gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 5, 5, 5);
		this.add(lblNombre, gbc);
		
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.75; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(txtfNombre, gbc);
		
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.25; gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(lblCorreo, gbc);
		
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.75; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(txtfCorreo, gbc);
		
		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.25; gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(lblOrigen, gbc);
		
		gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.75; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(cbOrigen, gbc);
		ActionListener a =
			e -> {
					if (cbOrigen.getSelectedItem().equals(cbDestino.getSelectedItem()))
						btnAceptar.setEnabled(false);
					else
						btnAceptar.setEnabled(true);
				 };
		a.actionPerformed(null);
		cbOrigen.addActionListener(a);
		
		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.25; gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(lblDestino, gbc);
		
		gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.75; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(cbDestino, gbc);
		cbDestino.addActionListener(a);
		
		gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.25; gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(lblCamino, gbc);
		
		gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.75; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(cbCamino, gbc);
		
		
		gbc.ipady = 15;
		gbc.insets = new Insets(30, 20, 10, 20);
		gbc.gridwidth = 1;
		
		gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.EAST; 
		
		this.add(btnAceptar, gbc);
		btnAceptar.addActionListener(
			e -> {
					DuplaCostoCamino auxDupla = null;
				
					switch((String) cbCamino.getSelectedItem())
					{
						case "El más corto":
							auxDupla = redDeTransporte.caminoMasCorto(
								estacionCb.get(cbOrigen.getSelectedItem()),
								estacionCb.get(cbDestino.getSelectedItem())
							);
								
							break;
						case "El más rápido":
							auxDupla = redDeTransporte.caminoMasRapido(
									estacionCb.get(cbOrigen.getSelectedItem()),
									estacionCb.get(cbDestino.getSelectedItem())
								);
							break;
						case "El más barato":
							auxDupla = redDeTransporte.caminoMasBarato(
									estacionCb.get(cbOrigen.getSelectedItem()),
									estacionCb.get(cbDestino.getSelectedItem())
								);
							break;
					}
					
					if (!auxDupla.camino.isEmpty())
					{
						List<String> caminoStr = this.getCaminoStr(auxDupla.camino);
						
						Boleto auxBoleto = new Boleto(
								txtfCorreo.getText(),
								txtfNombre.getText(),
								LocalDate.now(),
								caminoStr.get(0),
								caminoStr.get(caminoStr.size() - 1),
								calcularCostoCamino(auxDupla.camino),
								caminoStr
							);
							
							try {
								redDeTransporte.addBoleto(auxBoleto);
							} catch (ClassNotFoundException | SQLException e1) {
								e1.printStackTrace();
							}
							
							ventana.setContentPane(new DibujoRedDeTransporte(ventana, this, redDeTransporte, auxDupla.camino));
					}
					else
						JOptionPane.showMessageDialog(ventana, "Lo sentimos. No existen caminos posibles.");
					
				 }			
		);  
		
		gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.WEST;
		this.add(btnVolver, gbc);
		btnVolver.addActionListener(
			e -> {
					ventana.setContentPane(padre);
					ventana.pack();
					ventana.setVisible(true);
				 } 
		);		
	}

	private Double calcularCostoCamino(ArrayList<Tramo> camino)
	{
		Double suma = 0.0;
		
		for(Tramo t : camino)
			suma += t.getCosto();
		
		return suma;
	}

	private List<String> getCaminoStr(ArrayList<Tramo> camino) 
	{
		List<String> caminoStr = new ArrayList<String>();
		
		for (Tramo t : camino)
			caminoStr.add(
				"("   + redDeTransporte.getEstacion(t.getIdOrigen()).getNombre() + 
				", "  + redDeTransporte.getEstacion(t.getIdDestino()).getNombre() + 
				"): " + redDeTransporte.getLineaDeTransporte(t.getIdLineaDeTransporte()).getNombre()
			);

		return caminoStr;
	}
}
