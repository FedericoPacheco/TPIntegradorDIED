package grafo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.apache.commons.collections.buffer.PriorityBuffer;

import clasesUtiles.Dupla;
import clasesUtiles.Tripleta;
import entidades.db.BoletoDB;
import entidades.db.EstacionDB;
import entidades.db.LineaDeTransporteDB;
import entidades.db.TareaDeMantenimientoDB;
import entidades.db.TramoDB;
import entidades.valueObjects.Boleto;
import entidades.valueObjects.Estacion;
import entidades.valueObjects.LineaDeTransporte;
import entidades.valueObjects.TareaDeMantenimiento;
import entidades.valueObjects.Tramo;

// https://www.youtube.com/watch?v=XB4MIexjvY0
// https://stackoverflow.com/questions/28998597/how-to-save-shortest-path-in-dijkstra-algorithm
// https://stackoverflow.com/questions/1074781/double-in-hashmap
// https://stackoverflow.com/questions/62056713/error-the-package-org-apache-commons-is-not-accessible
// https://commons.apache.org/proper/commons-collections/javadocs/api-3.2.2/org/apache/commons/collections/BinaryHeap.html
// https://commons.apache.org/proper/commons-collections/javadocs/api-3.2.2/org/apache/commons/collections/buffer/PriorityBuffer.html

public class RedDeTransporte             // i.e. un digrafo
{
	public static final Integer INFINITO_POSITIVO = Integer.MAX_VALUE;
	public static final Integer INFINITO_NEGATIVO = Integer.MIN_VALUE;
	
	
	private TreeSet<Estacion> estaciones;    // Nodos
	private TreeSet<Tramo> tramos;		     // Aristas
	private TreeSet<LineaDeTransporte> lineasDeTransporte;
	private TreeSet<TareaDeMantenimiento> tareasDeMantenimiento;
	private TreeSet<Boleto> boletos;
	
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
	// *NO* agregar o quitar elementos directamente. Usar add...() o remove...(). 
	// Luego de hacer algun/os set...() sobre un objeto, usar update...()
	public List<Estacion> getAllEstaciones() 							{ return new ArrayList<Estacion>(estaciones); 							}
	public List<Tramo> getAllTramos() 		 						 	{ return new ArrayList<Tramo>(tramos); 									}
	public List<TareaDeMantenimiento> getAllTareasDeMantenimiento() 	{ return new ArrayList<TareaDeMantenimiento>(tareasDeMantenimiento);	}
	public List<LineaDeTransporte> getAllLineasDeTransporte() 			{ return new ArrayList<LineaDeTransporte>(lineasDeTransporte);	 		}
	public List<Boleto> getAllBoletos() 								{ return new ArrayList<Boleto>(boletos);								}
	
	
	public Tramo getTramo(Integer idTramo) 
	{
		for (Tramo t: tramos)
			if (t.getId().equals(idTramo))
				return t;
		return null;
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
	
	private Set<Estacion> getEstacionesAdyacentes(Estacion estacion)
	{
		Set<Estacion> estacionesAdyacentes = new HashSet<Estacion>();
		
		for(Tramo t : tramos)
		{
			if 	(
					t.getIdOrigen().equals(estacion.getId()) && 
					estacion.estaActiva()					 &&
					this.tramoEstaEfectivamenteActivo(t)
				)
					estacionesAdyacentes.add(this.getEstacion(t.getIdDestino())); // Si ya esta no se agrega
		}
			
		return estacionesAdyacentes;
	}
	private List<Tramo> getTramosEntre(Estacion estacionOrigen, Estacion estacionDestino)
	{
		List<Tramo> tramosEntreEstaciones = new LinkedList<Tramo>();
		
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
		
	public Dupla<Double, LinkedList<Tramo>> caminoMasRapido(Estacion estacionOrigen, Estacion estacionDestino) 
	{
		return 
			this.Dijkstra
			(
				estacionOrigen, 
				(Tramo t) -> (double) t.getDuracionViajeEnMin()
			).get(estacionDestino);
	}
	public Dupla<Double, LinkedList<Tramo>> caminoMasCorto(Estacion estacionOrigen, Estacion estacionDestino) 
	{
		return 
			this.Dijkstra
			(
				estacionOrigen, 
				(Tramo t) -> (double) t.getDistanciaEnKm()
			).get(estacionDestino);
	}
	public Dupla<Double, LinkedList<Tramo>> caminoMasBarato(Estacion estacionOrigen, Estacion estacionDestino) 
	{
		return 
			this.Dijkstra
			(
				estacionOrigen, 
				(Tramo t) -> (double) t.getCosto()
			).get(estacionDestino);
	}
	
	@SuppressWarnings("unchecked") 
	private Map<Estacion, Dupla<Double, LinkedList<Tramo>>> Dijkstra(Estacion estacionOrigen, Function<Tramo, Double> criterio)
	{
		/*
			Dupla:
				primero: costo
				segundo: camino
				
			Tripleta:
				primero: estacion
				segundo: costo
				tercero: camino
		*/
		
		Dupla<Double, LinkedList<Tramo>> auxDupla;
		Map<Estacion, Dupla<Double, LinkedList<Tramo>>> costoYCaminoHastaCadaEstacion 
    		= new HashMap<Estacion, Dupla<Double, LinkedList<Tramo>>>();
    	
		// Inicializar las distancias a "infinito"
    	for (Estacion e : estaciones)
    	{
    		auxDupla = new Dupla<Double, LinkedList<Tramo>>((double) INFINITO_POSITIVO, new LinkedList<Tramo>());
    		costoYCaminoHastaCadaEstacion.put(e, auxDupla);
    	}
    	auxDupla = new Dupla<Double, LinkedList<Tramo>>(0.0, new LinkedList<Tramo>());
    	costoYCaminoHastaCadaEstacion.put(estacionOrigen, auxDupla);
    	
    	// Visitados y pendientes
    	Set<Estacion> estacionesVisitadas = new HashSet<Estacion>();
    	PriorityBuffer estacionesAVisitar = new PriorityBuffer(
    		// https://stackoverflow.com/a/44356167
    		(t1, t2) -> ((Tripleta<Estacion, Double, LinkedList<Tramo>>) t1).segundo.compareTo(
    					((Tripleta<Estacion, Double, LinkedList<Tramo>>) t2).segundo)
    	);
    	
    	estacionesAVisitar.add(new Tripleta<Estacion, Double, LinkedList<Tramo>>(estacionOrigen, 0.0, new LinkedList<Tramo>()));
    	
    	// Iterar mientras haya estaciones pendientes
    	Tripleta<Estacion, Double, LinkedList<Tramo>> auxTripleta;
    	Tripleta<Estacion, Double, LinkedList<Tramo>> tripletaEstacion;
    	LinkedList<Tramo> auxCamino;
    	Tramo tramoMenosCostoso;
    	Double costoActualHastaEstacionAdyacente;
    	Double costoDeEstacionActualAAdyacente;
    	Double costoHastaEstacionActual;
    	Double nuevoCostoTotal;
    	while(!estacionesAVisitar.isEmpty())
    	{
    		tripletaEstacion = (Tripleta<Estacion, Double, LinkedList<Tramo>>) estacionesAVisitar.get();
    		estacionesAVisitar.remove(tripletaEstacion);
    		estacionesVisitadas.add(tripletaEstacion.primero);
    		
    		for (Estacion estacionAdyacente : this.getEstacionesAdyacentes(tripletaEstacion.primero))
    		{
    			if (!estacionesVisitadas.contains(estacionAdyacente))
    			{
    				tramoMenosCostoso = this.tramoDeMenorCosto(
    					this.getTramosEntre(tripletaEstacion.primero, estacionAdyacente),
    					criterio
    				);
    				
    			    costoDeEstacionActualAAdyacente = (double) criterio.apply(tramoMenosCostoso);
    			    costoHastaEstacionActual = tripletaEstacion.segundo;
    			    costoActualHastaEstacionAdyacente = costoYCaminoHastaCadaEstacion.get(estacionAdyacente).primero;
    			    nuevoCostoTotal = costoHastaEstacionActual + costoDeEstacionActualAAdyacente;
    				
    				// Si se encuentra un nuevo camino menos costoso, cambiar el "resultado final" (costoYCaminoHastaCadaEstacion)
    				if (nuevoCostoTotal < costoActualHastaEstacionAdyacente)
    				{
    					// Por si acaso se hacen copias de los caminos xD
    					
    					// Actualizar costo y camino hasta nodoAdyacente
    					auxCamino = (LinkedList<Tramo>) tripletaEstacion.tercero.clone();
    					auxCamino.add(tramoMenosCostoso);
    					
    					auxDupla = new Dupla<Double, LinkedList<Tramo>>(nuevoCostoTotal, auxCamino);
    					
    					costoYCaminoHastaCadaEstacion.put(estacionAdyacente, auxDupla);
    					
    					
    					// Agregar estacion adyacente a la lista de pendientes
    					auxCamino = (LinkedList<Tramo>) auxCamino.clone();
    					
    					auxTripleta = new Tripleta<Estacion, Double, LinkedList<Tramo>>(estacionAdyacente, nuevoCostoTotal, auxCamino);
    					
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
	// Flujo maximo: 
	
	public List<Dupla<LinkedList<Tramo>, Integer>> flujoMaximo(Estacion origen, Estacion destino)
	{
		List<Dupla<LinkedList<Tramo>, Integer>> resultado = 
			new LinkedList<Dupla<LinkedList<Tramo>, Integer>>();
		
		LinkedList<Tramo> caminoHastaOrigen = new LinkedList<Tramo>();
		
		HashSet<Estacion> estacionesVisitadas = new HashSet<Estacion>();
		estacionesVisitadas.add(origen);
		
		Map<Tramo, Integer> flujoRestanteTramos = new HashMap<Tramo, Integer>();
		for (Tramo t : tramos)
			flujoRestanteTramos.put(t, t.getCantidadMaximaPasajeros());
		
		this.flujoMaximoAux(origen, destino, caminoHastaOrigen, estacionesVisitadas, flujoRestanteTramos, resultado);
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private void flujoMaximoAux(Estacion origen, Estacion destino, LinkedList<Tramo> caminoHastaOrigen, 
								HashSet<Estacion> estacionesVisitadas, Map<Tramo, Integer> flujoRestanteTramos, 
								List<Dupla<LinkedList<Tramo>, Integer>> resultado) 
	{
		if (origen.equals(destino))
		{
			if (caminoHastaOrigen.isEmpty()) // Se eligio el origen "original" y destino iguales			
				resultado.add(new Dupla<LinkedList<Tramo>, Integer>(new LinkedList<>(), INFINITO_POSITIVO));
			else
			{
				Integer flujoMaximo = 
					flujoRestanteTramos.get(
						Collections.min(
							caminoHastaOrigen, 
							(t1, t2) -> flujoRestanteTramos.get(t1).compareTo(flujoRestanteTramos.get(t2))
						)
					);
				
				if (flujoMaximo > 0)
				{
					// Restar el "flujo" que ocupa este camino
					for (Tramo t : caminoHastaOrigen)
						flujoRestanteTramos.put(t, flujoRestanteTramos.get(t) - flujoMaximo);
					
					// Guardar en el resultado
					resultado.add(new Dupla<LinkedList<Tramo>, Integer>(caminoHastaOrigen, flujoMaximo));
				}
			}			
		}
		else
		{
			for (Estacion estacionAdyacente : this.getEstacionesAdyacentes(origen))
			{
				if (!estacionesVisitadas.contains(estacionAdyacente))
				{				
					for (Tramo tramoEstacionAdyacente : this.getTramosEntre(origen, estacionAdyacente))
					{			
						if (flujoRestanteTramos.get(tramoEstacionAdyacente) > 0)
						{
							LinkedList<Tramo> caminoHastaOrigenCopia = (LinkedList<Tramo>) caminoHastaOrigen.clone();
							caminoHastaOrigenCopia.add(tramoEstacionAdyacente);
							
							HashSet<Estacion> estacionesVisitadasCopia = (HashSet<Estacion>) estacionesVisitadas.clone();
							estacionesVisitadasCopia.add(estacionAdyacente);
							
							this.flujoMaximoAux(
								estacionAdyacente, destino, caminoHastaOrigenCopia, estacionesVisitadasCopia,
								flujoRestanteTramos, resultado);
						}
					}
				}
			}
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	// Page rank: 
	
	public Map<Estacion, Double> estacionRank(Double factorDeAmortiguacion, Double error) // i.e. pageRank
	{
		Map<Estacion, Double> estacionRank = new HashMap<Estacion, Double>();
		Map<Estacion, Double> estacionRankAnterior = new HashMap<Estacion, Double>();
		Map<Estacion, Integer> cantidadEnlacesSalientesEstacion = new HashMap<Estacion, Integer>();
		Double unEstacionRank;
		
		for (Estacion e : estaciones)
		{
			estacionRank.put(e, 1.0);
			cantidadEnlacesSalientesEstacion.put(e, this.getCantidadDeEnlacesSalientes(e));
		}
		
		do
		{
			for (Estacion e : estaciones)
			{
				estacionRankAnterior.put(e, estacionRank.get(e));
				
				unEstacionRank = 0.0;
				for (Estacion eI : this.getEstacionesIncidentes(e))
					unEstacionRank += estacionRank.get(eI) / cantidadEnlacesSalientesEstacion.get(eI);
				
				unEstacionRank *= factorDeAmortiguacion;
				unEstacionRank += 1 - factorDeAmortiguacion;
				
				estacionRank.put(e, unEstacionRank);
			}
		}
		while(hayError(estacionRank, estacionRankAnterior, error));
		
		return estacionRank;
	}
	
	private Set<Estacion> getEstacionesIncidentes(Estacion estacion)
	{
		Set<Estacion> estacionesIncidentes = new HashSet<Estacion>();
		
		for (Tramo t : tramos)
			if (t.getIdDestino().equals(estacion.getId()))
				estacionesIncidentes.add(this.getEstacion(t.getIdDestino()));
		
		return estacionesIncidentes;		
	}

	private Integer getCantidadDeEnlacesSalientes(Estacion estacion) 
	{
		Integer suma = 0;
		for (Tramo t : tramos)
			if (t.getIdOrigen().equals(estacion.getId()))
				suma++;
		
		if (suma == 0)
			return 1; 		// Sumamente peligroso que una estacion no tenga tramos
		else
			return suma;
	}

	private Boolean hayError(Map<Estacion, Double> estacionRank, 						// No se que tan necesario es hacerlo asi
							 Map<Estacion, Double> estacionRankAnterior, Double error) 
	{
		Boolean noHayError = true;
		Iterator<Estacion> i = estaciones.iterator();
		Estacion e;
		
		while (i.hasNext() && noHayError)
		{
			e = i.next();
			noHayError = Math.abs(estacionRank.get(e) - estacionRankAnterior.get(e)) < error; 
		}
		
		return !noHayError;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	// Proximo manteninimiento:
	
	@SuppressWarnings("unchecked")
	public List<Estacion> proximosMantenimientos()
	{
		PriorityBuffer monticuloMantenimientos = 
			new PriorityBuffer(
				(d1, d2) -> ((Dupla<Estacion, LocalDate>) d1).segundo.compareTo(((Dupla<Estacion, LocalDate>) d2).segundo));
		
		List<Estacion> ordenMantenimientos = new LinkedList<Estacion>();
		
		LocalDate auxFecha;
		List<Integer> auxMantenimientos;
		
		// Especie de heap sort
		for (Estacion e : estaciones)
		{
			auxMantenimientos = e.getIdsMantenimientosRealizados();
			
			if (auxMantenimientos.isEmpty())
				auxFecha = LocalDate.of(1970, 1, 1);
			else
				auxFecha = this.getTareaDeMantenimiento(
					auxMantenimientos.get(auxMantenimientos.size() - 1)
				).getFechaInicio();
		
			monticuloMantenimientos.add(new Dupla<Estacion, LocalDate>(e, auxFecha));
		}
		
		while(!monticuloMantenimientos.isEmpty())
			ordenMantenimientos.add(
				((Dupla<Estacion, LocalDate>) monticuloMantenimientos.remove()).primero
			);
		
		return ordenMantenimientos;
	}
}
	
	

