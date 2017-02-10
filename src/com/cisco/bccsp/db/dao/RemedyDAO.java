package com.cisco.bccsp.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cisco.bccsp.db.dao.UserDAO;
import com.cisco.bccsp.db.util.DBUtilRemedyCases;
import com.cisco.bccsp.model.RemedyIncident;
import com.cisco.bccsp.model.IncidentDetails;

public class RemedyDAO {

	private static Logger logger = Logger.getLogger(RemedyDAO.class.getName());
	/**
	 * getIncident
	 * 
	 * Get a single incident
	 * 
	 * @param incidentNumber
	 * @return
	 */
	public static RemedyIncident getIncident(String incidentNumber) {

		RemedyIncident incident = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtilRemedyCases.getConnection();

			String query =
			  " SELECT IM.INCIDENT_NUMBER, IM.ERMO_REL_IND, " +
		  	  "        ST.CHARACTER_FIELD_VALUE STATUS " +
			  " FROM AR7DW.DW_D_STATUS ST, " +
			  "      AR7DW.INCIDENT_MAIN IM " +
		      " WHERE IM.INCIDENT_NUMBER = ? " +
			  "   AND ST.DW_STATUS_ID = IM.STATUS_DW_ID " +
			  "   AND UPPER(ST.CHARACTER_FIELD_VALUE) not in " +
			  "         ('CLOSED', 'RESOLVED', 'CANCELLED','REJECTED') "; 
			
			stmt = conn.prepareStatement(query);

			stmt.setString(1, incidentNumber);
			
            rs = stmt.executeQuery();

            if (rs.next()) {
            	
                incident = new RemedyIncident();

                incident.setIncidentNumber(
                  rs.getString("INCIDENT_NUMBER"));
                incident.setErmoRelId(
                  rs.getString("ERMO_REL_IND"));
                incident.setStatus(
                  rs.getString("STATUS"));
                
                
                logger.info("get remedy incident");
                
            }


		} catch (SQLException e) {
			logger.error("No incident retrieved for remedy INCIDENT_MAIN Table", e);
		}

		finally {		
			DBUtilRemedyCases.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incident;
	}
	
	
	
	public static Response getStatus(String incidentNumber) {

		RemedyIncident incident = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		 Status status=Status.BAD_REQUEST; 
		try {
			conn = DBUtilRemedyCases.getConnection();
			
			String query ="select count(incident_number) as count from arsystem.v_hpd_help_desk where incident_number = ?";
			 
			
			stmt = conn.prepareStatement(query);
			
			stmt.setString(1, incidentNumber);
			
            rs = stmt.executeQuery();
           
            if (rs.next()) {
            	
            	if(rs.getInt("count")!=0)
            	{
            		
                status=Status.ACCEPTED;
            	}
                
            }


		} catch (SQLException e) {
			logger.error("No incident retrieved for remedy INCIDENT_MAIN Table", e);
		}

		finally {		
			DBUtilRemedyCases.releaseConnectionResources(conn, rs, stmt);
		}
		
		return Response.status(status).type(MediaType.APPLICATION_JSON).build();
	}
	
	
	public static IncidentDetails getIncidentDetails(String incidentNumber) {

		RemedyIncident incident = null;
		IncidentDetails incidentdetail = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		 boolean status=false;
		try {
			conn = DBUtilRemedyCases.getConnection();
			
			String query ="select incident_number inc_num,assignee_login_id,a.reported_date,a.last_resolved_date,a.customer_login_id,a.direct_contact_country,a.first_name,a.last_name ,a.description,job_title,a.detailed_decription,SUPPORT_GROUP_NAME,a.escalated_,c.status,c.id,a.last_resolved_date-a.reported_date,a.total_transfers,a.assignee, " +
					" DECODE (a.status, 1, 'Assigned', "+
                      " 2, 'In Progress', "+
                       " 3, 'Pending', "+
                       " 4, 'Resolved',"+
                       " 5,'Closed',"+
                       " 6,'Cancelled')  status "+
            " from  ARSYSTEM.V_HPD_HELP_DESK a, ARSYSTEM.V_CTM_SUPPORT_GROUP b,ARSYSTEM.V_CET_ESCALATION c where 1=1"+  

            " and a.ASSIGNED_GROUP_ID  = b.SUPPORT_GROUP_ID"+
            " AND  ( a.INCIDENT_NUMBER=c.REMEDY_CASE_NUM(+))"+
            " and a.INCIDENT_NUMBER=?"+ 
            " and SUPPORT_GROUP_NAME  in ( 'GSE-Q2O-CSCC-2TNG', 'GSE-Q2O-CSCC-ITServiceDesk', 'GSE-Q2O-CSCC-NEXTGENSERVICECONFIG', 'GSE-Q2O-CSCC-PRICINGANDDISCOUNTING', 'GSE-Q2O-CSCC-QO', 'GSE-Q2O-CSCC-VALIDATIONENGINE','GSE-Q2O-CCW-BNP','GSE-Q2O-CCW-PRICING','GSE-Q2O-CCW-QUOTETOORDER','GSE-Q2O-CCW-QUOTING','GSE-Q2O-CCW-QUOTING-GDR','GSE-Q2O-CCW-RETURNS','GSE-Q2O-DM-CHKPT','GSE-Q2O-DM-CTMP','GSE-Q2O-DM-DMR','GSE-Q2O-DM-MDM','GSE-Q2O-IFS-CAPTUREORDER','GSE-Q2O-PC-CIMS','ITDS-Q2O-PRICING-CP','GSE-Q2O-PRICING-DC-GDR','GSE-Q2O-PRICING-EDMS','GSE-Q2O-PRICING-PROMOTIONS','Q2O-CCW-QUOTING-RELEASE','Q2O-PRICING-RELEASE' )"; 
			
			stmt = conn.prepareStatement(query);
			
			stmt.setString(1, incidentNumber);
			
            rs = stmt.executeQuery();
           
            if (rs.next()) {
            	
            	
            		Long long_obj=Long.parseLong(rs.getString("reported_date"))  ;
            		long epoche=(long)System.currentTimeMillis()/1000;
        			
  				  long_obj=(epoche-long_obj)/86400;
  				  
  				  incidentdetail = new IncidentDetails();
  				  incidentdetail.setAge(long_obj.toString());
  				  incidentdetail.setCet_value(rs.getString("escalated_"));
  				  incidentdetail.setDescription(rs.getString("description"));
  				  incidentdetail.setIncidentNotes(rs.getString("detailed_decription"));
  				  
  				  incidentdetail.setIncidentNumber(rs.getString("inc_num"));
  				  
  	           
            	
                
            }


		} catch (SQLException e) {
			logger.error("No incident retrieved for remedy INCIDENT_MAIN Table", e);
		}

		finally {		
			DBUtilRemedyCases.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incidentdetail;
	}
public static String createIncident(String userID, String json) {
		
		try {
			logger.info("beginning");
			JSONObject req = (JSONObject) new JSONParser().parse(json);
			JSONObject returnObj = new JSONObject();
			String summary = "Case created from SEC Support Portal-testing";
			if(req.get("summary") != null)
				summary = req.get("summary").toString();
			String notes = req.get("description").toString();
			String id = req.get("assignedGroupId").toString();
			String manufacturer = "Cisco IT";
			logger.info("before mapping");
			String assignedGroupMapping = UserDAO.getAssignedGroupMapping(id);
			JSONObject obj = (JSONObject) new JSONParser().parse(assignedGroupMapping);
			logger.info("after mapping");
			returnObj.put("Summary", summary);
			returnObj.put("Notes", notes);
			returnObj.put("Source_Case_System", "BCC Support Portal testing");
			returnObj.put("Manufacturer", obj.get("manufacturer").toString());
			returnObj.put("Login_ID", userID);
			returnObj.put("Product_Categorization_Tier_1", obj.get("prodCat1").toString());
			returnObj.put("Product_Categorization_Tier_2", obj.get("prodCat2").toString());
			returnObj.put("Product_Categorization_Tier_3", obj.get("prodCat3").toString());
			returnObj.put("Product_Name", obj.get("productName").toString());
			logger.info("before oper");
			returnObj.put("Operational_Categorization_Tier_1", obj.get("operCat1").toString());
			returnObj.put("Operational_Categorization_Tier_2", obj.get("operCat2").toString());
			returnObj.put("Operational_Categorization_Tier_3", obj.get("operCat3").toString());
			logger.info(returnObj.toJSONString());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic SVRTVF9SRU1TRy5nZW46Q2lzY28kMTIz");
			headers.add("Content-Type", "application/json");
			HttpEntity<String> request = new HttpEntity<String>(returnObj.toJSONString(), headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(
		        "https://wsgx.cisco.com/remedy/service/arservice/remedyintegration/create/incident", HttpMethod.POST, request, String.class);
			String result = response.getBody();
			logger.info(result);
			
			return result;
			
		}
		catch (Exception e) {
			JSONObject returnObj = new JSONObject();
			returnObj.put("response", e.getStackTrace());
			return returnObj.toString();
		}
	}
	
}
