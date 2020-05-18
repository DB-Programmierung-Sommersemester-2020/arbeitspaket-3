package demo.repositories.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import demo.configurations.DBProperties;

public class DBConnection {

	private static DBConnection instance = new DBConnection();
	private DataSource datasource;

	private DBConnection() {
		super();
		Context ctxt;
		try {
			ctxt = new InitialContext();
			this.datasource = (DataSource) ctxt.lookup(DBProperties.getDBProperties().DATASOURCE_URL);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException{
		return this.datasource.getConnection();
	}
	
	public static DBConnection getInstance() {
		return instance;
	}


}
