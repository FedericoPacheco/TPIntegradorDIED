package interfazGrafica.ventaDeBoleto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

import entidades.Estacion;
import entidades.Tramo;
import grafo.RedDeTransporte;

// https://docs.oracle.com/javase/7/docs/api/java/awt/BasicStroke.html

@SuppressWarnings("serial")
public class DibujoRedDeTransporte extends JPanel
{
	private static final int ANCHO_VENTANA = 800;
	private static final int ALTO_VENTANA = 600;
	
	private static final int W = 80;
	private static final int H = 50;
	private static final int ARCH_W = 10;
	private static final int ARCH_H = 10;
	
	private static final int X_0 = 50;
	private static final int Y_0 = 50;
	private static final int PASO_X = 200;
	private static final int PASO_Y = 150;
	private static final int ESTACIONES_POR_FILA = (ANCHO_VENTANA - 2 * X_0 + W) / PASO_X;
	
	private static final Color COLOR_ESTACION_ACTIVA_FUERA_DEL_CAMINO = Color.GREEN;
	private static final Color COLOR_ESTACION_INACTIVA_FUERA_DEL_CAMINO = Color.RED;
	private static final Color COLOR_ESTACION_EN_CAMINO = Color.BLUE;
	private static final Color COLOR_TEXTO = Color.WHITE;
	
	// ------------------------------------------------
	
	private JFrame ventana;
	private JPanel padre;
	private RedDeTransporte redDeTransporte;
	private List<Tramo> camino;
	
	public DibujoRedDeTransporte(JFrame ventana, JPanel padre, RedDeTransporte redDeTransporte, ArrayList<Tramo> camino)
	{
		this.ventana = ventana;
		this.padre = padre;
		this.redDeTransporte = redDeTransporte;
		this.camino = camino; 
		
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
		this.dibujarTramos(g2d, ubicacionesEstaciones);
		this.dibujarEstaciones(g2d, ubicacionesEstaciones);
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
			
			if (j > ESTACIONES_POR_FILA - 1)
			{
				j = 0;
				i++;
			}
			else
				j++;
		}
		
		return ubicaciones;
	}

	private void dibujarTramos(Graphics2D g2d, Map<Integer, Point2D.Double> ubicacionesEstaciones) 
	{
		Point2D.Double puntoOrigen, puntoDestino;
		BasicStroke lineaEstandar = new BasicStroke(2.0f);
		float aux[] = {10.0f};
	    BasicStroke lineaPunteada = // Sacado de un tutorial, no se que hace cada cosa xD
	        new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, aux, 0.0f);
		double randX, randY;
		Random aleatorizador = new Random();
		
		for (Tramo t : redDeTransporte.getAllTramos())
		{
			puntoOrigen = ubicacionesEstaciones.get(t.getIdOrigen());
			puntoDestino = ubicacionesEstaciones.get(t.getIdDestino());
			
			randX = randY = 0.0;
			if (puntoOrigen.x == puntoDestino.x && puntoOrigen.y != puntoDestino.y) // Misma columna
				randX = aleatorizador.nextDouble() * W / 2;
			else if (puntoOrigen.x != puntoDestino.x && puntoOrigen.y == puntoDestino.y) // Misma fila
				randY = aleatorizador.nextDouble() * H / 2;
			
			if (camino.contains(t))
				g2d.setStroke(lineaPunteada);
			else
				g2d.setStroke(lineaEstandar);
			
			g2d.setColor(Color.decode(
				redDeTransporte.getLineaDeTransporte(t.getIdLineaDeTransporte()).getColor()
			));
			
			drawArrowLine(
				g2d,	
				(int) (puntoOrigen.x + randX), 
				(int) (puntoOrigen.y + randY),
				(int) (puntoDestino.x + randX),
				(int) (puntoDestino.y + randY),
				10,
				5
			);
		}
	}

	private void dibujarEstaciones(Graphics2D g2d, Map<Integer, Point2D.Double> ubicacionesEstaciones)
	{
		Point2D.Double auxPunto;
		
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
			g2d.fill(new RoundRectangle2D.Double(auxPunto.x, auxPunto.y, W, H, ARCH_W, ARCH_H));
			
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
}
