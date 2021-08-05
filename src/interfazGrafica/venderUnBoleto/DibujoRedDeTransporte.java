package interfazGrafica.ventaDeBoleto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import entidades.valueObjects.Estacion;
import entidades.valueObjects.LineaDeTransporte;
import entidades.valueObjects.Tramo;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

import grafo.RedDeTransporte;

// https://docs.oracle.com/javase/7/docs/api/java/awt/BasicStroke.html

@SuppressWarnings("serial")
public class DibujoRedDeTransporte extends JPanel
{
	private static final int ANCHO_VENTANA = 1280;
	private static final int ALTO_VENTANA = 720;
	
	// No se me ocurrio un nombre mejor
	private static final int IPADX_FONDO = 20; 
	private static final int IPADY_FONDO = 20;
	
	private static final int W = 100;
	private static final int H = 50;
	private static final int ARCH_W = 10;
	private static final int ARCH_H = 10;
	
	private static final int X_0 = 50;
	private static final int Y_0 = 50;
	private static final int PASO_X = 200;
	private static final int PASO_Y = 150;
	private static final int ESTACIONES_POR_FILA = (ANCHO_VENTANA - X_0 - IPADY_FONDO - W) / PASO_X;
	
	private static final Color COLOR_ESTACION_ACTIVA_FUERA_DEL_CAMINO = new Color(43, 120, 31);
	private static final Color COLOR_ESTACION_INACTIVA_FUERA_DEL_CAMINO = new Color(138, 28, 28);
	private static final Color COLOR_ESTACION_EN_CAMINO = new Color(107, 224, 81);
	private static final Color COLOR_BORDE_ESTACION = Color.BLACK;
	private static final Color COLOR_TEXTO = Color.BLACK;
	private static final Color COLOR_TRAMO_FUERA_DEL_CAMINO = Color.GRAY;
	private static final Color COLOR_FONDO = Color.WHITE;
	
	private static final int ANCHO_BTN = 100;
	private static final int ALTO_BTN = 30;
	
	// ------------------------------------------------
	
	private JButton btnAceptar; 
	private JButton btnLeyenda;

	private JFrame ventana;
	private JPanel padre;
	private RedDeTransporte redDeTransporte;
	private List<Tramo> camino;
	
	public DibujoRedDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte, List<Tramo> camino)
	{
		this.ventana = ventana;
		this.padre = padre;
		this.redDeTransporte = redDeTransporte;
		this.camino = camino; 
		
		this.setLayout(null);
		ventana.setSize(ANCHO_VENTANA, ALTO_VENTANA);
		ventana.setVisible(true);
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
		dibujarRedDeTransporte(g2d);
	}

	private void dibujarRedDeTransporte(Graphics2D g2d) 
	{
		Map<Integer, Point2D.Double> ubicacionesEstaciones = this.calcularUbicacionesEstaciones();
		this.dibujarFondoYBotones(g2d);
		this.dibujarTramos(g2d, ubicacionesEstaciones);
		this.dibujarEstaciones(g2d, ubicacionesEstaciones);
	}
	
	private void dibujarFondoYBotones(Graphics2D g2d)
	{
		// El fondo y los botones de aceptar fueron ajustados a ojo
		// con numeros magicos por probablemente el siguiente motivo:
		// https://stackoverflow.com/a/6593372
		
		Rectangle2D.Double fondo = new Rectangle2D.Double(
			IPADX_FONDO,
			IPADY_FONDO,
			ANCHO_VENTANA - 2.85 * IPADX_FONDO,  // No se porque 2 no queda bien
			ALTO_VENTANA - 6 * IPADY_FONDO
		);
		g2d.setColor(COLOR_FONDO);
		g2d.fill(fondo);
		
		btnLeyenda = new JButton("Leyenda");
		btnLeyenda.setBounds(
			IPADX_FONDO,
			ALTO_VENTANA - (int) (2.75 * ALTO_BTN),
			ANCHO_BTN,
			ALTO_BTN
		);
		btnLeyenda.addActionListener(e -> new Leyenda());
		this.add(btnLeyenda);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(
			ANCHO_VENTANA / 2 - ANCHO_BTN / 2, 
			ALTO_VENTANA - (int) (2.75 * ALTO_BTN),
			ANCHO_BTN, 
			ALTO_BTN
		);
		btnAceptar.addActionListener(
			e -> {
					ventana.setContentPane(padre);
					ventana.pack();
					ventana.setVisible(true);
				 }
		);
		this.add(btnAceptar);
	}
	
	private Map<Integer, Point2D.Double> calcularUbicacionesEstaciones() 
	{
		Map<Integer, Point2D.Double> ubicaciones = new HashMap<Integer, Point2D.Double>();
		int i = 0;
		int j = 0;
		
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			ubicaciones.put(
				e.getId(), 
				new Point2D.Double(
					X_0 + j * PASO_X,
					Y_0 + i * PASO_Y
				)	
			);
			
			if (j < ESTACIONES_POR_FILA)
				j++;
			else
			{
				j = 0;
				i++;
			}
		}
		
		return ubicaciones;
	}

	private void dibujarTramos(Graphics2D g2d, Map<Integer, Point2D.Double> ubicacionesEstaciones) 
	{
		Color auxColor;
		
		Point2D.Double puntoOrigen, puntoDestino;
		double auxXOrigen, auxYOrigen, auxXDestino, auxYDestino;
		
		BasicStroke lineaEstandar = new BasicStroke(2.0f);
		float aux[] = {10.0f};
	    BasicStroke lineaPunteada = // Sacado de un tutorial, no se que hace cada cosa xD
	        new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, aux, 0.0f);
		
	    Random aleatorizador = new Random();
		
	    
		auxXOrigen = auxYOrigen = auxXDestino = auxYDestino = 0.0; // Pedido por el compilador
		for (Tramo t : redDeTransporte.getAllTramos())
		{
			puntoOrigen = ubicacionesEstaciones.get(t.getIdOrigen());
			puntoDestino = ubicacionesEstaciones.get(t.getIdDestino());
			
			if (puntoOrigen.x == puntoDestino.x) 								// Misma columna
			{
				auxXOrigen = auxXDestino = aleatorizador.nextDouble() * W / 4 + W / 2;
				if (puntoOrigen.y < puntoDestino.y)								// Origen arriba
				{
					auxYOrigen = H;
					auxYDestino = 0.0;
				}
				else
				{
					auxYOrigen = 0.0;
					auxYDestino = H;
				}
			}
			else if (puntoOrigen.y == puntoDestino.y) 							// Misma fila
			{
				auxYOrigen = auxYDestino = aleatorizador.nextDouble() * H / 4 + H / 2;
				if (puntoOrigen.x < puntoDestino.x)								// Origen a la izquierda
				{
					auxXOrigen = W;
					auxXDestino = 0.0;
				}
				else
				{
					auxXOrigen = 0.0;
					auxXDestino = W;
				}
			}
			else if (puntoOrigen.y < puntoDestino.y)  							// Origen arriba
			{
				auxXDestino = W / 2;
				auxYDestino = 0.0;
				auxYOrigen = H;
				if (puntoOrigen.x < puntoDestino.x)								// Origen a la izquierda
					auxXOrigen = W;
				else
					auxXOrigen = 0.0;
			}
			else if (puntoDestino.y < puntoOrigen.y)							// Destino arriba
			{
				auxXOrigen = 0.0;
				auxYOrigen = 0.0;
				auxYDestino = H;
				if (puntoDestino.x < puntoOrigen.x)								// Destino a la izquierda
					auxXDestino = W / 2;
				else
					auxXDestino = 0.0;
			}
			
				
			if (camino.contains(t))
			{
				g2d.setStroke(lineaPunteada);
				auxColor = Color.decode(
					redDeTransporte.getLineaDeTransporte(t.getIdLineaDeTransporte()).getColor()
				);
			}
			else
			{
				g2d.setStroke(lineaEstandar);
				auxColor = COLOR_TRAMO_FUERA_DEL_CAMINO;
			}
			g2d.setColor(auxColor);
			
			
			drawArrowLine(
				g2d,	
				(int) (puntoOrigen.x + auxXOrigen), 
				(int) (puntoOrigen.y + auxYOrigen),
				(int) (puntoDestino.x + auxXDestino),
				(int) (puntoDestino.y + auxYDestino),
				10,
				5
			);
		}
	}

	private void dibujarEstaciones(Graphics2D g2d, Map<Integer, Point2D.Double> ubicacionesEstaciones)
	{
		Point2D.Double auxPunto;
		RoundRectangle2D.Double auxRectangulo;
		
		for (Estacion e : redDeTransporte.getAllEstaciones())
		{
			auxPunto = ubicacionesEstaciones.get(e.getId());
			
			
			if (estacionEstaEnCamino(e.getId()))
				g2d.setColor(COLOR_ESTACION_EN_CAMINO);
			else
			{
				if (e.estaActiva())
					g2d.setColor(COLOR_ESTACION_ACTIVA_FUERA_DEL_CAMINO);
				else
					g2d.setColor(COLOR_ESTACION_INACTIVA_FUERA_DEL_CAMINO);
			}	
			auxRectangulo = new RoundRectangle2D.Double(auxPunto.x, auxPunto.y, W, H, ARCH_W, ARCH_H);
			g2d.fill(auxRectangulo);
			g2d.setColor(COLOR_BORDE_ESTACION);
			g2d.draw(auxRectangulo);
			
			
			g2d.setColor(COLOR_TEXTO);
			g2d.drawString(e.getNombre(), (int) auxPunto.x + W / 8, (int) auxPunto.y + H / 2);
		}
	}
	
	private Boolean estacionEstaEnCamino(Integer idEstacion)
	{
		for (Tramo t : camino)
			if (t.getIdOrigen().equals(idEstacion) || t.getIdDestino().equals(idEstacion))
				return true;
		return false;
	}
	
	// Recuperado de: https://stackoverflow.com/a/27461352
	private void drawArrowLine(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
	    double dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);
	}
	
	private class Leyenda
	{
		public Leyenda() 
		{
			JFrame nuevaVentana = new JFrame();
			JPanel panel = new JPanel();
			GridBagConstraints gbc = new GridBagConstraints();
			
			JButton btnAceptar = new JButton("Aceptar");
			JLabel auxLblLinea;
			JLabel auxLblColor;
		
			ventana.setEnabled(false);
			panel.setLayout(new GridBagLayout());
			nuevaVentana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			nuevaVentana.setTitle("Colores de líneas de transporte");
			nuevaVentana.setLocationRelativeTo(ventana);
			
			
			int i = 0;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(6, 6, 6, 6);
			gbc.fill = GridBagConstraints.CENTER;
			for (LineaDeTransporte l : redDeTransporte.getAllLineasDeTransporte())
			{
				auxLblLinea = new JLabel(l.getNombre());
				auxLblLinea.setForeground(Color.BLACK);
				gbc.gridx = 0;
				gbc.gridy = i;
				panel.add(auxLblLinea, gbc);
				
				auxLblColor = new JLabel("███████");
				auxLblColor.setForeground(Color.decode(l.getColor()));
				gbc.gridx = 1;
				gbc.gridy = i;
				panel.add(auxLblColor, gbc);
		
				i++;
			}			
			
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(15, 5, 5, 5);
			gbc.fill = GridBagConstraints.CENTER;
			panel.add(btnAceptar, gbc);
			btnAceptar.addActionListener(
				e -> {
						nuevaVentana.dispose();
						ventana.setEnabled(true);
						ventana.setVisible(true);
					 }
			);
	
			
			nuevaVentana.setContentPane(panel);
			nuevaVentana.pack();
			nuevaVentana.setVisible(true);
		}
	}
}
