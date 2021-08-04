package entidades.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import entidades.valueObjects.Estacion;

// https://jdbc.postgresql.org/documentation/head/8-date-time.html

public class EstacionDB extends EntidadDB
{
	public EstacionDB() throws ClassNotFoundException, SQLException { super(); }
	
	public void createEstacion(Estacion estacion) throws SQLException 
	{
		Integer id = this.getIdTupla("tp_died.estacion_seq");
		
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO tp_died.estacion (id, nombre, hora_apertura, hora_cierre, estado) " +
			"VALUES (" + id + ", ?, ?, ?, ?);"
		);
				
		estacion.setId(id);
		completarDatosBasicosEstacion(ps, estacion);
		// Los mantenimientos realizados se ignoran porque deben tratarse aparte con la clase TareaDeMantenimientoDB
		// (el dato de la estación a la que se le hace mantenimiento esta en la tabla tarea_de_mantenimiento)
		ps.executeUpdate();
		
		ps.close();
	}
	
	public void updateEstacion(Estacion estacion) throws SQLException, ClassNotFoundException 
	{
		PreparedStatement ps = c.prepareStatement(
			"UPDATE tp_died.estacion " +
			"SET nombre = ?, hora_apertura = ?, hora_cierre = ?, estado = ? " +
			"WHERE id = ?;"
		);
		
		completarDatosBasicosEstacion(ps, estacion);
		// Los mantenimientos realizados se ignoran porque deben tratarse aparte con la clase TareaDeMantenimientoDB
		// (el dato de la estación a la que se le hace mantenimiento esta en la tabla tarea_de_mantenimiento)		
		ps.setInt(5, estacion.getId());
		ps.executeUpdate();
		
		ps.close();
	}

	public void deleteEstacion(Integer idEstacion) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement ps1 = c.prepareStatement("DELETE FROM tp_died.tarea_de_mantenimiento WHERE id_estacion = ?;");
		PreparedStatement ps2 = c.prepareStatement("DELETE FROM tp_died.tramo WHERE id_estacion_origen = ? OR id_estacion_destino = ?");
		PreparedStatement ps3 = c.prepareStatement("DELETE FROM tp_died.estacion WHERE id = ?;");
		
		ps1.setInt(1, idEstacion);
		ps2.setInt(1, idEstacion);
		ps2.setInt(2, idEstacion);
		ps3.setInt(1, idEstacion);
		ps1.executeUpdate();
		ps2.executeUpdate();
		ps3.executeUpdate();
		
		ps1.close();
		ps2.close();
		ps3.close();
	}
	
	public Estacion getEstacion(Integer idEstacion) throws SQLException, ClassNotFoundException
	{
		Estacion estacion = null;
		
		TareaDeMantenimientoDB tareaDB = new TareaDeMantenimientoDB();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.estacion WHERE id = ?;");		
		ResultSet rs; 
		
		ps.setInt(1, idEstacion);
		rs = ps.executeQuery();
		if (rs.next())
			estacion = recuperarEstacion(rs, tareaDB);
		
		tareaDB.close();
		rs.close();
		ps.close();
		
		return estacion;
	}

	public List<Estacion> getAllEstaciones() throws SQLException, ClassNotFoundException 
	{
		List<Estacion> estaciones = new ArrayList<Estacion>();
		
		TareaDeMantenimientoDB tareaDB = new TareaDeMantenimientoDB();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.estacion");// ORDER BY id;");		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next())
			estaciones.add(recuperarEstacion(rs, tareaDB));
		
		rs.close();
		ps.close();
		tareaDB.close();
		
		return estaciones;
	}

	private void completarDatosBasicosEstacion(PreparedStatement ps, Estacion estacion) throws SQLException
	{
		ps.setString(1, estacion.getNombre());
		ps.setObject(2, estacion.getHoraApertura());
		ps.setObject(3, estacion.getHoraCierre());
		if (estacion.getEstado() == Estacion.Estado.OPERATIVA)
			ps.setString(4, "OPERATIVA");
		else if (estacion.getEstado() == Estacion.Estado.EN_MANTENIMIENTO)
			ps.setString(4, "EN_MANTENIMIENTO");
	}
	
	private Estacion recuperarEstacion(ResultSet rs, TareaDeMantenimientoDB tareaDB) throws SQLException, ClassNotFoundException
	{
		Estacion estacionAux = new Estacion();
		String estadoEstacion;
		
		estacionAux.setId(rs.getInt("id"));
		estacionAux.setNombre(rs.getString("nombre"));
		estacionAux.setHoraApertura(rs.getObject("hora_apertura", LocalTime.class));
		estacionAux.setHoraCierre(rs.getObject("hora_cierre", LocalTime.class));
		
		estadoEstacion = rs.getString("estado");
		if(estadoEstacion.equals("OPERATIVA"))
			estacionAux.setEstado(Estacion.Estado.OPERATIVA);
		else if (estadoEstacion.equals("EN_MANTENIMIENTO"))
			estacionAux.setEstado(Estacion.Estado.EN_MANTENIMIENTO);
		
		estacionAux.setIdsMantenimientosRealizados(tareaDB.getAllTareasDeMantenimiento(rs.getInt("id")));
		
		return estacionAux;
	}
}
