package de.javanachhilfe.halfbroke.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class ConnectionManager {

	/**
	 * 
	 */
	public ConnectionManager() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:mysql://localhost:3306/halfbroke";
			String username = "root";
			String password = "root";

			Connection connection = DriverManager.getConnection(url, username, password);

			PreparedStatement stmt = connection.prepareStatement("insert into Emp values(?,?)");
			stmt.setInt(1, 101);// 1 specifies the first parameter in the query
			stmt.setString(2, "Ratan");

			int i = stmt.executeUpdate();
			System.out.println(i + " records inserted");

			connection.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}