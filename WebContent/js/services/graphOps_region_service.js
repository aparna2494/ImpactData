BCCapp.factory('graphOps_region_service',function($http){

	var factory={}
	factory.get_method=function(country_code)
	{
		return $http.get("http://vm-gsqs-opsplus:8080/OPS/rest/graphOps_details_region/"+country_code)
	}
	return factory;
})
