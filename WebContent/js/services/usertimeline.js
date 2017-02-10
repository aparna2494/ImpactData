BCCapp.factory('usertimeline',['$http',function($http){
	
	var factory={};
	factory.get_method=function(userId)
	{
		
	return $http.get('/api/timelinedetails/'+userId); 
	};
	return factory;
}])