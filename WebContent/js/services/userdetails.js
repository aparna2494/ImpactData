BCCapp.factory('userdetails',['$http',function($http){
	
	var factory={};
	factory.get_method=function(userId)
	{
		
	return $http.get('/api/userdetails/'+userId);
	};
	factory.verify_incident__number = function(incidentnumber)
	{
		 
		  
		      return $http({
		    	  method: 'GET',
		    	  url: 'api/verifyincident/'+incidentnumber		    	});
	}
	factory.savecalldetail=function(calldetail)
	{
		
		$http({
			
			      method: 'POST', url: '/api/calldetails/save',
			
			      data: calldetail
			
			    }).
			
			      success(function(data, status, headers, config) {
			
			       return data;
			
			      })
	 /*$http.post('/api/calldetails/save',calldetail);*/
	};
	return factory;
}])