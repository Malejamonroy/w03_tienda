package tienda.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FuenteDatos {

	private FuenteDatos() {
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3366/02_tienda", "root", "1234");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
