package grafo;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.apache.commons.collections.buffer.PriorityBuffer;

import baseDeDatos.BoletoDB;
import baseDeDatos.EstacionDB;
import baseDeDatos.LineaDeTransporteDB;
import baseDeDatos.TareaDeMantenimientoDB;
import baseDeDatos.TramoDB;
import entidades.Boleto;
import entidades.Estacion;
import entidades.LineaDeTransporte;
import entidades.TareaDeMantenimiento;
import entidades.Tramo;

// https://www.youtube.com/watch?v=XB4MIexjvY0
// https://stackoverflow.com/questions/28998597/how-to-save-shortest-path-in-dijkstra-algorithm
// https://stackoverflow.com/questions/1074781/double-in-hashmap
// https://stackoverflow.com/questions/62056713/error-the-package-org-apache-commons-is-not-accessible
// https://commons.apache.org/proper/commons-collections/javadocs/api-3.2.2/org/apache/commons/collections/BinaryHeap.html
// https://commons.apache.org/proper/commons-collections/javadocs/api-3.2.2/org/apache/commons/collections/buffer/PriorityBuffer.html

public class RedDeTransporte             // i.e. un digrafo
{
	private Set<Estacion> estaciones;    // Nodos
	private Set<Tramo> tramos;		     // Aristas
	private Set<LineaDeTransporte> lineasDeTransporte;
	private Set<TareaDeMantenimiento> tareasDeMantenimiento;
	private Set<Boleto> boletos;
	
	private EstacionDB estacionDB;
	private TramoDB tramoDB;
	private LineaDeTransporteDB lineaDeTransporteDB;
	private TareaDeMantenimientoDB tareaDeMantenimientoDB;
	private BoletoDB boletoDB;
	
	public RedDeTransporte() throws ClassNotFoundException, SQLException
	{
		estacionDB = new EstacionDB();
		tramoDB = new TramoDB();
		lineaDeTransporteDB = new LineaDeTransporteDB();
		tareaDeMantenimientoDB = new TareaDeMantenimientoDB();
		boletoDB = new BoletoDB();
		
		estaciones = new TreeSet<Estacion>(estacionDB.getAllEstaciones());
		tramos = new TreeSet<Tramo>(tramoDB.getAllTramos());
		lineasDeTransporte = new TreeSet<LineaDeTransporte>(lineaDeTransporteDB.getAllLineasDeTransporte());
		tareasDeMantenimiento = new TreeSet<TareaDeMantenimiento>(tareaDeMantenimientoDB.getAllTareasDeMantenimiento());
		boletos = new TreeSet<Boleto>(boletoDB.getAllBoletos());
	}
	
	public void close() throws SQLException
	{
		estacionDB.close();
		tramoDB.close();
		lineaDeTransporteDB.close();
		tareaDeMantenimientoDB.close();
		boletoDB.close();
	}
	
	public void addEstacion(Estacion estacion) throws SQLException 
	{
		estacionDB.createEstacion(estacion);
		estaciones.add(estacion);
	}
	public void updateEstacion(Estacion estacion) throws ClassNotFoundException, SQLException
	{
		estacionDB.updateEstacion(estacion);
	}
	public void deleteEstacion(Estacion estacion) throws ClassNotFoundException, SQLException
	{
		estacionDB.deleteEstacion(estacion.getId());
		estaciones.remove(estacion);
	}
	
	public void addTramo(Tramo tramo) throws SQLException 
	{
		tramoDB.createTramo(tramo);
		tramos.add(tramo);
	}
	public void updateTramo(Tramo tramo) throws ClassNotFoundException, SQLException
	{
		tramoDB.updateTramo(tramo);
	}
	public void deleteTramo(Tramo tramo) throws ClassNotFoundException, SQLException
	{
		tramoDB.deleteTramo(tramo.getId());
		tramos.remove(tramo);
	}
	
	public void addLineaDeTransporte(LineaDeTransporte lineaDeTransporte) throws SQLException, ClassNotFoundException 
	{
		lineaDeTransporteDB.createLineaDeTransporte(lineaDeTransporte);
		lineasDeTransporte.add(lineaDeTransporte);
	}
	public void updateLineaDeTransporte(LineaDeTransporte lineaDeTransporte) throws ClassNotFoundException, SQLException
	{
		lineaDeTransporteDB.updateLineaDeTransporte(lineaDeTransporte);
	}
	public void deleteLineaDeTransporte(LineaDeTransporte lineaDeTransporte) throws ClassNotFoundException, SQLException
	{
		lineaDeTransporteDB.deleteLineaDeTransporte(lineaDeTransporte.getId());
		lineasDeTransporte.remove(lineaDeTransporte);
	}
	
	public void addTareaDeMantenimiento(TareaDeMantenimiento tareaDeMantenimiento, Estacion estacion) throws SQLException, ClassNotFoundException 
	{
		tareaDeMantenimientoDB.createTareaDeMantenimiento(tareaDeMantenimiento, estacion.getId());
		tareasDeMantenimiento.add(tareaDeMantenimiento);
	}
	public void updateTareaDeMantenimiento(TareaDeMantenimiento tareaDeMantenimiento, Estacion estacion) throws ClassNotFoundException, SQLException
	{
		tareaDeMantenimientoDB.updateTareaDeMantenimiento(tareaDeMantenimiento, estacion.getId());
	}
	public void deleteTareaDeMantenimiento(TareaDeMantenimiento tareaDeMantenimiento) throws ClassNotFoundException, SQLException
	{
		tareaDeMantenimientoDB.deleteTareaDeMantenimiento(tareaDeMantenimiento.getId());
		tareasDeMantenimiento.remove(tareaDeMantenimiento);
	}
	
	public void addBoleto(Boleto boleto) throws SQLException, ClassNotFoundException 
	{
		boletoDB.createBoleto(boleto);
		boletos.add(boleto);
	}
	public void updateBoleto(Boleto boleto) throws ClassNotFoundException, SQLException
	{
		boletoDB.updateBoleto(boleto);
	}
	public void deleteBoleto(Boleto boleto) throws ClassNotFoundException, SQLException
	{
		boletoDB.deleteBoleto(boleto.getId());
		boletos.remove(boleto);
	}
	
	// Metodos necesarios para las consultas en la interfaz grafica. 
	// *NO* agregar o quitar elementos a las listas directamente. Usar add...() o remove...(). 
	// Luego de hacer algun/os set...() sobre un objeto, usar update...()
	public List<Estacion> getAllEstaciones() 							{ return new ArrayList<Estacion>(estaciones); 							}
	public List<Tramo> getAllTramos() 		 						 	{ return new ArrayList<Tramo>(tramos); 									}
	public List<TareaDeMantenimiento> getAllTareasDeMantenimiento() 	{ return new ArrayList<TareaDeMantenimiento>(tareasDeMantenimiento);	}
	public List<LineaDeTransporte> getAllLineasDeTransporte() 			{ return new ArrayList<LineaDeTransporte>(lineasDeTransporte);	 		}
	public List<Boleto> getAllBoletos() 								{ return new ArrayList<Boleto>(boletos);								}
	
	public List<Tramo> getAllTramos(LineaDeTransporte linea)
	{
		List<Tramo> tramosLinea = new ArrayList<Tramo>();
		
		for (Tramo t : tramos)
			if (t.getIdLineaDeTransporte().equals(linea.getId()))
				tramosLinea.add(t);
		
		return tramosLinea;
	}
	
	public TareaDeMantenimiento getTareaDeMantenimiento(Integer idMantenimiento) 
	{
		for (TareaDeMantenimiento t: tareasDeMantenimiento)
			if (t.getId().equals(idMantenimiento))
				return t;
		return null;
	}
	
	public Estacion getEstacion(Integer idEstacion)
	{
		for (Estacion e: estaciones)
			if (e.getId().equals(idEstacion))
				return e;
		return null;
	}
	
	public LineaDeTransporte getLineaDeTransporte(Integer idLinea)
	{
		for (LineaDeTransporte l: lineasDeTransporte)
			if (l.getId().equals(idLinea))
				return l;
		return null;
	}
	
	public Boolean tramoEstaEfectivamenteActivo(Tramo tramo)
	{
		return 
			this.getEstacion(tramo.getIdOrigen()).estaActiva()  &&
			this.getEstacion(tramo.getIdDestino()).estaActiva() &&
			tramo.getEstado() == Tramo.Estado.ACTIVO;
	}
	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	// Operaciones generales grafos:
	
	private List<Estacion> getEstacionesAdyacentes(Estacion estacion)
	{
		List<Estacion> estacionesAdyacentes = new ArrayList<Estacion>();
		
		for(Tramo t : tramos)
			if 	(
					t.getIdOrigen().equals(estacion.getId()) && 
					estacion.estaActiva()					 &&
					this.tramoEstaEfectivamenteActivo(t)
				)
				estacionesAdyacentes.add(this.getEstacion(t.getIdDestino()));
			
		return estacionesAdyacentes;
	}
	private List<Tramo> getTramosEntre(Estacion estacionOrigen, Estacion estacionDestino)
	{
		List<Tramo> tramosEntreEstaciones = new ArrayList<Tramo>();
		
		for (Tramo t : tramos)
			if	(
					t.getIdOrigen().equals(estacionOrigen.getId()) 		&& 
					t.getIdDestino().equals(estacionDestino.getId()) 	&&
					this.tramoEstaEfectivamenteActivo(t)
				)
				tramosEntreEstaciones.add(t);
		
		return tramosEntreEstaciones;
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	// Dijkstra: 
		
	public DuplaCostoCamino caminoMasRapido(Estacion estacionOrigen, Estacion estacionDestino) 
	{
		return 
			this.Dijkstra
			(
				estacionOrigen, 
				(Tramo t) -> (double) t.getDuracionViajeEnMin()
			).get(estacionDestino);
	}
	public DuplaCostoCamino caminoMasCorto(Estacion estacionOrigen, Estacion estacionDestino) 
	{
		return 
			this.Dijkstra
			(
				estacionOrigen, 
				(Tramo t) -> (double) t.getDistanciaEnKm()
			).get(estacionDestino);
	}
	public DuplaCostoCamino caminoMasBarato(Estacion estacionOrigen, Estacion estacionDestino) 
	{
		return 
			this.Dijkstra
			(
				estacionOrigen, 
				(Tramo t) -> (double) t.getCosto()
			).get(estacionDestino);
	}
	
	@SuppressWarnings("unchecked")
	//private Map<Estacion, Dupla<Integer, ArrayList<Tramo>>> Dijkstra(Estacion estacionOrigen) 
	private Map<Estacion, DuplaCostoCamino> Dijkstra(Estacion estacionOrigen, Function<Tramo, Double> criterio)
	{
		//Dupla<Estacion, ArrayList<Tramo>> auxDupla1;
		DuplaCostoCamino auxDupla;
		Map<Estacion, DuplaCostoCamino> costoYCaminoHastaCadaEstacion 
    		= new HashMap<Estacion, DuplaCostoCamino>();
    	
		// Inicializar las distancias a "infinito"
    	for (Estacion e : estaciones)
    	{
    		//auxDupla1 = new Dupla<Estacion, ArrayList<Tramo>>(Integer.MAX_VALUE, new ArrayList<Tramo>());
    		auxDupla = new DuplaCostoCamino(Double.MAX_VALUE, new ArrayList<Tramo>());
    		//costoYCaminoHastaCadaEstacion.put(e, auxDupla1);
    		costoYCaminoHastaCadaEstacion.put(e, auxDupla);
    	}
    	//auxDupla1 = new Dupla<Estacion, ArrayList<Tramo>>(0, new ArrayList<Tramo>());
    	auxDupla = new DuplaCostoCamino(0.0, new ArrayList<Tramo>());
    	//costoYCaminoHastaCadaEstacion.put(estacionOrigen, auxDupla1);
    	costoYCaminoHastaCadaEstacion.put(estacionOrigen, auxDupla);
    	
    	// Visitados y pendientes
    	Set<Estacion> estacionesVisitadas = new HashSet<Estacion>();
    	//TreeMap<Integer, Dupla<Estacion, ArrayList<Tramo>>> estacionesAVisitar 
    	//	= new TreeMap<Integer, Dupla<Estacion, ArrayList<Tramo>>>();         // Pseudo-monticulo
    	PriorityBuffer estacionesAVisitar = new PriorityBuffer();
    	
    	//Dupla<Estacion, ArrayList<Tramo>> auxDupla2
    	//	= new Dupla<Estacion, ArrayList<Tramo>>(estacionOrigen, new ArrayList<Tramo>());
    	//estacionesAVisitar.put(0, auxDupla2);
    	estacionesAVisitar.add(new TripletaEstacionCostoCamino(estacionOrigen, 0.0, new ArrayList<Tramo>()));
    	
    	// Iterar mientras haya estaciones pendientes
    	//Entry<Integer, Dupla<Estacion, ArrayList<Tramo>>> estacion;
    	TripletaEstacionCostoCamino auxTripleta;
    	TripletaEstacionCostoCamino tripletaEstacion;
    	ArrayList<Tramo> auxCamino;
    	Tramo tramoMenosCostoso;
    	Double costoActualHastaEstacionAdyacente;
    	Double costoDeEstacionActualAAdyacente;
    	Double costoHastaEstacionActual;
    	Double nuevoCostoTotal;
    	while(!estacionesAVisitar.isEmpty())
    	{
    		//estacion = estacionesAVisitar.pollFirstEntry();  // Sacar el minimo (costo)
    		tripletaEstacion = (TripletaEstacionCostoCamino) estacionesAVisitar.get();
    		estacionesAVisitar.remove(tripletaEstacion);
    		//estacionesVisitadas.add(estacion.getValue().primero);
    		estacionesVisitadas.add(tripletaEstacion.estacion);
    		
    		//for (Estacion estacionAdyacente : this.getEstacionesAdyacentes(estacion.getValue().primero))
    		for (Estacion estacionAdyacente : this.getEstacionesAdyacentes(tripletaEstacion.estacion))
    		{
    			if (!estacionesVisitadas.contains(estacionAdyacente))
    			{
    				//tramoMenosCostoso 
    				//	= this.tramoDeMenorCosto(this.getTramosEntre(estacion.getValue().primero, estacionAdyacente));
    				tramoMenosCostoso
    					= this.tramoDeMenorCosto(
    						this.getTramosEntre(tripletaEstacion.estacion, estacionAdyacente),
    						criterio
    					);
    				
    			    costoDeEstacionActualAAdyacente = (double) criterio.apply(tramoMenosCostoso);
    			    //costoHastaEstacionActual = estacion.getKey();
    			    costoHastaEstacionActual = tripletaEstacion.costo;
    			    //costoActualHastaEstacionAdyacente = costoYCaminoHastaCadaEstacion.get(estacionAdyacente).primero;
    			    costoActualHastaEstacionAdyacente = costoYCaminoHastaCadaEstacion.get(estacionAdyacente).costo;
    			    nuevoCostoTotal = costoHastaEstacionActual + costoDeEstacionActualAAdyacente;
    				
    				// Si se encuentra un nuevo camino menos costoso, cambiar el "resultado final" (costoYCaminoHastaCadaEstacion)
    				if (nuevoCostoTotal < costoActualHastaEstacionAdyacente)
    				{
    					// Por si acaso se hacen copias de los caminos xD
    					
    					// Actualizar costo y camino hasta nodoAdyacente
    					//auxCamino = (ArrayList<Tramo>) estacion.getValue().segundo.clone();
    					auxCamino = (ArrayList<Tramo>) tripletaEstacion.camino.clone();
    					auxCamino.add(tramoMenosCostoso);
    					
    					//auxDupla1 = new Dupla<Integer, ArrayList<Tramo>>(nuevoCostoTotal, auxCamino);
    					auxDupla = new DuplaCostoCamino(nuevoCostoTotal, auxCamino);
    					
    					//costoYCaminoHastaCadaEstacion.put(estacionAdyacente, auxDupla1);
    					costoYCaminoHastaCadaEstacion.put(estacionAdyacente, auxDupla);
    					
    					
    					// Agregar estacion adyacente a la lista de pendientes
    					auxCamino = (ArrayList<Tramo>) auxCamino.clone();
    					
    					//auxDupla2 = new Dupla<Estacion, ArrayList<Tramo>>(estacionAdyacente, auxCamino);
    					auxTripleta = new TripletaEstacionCostoCamino(estacionAdyacente, nuevoCostoTotal, auxCamino);
    					
    					//estacionesAVisitar.put(nuevoCostoTotal, auxDupla2);
    					estacionesAVisitar.add(auxTripleta);
    				}
    			}
    		}
    	}
    		
    	return costoYCaminoHastaCadaEstacion;
	}
	
	private Tramo tramoDeMenorCosto (List<Tramo> tramos, Function<Tramo, Double> criterio)
	{
		Tramo tramoDeMenorCosto = null;
		Double costoMinimo = Double.MAX_VALUE;
		Double costo;
		
		for (Tramo t : tramos)
		{
			costo = criterio.apply(t);		
			if(costo < costoMinimo)
			{
				tramoDeMenorCosto = t;
				costoMinimo = costo;
			}
		}
		
		return tramoDeMenorCosto;
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	// 
		
	
	
}
	
	

