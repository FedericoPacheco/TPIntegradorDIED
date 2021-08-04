package entidades.valueObjects;

import java.time.LocalDate;

public class TareaDeMantenimiento implements Comparable<TareaDeMantenimiento>
{
	private Integer id;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private String observaciones;
	
	public TareaDeMantenimiento() {}
	
	public TareaDeMantenimiento(LocalDate fechaInicio, LocalDate fechaFin, String observaciones) 
	{
		// El id lo asigna la DB
		
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.observaciones = observaciones;
	}
	
	@Override
	public String toString() 
	{
		return "[" + id + ", " + fechaInicio.toString() + ", " + fechaFin.toString() + ", " + observaciones + "]";
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
		TareaDeMantenimiento other = (TareaDeMantenimiento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(TareaDeMantenimiento t) {
		return this.id.compareTo(t.getId());
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
}
