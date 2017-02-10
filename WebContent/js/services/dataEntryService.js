 BCCapp.factory('dataEntryService',['$http',function($http){
	
	var factory={};
	
	
	factory.savenodedetails=function(Node)
	{
		
		return $http({
			
			      method: 'POST', url: '/api/nodedetails/save',
			
			      data: Node
			
			    })
	 /*$http.post('/api/calldetails/save',calldetail);*/
	};
	factory.savekpidetails=function(EntityModel)
	{	
		
		return $http({
			
			      method: 'POST', url: '/api/nodedetails/savekpi',
			
			      data: EntityModel
			
			    })
	 /*$http.post('/api/calldetails/save',calldetail);*/
	};
	factory.saveedgedetails=function(edgedetails)
	{
		
		return $http({
			
			      method: 'POST', url: '/api/edgedetails/save',
			
			      data: edgedetails
			
			    })
	 
	};
	factory.saveFlow=function(data)
	{
		console.log(data)
		return $http({
			
			      method: 'PUT', url: '/api/saveflow/save',
			
			      data: data
			
			    })
	 
	};
	factory.publishFlow=function(data)
	{
		console.log(data)
		return $http({
			
			      method: 'PUT', url: '/api/saveflow/publish',
			
			      data: data
			
			    })
	 
	};
	
	factory.fetchMaxNodeID = function()
	{
		return $http.get('/api/nodedetails/fetchnodeID');
	}
	return factory;
}])