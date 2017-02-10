package com.cisco.bccsp.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.util.DBUtilCG1Orders;
import com.cisco.bccsp.model.Transaction;

public class CG1OrdersDAO {
	
	private static Logger logger = Logger.getLogger(CG1OrdersDAO.class.getName());
	
	/**
	 * getOrder
	 * 
	 * Get a single incident
	 * 
	 * @param incidentNumber
	 * @return
	 */
	public static Transaction getOrder(Transaction transaction) {

		Transaction orderTransaction = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilCG1Orders.getConnection();

			String query =
			  " SELECT HEAD.ORDER_NUMBER, " + 
			  "        SUM(LINE.UNIT_SELLING_PRICE * " + 
			  "            LINE.ORDERED_QUANTITY) ORDER_TOTAL, " +
		  	  "        LINE.SHIPMENT_NUMBER, HEAD.END_CUSTOMER_ID, " +
			  "        PARTY.PARTY_NAME, HEAD.FLOW_STATUS_CODE ORDER_STATUS " +
			  " FROM OE_ORDER_HEADERS_ALL HEAD, " +
			  "      OE_ORDER_LINES_ALL LINE, " +
			  "      HZ_CUST_ACCOUNTS ACCT, " +
			  "      HZ_PARTIES PARTY " +
		      " WHERE HEAD.ORDER_NUMBER = ? " +
			  "   AND LINE.HEADER_ID = HEAD.HEADER_ID " +
			  "   AND ACCT.CUST_ACCOUNT_ID = HEAD.END_CUSTOMER_ID " +
			  "   AND PARTY.PARTY_ID = ACCT.PARTY_ID  " +
			  " GROUP BY HEAD.ORDER_NUMBER, LINE.SHIPMENT_NUMBER, " + 
			  "       HEAD.END_CUSTOMER_ID, PARTY.PARTY_NAME, " + 
			  "       HEAD.FLOW_STATUS_CODE "; 
			
			stmt = conn.prepareStatement(query);

			stmt.setLong(1, transaction.getTransactionNumber());
			
            rs = stmt.executeQuery();

            if (rs.next()) {
            	
            	orderTransaction = new Transaction();

            	orderTransaction.setTransactionNumber(
                  rs.getLong("ORDER_NUMBER"));
            	orderTransaction.setAmount(
                  rs.getFloat("ORDER_TOTAL"));
            	orderTransaction.setShipSet(
                  rs.getString("SHIPMENT_NUMBER"));
            	orderTransaction.setCustomerId(
                  rs.getString("END_CUSTOMER_ID"));            	
            	orderTransaction.setCustomerName(
                  rs.getString("PARTY_NAME"));
            	orderTransaction.setTransactionStatus(
                  rs.getString("ORDER_STATUS"));                
                
                logger.info("get cg1 order");
                
            }


		} catch (SQLException e) {
			logger.error(
			  "No order transaction retrieved for OE_ORDER_HEADERS_ALL Table", 
			  e);
		}

		finally {		
			DBUtilCG1Orders.releaseConnectionResources(conn, rs, stmt);
		}
		
		return orderTransaction;
	}
}
