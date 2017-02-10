BCCapp.factory('secondLevelNodes',['$http',function($http){
	
	var factory={};
	factory.getNodes=function(nodeid,pos_x,pos_y)
	{
		
	//return $http.get('/api/getalertdetailsdataservice/getalertdetails/'+alertID);
		return $http.get('/api/secondlevelnodes/details/'+nodeid+'/'+pos_x+'/'+pos_y);
		/*var nodes=[]
		nodes.push({
            "locked" : false,
            "selected" : false,
            "group" : "nodes",
            "classes" : "",
            "node_fixed" : true,
            "grabbable" : true,
            "position" : {
                "y" : -100,
                "x" : 500
            },
            "selectable" : true,
            "removed" : false,
            "data" : {
                "kpi_data" : [ 
                    
                ],
                "weight" : 1,
                "track_id" : 1,
                "track_name" : "Service Quotes",
                "id" : nodeid+"1",
                "component_name" : "sub1",
                "parent":nodeid	
            }
        })
        nodes.push({
            "locked" : false,
            "selected" : false,
            "group" : "nodes",
            "classes" : "",
            "node_fixed" : true,
            "grabbable" : true,
            "position" : {
                "y" : -100,
                "x" : 400
            },
            "selectable" : true,
            "removed" : false,
            "data" : {
                "kpi_data" : [ 
                    
                ],
                "weight" : 1,
                "track_id" : 1,
                "track_name" : "Service Quotes",
                "id" : nodeid+"2",
                "component_name" : "sub2",
                "parent":nodeid	
            }
        })
        var edges=[]
		edges.push({
            "data" : {
                "source" : nodeid+"1",
                "edge_fixed" : true,
                "target" : nodeid+"2",
                "id" : nodeid+"1"+"R"+nodeid+2
            }
        })
        var data={nodes:nodes,edges:edges}
		return data;*/
	};
	return factory
}])