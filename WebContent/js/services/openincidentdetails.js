BCCapp.factory('openincidentdetails',['$http',function($http){
	
	var factory={};
	factory.get_method=function(cpe_name)
	{
		
	return $http.get('/api/openincidentdetails/'+cpe_name);
	};
	return factory;
}])