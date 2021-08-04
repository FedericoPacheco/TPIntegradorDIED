package entidades.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entidades.valueObjects.TareaDeMantenimiento;

public class TareaDeMantenimientoDB extends EntidadDB
{
	public TareaDeMantenimientoDB() throws ClassNotFoundException, SQLException { super(); }
	
	public void createTareaDeMantenimiento(TareaDeMantenimiento tarea, Integer idEstacion) throws SQLException
	{
		Integer id = this.getIdTupla("tp_died.tarea_de_mantenimiento_seq");
		
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO tp_died.tarea_de_mantenimiento (id, fecha_inicio, fecha_fin, observaciones, id_estacion) " +
			"VALUES (" + id + ", ?, ?, ?, ?);"
		);
				
		tarea.setId(id);
		completarDatosBasicosTareaDeMantenimiento(ps, tarea, idEstacion);
		ps.executeUpdate();
		
		ps.close();
	}
	
	public void updateTareaDeMantenimiento(TareaDeMantenimiento tarea, Integer idEstacion) throws SQLException, ClassNotFoundException 
	{
		PreparedStatement ps = c.prepareStatement(
			"UPDATE tp_died.tarea_de_mantenimiento " +
			"SET fecha_inicio = ?, fecha_fin = ?, observaciones = ?, id_estacion = ? " +
			"WHERE id = ?;"
		);
		
		completarDatosBasicosTareaDeMantenimiento(ps, tarea, idEstacion);
		ps.setInt(5, tarea.getId());
		ps.executeUpdate();
		
		ps.close();
	}

	public void deleteTareaDeMantenimiento(Integer idTarea) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement ps = c.prepareStatement("DELETE FROM tp_died.tarea_de_mantenimiento WHERE id = ?;");
		
		ps.setInt(1, idTarea);
		ps.executeUpdate();
		
		ps.close();
	}
	
	public TareaDeMantenimiento getTareaDeMantenimiento(Integer idTarea) throws SQLException, ClassNotFoundException
	{
		TareaDeMantenimiento tarea = null;
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.tarea_de_mantenimiento WHERE id = ?;");		
		ResultSet rs; 
		
		ps.setInt(1, idTarea);
		rs = ps.executeQuery();
		if (rs.next())
			tarea = recuperarTareaDeMantenimiento(rs);
		
		rs.close();
		ps.close();
		
		return tarea;
	}

	public List<TareaDeMantenimiento> getAllTareasDeMantenimiento() throws SQLException, ClassNotFoundException 
	{
		List<TareaDeMantenimiento> tareas = new ArrayList<TareaDeMantenimiento>();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.tarea_de_mantenimiento;");// ORDER BY id;");		
		ResultSet rs = ps.executeQuery();
	
		while (rs.next())
			tareas.add(recuperarTareaDeMantenimiento(rs));
		
		rs.close();
		ps.close();
		
		return tareas;
	}
	
	public List<Integer> getAllTareasDeMantenimiento(Integer idEstacion) throws SQLException
	{
		List<Integer> idsTareas = new ArrayList<Integer>();
		
		PreparedStatement ps = c.prepareStatement("SELECT id FROM tp_died.tarea_de_mantenimiento WHERE id_estacion = ?;");// ORDER BY id;");		
		ResultSet rs;
		
		ps.setInt(1, idEstacion);
		rs = ps.executeQuery();
		while (rs.next())
			idsTareas.add(rs.getInt("id"));
		
		rs.close();
		ps.close();
		
		return idsTareas;
	}

	private void completarDatosBasicosTareaDeMantenimiento(PreparedStatement ps, TareaDeMantenimiento tarea, Integer idEstacion) throws SQLException
	{
		ps.setObject(1, tarea.getFechaInicio());
		ps.setObject(2, tarea.getFechaFin());
		ps.setString(3, tarea.getObservaciones());
		ps.setInt(4, idEstacion);
	}
	
	private TareaDeMantenimiento recuperarTareaDeMantenimiento(ResultSet rs) throws SQLException
	{
		TareaDeMantenimiento tareaAux = new TareaDeMantenimiento();
		
		tareaAux.setId(rs.getInt("id"));
		tareaAux.setFechaInicio(rs.getObject("fecha_inicio", LocalDate.class));
		tareaAux.setFechaFin(rs.getObject("fecha_fin", LocalDate.class));
		tareaAux.setObservaciones(rs.getString("observaciones"));
		
		return tareaAux;
	}
}
