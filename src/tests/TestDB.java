package tests;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

public class TestDB 
{
	private TestDB() {}
	
	private static EstacionDB estDB;
	private static TareaDeMantenimientoDB tarDB;
	private static LineaDeTransporteDB linDB;
	private static TramoDB traDB;
	private static BoletoDB bolDB;
	
	private static Estacion est1, est2, est3, est4;
	private static TareaDeMantenimiento tar1, tar2;
	private static LineaDeTransporte lin1, lin2;
	private static Tramo tra1, tra2, tra3;
	private static Boleto bol1;
	
	/* Ayuda:
	 
		DELETE FROM tp_died.tarea_de_mantenimiento;
		DELETE FROM tp_died.tramo;
		DELETE FROM tp_died.linea_de_transporte;
		DELETE FROM tp_died.estacion;
		DELETE FROM tp_died.boleto;
		
		ALTER SEQUENCE tp_died.tarea_de_mantenimiento_seq RESTART;
		ALTER SEQUENCE tp_died.linea_de_transporte_seq RESTART;
		ALTER SEQUENCE tp_died.tramo_seq RESTART;
		ALTER SEQUENCE tp_died.estacion_seq RESTART;
		ALTER SEQUENCE tp_died.boleto_seq RESTART;
	*/
	
	public static void testear()
	{
		try 
		{
			TestDB.inicializar();
			
			///*
			System.out.println("Estaciones: ");
			TestDB.estaciones();
			System.out.println();
			
			System.out.println("Tareas de mantenimiento: ");
			TestDB.tareasDeMantenimiento();
			System.out.println();
			//*/
			System.out.println("Lineas de transporte: ");
			TestDB.lineasDeTransporte();
			System.out.println();
			
			System.out.println("Tramos: ");
			TestDB.tramos();
			System.out.println();
			
			System.out.println("Boletos: ");
			TestDB.boletos();
			System.out.println();
		} 
		catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
	}
	
	public static void inicializar() throws ClassNotFoundException, SQLException
	{
		estDB = new EstacionDB();
		tarDB = new TareaDeMantenimientoDB();
		linDB = new LineaDeTransporteDB();
		traDB = new TramoDB();
		bolDB = new BoletoDB();
		
		est1 = new Estacion("Estacion A", LocalTime.now(), LocalTime.now().plusHours(7), Estacion.Estado.OPERATIVA);
		est2 = new Estacion("Estacion B", LocalTime.now(), LocalTime.now().plusHours(8), Estacion.Estado.EN_MANTENIMIENTO);
		est3 = new Estacion("Estacion C", LocalTime.now(), LocalTime.now().plusHours(9), Estacion.Estado.OPERATIVA);
		est4 = new Estacion("Estacion D", LocalTime.now(), LocalTime.now().plusHours(10), Estacion.Estado.OPERATIVA);
	
		tar1 = new TareaDeMantenimiento(LocalDate.now(), LocalDate.now().plusDays(7), "");
		tar2 = new TareaDeMantenimiento(LocalDate.now(), LocalDate.now().plusDays(14), "Hey");
		
		lin1 = new LineaDeTransporte("Linea A", "Amarilla y Negra", LineaDeTransporte.Estado.ACTIVA);
		lin2 = new LineaDeTransporte("Linea B", "Blanca", LineaDeTransporte.Estado.INACTIVA);
		
		tra1 = new Tramo(1.0, 10, 10, Tramo.Estado.ACTIVO, 100.0, null, null, null);
		tra2 = new Tramo(2.0, 10, 20, Tramo.Estado.ACTIVO, 200.0, null, null, null);
		tra3 = new Tramo(3.0, 10, 15, Tramo.Estado.ACTIVO, 250.0, null, null, null);
		
		List<String> camino = new ArrayList<String>(); 
		camino.add(est1.getNombre()); camino.add(est2.getNombre()); camino.add(est3.getNombre());
		bol1 = new Boleto("fedepacheco2112@gmail.com", "Federico Pacheco", LocalDate.now(), est1.getNombre(), est3.getNombre(), 100.0, camino);
	}
	
	private static void estaciones() throws SQLException, ClassNotFoundException
	{
		estDB.createEstacion(est1);
		estDB.createEstacion(est2);
		estDB.createEstacion(est3);
		estDB.createEstacion(est4);
		
		for (Estacion e : estDB.getAllEstaciones())
			System.out.println(e);
		
		System.out.println();
		
		est1.setNombre("Estacion A yay!"); 
		est3.setNombre("Estacion C yay!");
		estDB.updateEstacion(est1);
		estDB.updateEstacion(est3);
		estDB.deleteEstacion(est4.getId());	
		
		for (Estacion e : estDB.getAllEstaciones())
			System.out.println(e);
	}
	
	private static void tareasDeMantenimiento() throws SQLException, ClassNotFoundException
	{
		tarDB.createTareaDeMantenimiento(tar1, est2.getId());
		tarDB.createTareaDeMantenimiento(tar2, est2.getId());
		
		tar1.setObservaciones(":o");
		tarDB.updateTareaDeMantenimiento(tar1, est2.getId());
		
		for (TareaDeMantenimiento t : tarDB.getAllTareasDeMantenimiento())
			System.out.println(t);
		
		System.out.println();
		
		tarDB.deleteTareaDeMantenimiento(tar1.getId());
		
		for (TareaDeMantenimiento t : tarDB.getAllTareasDeMantenimiento())
			System.out.println(t);
		
		//System.out.println();
		//System.out.println(estDB.getEstacion(est1.getId()));
	}
	
	private static void lineasDeTransporte() throws ClassNotFoundException, SQLException
	{
		linDB.createLineaDeTransporte(lin1);
		linDB.createLineaDeTransporte(lin2);
		
		for (LineaDeTransporte l : linDB.getAllLineasDeTransporte())
			System.out.println(l);
		
		System.out.println();
		
		lin1.setNombre("Linea A yay!");
		linDB.updateLineaDeTransporte(lin1);
		linDB.deleteLineaDeTransporte(lin2.getId());
		
		for (LineaDeTransporte l : linDB.getAllLineasDeTransporte())
			System.out.println(l);
		
		System.out.println();
	}
	
	private static void tramos() throws SQLException
	{
		tra1.setIdOrigen(est1.getId()); tra1.setIdDestino(est2.getId()); tra1.setIdLineaDeTransporte(lin1.getId());
		tra2.setIdOrigen(est2.getId()); tra2.setIdDestino(est3.getId()); tra2.setIdLineaDeTransporte(lin1.getId());
		tra3.setIdOrigen(est3.getId()); tra3.setIdDestino(est1.getId()); tra3.setIdLineaDeTransporte(lin1.getId());
		
		traDB.createTramo(tra1);
		traDB.createTramo(tra2);
		traDB.createTramo(tra3);
		
		for (Tramo t : traDB.getAllTramos())
			System.out.println(t);
		
		System.out.println();
		
		tra2.setEstado(Tramo.Estado.INACTIVO);
		traDB.updateTramo(tra2);
		
		traDB.deleteTramo(tra1.getId());
		
		for (Tramo t : traDB.getAllTramos())
			System.out.println(t);
	}
	
	private static void boletos() throws SQLException
	{
		bolDB.createBoleto(bol1);
		
		bol1.setNombreCliente("Federico Ignacio Pacheco Pilan");
		bolDB.updateBoleto(bol1);
		
		for (Boleto b : bolDB.getAllBoletos())
			System.out.println(b);
		System.out.println();
		
		bolDB.deleteBoleto(bol1.getId());
		
		for (Boleto b : bolDB.getAllBoletos())
			System.out.println(b);
	}
}
