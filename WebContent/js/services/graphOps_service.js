BCCapp.factory('graphOps_service',function($http){

	var factory={}
	factory.get_method=function()
	{
		return $http.get("http://vm-gsqs-opsplus:8080/OPS/rest/graphOps_details/")
	}
	return factory;
})
