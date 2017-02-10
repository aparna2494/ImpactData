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
import org.json.simple.parser.ParseException;

import com.cisco.bccsp.db.util.DBUtilApoyo;
import com.cisco.bccsp.db.util.DBUtilRemedyCases;
import com.cisco.bccsp.model.User;

public class UserDAO {

	private static Logger logger = Logger
			.getLogger(UserDAO.class.getName());

	public static User getUserbyUserID(String userID) {

		User user = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilApoyo.getConnection();

			String query = " SELECT " 
						 + " AU.USER_ID, " 
						 + " AU.FULL_NAME, "
						 + " AU.ACTIVE, "
						 + " AU.CREATE_DATE, " 
						 + " AU.CREATE_USER, "
						 + " AU.UPDATE_DATE, " 
						 + " AU.UPDATE_USER, "
						 + " AU.LAST_LOGIN, " 
						 + " AU.ACCESS_LVL_ID " 
						 + " FROM APOYO_USERS AU "
						 + " WHERE AU.USER_ID = ? AND "
						 + " AU.ACTIVE = 'Y' ";

			stmt = conn.prepareStatement(query);

			stmt.setString(1, userID);

			rs = stmt.executeQuery();

			// get results
			if (rs.next()) {

				user = new User();

				
				user.setUserId(rs.getString("USER_ID"));
				user.setFullName(rs.getString("FULL_NAME"));
				user.setActiveFlag(rs.getString("ACTIVE"));
				java.sql.Timestamp timestamp = rs.getTimestamp("CREATE_DATE");
				if (timestamp != null) {
					user.setCreateDate(new java.util.Date(timestamp
							.getTime()));
				}
				user.setCreateUser(rs.getString("CREATE_USER"));
				
				java.sql.Timestamp utimestamp = rs.getTimestamp("UPDATE_DATE");
				if (utimestamp != null) {
					user.setUpdateDate(new java.util.Date(utimestamp
							.getTime()));
				}
				user.setUpdateUser(rs.getString("UPDATE_USER"));
				
				java.sql.Timestamp logintimestamp = rs
						.getTimestamp("LAST_LOGIN");
				if (logintimestamp != null) {
					user.setLastLogin(new java.util.Date(logintimestamp
							.getTime()));
				}
				user.setAccessLvlId(rs.getLong("ACCESS_LVL_ID"));
				
			}

		} catch (SQLException e) {
			logger.error("Not able to query Resource", e);
		} finally {
			DBUtilApoyo.releaseConnectionResources(conn, rs, stmt);
		}

		return user;
	}
	
	public static List<String> getUserIdsByGroup(String userID) {

		List<String> userIds = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilApoyo.getConnection();

			String query = " SELECT " 
						 + " AU.USER_ID "
					     + " FROM APOYO_GROUP_USERS AU "
					     + " WHERE AU.GROUP_ID IN "
						 + " (SELECT AU.GROUP_ID FROM APOYO_GROUP_USERS AU WHERE AU.USER_ID = ? )";

			stmt = conn.prepareStatement(query);

			stmt.setString(1, userID);

			rs = stmt.executeQuery();

			// get results
			while(rs.next()) {
				userIds.add(rs.getString("USER_ID"));
			}

		} catch (SQLException e) {
			logger.error("Not able to query Resource", e);
		} finally {
			DBUtilApoyo.releaseConnectionResources(conn, rs, stmt);
		}

		return userIds;
	}

	@SuppressWarnings("unchecked")
	public static String getAssignedGroupsByUser(String userID) {

		JSONArray arr = new JSONArray();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilApoyo.getConnection();

			String query = "SELECT AG.ITSM_QUEUE_ID, AG.SRD_TITLE FROM APOYO_USER_ITSM US INNER JOIN APOYO_ASSIGNED_GROUPS AG ON US.ITSM_QUEUE_ID = AG.ITSM_QUEUE_ID WHERE US.USER_ID = ? ";
			
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userID);
			
			logger.info(query);
			
            rs = stmt.executeQuery();

            while (rs.next()) {
            	
            	JSONObject obj = new JSONObject();

            	obj.put("id", rs.getInt("ITSM_QUEUE_ID"));
                obj.put("assignedGroup", rs.getString("SRD_TITLE"));
                arr.add(obj);          	
            }

		} catch (SQLException e) {
			logger.error("Error while fetching the assigned group from the database", e);
		}

		finally {		
			DBUtilRemedyCases.releaseConnectionResources(conn, rs, stmt);
		}
		if(!arr.isEmpty())
			return arr.toJSONString();
		else
			return "No Assigned Groups associated with your username";
	}
	
	@SuppressWarnings("unchecked")
	public static String getAssignedGroupMapping(String id) {

		JSONObject obj = new JSONObject();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilApoyo.getConnection();

			String query = "SELECT * FROM APOYO_ASSIGNED_GROUPS WHERE ITSM_QUEUE_ID = ? ";
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(id));
			
			logger.info(query);
			
            rs = stmt.executeQuery();

            if (rs.next()) {

            	obj.put("id", rs.getInt("ITSM_QUEUE_ID"));
                obj.put("assignedGroup", rs.getString("ASSIGNED_GROUP"));
                obj.put("prodCatTier1", rs.getString("PRODUCT_CAT_TIER_1"));
                obj.put("prodCatTier2", rs.getString("PRODUCT_CAT_TIER_2"));
                obj.put("prodCatTier3", rs.getString("PRODUCT_CAT_TIER_3"));
                obj.put("operationalCatTier1", rs.getString("OPERATIONAL_CAT_TIER_1"));
                if(rs.getString("OPERATIONAL_CAT_TIER_2") != null)
                	obj.put("operationalCatTier2", rs.getString("OPERATIONAL_CAT_TIER_2"));
                else
                	obj.put("operationalCatTier2", "");
                if(rs.getString("OPERATIONAL_CAT_TIER_3") != null)
                	obj.put("operationalCatTier3", rs.getString("OPERATIONAL_CAT_TIER_3"));
                else
                	obj.put("operationalCatTier3", "");
                obj.put("productName", rs.getString("PRODUCT_NAME"));        	
            }

		} catch (SQLException e) {
			logger.error("Error while fetching the assigned group mapping from the database", e);
		}

		finally {		
			DBUtilRemedyCases.releaseConnectionResources(conn, rs, stmt);
		}
		if(!obj.isEmpty())
			return obj.toJSONString();
		else
			return "No Assigned Group mapping found in database";
	}
	
	public static String addFeedback(String userId, String json) throws ParseException {

		Connection conn = null;
		PreparedStatement stmt = null;
		String response = null;
		JSONObject obj = new JSONObject();

		try {
			
			JSONObject req = (JSONObject) new JSONParser().parse(json);
			String feedbackType = req.get("feedbackType").toString().toUpperCase();
			String feedbackText = req.get("feedbackText").toString();
			
			conn = DBUtilApoyo.getConnection();

			String insertQuery = "INSERT INTO APOYO_FEEDBACK(FEEDBACK_TYPE, FEEDBACK_TEXT, SUBMITTED_BY, SUBMITTED_DATE) VALUES "
					           + "(?, ?, ?, SYSDATE) ";

			stmt = conn.prepareStatement(insertQuery);

			stmt.setString(1, feedbackType);
			stmt.setString(2, feedbackText);
			stmt.setString(3, userId);

			int count = stmt.executeUpdate();

			if (count > 0) {
				logger.info("added feedback");
				response = "Added feedback successfully";
			}
			else
				response = "Feedback was not added";
			obj.put("Message", response);

		} catch (SQLException e) {
			logger.error("Error while adding feedback to database", e);
		} finally {
			DBUtilApoyo.releaseConnectionResources(conn, null, stmt);
		}
		return obj.toJSONString();

	}
	
	public static boolean isValidResources(Connection conn, List<String> resources) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean validResources = false;
		int howMany = 0;
			
		try {
			
			String query = " SELECT COUNT(*) HOW_MANY "
						 + " FROM TS_RESOURCES T " 
						 + " WHERE T.RESOURCE_ID IN (  ";

			boolean first = true;
			for (String resourceId : resources) {
				if (first) {
					query += "?";
					first = false;
				} else {
					query += ",?";
				}
			}

			query += ")";

			stmt = conn.prepareStatement(query);

			int i = 0;
			for (String resourceId : resources) {
				int rid = Integer.parseInt(resourceId);
				stmt.setInt((++i), rid);
			}

			rs = stmt.executeQuery();

			if (rs.next()) {
				howMany = rs.getInt("HOW_MANY");
			}

			if (howMany == resources.size()) {
				validResources = true;
			}

		} catch (SQLException e) {
			logger.error("Not able to query valid resources", e);
		} finally {
			DBUtilApoyo.releaseConnectionResources(null, rs, stmt);
		}

		return validResources;

	}

	public static void registerLogin(String userId) {

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DBUtilApoyo.getConnection();

			String updateQuery = "UPDATE APOYO_USERS AU "
								+ "SET AU.LAST_LOGIN = SYSDATE "
								+ "WHERE AU.USER_ID = ? ";

			stmt = conn.prepareStatement(updateQuery);

			stmt.setString(1, userId);

			int count = stmt.executeUpdate();

			if (count > 0) {
				logger.info("updated last login");
			}

		} catch (SQLException e) {
			logger.error("No login time updated", e);
		} finally {
			DBUtilApoyo.releaseConnectionResources(conn, null, stmt);
		}

	}


	

}
