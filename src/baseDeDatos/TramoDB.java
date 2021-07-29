package baseDeDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entidades.Tramo;

public class TramoDB extends EntidadDB
{
	public TramoDB() throws ClassNotFoundException, SQLException { super(); }

	public void createTramo(Tramo tramo) throws SQLException
	{
		Integer id = this.getIdTupla("tp_died.tramo_seq");
		
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO tp_died.tramo (id, distancia_en_km, duracion_viaje_en_min, cantidad_maxima_pasajeros, " +
									   "estado, costo, id_estacion_origen, id_estacion_destino) " +
			"VALUES (" + id + ", ?, ?, ?, ?, ?, ?, ?);"
		);
			
		tramo.setId(id);
		completarDatosBasicosTramo(ps, tramo);
		ps.executeUpdate();
			
		ps.close();
	}

	public void updateTramo(Tramo tramo) throws SQLException
	{
		PreparedStatement ps = c.prepareStatement(
			"UPDATE tp_died.tramo " +
			"SET distancia_en_km = ?, duracion_viaje_en_min = ?, cantidad_maxima_pasajeros = ?, " +
				"estado = ?, costo = ?, id_estacion_origen = ?, id_estacion_destino = ?" +
			"WHERE id = ?;"
		);
						
		completarDatosBasicosTramo(ps, tramo);
		ps.setInt(8, tramo.getId());
		ps.executeUpdate();
				
		ps.close();
	}
	
	public void deleteTramo(Integer idTramo) throws SQLException
	{
		PreparedStatement ps = c.prepareStatement("DELETE FROM tp_died.tramo WHERE id = ?;");
	
		ps.setInt(1, idTramo);
		ps.executeUpdate();
		
		ps.close();
	}	
	
	public Tramo getTramo(Integer idTramo) throws SQLException
	{
		Tramo tramo = null;
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.tramo WHERE id = ?;");		
		ResultSet rs; 
		
		ps.setInt(1, idTramo);
		rs = ps.executeQuery();
		if (rs.next())
			tramo = recuperarTramo(rs);
		
		rs.close();
		ps.close();
		
		return tramo;
	}
	
	public List<Tramo> getAllTramos() throws SQLException
	{
		List<Tramo> tramos = new ArrayList<Tramo>();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.tramo");// ORDER BY id;");		
		ResultSet rs; 
		
		rs = ps.executeQuery();
		while(rs.next())
			tramos.add(recuperarTramo(rs));
		
		rs.close();
		ps.close();
		
		return tramos;
	}

	private void completarDatosBasicosTramo(PreparedStatement ps, Tramo tramo) throws SQLException 
	{
		ps.setDouble(1, tramo.getDistanciaEnKm());
		ps.setInt(2, tramo.getDuracionViajeEnMin());
		ps.setInt(3, tramo.getCantidadMaximaPasajeros());
		
		if (tramo.getEstado() == Tramo.Estado.ACTIVO)
			ps.setString(4, "ACTIVO");
		else if (tramo.getEstado() == Tramo.Estado.INACTIVO)
			ps.setString(4, "INACTIVO");
		
		ps.setDouble(5, tramo.getCosto());
		ps.setInt(6, tramo.getIdOrigen());
		ps.setInt(7, tramo.getIdDestino());
	} 
	
	private Tramo recuperarTramo(ResultSet rs) throws SQLException
	{
		Tramo tramoAux = new Tramo();
		String estadoTramo;
		
		tramoAux.setId(rs.getInt("id"));
		tramoAux.setDistanciaEnKm(rs.getDouble("distancia_en_km"));
		tramoAux.setDuracionViajeEnMin(rs.getInt("duracion_viaje_en_min"));
		tramoAux.setCantidadMaximaPasajeros(rs.getInt("cantidad_maxima_pasajeros"));
		
		estadoTramo = rs.getString("estado");
		if (estadoTramo.equals("ACTIVO"))
			tramoAux.setEstado(Tramo.Estado.ACTIVO);
		else if (estadoTramo.equals("ACTIVO"))
			tramoAux.setEstado(Tramo.Estado.INACTIVO);
		
		tramoAux.setCosto(rs.getDouble("costo"));
		tramoAux.setIdOrigen(rs.getInt("id_estacion_origen"));
		tramoAux.setIdDestino(rs.getInt("id_estacion_destino"));
		
		return tramoAux;
	}
}
