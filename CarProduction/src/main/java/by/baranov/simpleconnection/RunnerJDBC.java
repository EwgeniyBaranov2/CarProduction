package by.baranov.simpleconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunnerJDBC {
	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3306/mydatabasecarproduction?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		// jdbc:mysql://localhost:3306/mydatabasecarproduction";
		String user = "root";
		String password = "";
		Connection cn = null;

		try {
			cn = DriverManager.getConnection(url, user, password);
			Statement st = null;
			try {
				st = cn.createStatement();
				ResultSet rs = null;
				try {
					rs = st.executeQuery("SELECT * FROM CUSTOMERS");
					ArrayList<Customer> lst = new ArrayList<>();
					while (rs.next()) {
						int id = rs.getInt(1);
						String name = rs.getString("name");
						String date = rs.getString("date");
						int address_id = rs.getInt(4);
						lst.add(new Customer(id, name, date, address_id));
					}
					if (lst.size() > 0) {
						logger.log(Level.INFO, lst);
					} else {
						logger.log(Level.INFO, "Not found");
					}
				} finally {
					DbUtils.closeQuietly(rs);
					logger.log(Level.INFO, "ResultSet is closed");
				}
			} finally {
				DbUtils.closeQuietly(st);
				logger.log(Level.INFO, "Statement is closed");
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "DataBase connection error: " + e);
		} finally {
			DbUtils.closeQuietly(cn);
			logger.log(Level.INFO, "Connection is closed");
		}
	}
}
