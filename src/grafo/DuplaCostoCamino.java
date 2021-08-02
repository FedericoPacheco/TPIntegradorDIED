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

/*
public final class Dupla <T1, T2> 
{
	public T1 primero;
	public T2 segundo;
	
	public Dupla() 
	{
		primero = null;
		segundo = null;
	}
	
	public Dupla(T1 primero, T2 segundo)
	{
		this.primero = primero;
		this.segundo = segundo;
	}
}
*/
