package entidades.valueObjects;

public class Tramo implements Comparable<Tramo>
{
	public enum Estado 
	{
		ACTIVO,
		INACTIVO
	}
	
	private Integer id;
	private Double distanciaEnKm;
	private Integer duracionViajeEnMin;
	private Integer cantidadMaximaPasajeros;
	private Estado estado;
	private Double costo;
	private Integer idOrigen;
	private Integer idDestino;
	private Integer idLineaDeTransporte;
	
	public Tramo() {}
	
	public Tramo(
		Double distanciaEnKm, Integer duracionViajeEnMin, Integer cantidadMaximaPasajeros, 
		Estado estado, Double costo, Integer idOrigen, Integer idDestino, Integer idLineaDeTransporte
	) 
	{	
		// El id lo asigna la DB
		
		this.distanciaEnKm = distanciaEnKm;
		this.duracionViajeEnMin = duracionViajeEnMin;
		this.cantidadMaximaPasajeros = cantidadMaximaPasajeros;
		this.estado = estado;
		this.costo = costo;
		this.idOrigen = idOrigen;
		this.idDestino = idDestino;
		this.idLineaDeTransporte = idLineaDeTransporte;
	}
	
	@Override
	public String toString()
	{
		return "[" + id + ", " + distanciaEnKm + ", " + duracionViajeEnMin + ", " + cantidadMaximaPasajeros + ", " +
				((estado == Estado.ACTIVO)? "ACTIVO" : "INACTIVO") + ", " + costo + ", " + idOrigen + ", " 
				+ idDestino + ", " + idLineaDeTransporte + "]";
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
		Tramo other = (Tramo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Tramo t) {
		return this.id.compareTo(t.getId());
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getDistanciaEnKm() {
		return distanciaEnKm;
	}
	public void setDistanciaEnKm(Double distanciaEnKm) {
		this.distanciaEnKm = distanciaEnKm;
	}
	public Integer getDuracionViajeEnMin() {
		return duracionViajeEnMin;
	}
	public void setDuracionViajeEnMin(Integer duracionViajeEnMin) {
		this.duracionViajeEnMin = duracionViajeEnMin;
	}
	public Integer getCantidadMaximaPasajeros() {
		return cantidadMaximaPasajeros;
	}
	public void setCantidadMaximaPasajeros(Integer cantidadMaximaPasajeros) {
		this.cantidadMaximaPasajeros = cantidadMaximaPasajeros;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Integer getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(Integer idOrigen) {
		this.idOrigen = idOrigen;
	}
	public Integer getIdDestino() {
		return idDestino;
	}
	public void setIdDestino(Integer idDestino) {
		this.idDestino = idDestino;
	}
	public Integer getIdLineaDeTransporte() {
		return idLineaDeTransporte;
	}
	public void setIdLineaDeTransporte(Integer idLineaDeTransporte) {
		this.idLineaDeTransporte = idLineaDeTransporte;
	}
}


