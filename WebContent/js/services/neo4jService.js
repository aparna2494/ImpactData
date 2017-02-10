BCCapp.factory('neo4jService',['$http',function($http){
	
	var factory={};
	
	factory.getjobdata=function(node)
	{
	return $http.get('/api/neo4j/job/'+node);
	};
	factory.getscmdata=function()
	{
	return $http.get('/api/neo4j/scm');
	};
	factory.fetchTracks=function()
	{
		return $http.get('/api/neo4j/fetchTracks')
	}
	factory.getTrackflow=function(track_name,counter)
	{
		return $http.get('/api/neo4j/getflow/'+track_name+'/'+counter)
	}
    factory.getNeoData=function(component_name)
    {
    	console.log(component_name)
    	return $http.get('/api/neo4j/nodeinfo/'+component_name);
    }
	return factory;
}])