BCCapp.factory('closedincidentdetails',['$http',function($http){
	
	var factory={};
	factory.get_method=function(cpe_name)
	{
		
	return $http.get('/api/closedincidentdetails/'+cpe_name);
	};
	return factory;
}])