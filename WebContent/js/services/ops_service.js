BCCapp.factory('ops_service',['$http',function($http){
	
	var factory={};
	factory.get_method=function(incident_number)
	{
		//console.log('in cep_service');
		//console.log('vm-gse-analytics:8080/cepAPI/rest/'+incident_number);
	
	//return {'color':'red'};
		//var color={'color':'red'};
		//console.log(incident_number);
		
		
	return $http.get('http://vm-gsqs-opsplus:8080/OPS/rest/ops_summary/'+incident_number)
		//return color;
	};
	return factory;
}])
