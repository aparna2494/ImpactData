BCCapp.factory('dataservices',['$http',function($http){
	
	var factory={};
	factory.csccdataaction=function()
	{
	return $http.get('/api/getCSCCdataservice/getCSCCdata');
	};
	factory.getnid=function(kpi_id,component_id)
	{
	return $http.get('/api/getflowdata/getnid/'+kpi_id+'/'+component_id);
	};
	factory.ccwdataaction=function()
	{
	return $http.get('/api/getCCWdataservice/getCCWdata');
	};
	factory.flowdiagram=function(collection)
	{
	return $http.get('/api/getflowdata/getflows'+'/'+collection);
	};
	factory.neoflowdiagram=function()
	{
		console.log("in neo service")
	return $http.get('/api/getflowdata/getneoflows');
	};
	factory.fetchRoomDetails = function(track_id)
	{
		return $http.get('/api/getalertsdataservice/getroomdetails/'+track_id);
	};
	factory.alertsmonitoringaction=function()
	{
		return $http.get('/api/getalertsdataservice/getalertsdata');
	}
	factory.alertsdataaction=function(collection)
	{

		console.log("******************************")
		console.log(collection)
		if(collection==undefined)
			{
			collection="bcc_alerts_sec"
			}
		return $http.get('/api/getalertsdataservice/getalertsmonitoringdata/'+collection);
	}
	
	return factory;
}])