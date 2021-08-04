package entidades.valueObjects;

import java.util.ArrayList;
import java.util.List;

public class LineaDeTransporte implements Comparable<LineaDeTransporte> 
{
	public enum Estado
	{
		ACTIVA,
		INACTIVA
	}
	
	private Integer id;
	private String nombre;
	private String color;
	private Estado estado;
	private List<Integer> idsTramos;
	
	public LineaDeTransporte() 
	{
		idsTramos = new ArrayList<Integer>();
	}
	
	public LineaDeTransporte(String nombre, String color, Estado estado) 
	{
		// El id lo asigna la DB
		
		this.nombre = nombre;
		this.color = color;
		this.estado = estado;
		
		idsTramos = new ArrayList<Integer>();
	}
	
	@Override
	public String toString()
	{
		return "[" + id + ", " + nombre + ", " + color + ", " + 
				((estado == Estado.ACTIVA)? "ACTIVA" : "INACTIVA") + ", " + idsTramos.toString() + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LineaDeTransporte other = (LineaDeTransporte) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(LineaDeTransporte l) {
		return this.id.compareTo(l.getId());
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public List<Integer> getIdsTramos() {
		return idsTramos;
	}
	public void setIdsTramos(List<Integer> idsEstaciones) {
		this.idsTramos = idsEstaciones;
	}
	
	// Solo para mantener consistente este objeto sin ir a la DB: 
	public void addIdTramo(Integer idTramo) {
		idsTramos.add(idTramo);
	}
	public void removeIdTramo(Integer idTramo) {
		idsTramos.remove(idTramo);
	}
}
