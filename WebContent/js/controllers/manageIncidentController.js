angular.module('BCCapp').controller('manageIncidentController',['$scope','$filter','$rootScope','$stateParams','IncidentDetailsService','ops_service','cep_service', function($scope,$filter, $rootScope,$stateParams,IncidentDetailsService,ops_service,cep_service) {
    $scope.$on('$viewContentLoaded', function() {   
        // initialize core components
        App.initAjax();
    });
   
    $scope.incidentdata={};
    $scope.opsplusdata = {};
    $scope.cepdata = {};
    $scope.smedata = [];
    $scope.AG="";
    $scope.incidentNumber=$stateParams.incidentNumber
  	IncidentDetailsService.getRemedyIncidentDetails($scope.incidentNumber).success(function(data){
  		
  	$scope.incidentdata=data.incidentDetails;
  	
  	$scope.AG=data.incidentDetails.Assigned_Group
  	
  	IncidentDetailsService.getSMEdetails($scope.AG).success(function(data){
  		
  		$scope.smeList=data
  		getjabberstatus({id:'#sme_jabber_table',values:$scope.smeList,createRoom:true,room_id:$scope.incidentNumber});
  	})
  	})
  	
  	IncidentDetailsService.getRemedyWorkInfoDetails($scope.incidentNumber).success(function(data){
  
  $scope.workinfo=[]
  
  for (var d in data.workLogDetails)
	  {
	  var value_wl={};
	  
	  value_wl['Notes']=data.workLogDetails[d]['Notes']
	  value_wl['Summary']=data.workLogDetails[d]['Summary']
	  value_wl['Submitter']=data.workLogDetails[d]['Submitter']
	  value_wl['Work_Log_Submit_Date']=data.workLogDetails[d]['Work_Log_Submit_Date']
	  value_wl['Work_Log_Type']=data.workLogDetails[d]['Work_Log_Type']
	  if(data.workLogDetails[d]['Work_Log_Type']=="Email System")
		  {
		  value_wl['Work_Log_Type_Icon']="fa fa-envelope"
	      value_wl['Work_Log_Type_Color']="label label-sm label-default"	  
		  }
	  else if (data.workLogDetails[d]['Work_Log_Type']=="Customer Communication")
		  {
		  value_wl['Work_Log_Type_Icon']="fa fa-user"
		  value_wl['Work_Log_Type_Color']="label label-sm label-success"	  
		  }
	  else
		  {
		  value_wl['Work_Log_Type_Icon']="fa fa-info-circle"
		  value_wl['Work_Log_Type_Color']="label label-sm label-info"	  
		  }
	  $scope.workinfo.push(value_wl)
	  $scope.workinfo=$filter('orderBy')($scope.workinfo,'-Work_Log_Submit_Date')
	 
	  
	  }
  	
  	})
  	ops_service.get_method($scope.incidentNumber).success(function(data){
  		
  	  		$scope.ops_incident_amount=0
	  		$scope.ops_incident_issue='-'
	  		$scope.ops_incident_customer='-'
	  		$scope.no_data_msg ='';
	  			
	  		if(data.length==0)
  	  		{
	  			$scope.no_data_msg = 'No $impact and customer impact analystics available for this incident yet.';
  	  		}
	  		else {
	  	  	  	angular.forEach(data,function(value,key){
	  	    	  	
	  	  	  		if(value.amount>=$scope.ops_incident_amount)
	  	  	  			{
	  	  	  		$scope.ops_incident_amount=value.amount
	  		  		$scope.ops_incident_issue=value.issue
	  		  		$scope.ops_incident_customer=value.customers
	  	  	  			}
	  	  	  		
	  	  	  	})	  			
	  		}

  	  	});
  	  	cep_service.get_method($scope.incidentNumber).success(function(data){
  	  		
  	  		if(data.LR=="Y" & data.Clas=="Y")
  	  			{
  	  			$scope.cep_flag="high";
  	  			$scope.cep_class="label-danger";
  	  			}
  	  		else if(data.LR=="N" & data.Clas=="N")
  	  			{
  	  			$scope.cep_flag="Low";
  	  			$scope.cep_class="label-info";
  	  			}
  	  		else
  	  			{
  	  			$scope.cep_flag="medium";
  	  		$scope.cep_class="label-warning";
  	  			}
  	  		
  	  	})
  	  	
  	  $scope.locatesme = function(){
       	if($scope.AG!="")
    		{
    	IncidentDetailsService.getSMEdetails($scope.AG).success(function(data){
      		
      		$scope.smeList=data
      		getjabberstatus({id:'#sme_jabber_table',values:$scope.smeList,createRoom:true,room_id:$scope.incidentNumber});
      	})
    		}
    
    }
  	  $scope.updateCaseNotes=function()
      {
      	
      	
      	IncidentDetailsService.updateCaseNotesService($scope.incidentNumber,$scope.caseNotes).success(function(data){
      		
      		
      	})
      }
   /* $scope.incidentdata= {
    		"workLogDetails": "{\"WLG000009986823\":{\"Work_Log_ID\":\"WLG000009986823\",\"Notes\":\"Testing...pls ignore\",\"Submitter\":\"ITST_ETOOL.gen\",\"Summary\":\"Testing...pls ignore\",\"Work_Log_Submit_Date\":\"2016-04-09 20:37:20 UTC\",\"Communication_Type\":\"Inbound\",\"No_Of_Attachments\":\"\",\"Attachment1\":\"\",\"Attachment2\":\"\",\"Attachment3\":\"\",\"View_Access\":\"Internal\",\"Work_Log_Type\":\"General Information\",\"Communication_Source\":\"Web\"},\"WLG000009986668\":{\"Work_Log_ID\":\"WLG000009986668\",\"Notes\":\"Assignment Group - Previous :GSE-OM-ServiceDesk\\nAssignment Group - Reassigned to :GSE-SCM-DELIVER-OE\\nReassignment Reason :Escalating to next level of Support\",\"Submitter\":\"kakm\",\"Summary\":\"Reassignment by kakm - Escalating to next level of Support\",\"Work_Log_Submit_Date\":\"2016-04-09 18:39:42 UTC\",\"Communication_Type\":\"Inbound\",\"No_Of_Attachments\":\"\",\"Attachment1\":\"\",\"Attachment2\":\"\",\"Attachment3\":\"\",\"View_Access\":\"Internal\",\"Work_Log_Type\":\"Support Group Reassignment\",\"Communication_Source\":\"Other\"},\"WLG000009986667\":{\"Work_Log_ID\":\"WLG000009986667\",\"Notes\":\"Subject: Reassignment of Case # INC800002932765\\n\\n\\nHello DELIVER OE Team,\\n\\nI have analyzed the case and found that this is related to your team.\\n\\nAs per check SO#102166347 PID# UCSC-C3160-SIOC Ln#1.1.25 the line is in AWAITING_FULFILLMENT status.And there is no cancellation hold applied on the line,\\n\\nPlease check and help the user accordingly. You can contact me, for any issues or clarifications.\\n\\nHello Karthick,\\n\\nThis is Karthikeyan K M from the GSE Servicedesk team. I am contacting you regarding your Case # INC800002932765. I have analyzed the case and it turned out to be related to the DELIVER OE. In an effort to provide the quickest and most efficient solution possible, I am forwarding this case to their group for a faster resolution.\\n\\nPlease check with DELIVER OE for further updates.\\n\\nRegards,\\nKarthikeyan K M\\nGSE Servicedesk\",\"Submitter\":\"kakm\",\"Summary\":\"Subject: Reassignment of \",\"Work_Log_Submit_Date\":\"2016-04-09 18:39:38 UTC\",\"Communication_Type\":\"Inbound\",\"No_Of_Attachments\":\"\",\"Attachment1\":\"\",\"Attachment2\":\"\",\"Attachment3\":\"\",\"View_Access\":\"Public\",\"Work_Log_Type\":\"General Information\",\"Communication_Source\":\"\"},\"WLG000009986562\":{\"Work_Log_ID\":\"WLG000009986562\",\"Notes\":\"Subject: Acceptance of Case # INC800002932765\\n\\n\\nHello Karthick,\\n\\nThank you for contacting the GSE Servicedesk team regarding your issue. I have accepted your case and I am currently working on on analyzing the issue. We will contact you with questions if needed, as well as notify you, by email\\\/communicator(WebEx\\\/Jabber) when a resolution has been reached.\\n\\nI will update the case notes as soon as I have more information.\\n\\nRegards,\\nKarthikeyan K M\\nGSE Servicedesk\",\"Submitter\":\"kakm\",\"Summary\":\"Subject: Acceptance of Ca\",\"Work_Log_Submit_Date\":\"2016-04-09 18:39:11 UTC\",\"Communication_Type\":\"Inbound\",\"No_Of_Attachments\":\"\",\"Attachment1\":\"\",\"Attachment2\":\"\",\"Attachment3\":\"\",\"View_Access\":\"Public\",\"Work_Log_Type\":\"General Information\",\"Communication_Source\":\"\"},\"WLG000009986552\":{\"Work_Log_ID\":\"WLG000009986552\",\"Notes\":\"This ticket was created from the service request system.\",\"Submitter\":\"Remedy Application Service\",\"Summary\":\"This ticket was created from the service request system.\",\"Work_Log_Submit_Date\":\"2016-04-09 18:16:10 UTC\",\"Communication_Type\":\"Inbound\",\"No_Of_Attachments\":\"\",\"Attachment1\":\"\",\"Attachment2\":\"\",\"Attachment3\":\"\",\"View_Access\":\"Internal\",\"Work_Log_Type\":\"Customer Communication\",\"Communication_Source\":\"\"}}",
    		"result": "success",
    		"incidentDetails": {
    			"Company": "Cisco Systems",
    			"Resolution_Categorization_Tier_1": "",
    			"Resolution_Categorization_Tier_3": "",
    			"Middle_Initial": "",
    			"Resolution_Categorization_Tier_2": "",
    			"Project_Name": "",
    			"Product_Categorization_Tier_3": "OM R12",
    			"Additional_Details": "",
    			"Department": "CS Globals Ops - US\/AI Outsourcing",
    			"Assigned_Support_Organization": "Global Support Experience (GSE)",
    			"Product_Categorization_Tier_1": "Customer Value Chain Management (CVCM) IT",
    			"Product_Categorization_Tier_2": "Quote To Cash",
    			"Manufacturer": "Oracle",
    			"Reported_Source": "SRM",
    			"Release_Cycle": "",
    			"Status": "Assigned",
    			"Impact": "Severity 4",
    			"Assignee": "",
    			"Release_Prioritization": "",
    			"Priority": "P6",
    			"Product_Name": "order copy-om r12",
    			"City": "BANGALORE",
    			"Incident_ID": "INC800002932765",
    			"Release_Relation": "",
    			"Release_Date": "",
    			"Country": "India",
    			"Last_Modified_By": "ITSTINT.gen",
    			"Region": "INDIA",
    			"Login_ID": "karprabh",
    			"Resolution": "",
    			"Status_Reason": "",
    			"Site": "BANGALORE INFOSYS BPO 1",
    			"Internet_E-mail": "karprabh@cisco.com",
    			"Closure_Product_Name": "",
    			"Last_Resolved_Date": "",
    			"Organization": "Cust Svc & Operational Systems",
    			"Operational_Categorization_Tier_1": "Modify",
    			"Assigned_Support_Company": "Cisco Systems",
    			"Phone_Number": "###",
    			"Operational_Categorization_Tier_3": "Adhoc",
    			"Operational_Categorization_Tier_2": "Data",
    			"First_Name": "Karthick",
    			"Priority_Weight": "1",
    			"Assignee_ID": "",
    			"Closure_Product_Category_Tier2": "",
    			"Closure_Product_Category_Tier1": "",
    			"Closure_Product_Category_Tier3": "",
    			"Last_Modified_Date": "2016-04-09 20:37:21 UTC",
    			"Notes": "Does this issue impact UCS Product orderability?:YES\nPlease describe you issue? (Kindly provide Sales Order#, Screen shot of errors  if any)?:Hi Team,\n\nThis is with reference SO#102166347\n\nPlease cancel line Ln#1.1.25 part#UCSC-C3160-SIOC from the major line Ln#1 asap.\n\nRegards\nKarthick.P\nSelect the Product type with which you are facing the issue::order - cancellation -om\nWhat is the category of issue?:order copy-om r12\nWhat is the type of your request ? :Data\/Bug\n",
    			"Product_Model\/Version": "",
    			"Resolution_Category": "",
    			"Submit_Date": "2016-04-09 18:16:13 UTC",
    			"Owner_Support_Company": "Cisco Systems",
    			"Assigned_Group": "GSE-SCM-DELIVER-OE",
    			"Last_Name": "Prabhakar",
    			"Service_Type": "User Service Request",
    			"Summary": "Order Management R12 (CG1PRD)",
    			"Instance_ID": "IDHJR030HEIOHAOF3P4UF1RZ4INA7V",
    			"Desk_Location": " \/ \/BANGALORE INFOSYS BPO 1",
    			"VIP": "No",
    			"Urgency": "P5\/P6",
    			"Contact_Company": "Cisco Systems"
    		}
    	};*/
	
}]);