package com.cisco.bccsp.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cisco.bccsp.db.util.DBUtilCaseImpact;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.Incident;
import com.cisco.bccsp.model.IncidentDetails;
import com.cisco.bccsp.model.Transaction;

public class IncidentDAO {

	private static Logger logger = Logger.getLogger(IncidentDAO.class.getName());
	
	public static String updateIncident(IncidentDetails incident) {

		JSONObject returnObj = new JSONObject();
		JSONObject obj = new JSONObject();
		try {
			obj.put("Notes", incident.getNotes());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic SVRTVF9FVE9PTC5nZW46ZXRvb2xjZXQ=");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(obj.toJSONString(), headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(
		        "https://wsgx.cisco.com/remedy/service/arservice/remedyintegration/createworkinfo/incident/" + incident.getIncidentNumber(), HttpMethod.POST, request, String.class);
		String result = response.getBody();
		returnObj.put("response", result);
		return returnObj.toJSONString();
		}
		catch (Exception e) {
			returnObj.put("response", e.getStackTrace());
			return returnObj.toString();
		}
	}
	
	public static String getIncidentDetails(String incidentNumber) {
		
		JSONObject finalObj = new JSONObject();		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic SVRTVF9SRU1TRy5nZW46Q2lzY28kMTIz");
			headers.add("Content-Type", "application/json");
			HttpEntity<String> request = new HttpEntity<String>(null, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(
		        "https://wsgx.cisco.com/remedy/service/arservice/remedyintegration/get/incident/details/" + incidentNumber, HttpMethod.GET, request, String.class);
			String result = response.getBody();
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result);
			if(obj.containsKey(incidentNumber)) {
				finalObj.put("incidentDetails",obj.get(incidentNumber));
				finalObj.put("result", "success");
			}
			else
				finalObj.put("result", "error: Incident not found");
			
		}
		catch (Exception e) {
			logger.error("Error, " + e);
		}
		return finalObj.toJSONString();
	}
	
public static String getIncidentWLDetails(String incidentNumber) {
		
		JSONObject finalObj = new JSONObject();		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic SVRTVF9SRU1TRy5nZW46Q2lzY28kMTIz");
			headers.add("Content-Type", "application/json");
			HttpEntity<String> request = new HttpEntity<String>(null, headers);
			RestTemplate remTemplate = new RestTemplate();
			ResponseEntity<String> response = remTemplate.exchange(
			        "https://wsgx.cisco.com/remedy/service/arservice/remedyintegration/get/incident/workinfo/" + incidentNumber, HttpMethod.GET, request, String.class);
			String res = response.getBody();
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(res);
			finalObj.put("workLogDetails", obj);
			finalObj.put("result", "success");
			
		}
		catch (Exception e) {
			logger.error("Error, " + e);
		}
		return finalObj.toJSONString();
	}

public static String getRKMDetails(String keyword) {
	
	JSONObject obj = new JSONObject();		
	try {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic SVRTVF9SRU1TRy5nZW46Q2lzY28kMTIz");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(null, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(
	        "https://wsgx-stage.cisco.com/remedy/service/arservice/remedyintegration/get/knowledge/articles/" + keyword, HttpMethod.GET, request, String.class);
		String result = response.getBody();
		result = ""; /*commented the response for DEMO as we showing non-related RMK articles*/

		JSONParser parser = new JSONParser();
		obj = (JSONObject) parser.parse(result);
		
	}
	catch (Exception e) {
		logger.error("Error, " + e);
	}
	return obj.toJSONString();
}
	
	/**
	 * getIncidents
	 * 
	 * Gets a list of incidents, if a filter is present it uses the string
	 * to reduce the result set.
	 * 
	 * @param searchFilter
	 * @return
	 */
	public static List<Incident> getIncidents(String searchFilter) {

		ArrayList<Incident> incidentList = new ArrayList<Incident>();
		Incident incident = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean searchFilterPresent = !"ALL".equals(searchFilter);
		
		try {
			conn = DBUtilCaseImpact.getConnection();

			String query = 
					" SELECT INCIDENT_NUMBER, RELEASE_PRIORITY_BY_USER, " +
					"        ISSUE_EXIST_BEFORE_RELEASE, WORKAROUND_AVAILABLE, " +
					"        CONTACTS, INCIDENT_STATUS_IN_REMEDY, " +
					"        RELEASE_PRIORITY_IN_REMEDY, TAGGED_AS_RELEASE_ISSUE, TRANSACTIONS_IMPACTED, " +
					"        IS_SOFTWARE_ISSUE, IMP_TRANSACTION_TYPE, AMOUNT_ENTERED_BY_USER," +
					"        CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE, PROJECT " +
					" FROM CI_INCIDENTS ";
			
			if (searchFilterPresent) {
			    query += " WHERE INCIDENT_NUMBER LIKE ? "; 
			}
			
			stmt = conn.prepareStatement(query);

			if (searchFilterPresent) {
			    stmt.setString(1, "%" + searchFilter + "%");
			}
			
            rs = stmt.executeQuery();

            while (rs.next()) {
            	
                incident = new Incident();

                incident.setIncidentNumber(
                  rs.getString("INCIDENT_NUMBER"));
                incident.setReleasePriorityByUser(
                  rs.getString("RELEASE_PRIORITY_BY_USER"));
                incident.setIssueExistBeforeRelease(
                  rs.getString("ISSUE_EXIST_BEFORE_RELEASE"));
                incident.setWorkaroundAvailable(
                  rs.getString("WORKAROUND_AVAILABLE"));
                incident.setContacts(
                  rs.getString("CONTACTS"));
                incident.setIncidentStatusInRemedy(
                  rs.getString("INCIDENT_STATUS_IN_REMEDY"));
                incident.setReleasePriorityInRemedy(
                  rs.getString("RELEASE_PRIORITY_IN_REMEDY"));                
                incident.setTaggedAsReleaseIssue(
                  rs.getString("TAGGED_AS_RELEASE_ISSUE"));

                incident.setTransactionsImpacted(
                        rs.getString("TRANSACTIONS_IMPACTED"));

                incident.setIsSoftwareIssue(
                        rs.getString("IS_SOFTWARE_ISSUE"));

                incident.setImpTransactionType(
                        rs.getString("IMP_TRANSACTION_TYPE"));
                
                incident.setAmountEnteredByUser(
                        rs.getString("AMOUNT_ENTERED_BY_USER"));

                
                incident.setCreateUser(rs.getString("CREATE_USER"));
                
                incident.setProject(rs.getString("PROJECT"));
                
                java.sql.Timestamp timestamp = rs.getTimestamp("CREATE_DATE");
                if (timestamp != null) {
                	incident.setCreateDate(new java.util.Date(timestamp.getTime()));
                }
                incident.setUpdateUser(rs.getString("UPDATE_USER"));

                timestamp = rs.getTimestamp("UPDATE_DATE");
                if (timestamp != null) {
                	incident.setUpdateDate(new java.util.Date(timestamp.getTime()));
                }
                
                // get associated transactions
                List<Transaction> transactions = 
                  TransactionDAO.getActiveTrasnactions(conn, 
                    incident.getIncidentNumber());
                
                incident.setTransactions(transactions);
                
                // add up the amounts
                float totalAmount = 0.0f;
                if (transactions != null) {
                    for (Transaction transaction : transactions) {
                	    totalAmount += transaction.getAmount().floatValue();
                    }
                }
                incident.setTotalAmount(new Float(totalAmount));
                
                incidentList.add(incident);
            	
            }


		} catch (SQLException e) {
			logger.error("No incidents retrieved for CI_INCIDENTS Table", e);
		}

		finally {		
			DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incidentList;
	}
	
	
	
	/**
	 * getIncident
	 * 
	 * Get a single incident
	 * 
	 * @param incidentNumber
	 * @return
	 */
	public static Incident getIncident(String incidentNumber) {

		Incident incident = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilCaseImpact.getConnection();

			String query = 
					" SELECT INCIDENT_NUMBER, RELEASE_PRIORITY_BY_USER, " +
					"        ISSUE_EXIST_BEFORE_RELEASE, WORKAROUND_AVAILABLE, " +
					"        CONTACTS, INCIDENT_STATUS_IN_REMEDY, " +
					"        RELEASE_PRIORITY_IN_REMEDY, TAGGED_AS_RELEASE_ISSUE, TRANSACTIONS_IMPACTED," +
					"        IS_SOFTWARE_ISSUE, IMP_TRANSACTION_TYPE, AMOUNT_ENTERED_BY_USER," +
					"        CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE, PROJECT " +
					" FROM CI_INCIDENTS " +
				    " WHERE INCIDENT_NUMBER = ? "; 
			
			stmt = conn.prepareStatement(query);

			stmt.setString(1, incidentNumber);
			
            rs = stmt.executeQuery();

            if (rs.next()) {
            	
                incident = new Incident();

                incident.setIncidentNumber(
                  rs.getString("INCIDENT_NUMBER"));
                incident.setReleasePriorityByUser(
                  rs.getString("RELEASE_PRIORITY_BY_USER"));
                incident.setIssueExistBeforeRelease(
                  rs.getString("ISSUE_EXIST_BEFORE_RELEASE"));
                incident.setWorkaroundAvailable(
                  rs.getString("WORKAROUND_AVAILABLE"));
                incident.setContacts(
                  rs.getString("CONTACTS"));
                incident.setIncidentStatusInRemedy(
                  rs.getString("INCIDENT_STATUS_IN_REMEDY"));
                incident.setReleasePriorityInRemedy(
                  rs.getString("RELEASE_PRIORITY_IN_REMEDY"));                
                incident.setTaggedAsReleaseIssue(
                  rs.getString("TAGGED_AS_RELEASE_ISSUE"));
                incident.setTransactionsImpacted(
                        rs.getString("TRANSACTIONS_IMPACTED"));

                incident.setIsSoftwareIssue(
                        rs.getString("IS_SOFTWARE_ISSUE"));

                incident.setImpTransactionType(
                        rs.getString("IMP_TRANSACTION_TYPE"));
                incident.setAmountEnteredByUser(
                        rs.getString("AMOUNT_ENTERED_BY_USER"));

                incident.setCreateUser(rs.getString("CREATE_USER"));
                
                incident.setProject(rs.getString("PROJECT"));
 
                java.sql.Timestamp timestamp = rs.getTimestamp("CREATE_DATE");
                if (timestamp != null) {
                	incident.setCreateDate(new java.util.Date(timestamp.getTime()));
                }
                incident.setUpdateUser(rs.getString("UPDATE_USER"));

                timestamp = rs.getTimestamp("UPDATE_DATE");
                if (timestamp != null) {
                	incident.setUpdateDate(new java.util.Date(timestamp.getTime()));
                }
                
                // get associated transactions
                List<Transaction> transactions = 
                  TransactionDAO.getActiveTrasnactions(conn, 
                    incident.getIncidentNumber());
                
                incident.setTransactions(transactions);
                
                // add up the amounts
                float totalAmount = 0.0f;
                if (transactions != null) {
                    for (Transaction transaction : transactions) {
                	    totalAmount += transaction.getAmount().floatValue();
                    }
                }
                incident.setTotalAmount(new Float(totalAmount));
                           	
            }


		} catch (SQLException e) {
			logger.error("No incident retrieved for CI_INCIDENTS Table", e);
		}

		finally {		
			DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incident;
	}
	
	
	
	/**
	 * insertIncident 
	 * 
	 * Inserts the given incident
	 * 
	 * @param incident
	 * @return
	 */
	public static Incident insertIncident(String user, Incident incident) {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date createDate = null;
		java.util.Date updateDate = null;		

		try {
			conn = DBUtilCaseImpact.getConnection();
			
			conn.setAutoCommit(false);
			
			logger.info("incident processing... " + incident);

			String insertQuery = 
					" INSERT INTO CI_INCIDENTS ( " +
					"     INCIDENT_NUMBER, " +
					"     RELEASE_PRIORITY_BY_USER, " +
					"     ISSUE_EXIST_BEFORE_RELEASE, " + 
					"     WORKAROUND_AVAILABLE, " + 
					"     CONTACTS, " + 
				    "     INCIDENT_STATUS_IN_REMEDY, " + 
					"     RELEASE_PRIORITY_IN_REMEDY, " + 
					"     TAGGED_AS_RELEASE_ISSUE, " +
					"     CREATE_USER, " +
					"     CREATE_DATE, " + 
					"     UPDATE_USER, " +
					"     UPDATE_DATE, " +
					"     TRANSACTIONS_IMPACTED, " +
					"        IS_SOFTWARE_ISSUE," +
					" 		IMP_TRANSACTION_TYPE, " +
					"		AMOUNT_ENTERED_BY_USER, " +
					"     PROJECT"+
					" ) VALUES ( " + 
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     ?, " + 
					"     ?, " + 
					"     ?, " + 
					"     SYSDATE, " +
					"     ?, " + 
					"     SYSDATE, " + 
					"     ?, " + 
					"     ?, " + 
					"     ?, " + 
					"     ?, " + 
					"     ?) ";

			stmt = conn.prepareStatement(insertQuery, new String[] { 
					"CREATE_DATE", "UPDATE_DATE" });

			logger.info("post prepare incident processing... " + incident);
			
			incident.setCreateUser(user);
			incident.setUpdateUser(user);

			stmt.setString(1, incident.getIncidentNumber());
			stmt.setString(2, incident.getReleasePriorityByUser());
			stmt.setString(3, incident.getIssueExistBeforeRelease());
			stmt.setString(4, incident.getWorkaroundAvailable());
			stmt.setString(5, incident.getContacts());
			stmt.setString(6, incident.getIncidentStatusInRemedy());
			stmt.setString(7, incident.getReleasePriorityInRemedy());
			stmt.setString(8, incident.getTaggedAsReleaseIssue());
			stmt.setString(9, incident.getCreateUser());
			stmt.setString(10, incident.getUpdateUser());
			stmt.setString(11, incident.getTransactionsImpacted());
			stmt.setString(12, incident.getIsSoftwareIssue());
			stmt.setString(13, incident.getImpTransactionType());
			stmt.setString(14, incident.getAmountEnteredByUser());
			stmt.setString(15, incident.getProject());

			logger.info("pre update incident processing... " + incident);
			
			if (stmt.executeUpdate() > 0) {

				logger.info("post update incident processing... " + incident);
				
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

				incident.setCreateDate(createDate);
				incident.setUpdateDate(updateDate);
				
			}

			float totalAmount = 0.0f;

			List<Transaction> transactions = incident.getTransactions();
			if (transactions != null && transactions.size() > 0) {
				for (Transaction transaction: transactions) {
					transaction.setCreateUser(user);
					transaction.setUpdateUser(user);
					TransactionDAO.insertTransaction(conn, transaction);
					totalAmount += transaction.getAmount().floatValue();
				}
			}
			
			incident.setTotalAmount(new Float(totalAmount));
			
			conn.commit();			
			
		} catch (SQLException e) {
			
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}

			}
			
			logger.error("No row inserted for CI_INCIDENTS Table", e);
		}

		finally {
			
			if (conn != null) {
				// make sure to reset autocommit
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
			
			DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incident;
	}
	
	/**
	 * Update incident and transactions
	 * 
	 * @param user
	 * @param incident
	 * @return
	 */
	public static Incident updateIncident(String user, Incident incident) {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilCaseImpact.getConnection();
			
			conn.setAutoCommit(false);
			
			java.sql.Timestamp updateTimestamp = 
			  DBUtilCaseImpact.getCurrentTimestamp(conn);
			
			String insertQuery = 
					" UPDATE CI_INCIDENTS " +
					"   SET RELEASE_PRIORITY_BY_USER = ?, " +
					"       ISSUE_EXIST_BEFORE_RELEASE = ?, " + 
					"       WORKAROUND_AVAILABLE = ?, " + 
					"       CONTACTS = ?, " + 
				    "       INCIDENT_STATUS_IN_REMEDY = ?, " + 
					"       RELEASE_PRIORITY_IN_REMEDY = ?, " + 
					"       TAGGED_AS_RELEASE_ISSUE = ?, " +
					"       UPDATE_USER = ?, " +
					"       UPDATE_DATE = ?, " +
					"       TRANSACTIONS_IMPACTED = ?, " +
					"       IS_SOFTWARE_ISSUE = ?, " +
					"       IMP_TRANSACTION_TYPE = ?, " +
					"		AMOUNT_ENTERED_BY_USER = ?, " +
					"       PROJECT = ? "+
					"	WHERE INCIDENT_NUMBER = ? ";

			stmt = conn.prepareStatement(insertQuery);
			
			incident.setUpdateUser(user);

			int i = 1;
			stmt.setString(i++, incident.getReleasePriorityByUser());
			stmt.setString(i++, incident.getIssueExistBeforeRelease());
			stmt.setString(i++, incident.getWorkaroundAvailable());
			stmt.setString(i++, incident.getContacts());
			stmt.setString(i++, incident.getIncidentStatusInRemedy());
			stmt.setString(i++, incident.getReleasePriorityInRemedy());
			stmt.setString(i++, incident.getTaggedAsReleaseIssue());
			stmt.setString(i++, incident.getUpdateUser());
			stmt.setTimestamp(i++, updateTimestamp);
			stmt.setString(i++, incident.getTransactionsImpacted());
			stmt.setString(i++, incident.getIsSoftwareIssue());
			stmt.setString(i++, incident.getImpTransactionType());
			stmt.setString(i++, incident.getAmountEnteredByUser());
			stmt.setString(i++, incident.getProject());

			stmt.setString(i++, incident.getIncidentNumber());
						
			if (stmt.executeUpdate() > 0) {
				incident.setUpdateDate(updateTimestamp);
			}

			float totalAmount = 0.0f;

			List<Transaction> transactions = incident.getTransactions();
			if (transactions != null && transactions.size() > 0) {
				
				// soft delete all transactions for this incident
				TransactionDAO.deleteTransactionByIncidentNumber(conn, user, 
				  incident.getIncidentNumber());
				
				
				for (Transaction transaction: transactions) {
					
					transaction.setUpdateUser(user);
					
					// check to see if this exists in the database already
					Transaction dbtransaction = 
					  TransactionDAO.transactionExistsIgnoreActiveFlag(conn, 
				        transaction.getIncidentNumber(), 
				        transaction.getTransactionNumber());
					
					// if exists update and set active flag to Y
					if (dbtransaction != null) {
						TransactionDAO.updateTransaction(conn, transaction);
						
					// if doesn't exist then insert and set active flag to Y	
					} else {
						transaction.setCreateUser(user);
						TransactionDAO.insertTransaction(conn, transaction);		
					}
					
					totalAmount += transaction.getAmount().floatValue();
					
				}
				
			}
			
			incident.setTotalAmount(new Float(totalAmount));
			
			conn.commit();			
			
		} catch (SQLException e) {
			
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}

			}
			
			logger.error("No row updated for CI_INCIDENTS Table", e);
		}

		finally {
			
			if (conn != null) {
				// make sure to reset autocommit
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
			
			DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incident;
	}
	
}
