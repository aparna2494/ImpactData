package com.cisco.bccsp.action.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.dao.CG1OrdersDAO;
import com.cisco.bccsp.db.dao.IncidentDAO;
import com.cisco.bccsp.email.EMailUtil;
import com.cisco.bccsp.error.ServiceError;
import com.cisco.bccsp.model.Incident;
import com.cisco.bccsp.model.Transaction;

public class IncidentActionBO {

	private static Logger logger = Logger.getLogger(IncidentActionBO.class
			.getName());
	
	public IncidentActionBO() {
	}

	/**
	 * Check for existence in the database already
	 * 
	 * @param incidentNumber
	 * @return
	 */
	public boolean incidentExists(String incidentNumber) {

		Incident incident = null;

		incident = IncidentDAO.getIncident(incidentNumber);

		return (incident != null) ? true : false;

	}

	/**
	 * Loop through the transactions to ensure they match the incident
	 * 
	 * @param incidentNumber
	 * @param transactions
	 * @return
	 */
	private boolean transactionsIncMatch(String incidentNumber,
			List<Transaction> transactions) {

		boolean match = true;

		if (transactions != null) {
			for (Transaction transaction : transactions) {
				if (!incidentNumber.equals(transaction.getIncidentNumber())) {
					match = false;
					break;
				}
			}
		}

		return match;

	}
	
	
	
	/**
	 * Query the list of orders to make sure they exist in the DB
	 * 
	 * @param transactions
	 * @return
	 */
	private List<Transaction> checkForMissingOrders(
	  List<Transaction> transactions) {
		
		List<Transaction> missingOrders = null;
		
		if (transactions != null) {
			for (Transaction transaction : transactions) {
				Transaction order = CG1OrdersDAO.getOrder(transaction);
				if (order != null) {
					transaction.setAmount(order.getAmount());
					transaction.setShipSet(order.getShipSet());
					transaction.setCustomerId(order.getCustomerId());
					transaction.setCustomerName(order.getCustomerName());
					transaction.setTransactionStatus(
					  order.getTransactionStatus());
				} else {
					if (missingOrders == null) {
						missingOrders = new ArrayList<Transaction>();
					}
					missingOrders.add(transaction);
				}
			}
		}
		
		return missingOrders;
		
	}
	
	
	
	/**
	 * Check the transaction type to see which of the databases
	 * to connect to and do the check
	 * 
	 * @param transactionType
	 * @param transactions
	 * @return
	 */
	private List<Transaction> checkForMissingTransactions(
	  String transactionType, List<Transaction> transactions) {
		
		List<Transaction> missingTransactions = null;
		
		if  ("DEAL".equals(transactionType)) {
			// implement DEAL check
		} else if ("QUOTE".equals(transactionType)) {
			// implement QUOTE check
		} else if ("ORDER".equals(transactionType)) {
			missingTransactions = checkForMissingOrders(transactions);
		} else if ("EXPEDITE".equals(transactionType)) {
			// implement EXPEDITE check
		} else if ("INVOICE".equals(transactionType)) {
			// implement INVOICE check
		} else if ("RETURN".equals(transactionType)) {
			// implement RETURN check
		} else {
			// wait we don't know what your asking for, they are all bad!
			missingTransactions = transactions;
		}
				
		return missingTransactions;
		
	}
	
	
	
	/**
	 * Send an email about the incident
	 * 
	 * @param user
	 * @param incident
	 */
	private void sendEmail(String user, Incident incident) {
		
		int transactionsCount = 0;
		
		if (incident.getTransactions() != null) {
			transactionsCount = incident.getTransactions().size();					
		}
		
		String IssueExistBeforeRelease = 
		  incident.getIssueExistBeforeRelease().equals("Y") ? "Yes" : "No" ;
		String WorkaroundAvailable = 
		  incident.getWorkaroundAvailable().equals("Y") ? "Yes" : "No";
		String TransactionsImpacted = 
		  incident.getTransactionsImpacted().equals("Y") ? "Yes" : "No";
		String SoftwareIssue = 
		  incident.getIsSoftwareIssue().equals("Y") ? "Yes" : "No";
		
		String totalAmount = "";
		if(incident.getImpTransactionType().equalsIgnoreCase("order")) {
			totalAmount = incident.getTotalAmount().toString();	
		} else {
			totalAmount = incident.getAmountEnteredByUser();
		}
		
		String emailTemplate = 
				  "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
				+ " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Case Impact Email</title>"
				+ "<meta name=\"viewport\" content=\"width=device-width\">"
				+ "<style type=\"text/css\">#outlook a{padding:0}body{width:100%!important;min-width:100%;"
				+ "-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;margin:0;padding:0}"
				+ "img{outline:0;text-decoration:none;-ms-interpolation-mode:bicubic;width:auto;height:auto;max-width:100%;float:left;clear:both;display:block}"
				+ "@media screen and (min-width:0\\0){img.ie10-responsive{width:100%!important}}center{width:100%;min-width:580px}"
				+ "a img{border:0}p{margin:0 0 0 10px}table{border-spacing:0;border-collapse:collapse}"
				+ "td{word-break:break-word;-webkit-hyphens:auto;-moz-hyphens:auto;hyphens:auto;border-collapse:collapse!important}"
				+ "table,tr,td{padding:0;vertical-align:top;text-align:left}hr{color:#d9d9d9;background-color:#d9d9d9;height:1px;border:0}"
				+ "table.body{height:100%;width:100%}table.container{width:580px;margin:0 auto;text-align:inherit}"
				+ "table.row{padding:0;width:100%;position:relative}table.container table.row{display:block}"
				+ "td.wrapper{padding:10px 20px 0 0;position:relative}table.columns,table.column{margin:0 auto}"
				+ "table.columns td,table.column td{padding:0 0 10px}table.columns td.sub-columns,table.column td.sub-columns,"
				+ "table.columns td.sub-column,table.column td.sub-column{padding-right:10px}"
				+ "td.sub-column,td.sub-columns{min-width:0}table.row td.last,table.container td.last{padding-right:0}"
				+ "table.four{width:180px}table.five{width:230px}table.six{width:280px}table.twelve{width:580px}"
				+ "table.center,td.center{text-align:center}body,table.body,h1,h2,h3,h4,h5,h6,p,"
				+ "td{color:#333;font-family:\"Helvetica\",\"Arial\",sans-serif;font-weight:normal;padding:0;margin:0;text-align:left;line-height:1.3}"
				+ "h1,h2,h3,h4,h5,h6{word-break:normal}h1{font-size:40px}h2{font-size:36px}h3{font-size:32px}h4{font-size:28px}h5{font-size:24px}"
				+ "h6{font-size:20px}body,table.body,p,td{font-size:14px;line-height:19px}p{margin-bottom:10px}"
				+ "small{font-size:10px}a{color:#2ba6cb;text-decoration:none}a:hover{color:#2795b6!important}"
				+ "a:active{color:#2795b6!important}a:visited{color:#2ba6cb!important}"
				+ ".panel{background:#f2f2f2;border:1px solid #d9d9d9;padding:10px!important}"
				+ "table.radius td{-webkit-border-radius:3px;-moz-border-radius:3px;border-radius:3px}"
				+ "table.round td{-webkit-border-radius:500px;-moz-border-radius:500px;border-radius:500px}"
				+ "body.outlook p{display:inline!important}"
				+ "@media only screen and (max-width:600px){table[class=\"body\"] "
				+ "img{width:auto!important;height:auto!important}table[class=\"body\"] "
				+ ".container{width:95%!important}table[class=\"body\"] .row{width:100%!important;display:block!important}"
				+ "table[class=\"body\"] .wrapper{display:block!important;padding-right:0!important}}</style><style>"
				+ "body{direction:ltr;background:#f6f8f1}.highlight-total{font-size:20px}.highlight-priority{font-size:20px}"
				+ "a:hover{text-decoration:underline}h1{font-size:34px}h2{font-size:30px}h3{font-size:26px}h4{font-size:22px}"
				+ "h5{font-size:18px}h6{font-size:16px}h4,h3,h2,h1{display:block;margin:5px 0 15px 0}h7,h6,"
				+ "h5{display:block;margin:5px 0 5px 0!important}.graybg{background:#f6f6f6!important}"
				+ ".note .panel{padding:10px!important;background:#ecf8ff;border:0}.page-header{width:100%;background:#3b3f51}"
				+ "table.container.content>tbody>tr>td{background:#fff;padding:15px!important}.page-footer{width:100%;background:#3b3f51}"
				+ ".page-footer td{vertical-align:middle;color:#fff}table.email-cont>tr>td{padding:5px}"
				+ "@media only screen and (max-width:600px){body{background:#fff}h1{font-size:30px}"
				+ "h2{font-size:26px}h3{font-size:22px}h4{font-size:20px}h5{font-size:16px}"
				+ "h6{font-size:14px}table.container.content>tbody>tr>td{padding:0!important}table[class=\"body\"] "
				+ "table.columns .social-icons td{width:auto!important}.header{padding:10px!important}}</style></head>"
				+ "<body style=\"min-width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;margin:0;padding:0;"
				+ "direction:ltr;background:#f6f8f1;width:100%!important\"><table class=\"body\">"
				+ "<tr><td class=\"center\" align=\"center\" valign=\"top\"><table class=\"page-header\" align=\"center\" style=\"width:100%;background:#3b3f51\">"
				+ "<tr><td class=\"center\" align=\"center\"><table class=\"container\" align=\"center\">"
				+ "<tr><td><table class=\"row\"><tr><td class=\"wrapper vertical-middle\"><table class=\"six columns\">"
				+ "<tr><td class=\"vertical-middle\"><a href=\"http://caseimpact.cloudapps.cisco.com\" class=\"logo-link\""
				+ "style=\"font-size: 16px;text-transform: uppercase;color: #d14836;text-decoration: none;\">"
				+ "CASE IMPACT"
				+ "</a></td></tr></table></td><td class=\"wrapper vertical-middle last\"></td></tr></table></td></tr></table>"
				+ "</td></tr></table><table class=\"container content\" align=\"center\"><tr><td><table class=\"row note\">"
				+ "<tr><td class=\"wrapper last\"><h5 style=\"font-size:18px;display:block;margin:5px 0 15px 0\">"
				+ "Incident #<a href=\"http://caseimpact.cloudapps.cisco.com/#/viewcaseimpact.html/"
				+ incident.getIncidentNumber() +"\">"
				+ incident.getIncidentNumber()
				+ " </a>has been escalated by "
				+ "<a href=\"http://wwwin-tools.cisco.com/dir/reports/"+user+"\">"+user+"</a>"
				+ "</h5><p><table class=\"twelve columns email-cont\"><tr><td style=\"padding:5px\">"
				+ "<span>Issue release priority</span></td><td style=\"padding:5px\">"
				+ "<span class=\"highlight-priority\" style=\"font-size:20px\">"
				+ incident.getReleasePriorityByUser()
				+ "</span></td></tr>"
				+ "<tr class=\"graybg vertical-middle\" style=\"background:#f6f6f6!important\">"
				+ "<td class=\"vertical-middle\" style=\"padding:5px\">Issue exist before release?</td>"
				+ "<td style=\"padding:5px\">"
				+ IssueExistBeforeRelease 
				+ "</td></tr><tr><td style=\"padding:5px\">Workaround available?</td>"
				+ "<td style=\"padding:5px\">"
				+  WorkaroundAvailable
				+ "</td></tr>"
				+ "<tr class=\"graybg\" style=\"background:#f6f6f6!important\">"
				+ "<td style=\"padding:5px\">Is this Software issue?</td><td style=\"padding:5px\">"
				+ SoftwareIssue 
				+ "</td></tr>"
				+ "<tr>"
				+ "<td style=\"padding:5px\">Is this impacting transactions?</td><td style=\"padding:5px\">"
				+ TransactionsImpacted 
				+ "</td></tr>"
				+ "<tr class=\"graybg\" style=\"background:#f6f6f6!important\"><td style=\"padding:5px\">Total impacted transactions</td><td style=\"padding:5px\">"
				+ "<a href=\"http://caseimpact.cloudapps.cisco.com/#/viewcaseimpact.html/"+incident.getIncidentNumber()+"\">"+transactionsCount + " "+ incident.getImpTransactionType()+"(s)" +"</a>" 
				+ "</td>"
				+ "</tr><tr><td style=\"padding:5px\">Total $impact</td>"
				+ "<td style=\"padding:5px\"> <span class=\"highlight-total\" style=\"font-size:20px\">"
				+ "$"+totalAmount 
				+ "</span></td></tr>"
				+ "<tr class=\"graybg\" style=\"background:#f6f6f6!important\"><td style=\"padding:5px\">Contacts for further details</td><td style=\"padding:5px\">"
				+ incident.getContacts()
				+ "</td></tr>"
				+ "</table></p></td></tr></table></td></tr></table><table class=\"page-footer\" "
				+ "align=\"center\" style=\"width:100%;background:#3b3f51\"><tr><td class=\"center\" "
				+ "align=\"center\" style=\"vertical-align:middle;color:#fff\"><table class=\"container\" align=\"center\">"
				+ "<tr><td style=\"vertical-align:middle;color:#fff\"><table class=\"row\"><tr><td class=\"wrapper\" "
				+ "style=\"vertical-align:middle;color:#fff\"><table class=\"four columns\"><tr><td class=\"vertical-middle\" "
				+ "style=\"vertical-align:middle;color:#fff\">&copy; 2015 Cisco Systems Inc.</td></tr></table></td></tr></table>"
				+ "</td></tr></table></td></tr></table></td></tr></table></body></html>";

		EMailUtil.send(user + "@cisco.com","CE "+incident.getProject()+" - "+
		  incident.getReleasePriorityByUser() + " Incident# " + 
		  incident.getIncidentNumber() + " has been escalated",
		  emailTemplate);		
		
	}

	
	
	/**
	 * Insert the incident and transactions, check integrity to ensure things
	 * match up.
	 * 
	 * @param incident
	 * @return
	 */
	public Object insertIncident(String user, Incident incident) {

		Object entity = incident;

		boolean incidentCreated = false;

		// does incident already exist in the database?
		boolean exists = incidentExists(incident.getIncidentNumber());

		// do all the transactions have the same incident number?
		boolean transactionsIncMatch = transactionsIncMatch(
				incident.getIncidentNumber(), incident.getTransactions());

		// TODO
		// Check Remedy to ensure it is a valid INC number
		//

		
		List<Transaction> missingTransactions = 
		  checkForMissingTransactions(
	        incident.getImpTransactionType().toUpperCase(),
	        incident.getTransactions());

		boolean validTransactions = (missingTransactions == null);
		
		// if all is perfect then insert the incident and transactions
		if (!exists && transactionsIncMatch && validTransactions) {

			// clear this out because a new one will be generated
			incident.setCreateDate(null);

			incident = IncidentDAO.insertIncident(user, incident);

			if (incident.getCreateDate() != null) {

				incidentCreated = true;
				
				sendEmail(user, incident);

			}
		}

		if (!incidentCreated) {
			
			Object sourceObject = incident;

			String serviceCode = ServiceError.UNKNOWN;
			String friendlyMessage = "A technical error occurred while adding the incident: "
					+ incident.getIncidentNumber();
			String technicalMessage = "Please check for database errors: "
					+ incident.getIncidentNumber();

			if (exists) {
				serviceCode = ServiceError.DUP;
				friendlyMessage = "This incident already exists: "
						+ incident.getIncidentNumber();
				technicalMessage = "duplicate key in the database: "
						+ incident.getIncidentNumber();
			} else if (!transactionsIncMatch) {
				serviceCode = ServiceError.SEVERE;
				friendlyMessage = "The incident "
						+ incident.getIncidentNumber()
						+ " entered does not match the transaction incident number";
				technicalMessage = "The incident "
						+ incident.getIncidentNumber()
						+ " entered does not match the transaction incident number";
			} else if (!validTransactions) {
				serviceCode = ServiceError.BADTRX;
				friendlyMessage = "You have invalid transactions";
				technicalMessage = 
				  "There are transactions which do not exist in the database";
				sourceObject = missingTransactions;
			}

			entity = new ServiceError(serviceCode, friendlyMessage,
					technicalMessage, sourceObject);
		}

		return entity;

	}
	
	
	/**
	 * Insert the incident and transactions, check integrity to ensure things
	 * match up.
	 * 
	 * @param incident
	 * @return
	 */
	public Object updateIncident(String user, Incident incident) {

		Object entity = incident;

		boolean incidentUpdated = false;

		// does incident already exist in the database, 
		// it should we are updating
		boolean exists = incidentExists(incident.getIncidentNumber());

		// do all the transactions have the same incident number?
		boolean transactionsIncMatch = transactionsIncMatch(
				incident.getIncidentNumber(), incident.getTransactions());

		// TODO
		// Check Remedy to ensure it is a valid INC number
		//

		List<Transaction> missingTransactions = 
				  checkForMissingTransactions(
			        incident.getImpTransactionType().toUpperCase(),
			        incident.getTransactions());

		boolean validTransactions = (missingTransactions == null);
		
		// if all is perfect then update the incident and transactions
		if (exists && transactionsIncMatch && validTransactions) {

			// clear this out because a new one will be generated
			incident.setUpdateDate(null);
			
			incident = IncidentDAO.updateIncident(user, incident);

			if (incident.getUpdateDate() != null) {

				incidentUpdated = true;
				
				// sendEmail(user, incident);
				
			}
		}

		if (!incidentUpdated) {
			
			Object sourceObject = incident;

			String serviceCode = ServiceError.UNKNOWN;
			String friendlyMessage = "A technical error occurred while adding the incident: "
					+ incident.getIncidentNumber();
			String technicalMessage = "Please check for database errors: "
					+ incident.getIncidentNumber();

			if (!exists) {
				serviceCode = ServiceError.WARN;
				friendlyMessage = "This incident is not currently being tracked: "
						+ incident.getIncidentNumber();
				technicalMessage = "incident cannot be found in the database: "
						+ incident.getIncidentNumber();
			} else if (!transactionsIncMatch) {
				serviceCode = ServiceError.SEVERE;
				friendlyMessage = "The incident "
						+ incident.getIncidentNumber()
						+ " entered does not match the transaction incident number";
				technicalMessage = "The incident "
						+ incident.getIncidentNumber()
						+ " entered does not match the transaction incident number";
			} else if (!validTransactions) {
				serviceCode = ServiceError.BADTRX;
				friendlyMessage = "You have invalid transactions";
				technicalMessage = 
				  "There are transactions which do not exist in the database";
				sourceObject = missingTransactions;
			}

			entity = new ServiceError(serviceCode, friendlyMessage,
					technicalMessage, sourceObject);
		}

		return entity;

	}	

}
