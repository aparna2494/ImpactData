BCCapp.factory('sme_list',['$http',function($http){
	
	var factory={};
	factory.get_method=function(portfolio,application)
	{
		//console.log('in cep_service');
		//console.log('vm-gse-analytics:8080/cepAPI/rest/'+incident_number);
	
	//return {'color':'red'};
		//var color={'color':'red'};
		//console.log(incident_number);
		
	return $http.get('http://vm-gsqs-opsplus:8080/Web_service_mongo/rest/UserService_test/users/'+portfolio+'/'+application)
		//return color;
	};
	return factory;
}])
