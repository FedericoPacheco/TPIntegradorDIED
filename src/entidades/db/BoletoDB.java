package entidades.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entidades.valueObjects.Boleto;

// https://stackoverflow.com/questions/48643892/jdbc-inserting-an-array-variable-into-a-postgresql-table
// https://stackoverflow.com/questions/14935016/convert-a-result-set-from-sql-array-to-array-of-strings

public class BoletoDB extends EntidadDB 
{
	public BoletoDB() throws ClassNotFoundException, SQLException { super(); }

	public void createBoleto(Boleto boleto) throws SQLException
	{
		Integer id = this.getIdTupla("tp_died.boleto_seq");
		
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO tp_died.boleto (id, correo_electronico_cliente, nombre_cliente, fecha_venta, " +
									   "nombre_estacion_origen, nombre_estacion_destino, costo, camino) " +
			"VALUES (" + id + ", ?, ?, ?, ?, ?, ?, ?);"
		);
		
		boleto.setId(id);
		completarDatosBasicosBoleto(ps, boleto);
		ps.executeUpdate();
			
		ps.close();
	}

	public void updateBoleto(Boleto boleto) throws SQLException
	{
		PreparedStatement ps = c.prepareStatement(
			"UPDATE tp_died.boleto " +
			"SET correo_electronico_cliente = ?, nombre_cliente = ?, fecha_venta = ?," +
				"nombre_estacion_origen = ?, nombre_estacion_destino = ?, costo = ?, camino = ? " +
			"WHERE id = ?;"
		);
						
		completarDatosBasicosBoleto(ps, boleto);
		ps.setInt(8, boleto.getId());
		ps.executeUpdate();
				
		ps.close();
	}
	
	public void deleteBoleto(Integer idBoleto) throws SQLException
	{
		PreparedStatement ps = c.prepareStatement("DELETE FROM tp_died.boleto WHERE id = ?;");
	
		ps.setInt(1, idBoleto);
		ps.executeUpdate();
		
		ps.close();
	}	
	
	public Boleto getBoleto(Integer idBoleto) throws SQLException
	{
		Boleto boleto = null;
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.boleto WHERE id = ?;");		
		ResultSet rs; 
		
		ps.setInt(1, idBoleto);
		rs = ps.executeQuery();
		if (rs.next())
			boleto = recuperarBoleto(rs);
		
		rs.close();
		ps.close();
		
		return boleto;
	}
	
	public List<Boleto> getAllBoletos() throws SQLException
	{
		List<Boleto> boletos = new ArrayList<Boleto>();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tp_died.boleto;");// ORDER BY id;");		
		ResultSet rs; 
		
		rs = ps.executeQuery();
		while(rs.next())
			boletos.add(recuperarBoleto(rs));
		
		rs.close();
		ps.close();
		
		return boletos;
	}

	private void completarDatosBasicosBoleto(PreparedStatement ps, Boleto boleto) throws SQLException 
	{
		ps.setString(1, boleto.getCorreoElectronicoCliente());
		ps.setString(2, boleto.getNombreCliente());
		ps.setObject(3, boleto.getFechaVenta());
		ps.setString(4, boleto.getNombreEstacionOrigen());
		ps.setString(5, boleto.getNombreEstacionDestino());
		ps.setDouble(6, boleto.getCosto());
		
		ps.setArray(7, c.createArrayOf("VARCHAR", boleto.getCamino().toArray())); // Complicado, pero funciona
		//ps.setObject(7, boleto.getCamino()); 
	} 
	
	//@SuppressWarnings("unchecked")
	private Boleto recuperarBoleto(ResultSet rs) throws SQLException
	{
		Boleto boletoAux = new Boleto();
		
		boletoAux.setId(rs.getInt("id"));
		boletoAux.setCorreoElectronicoCliente(rs.getString("correo_electronico_cliente"));
		boletoAux.setNombreCliente(rs.getString("nombre_cliente"));
		boletoAux.setFechaVenta(rs.getObject("fecha_venta", LocalDate.class));
		boletoAux.setNombreEstacionOrigen(rs.getString("nombre_estacion_origen"));
		boletoAux.setNombreEstacionDestino(rs.getString("nombre_estacion_destino"));
		
		boletoAux.setCamino(Arrays.asList((String[]) rs.getArray("camino").getArray()));  // Complicado, pero funciona
		//boletoAux.setCamino(rs.getObject("camino", ArrayList.class)); 
		
		return boletoAux;
	}
}
