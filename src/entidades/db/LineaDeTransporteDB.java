package entidades.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entidades.valueObjects.LineaDeTransporte;

public class LineaDeTransporteDB extends EntidadDB
{
	public LineaDeTransporteDB() throws ClassNotFoundException, SQLException { super(); }
	
	public void createLineaDeTransporte(LineaDeTransporte linea) throws SQLException, ClassNotFoundException
	{
		Integer id;
		id = this.getIdTupla("tp_died.linea_de_transporte_seq");
		
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO tp_died.linea_de_transporte (id, nombre, color, estado) " +
			"VALUES (" + id + ", ?, ?, ?);"
		);
				
		linea.setId(id);
		completarDatosBasicosLineaDeTransporte(ps, linea);
		ps.executeUpdate();
		
		ps.close();
	}
	
	public void updateLineaDeTransporte(LineaDeTransporte linea) throws SQLException, ClassNotFoundException 
	{
		PreparedStatement ps = c.prepareStatement(
			"UPDATE tp_died.linea_de_transporte " +
			"SET nombre = ?, color = ?, estado = ? " +
			"WHERE id = ?;"
		);
		
		completarDatosBasicosLineaDeTransporte(ps, linea);
		ps.setInt(4, linea.getId());
		ps.executeUpdate();
		
		ps.close();
	}
	
	public void deleteLineaDeTransporte(Integer idLineaDeTransporte) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement ps1 = c.prepareStatement("DELETE FROM tp_died.tramo WHERE id_linea_de_transporte = ?;");
		PreparedStatement ps2 = c.prepareStatement("DELETE FROM tp_died.linea_de_transporte WHERE id = ?;");
		
		ps1.setInt(1, idLineaDeTransporte);
		ps2.setInt(1, idLineaDeTransporte);
		
		ps1.executeUpdate();
		ps2.executeUpdate();
		
		ps1.close();
		ps2.close();
	}
	
	public List<LineaDeTransporte> getAllLineasDeTransporte() throws SQLException, ClassNotFoundException 
	{
		List<LineaDeTransporte> lineas = new ArrayList<LineaDeTransporte>();
		
		TramoDB tramoDB = new TramoDB();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.linea_de_transporte;");// ORDER BY id;");		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next())
			lineas.add(recuperarLineaDeTransporte(rs, tramoDB));
		
		
		rs.close();
		ps.close();
		tramoDB.close();
		
		return lineas;
	}
	
	private void completarDatosBasicosLineaDeTransporte(PreparedStatement ps, LineaDeTransporte linea) throws SQLException
	{
		ps.setString(1, linea.getNombre());
		ps.setString(2, linea.getColor());
		if (linea.getEstado() == LineaDeTransporte.Estado.ACTIVA)
			ps.setString(3, "ACTIVA");
		else if (linea.getEstado() == LineaDeTransporte.Estado.INACTIVA)
			ps.setString(3, "INACTIVA");	
	}

	private LineaDeTransporte recuperarLineaDeTransporte(ResultSet rs, TramoDB tramoDB) throws SQLException, ClassNotFoundException
	{
		LineaDeTransporte lineaDeTransporteAux = new LineaDeTransporte();
		String estadoLineaDeTransporte;
		
		lineaDeTransporteAux.setId(rs.getInt("id"));
		lineaDeTransporteAux.setNombre(rs.getString("nombre"));
		lineaDeTransporteAux.setColor(rs.getString("color"));
				
		estadoLineaDeTransporte = rs.getString("estado");
		if(estadoLineaDeTransporte.equals("ACTIVA"))
			lineaDeTransporteAux.setEstado(LineaDeTransporte.Estado.ACTIVA);
		else if (estadoLineaDeTransporte.equals("INACTIVA"))
			lineaDeTransporteAux.setEstado(LineaDeTransporte.Estado.INACTIVA);
		
		lineaDeTransporteAux.setIdsTramos(tramoDB.getAllTramos(rs.getInt("id")));
		
		return lineaDeTransporteAux;
	}
}
