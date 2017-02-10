BCCapp.factory('IncidentDetailsService',function($http){

	var factory={}
	factory.getRemedyIncidentDetails=function(incident_number)
	{
		return $http.get("api/incident/details/"+incident_number)
	}
	
	factory.getSMEdetails=function(AG)
	{
		return $http.post('/api/chat/currentAvailability',{"ag":AG});
	}
	factory.getRemedyWorkInfoDetails=function(incident_number)
	{
		return $http.get('api/incident/workinfo/'+incident_number)
	}
	factory.updateCaseNotesService=function(incident_number,casenotes)
	{
		
		return $http.post('api/incident/update',{"notes":casenotes, "incidentNumber":incident_number})
	}
	
	return factory;
	
})
