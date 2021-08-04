package interfazGrafica.utilidades;

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

	
	@Override
	public String toString() {
		return "[" + primero.toString() + "; " + segundo.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((primero == null) ? 0 : primero.hashCode());
		result = prime * result + ((segundo == null) ? 0 : segundo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dupla<?, ?> other = (Dupla<?, ?>) obj;
		if (primero == null) {
			if (other.primero != null)
				return false;
		} else if (!primero.equals(other.primero))
			return false;
		if (segundo == null) {
			if (other.segundo != null)
				return false;
		} else if (!segundo.equals(other.segundo))
			return false;
		return true;
	}
}
