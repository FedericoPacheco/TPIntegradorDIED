package interfazGrafica.utilidades;

public final class Tripleta<T1, T2, T3> 
{
	public T1 primero;
	public T2 segundo;
	public T3 tercero;
	
	public Tripleta(T1 primero, T2 segundo, T3 tercero) 
	{
		this.primero = primero;
		this.segundo = segundo;
		this.tercero = tercero;
	}
	
	public Tripleta()
	{
		primero = null;
		segundo = null;
		tercero = null;
	}
	
	@Override
	public String toString() {
		return "[" + primero.toString() + "; " + segundo.toString() + "; " + tercero.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((primero == null) ? 0 : primero.hashCode());
		result = prime * result + ((segundo == null) ? 0 : segundo.hashCode());
		result = prime * result + ((tercero == null) ? 0 : tercero.hashCode());
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
		Tripleta<?, ?, ?> other = (Tripleta<?, ?, ?>) obj;
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
		if (tercero == null) {
			if (other.tercero != null)
				return false;
		} else if (!tercero.equals(other.tercero))
			return false;
		return true;
	}
}
