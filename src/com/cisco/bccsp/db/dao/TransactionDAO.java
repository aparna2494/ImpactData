package com.cisco.bccsp.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.util.DBUtilCaseImpact;
import com.cisco.bccsp.model.Transaction;

public class TransactionDAO {
	
	private static Logger logger = Logger.getLogger(TransactionDAO.class.getName());

	
	public static List<Transaction> getActiveTrasnactions(Connection conn, 
	  String incidentNumber) throws SQLException {

		List<Transaction> transactions = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			String query = 
					  " SELECT INCIDENT_NUMBER, TRANSACTION_NUMBER, "
					+ "        SHIPSET, LINE_NUMBER, "
					+ "        TRANSACTION_TYPE, CUSTOMER_ID, "
					+ "        CUSTOMER_NAME, AMOUNT, "
					+ "        ACTIVE_FLAG, TRANSACTION_STATUS, "
					+ "        CREATE_USER, CREATE_DATE, "
					+ "        UPDATE_USER, UPDATE_DATE "
					+ " FROM CI_IMP_TRANSACTIONS "
					+ " WHERE INCIDENT_NUMBER = ? "
					+ "   AND ACTIVE_FLAG = 'Y' ";

			stmt = conn.prepareStatement(query);

			int i = 1;
			stmt.setString(i++, incidentNumber);;

			rs = stmt.executeQuery();

			while (rs.next()) {

				Transaction transaction = new Transaction();

				transaction.setIncidentNumber(rs.getString("INCIDENT_NUMBER"));
				transaction.setTransactionNumber(rs.getLong("TRANSACTION_NUMBER"));
				transaction.setShipSet(rs.getString("SHIPSET"));
				transaction.setLineNumber(rs.getString("LINE_NUMBER"));
				transaction
						.setTransactionType(rs.getString("TRANSACTION_TYPE"));
				transaction.setCustomerId(Integer.toString(rs
						.getInt("CUSTOMER_ID")));
				transaction.setCustomerName(rs.getString("CUSTOMER_NAME"));
				transaction.setAmount(rs.getFloat("AMOUNT"));
				transaction.setActiveFlag(rs.getString("ACTIVE_FLAG"));
				transaction.setTransactionStatus(rs
						.getString("TRANSACTION_STATUS"));

				transaction.setCreateUser(rs.getString("CREATE_USER"));

				java.sql.Timestamp timestamp = rs.getTimestamp("CREATE_DATE");
				if (timestamp != null) {
					transaction.setCreateDate(new java.util.Date(timestamp
							.getTime()));
				}

				transaction.setUpdateUser(rs.getString("UPDATE_USER"));

				timestamp = rs.getTimestamp("UPDATE_DATE");
				if (timestamp != null) {
					transaction.setUpdateDate(new java.util.Date(timestamp
							.getTime()));
				}
				
				if (transactions == null) {
					transactions = new ArrayList<Transaction>();
				}
				transactions.add(transaction);

			}

		} catch (SQLException e) {
			logger.error("No incident retrieved for CI_IMP_TRANSACTIONS Table", e);
			throw e;
		}

		finally {
			DBUtilCaseImpact.releaseConnectionResources(null, rs, stmt);
		}

		return transactions;

	}

	/**
	 * Finds a transaction by ignoring the active flag
	 * 
	 * @param conn
	 * @param incidentNumber
	 * @param transactionNumber
	 * @return
	 * @throws SQLException 
	 */
	public static Transaction transactionExistsIgnoreActiveFlag(Connection conn, 
	  String incidentNumber, Long transactionNumber) throws SQLException {
		
		Transaction transaction = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			String query = 
					" SELECT INCIDENT_NUMBER, TRANSACTION_NUMBER, " +
					"        SHIPSET, LINE_NUMBER, " +
					"        TRANSACTION_TYPE, CUSTOMER_ID, " +
					"        CUSTOMER_NAME, AMOUNT, " +
					"        ACTIVE_FLAG, TRANSACTION_STATUS, " + 
					"        CREATE_USER, CREATE_DATE, " + 
					"        UPDATE_USER, UPDATE_DATE " +
					" FROM CI_IMP_TRANSACTIONS " +
				    " WHERE INCIDENT_NUMBER = ? " + 
   	          	    "   AND TRANSACTION_NUMBER = ? "; 
			
			stmt = conn.prepareStatement(query);

			int i = 1;
			stmt.setString(i++, incidentNumber);
			stmt.setLong(i++, transactionNumber);
			
            rs = stmt.executeQuery();

            if (rs.next()) {
            	
                transaction = new Transaction();

                transaction.setIncidentNumber(
                  rs.getString("INCIDENT_NUMBER"));
                transaction.setTransactionNumber(rs.getLong("TRANSACTION_NUMBER"));
                transaction.setShipSet(rs.getString("SHIPSET"));
                transaction.setLineNumber(
                  rs.getString("LINE_NUMBER"));
                transaction.setTransactionType(
                  rs.getString("TRANSACTION_TYPE"));
                transaction.setCustomerId(
                  Integer.toString(rs.getInt("CUSTOMER_ID")));
                transaction.setCustomerName(
                  rs.getString("CUSTOMER_NAME"));
                transaction.setAmount(
                  rs.getFloat("AMOUNT"));                
                transaction.setActiveFlag(
                  rs.getString("ACTIVE_FLAG"));
                transaction.setTransactionStatus(
                  rs.getString("TRANSACTION_STATUS"));
                
                transaction.setCreateUser(rs.getString("CREATE_USER"));
 
                java.sql.Timestamp timestamp = rs.getTimestamp("CREATE_DATE");
                if (timestamp != null) {
                	transaction.setCreateDate(new java.util.Date(timestamp.getTime()));
                }
                
                transaction.setUpdateUser(rs.getString("UPDATE_USER"));

                timestamp = rs.getTimestamp("UPDATE_DATE");
                if (timestamp != null) {
                	transaction.setUpdateDate(new java.util.Date(timestamp.getTime()));
                }
            	
            }


		} catch (SQLException e) {
			logger.error("No transaction retrieved for CI_IMP_TRANSACTIONS Table", e);
			throw e;
		}

		finally {		
			DBUtilCaseImpact.releaseConnectionResources(null, rs, stmt);
		}
		
		return transaction;		
		
	}
	
	
	/**
	 * insertTransaction 
	 * 
	 * Inserts the given transaction
	 * 
	 * @param transaction
	 * @return
	 */
	public static Transaction insertTransaction(Connection conn, 
	  Transaction transaction) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date createDate = null;
		java.util.Date updateDate = null;		

		try {
			
			String insertQuery = 
					" INSERT INTO CI_IMP_TRANSACTIONS ( " +
					"     INCIDENT_NUMBER, " +
					"     TRANSACTION_NUMBER, " +
					"     SHIPSET, " + 
					"     LINE_NUMBER, " + 
					"     TRANSACTION_TYPE, " + 
				    "     CUSTOMER_ID, " + 
					"     CUSTOMER_NAME, " + 
					"     AMOUNT, " +
					"     ACTIVE_FLAG, " +
					"     TRANSACTION_STATUS, " +
					"     CREATE_USER, " +
					"     CREATE_DATE, " + 
					"     UPDATE_USER, " +
					"     UPDATE_DATE " +
					" ) VALUES ( " + 
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     'Y', " + 
					"     ?, " + 
					"     ?, " + 
					"     SYSDATE, " +
					"     ?, " + 
					"     SYSDATE " + 
					" ) ";

			stmt = conn.prepareStatement(insertQuery, new String[] { 
					"CREATE_DATE", "UPDATE_DATE" });

			int i = 1;
			stmt.setString(i++, transaction.getIncidentNumber());
			stmt.setLong(i++, transaction.getTransactionNumber());
			stmt.setString(i++, transaction.getShipSet());
			stmt.setString(i++, transaction.getLineNumber());
			stmt.setString(i++, transaction.getTransactionType());
			stmt.setInt(i++, Integer.parseInt(transaction.getCustomerId()));
			stmt.setString(i++, transaction.getCustomerName());
			stmt.setFloat(i++, transaction.getAmount().floatValue());
			stmt.setString(i++, transaction.getTransactionStatus());
			stmt.setString(i++, transaction.getCreateUser());
			stmt.setString(i++, transaction.getUpdateUser());

			if (stmt.executeUpdate() > 0) {

				ResultSet generatedKeys = stmt.getGeneratedKeys();

				if ((generatedKeys != null) && generatedKeys.next()) {

					java.sql.Timestamp timestamp= generatedKeys.getTimestamp(1);
					if (timestamp != null) {
						createDate = new java.util.Date(timestamp.getTime());
					}
					
					timestamp= generatedKeys.getTimestamp(2);
					if (timestamp != null) {
						updateDate = new java.util.Date(timestamp.getTime());
					}
					
				}

				transaction.setActiveFlag("Y");
				transaction.setCreateDate(createDate);
				transaction.setUpdateDate(updateDate);
				
			}

		} catch (SQLException e) {
			logger.error("No row inserted for CI_IMP_TRANSACTIONS Table", e);
		}

		finally {		
			DBUtilCaseImpact.releaseConnectionResources(null, rs, stmt);
		}
		
		return transaction;
	}
	
	
	
	/**
	 * Update a transaction, assume it is active if it is being updated
	 * 
	 * @param conn
	 * @param transaction
	 * @return
	 */
	public static Transaction updateTransaction(Connection conn, 
			  Transaction transaction) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date updateDate = null;		

		try {
			
			java.sql.Timestamp updateTimestamp = 
					  DBUtilCaseImpact.getCurrentTimestamp(conn);	
			
			String updateQuery = 
							" UPDATE CI_IMP_TRANSACTIONS  " +
							"     SET SHIPSET = ?, " + 
							"         LINE_NUMBER = ?, " + 
							"         TRANSACTION_TYPE = ?, " + 
						    "         CUSTOMER_ID = ?, " + 
							"         CUSTOMER_NAME = ?, " + 
							"         AMOUNT = ?, " +
							"         ACTIVE_FLAG = 'Y', " +
							"         TRANSACTION_STATUS = ?, " +
							"         UPDATE_USER = ?, " +
							"         UPDATE_DATE = ? " +
							" WHERE INCIDENT_NUMBER = ? " +
							"  AND TRANSACTION_NUMBER = ? ";

			stmt = conn.prepareStatement(updateQuery);

			int i = 1;
			stmt.setString(i++, transaction.getShipSet());
			stmt.setString(i++, transaction.getLineNumber());
			stmt.setString(i++, transaction.getTransactionType());
			stmt.setInt(i++, Integer.parseInt(transaction.getCustomerId()));
			stmt.setString(i++, transaction.getCustomerName());
			stmt.setFloat(i++, transaction.getAmount().floatValue());
			stmt.setString(i++, transaction.getTransactionStatus());
			stmt.setString(i++, transaction.getUpdateUser());
			stmt.setTimestamp(i++, updateTimestamp);
			stmt.setString(i++, transaction.getIncidentNumber());
			stmt.setLong(i++, transaction.getTransactionNumber());

			if (stmt.executeUpdate() > 0) {
				transaction.setUpdateDate(updateTimestamp);
			}

		} catch (SQLException e) {
			logger.error("No row updated for CI_IMP_TRANSACTIONS Table", e);
		}

		finally {		
			DBUtilCaseImpact.releaseConnectionResources(null, rs, stmt);
		}
				
		return transaction;
	}	
	
	
	
	/**
	 * Soft delete all transaction associated with the Incident
	 * 
	 * @param user
	 * @param incidentNumber
	 * @return
	 */
	public static boolean deleteTransactionByIncidentNumber(Connection conn,
	  String user, String incidentNumber) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean deleted = false;		

		try {
			
			conn = DBUtilCaseImpact.getConnection();			
					
			String updateQuery = 
				" UPDATE CI_IMP_TRANSACTIONS  " +
				"   SET ACTIVE_FLAG = 'N', " +
				"       UPDATE_USER = ?, " +
				"       UPDATE_DATE = SYSDATE " +
				" WHERE INCIDENT_NUMBER = ? ";
			
			stmt = conn.prepareStatement(updateQuery);
				
			int i = 1;
			stmt.setString(i++, user);
			stmt.setString(i++, incidentNumber);
			
			int totalDeleted = stmt.executeUpdate();
			
			logger.info("transactions deleted: " + totalDeleted);
			
			deleted = (totalDeleted > 0);
						
		} catch (SQLException e) {
			logger.error("No rows soft deleted for CI_IMP_TRANSACTIONS Table", e);
		}

		finally {		
			DBUtilCaseImpact.releaseConnectionResources(null, rs, stmt);
		}
				
		return deleted;
	}
	
}
