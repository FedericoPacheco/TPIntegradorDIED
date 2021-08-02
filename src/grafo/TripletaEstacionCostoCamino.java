package grafo;

import java.util.ArrayList;
import entidades.Estacion;
import entidades.Tramo;

public final class TripletaEstacionCostoCamino implements Comparable<TripletaEstacionCostoCamino>
{
	// No el mejor disenio orientado a objetos
	public Estacion estacion;
	public Double costo;
	public ArrayList<Tramo> camino;

	public TripletaEstacionCostoCamino(Estacion estacion, Double costo, ArrayList<Tramo> camino) 
	{
		this.estacion = estacion;
		this.costo = costo;
		this.camino = camino;
	}

	@Override
	public int compareTo(TripletaEstacionCostoCamino t) 
	{
		return costo.compareTo(t.costo);
	}
}

/*
public final class Tripleta<T1, T2, T3> implements Comparable
{
	public T1 primero;
	public T2 segundo;
	public T3 tercero;
	
	public Tripleta() { }

	public Tripleta(T1 primero, T2 segundo, T3 tercero) 
	{	
		this.primero = primero;
		this.segundo = segundo;
		this.tercero = tercero;
	}

	@Override
	public int compareTo(Object o) {
		return ((Integer) this.segundo).compareTo((Integer) ((Tripleta) o).segundo);
	}
}
*/
