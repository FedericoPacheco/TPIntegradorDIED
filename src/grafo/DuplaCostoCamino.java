package grafo;

import java.util.ArrayList;
import entidades.Tramo;

public final class DuplaCostoCamino
{
	// No el mejor disenio orientado a objetos
	public Double costo;
	public ArrayList<Tramo> camino;
	
	public DuplaCostoCamino(Double costo, ArrayList<Tramo> camino) 
	{
		this.costo = costo;
		this.camino = camino;
	}
}
