package com.cisco.bccsp.application;

import java.util.HashSet;
import java.util.Set;


import javax.ws.rs.core.Application;

import com.cisco.bccsp.services.AutoCompleteService;
import com.cisco.bccsp.services.CepService;
import com.cisco.bccsp.services.ChatService;
import com.cisco.bccsp.services.ClosedIncidentDetailsService;
import com.cisco.bccsp.services.CpeDetailsService;
import com.cisco.bccsp.services.IncidentDetailService;
import com.cisco.bccsp.services.IncidentService;
import com.cisco.bccsp.services.Neo4jService;
import com.cisco.bccsp.services.OpenIncidentDetailsService;
import com.cisco.bccsp.services.RegisterCallDetails;
import com.cisco.bccsp.services.RosterService;
import com.cisco.bccsp.services.SKMSService;
import com.cisco.bccsp.services.SecondLevelNodesService;
import com.cisco.bccsp.services.UserDetailsService;
import com.cisco.bccsp.services.UserIncidentDetailsService;
import com.cisco.bccsp.services.UserInfo;
import com.cisco.bccsp.services.getAlertDataService;
import com.cisco.bccsp.services.getAlertDetails;
import com.cisco.bccsp.services.getCCWDataService;
import com.cisco.bccsp.services.getCSCCDataService;
import com.cisco.bccsp.services.saveEdgeDetails;
import com.cisco.bccsp.services.saveFlow;
import com.cisco.bccsp.services.saveNodeDetails;
import com.cisco.bccsp.services.searchIndexList;
import com.cisco.bccsp.services.timelineService;
import com.cisco.bccsp.services.getFlowDataService;
import com.cisco.bccsp.services.verifyIncident;
import com.cisco.bccsp.services.getGroupMembers;
import com.cisco.bccsp.services.AGList;
import com.cisco.bccsp.services.ApplicationListService;
import com.cisco.bccsp.services.KPIDetails;
import com.cisco.secwebservice.main.test_main;

public class CaseImpactApplication extends Application {
	
	   private Set<Object> singletons = new HashSet<Object>();
	   private Set<Class<?>> empty = new HashSet<Class<?>>();

	   public CaseImpactApplication()
	   {
		  singletons.add(new AutoCompleteService());
	      singletons.add(new IncidentService());
	      singletons.add(new CpeDetailsService());
	      singletons.add(new OpenIncidentDetailsService());
	      singletons.add(new ClosedIncidentDetailsService());
	      singletons.add(new getCSCCDataService());
	      singletons.add(new getCCWDataService());
	      singletons.add(new getFlowDataService());
	      singletons.add(new getAlertDataService());
	      singletons.add(new getAlertDetails());
	      singletons.add(new SKMSService());
	      singletons.add(new UserInfo());
	      singletons.add(new verifyIncident()); 
	      singletons.add(new IncidentDetailService());
	      singletons.add(new UserDetailsService());
	      singletons.add(new UserIncidentDetailsService());
	      singletons.add(new timelineService());
	      singletons.add(new ChatService());
	      singletons.add(new CepService());
	      singletons.add(new searchIndexList());
	      singletons.add(new RosterService());
	      singletons.add(new RegisterCallDetails());
	      singletons.add(new saveNodeDetails());
	      singletons.add(new saveEdgeDetails());
	      singletons.add(new ApplicationListService());
	      singletons.add(new getGroupMembers());
	      singletons.add(new AGList());
	      singletons.add(new Neo4jService());
	      singletons.add(new saveFlow());
	      singletons.add(new SecondLevelNodesService());
	      singletons.add(new KPIDetails());
	      singletons.add(new test_main());
	      //singletons.add(new RequestFilter());
	   }

	   @Override
	   public Set<Class<?>> getClasses()
	   {
	      return empty;
	   }

	   @Override
	   public Set<Object> getSingletons()
	   {
	      return singletons;
	   }

}
