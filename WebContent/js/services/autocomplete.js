BCCapp.factory('autocomplete',['$http',function($http){
	
	var factory={};
	factory.get_method=function()
	{
		
	return $http.get('/api/autocomplete_list/');
	};
	factory.getSearchIndex=function()
	{
		
	return $http.get('/api/searchIndexList/');
	};
	
	factory.verifyUser=function(userid)
	{
		return $http.get('/api/userinfo/'+userid);
	}
	return factory;
}])