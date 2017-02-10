package com.cisco.bccsp.db.util;

//Example Java Program - Oracle Database Connectivity
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class CiscoCalendarLoader {
	public static final String DBURL = "jdbc:oracle:thin:@ldap://ldap-ldprd.cisco.com:5000/cn=OracleContext,dc=cisco,dc=com/crmprd";
	//public static final String DBURL = "jdbc:oracle:thin:@scan-prd-2028.cisco.com:1541:crmprd";
	public static final String DBUSER = "edw_stage";
	public static final String DBPASS = "";
	private static MongoDatabase db = null;

	private static Connection getOracleConnection() throws SQLException {
		// Load Oracle JDBC Driver
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

		Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);

		return con;
	}

	private static DB getMongoDBConnection() {
		MongoClient mongoClient = new MongoClient("vm-cisco-ops.cisco.com",
				27017);
		DB mongodb = mongoClient.getDB("bcc_db");

		return mongodb;
	}

	private static void runOneTimeLoad(Connection con, DB mongodb) {
		DBCollection col = mongodb.getCollection("bcc_time_hierarchy");
		try {
			con.setAutoCommit(false);

			String SQL = "select * from edw_time_hierarchy where calendar_date > '23-Jan-2017'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(SQL);
			rs.setFetchSize(5000);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			List<JSONObject> list = new ArrayList<JSONObject>();
			int count = 0;
			List docList = new ArrayList();

			while (rs.next()) {
				count++;
				JSONObject row = new JSONObject();
				BasicDBObject document = new BasicDBObject();
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					// System.out.print(columnValue + " " +
					// rsmd.getColumnName(i));
					row.put(rsmd.getColumnName(i), columnValue);
					document.put(rsmd.getColumnName(i), columnValue);
				}
				// list.add(row);
				// col.insert(document);
				docList.add(document);
				if (docList.size() == 5000) {
					col.insert(docList);
					docList = new ArrayList();
				}
			}

			if (docList.size() > 0)
				col.insert(docList);
			System.out.println("\ncount==" + count);
			rs.close();
			statement.close();		
			System.out.print("mongo col::" + col.count());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) throws SQLException {

		// Connect to Oracle Database
		Connection con = getOracleConnection();
		DB mongodb = getMongoDBConnection();
		runOneTimeLoad(con,mongodb);
		long startTime = System.nanoTime();
		con.close();
		long estimatedTime = System.nanoTime() - startTime;
		System.out.print("\nTime taken to complete the LOAD::" + estimatedTime/1000/1000	);

	}

}