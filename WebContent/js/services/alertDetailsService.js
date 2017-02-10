BCCapp.factory('alertDetailsService',['$http',function($http){
	
	var factory={};
	factory.alertdetailsaction=function(alertID)
	{
		
	return $http.get('/api/getalertdetailsdataservice/getalertdetails/'+alertID);
	};
	
	factory.alerttrendsaction=function(alertID)
	{
		
	return $http.get('/api/getalertdetailsdataservice/getalerttrenddetails/'+alertID);
	};
	factory.alerthistoryaction=function(flowComponentId)
	{
		
	return $http.get('/api/getalertdetailsdataservice/getalerthistorydetails/'+flowComponentId);
	};
	factory.commentssaveaction=function(comment,alertID,collectionname)
	{
		if(collectionname==undefined)
		{
		collectionname="bcc_alerts_sec"
		}
		return $http.get('/api/getalertdetailsdataservice/savealertcomment/'+comment+'/'+alertID+'/'+collectionname);
	}
	factory.assignsaveaction=function(alertID,assigneename,collectionname)
	{
		if(collectionname==undefined)
			{
			collectionname="bcc_alerts_sec"
			}
		return $http.get('/api/getalertdetailsdataservice/saveassignee/'+alertID+'/'+assigneename+'/'+collectionname);
	}
	factory.verifyuser=function(newassignee)
	{
		return $http.get('/api/getalertdetailsdataservice/verifyassignee/'+newassignee);
	}
	factory.closealertaction=function(alertID)
	{
		return $http.get('/api/getalertdetailsdataservice/closealert/'+alertID);
	}
	factory.smelocatoraction=function(trackID)
	{
		if (isNaN(trackID)) {
	        trackID = null;
	    } else {  
	        var data = {"appId":trackID};
	    }
		console.log(trackID);
		return $http.post('/api/chat/applicationAvailability',data);	
	}
	return factory;
}])