package app;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import entidades.Estacion;
import grafo.RedDeTransporte;
import interfazGrafica.MenuPrincipal;
import tests.TestDB;

// https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
// https://stackoverflow.com/questions/30259812/can-we-use-the-lambda-expression-for-windowlistener-if-yes-how-if-no-why-can
// https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution

public final class App 
{
	static private RedDeTransporte redDeTransporte = null;
	static private JFrame ventana = new JFrame();
	
	public static void main (String[] args)
	{
		//TestDB.testear();
		
		try { redDeTransporte = new RedDeTransporte(); } 
		catch (ClassNotFoundException | SQLException e) { System.exit(137); }
		
		// Cambiar opciones de "Si" y "No" al espaniol
		UIManager.put("OptionPane.yesButtonText", "Sí");
		UIManager.put("OptionPane.noButtonText", "No");
		ventana.addWindowListener
		(
			new WindowAdapter() 
			{
				@Override
				public void windowClosing(WindowEvent windowEvent) 
				{
					if (
							JOptionPane.showConfirmDialog
							(
								ventana, 
								"¿Está seguro que quiere salir?", 
								"", 
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE
							) == JOptionPane.YES_OPTION)	
					{
						App.cerrarConexion();
						System.exit(0);
					}
		        }

				
		    }
		);
		ventana.setTitle("TP integrador - DIED - UTN FRSF"); 
		ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		ventana.setContentPane(new MenuPrincipal(ventana, redDeTransporte));
		ventana.pack();
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
	
	private static void cerrarConexion()
	{
		try { redDeTransporte.close(); }
		catch (SQLException e) { e.printStackTrace(); }
	}
}
