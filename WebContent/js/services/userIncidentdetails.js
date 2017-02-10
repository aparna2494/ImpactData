BCCapp.factory('userIncidentdetails',['$http',function($http){
	
	var factory={};
	factory.get_method=function(userId)
	{
		
	return $http.get('/api/userincidentdetails/'+userId);
	};
	return factory;
}])