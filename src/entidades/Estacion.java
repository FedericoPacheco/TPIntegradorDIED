package entidades;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Estacion implements Comparable<Estacion>
{
	public enum Estado
	{
		OPERATIVA,
		EN_MANTENIMIENTO
	};
	
	private Integer id;
	private String nombre;
	private LocalTime horaApertura;
	private LocalTime horaCierre;
	private Estado estado;
	private List <Integer> idsMantenimientosRealizados;
	
	public Estacion() {
		idsMantenimientosRealizados = new ArrayList<Integer>();
	}
	
	public Estacion(String nombre, LocalTime horaApertura, LocalTime horaCierre, Estado estado)
	{	
		//El id lo asigna la DB
		
		this.nombre = nombre;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.estado = estado;
		
		idsMantenimientosRealizados = new ArrayList<Integer>();
	}

	public Boolean estaActiva()
	{
		return 
			(estado == Estacion.Estado.OPERATIVA) 			&&
			(LocalTime.now().compareTo(horaApertura) >= 0)	&&
			(LocalTime.now().compareTo(horaCierre) <= 0);
	}
	
	@Override
	public String toString()
	{
		return "[" + id + ", " + nombre + ", " + horaApertura.toString() + ", " 
				+ horaCierre + ", " + ((estado == Estado.OPERATIVA)? "OPERATIVA" : "EN_MANTENIMIENTO") 
				+ ", " + idsMantenimientosRealizados.toString() + "]";
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
		Estacion other = (Estacion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Estacion e) {
		return this.id.compareTo(e.getId());
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
	public LocalTime getHoraApertura() {
		return horaApertura;
	}
	public void setHoraApertura(LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}
	public LocalTime getHoraCierre() {
		return horaCierre;
	}
	public void setHoraCierre(LocalTime horaCierre) {
		this.horaCierre = horaCierre;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public List<Integer> getIdsMantenimientosRealizados() {
		return idsMantenimientosRealizados;
	}
	public void setIdsMantenimientosRealizados(List<Integer> idsMantenimientosRealizados) {
		this.idsMantenimientosRealizados = idsMantenimientosRealizados;
	}
	
	// Solo para mantener consistente este objeto sin ir a la DB: 
	public void addIdMantenimiento(Integer idMantenimiento) {
		idsMantenimientosRealizados.add(idMantenimiento);
	}
	public void removeIdMantenimiento(Integer idMantenimiento) {
		idsMantenimientosRealizados.remove(idMantenimiento);
	}
}
