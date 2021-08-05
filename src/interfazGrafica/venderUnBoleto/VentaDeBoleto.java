package interfazGrafica.ventaDeBoleto;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clasesUtiles.Dupla;
import clasesUtiles.GUIAgregarEntidadGenerico;
import entidades.valueObjects.Boleto;
import entidades.valueObjects.Estacion;
import entidades.valueObjects.Tramo;
import grafo.RedDeTransporte;

@SuppressWarnings("serial")
public class VentaDeBoleto extends JPanel 
{
	private JFrame ventana;
	private JPanel padre;
	
	private RedDeTransporte redDeTransporte;
	private GUIAgregarEntidadGenerico agregarBoleto;
	private Map<String, Estacion> estacionCb;
	
	public VentaDeBoleto(JFrame ventana, JPanel panelPadre, RedDeTransporte redDeTransporte)
	{
		this.redDeTransporte = redDeTransporte;
		this.ventana = ventana;
		this.padre = panelPadre;
		
		agregarBoleto = new GUIAgregarEntidadGenerico(ventana, this, panelPadre);
		estacionCb = new HashMap<String, Estacion>();
		
		this.completarComponentes();
		agregarBoleto.armar();
	}
	
	private void completarComponentes() 
	{
		agregarBoleto
			.addEtiqueta("Nombre del cliente")
			.addEtiqueta("Correo electrónico")
			.addEtiqueta("Estación origen")
			.addEtiqueta("Estación destino")
			.addEtiqueta("Camino a elegir");
		
		JTextField txtfNombre = new JTextField(25);
		JTextField txtfCorreo = new JTextField(25);
		
		JComboBox<String> cbOrigen = new JComboBox<String>();
		JComboBox<String> cbDestino = new JComboBox<String>();
		
		String auxString;
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			auxString = e.getNombre() + " (id :" + e.getId() + ")";
			
			estacionCb.put(auxString, e);
			cbOrigen.addItem(auxString);
			cbDestino.addItem(auxString);
		}
		cbDestino.setSelectedItem((cbOrigen.getSelectedIndex() + 1));
		
		JComboBox<String> cbCamino = new JComboBox<String>();
		cbCamino.addItem("El más corto");
		cbCamino.addItem("El más rápido");
		cbCamino.addItem("El más barato");
	
		ActionListener a =
			e -> {
					if (cbOrigen.getSelectedItem().equals(cbDestino.getSelectedItem()))
						agregarBoleto.activarAceptar(false);
					else
						agregarBoleto.activarAceptar(true);
				 };
		cbOrigen.addActionListener(a);
		cbDestino.addActionListener(a);
		agregarBoleto.addAccionAuxiliar(a);
				 
		
		agregarBoleto.setAccionAceptar(
			e -> {
					Dupla<Double, LinkedList<Tramo>> auxDupla = null;
				
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
					
					if (txtfCorreo.getText().equals("") || txtfNombre.getText().equals("")) 
						JOptionPane.showMessageDialog(ventana, "Complete los datos restantes, por favor.", "", JOptionPane.ERROR_MESSAGE);
					else
					{
						if (!auxDupla.segundo.isEmpty())
						{
							List<String> caminoStr = VentaDeBoleto.getCaminoStr(auxDupla.segundo, redDeTransporte);
							
							Boleto auxBoleto = new Boleto(
									txtfCorreo.getText(),
									txtfNombre.getText(),
									LocalDate.now(),
									redDeTransporte.getEstacion(auxDupla.segundo.get(0).getIdOrigen()).getNombre(),
									redDeTransporte.getEstacion(auxDupla.segundo.get(auxDupla.segundo.size() - 1).getIdDestino()).getNombre(),
									calcularCostoCamino(auxDupla.segundo),
									caminoStr
								);
								
								try {
									redDeTransporte.addBoleto(auxBoleto);
								} catch (ClassNotFoundException | SQLException e1) {
									e1.printStackTrace();
								}
								
								ventana.setContentPane(new DibujoRedDeTransporte(ventana, padre, redDeTransporte, auxDupla.segundo));
						}
						else
							JOptionPane.showMessageDialog(ventana, "No existen caminos posibles entre las estaciones especificadas.", "", JOptionPane.INFORMATION_MESSAGE);				
					}
				}			
		);  
		
		
		agregarBoleto
			.addComponente(txtfNombre)
			.addComponente(txtfCorreo)
			.addComponente(cbOrigen)
			.addComponente(cbDestino)
			.addComponente(cbCamino);
	}

	public static Double calcularCostoCamino(List<Tramo> camino)
	{
		Double suma = 0.0;
		
		for(Tramo t : camino)
			suma += t.getCosto();
		
		return suma;
	}

	public static List<String> getCaminoStr(List<Tramo> camino, RedDeTransporte redDeTransporte) 
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
