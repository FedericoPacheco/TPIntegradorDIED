package entidades.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// https://stackoverflow.com/questions/30964016/how-to-resolve-no-suitable-driver-found-error
// https://stackoverflow.com/questions/33350971/no-suitable-driver-for-postgres-even-though-class-forname-works
// https://www.youtube.com/watch?v=C4EUAfDg4Hc

public abstract class EntidadDB 
{
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String user = "postgres";
	private static final String pass = "postgres";
	
	protected Connection c;
	
	public EntidadDB() throws ClassNotFoundException, SQLException 
	{
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(url, user, pass); 
	}
	
	public void close() throws SQLException { 
		c.close(); 
	}
	
	// Llamar con "tp_died.<nombre>_seq"
	protected Integer getIdTupla(String secuencia) throws SQLException 
	{
		PreparedStatement ps = c.prepareStatement("SELECT NEXTVAL('" + secuencia + "');");
		ResultSet rs = ps.executeQuery();
		rs.next();
		Integer id = rs.getInt(1);
		rs.close();
		ps.close();
		
		return id;
	}
}
