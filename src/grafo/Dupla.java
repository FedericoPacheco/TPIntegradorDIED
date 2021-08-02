package grafo;

public class Dupla<T1, T2> 
{
	public T1 primero;
	public T2 segundo;
	
	public Dupla(T1 primero, T2 segundo) 
	{
		this.primero = primero;
		this.segundo = segundo;
	}
	
	public Dupla()
	{
		primero = null;
		segundo = null;
	}
}
