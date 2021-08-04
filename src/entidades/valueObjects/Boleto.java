package entidades.valueObjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Boleto implements Comparable<Boleto>
{
	private Integer id;
	private String correoElectronicoCliente;
	private String nombreCliente;
	private LocalDate fechaVenta;
	private String nombreEstacionOrigen;
	private String nombreEstacionDestino;
	private Double costo;
	private List<String> camino;
	
	public Boleto() 
	{
		camino = new ArrayList<String>();
	}
	
	public Boleto
	(
		String correoElectronicoCliente, String nombreCliente, LocalDate fechaVenta,
		String nombreEstacionOrigen, String nombreEstacionDestino, Double costo, List<String> camino
	) 
	{
		// El id lo asigna la DB
		
		this.correoElectronicoCliente = correoElectronicoCliente;
		this.nombreCliente = nombreCliente;
		this.fechaVenta = fechaVenta;
		this.nombreEstacionOrigen = nombreEstacionOrigen;
		this.nombreEstacionDestino = nombreEstacionDestino;
		this.costo = costo;
		this.camino = camino;
	}
	
	@Override
	public String toString()
	{
		return "[" + id + ", " + correoElectronicoCliente + ", " + nombreCliente + ", " + fechaVenta.toString()	+ ", " +
				nombreEstacionOrigen + ", " + nombreEstacionDestino + ", " + camino.toString() + "]"; 
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
		Boleto other = (Boleto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Boleto b) {
		return this.id.compareTo(b.getId());
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCorreoElectronicoCliente() {
		return correoElectronicoCliente;
	}
	public void setCorreoElectronicoCliente(String correoElectronicoCliente) {
		this.correoElectronicoCliente = correoElectronicoCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public LocalDate getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(LocalDate fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public String getNombreEstacionOrigen() {
		return nombreEstacionOrigen;
	}
	public void setNombreEstacionOrigen(String nombreEstacionOrigen) {
		this.nombreEstacionOrigen = nombreEstacionOrigen;
	}
	public String getNombreEstacionDestino() {
		return nombreEstacionDestino;
	}
	public void setNombreEstacionDestino(String nombreEstacionDestino) {
		this.nombreEstacionDestino = nombreEstacionDestino;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public List<String> getCamino() {
		return camino;
	}
	public void setCamino(List<String> camino) {
		this.camino = camino;
	}
	
	public void addEstacionEnCamino(String nombreEstacion) {
		camino.add(nombreEstacion);
	}
	public void removeEstacionEnCamino(String nombreEstacion) {
		camino.remove(nombreEstacion);
	}
}
