BCCapp.factory('graphOps_summary_service',function($http){

	var factory={}
	factory.get_method=function(country_code)
	{
		return $http.get("http://vm-gsqs-opsplus:8080/OPS/rest/graphOps_summary/")
	}
	return factory;
})
