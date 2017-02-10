BCCapp.factory('cpedetails',['$http',function($http){
	
	var factory={};
	factory.get_method=function(entity, category )
	{
		
	return $http.get('/api/cpedetails/'+entity+'/'+category);
	};
	return factory;
}])