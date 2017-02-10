BCCapp.controller('NeoNetworkController', ['$rootScope', '$scope', 'flowGraph','dataservices','alertDetailsService','$state','$interval','$timeout','neo4jService','KPIDetails','$filter','$stateParams',
		function($rootScope,$scope, flowGraph,dataservices,alertDetailsService,$state,$interval,$timeout,neo4jService,KPIDetails,$filter,$stateParams) {

	//$scope.DBnames=["1","2","3"]
	$scope.count=0;
	$scope.dependencytypes=[{type:"Application"},{type:"Database"},{type:"Others"}]
	$scope.entitytypes=[{type:"Software"},{type:"Product"},{type:"URL"},{type:"Others"}]
	$scope.alertCollection = $state.current.data.alertCollection 
	$scope.flowCollection = $state.current.data.flowCollection 
	$scope.styles=[ {
		"selector" : "node",
		"css" : {
			
			'display':'none'
		}
	}]
	$('#chat_portlet').addClass('hide')
	
$scope.alerts=[];
	var initdatacaller=0;
$scope.neovalues;
$scope.selected_component_node;
$scope.selected_node="";
$scope.alerts_filtered=[];
$scope.filtered_node=[];
$scope.newassignee="loggeduser";
$scope.alertmode=[];
$scope.alerttracks=[];
$scope.AlertComponentArray=[];
$scope.AlertParentComponentArray=[];
$scope.data=[];
$scope.allParents=[]
$scope.refreshgraph=false;
var availableTags=[];
$scope.availableTags=[];
$scope.trackarray=[];

$scope.selectedalertmodelabels=[]
$scope.jobdetails={}
var promise
$scope.refreshfrequencyset=[1,5,10,30,60]
$scope.alertshow={value:false};
$scope.historyshow={value:false};
$scope.updateNodes=function()
{
 console.log("inside update nodes")
 angular.forEach($scope.data.nodes, function(value, key) {
	 $scope.data.nodes[key].data.alert='N';
 
 });
 angular.forEach($scope.data.nodes, function(value, key) {
	
	 if((value.data.masterid!="null") && $scope.allParents.indexOf(value.data.masterid)<0 && $scope.AlertParentComponentArray.indexOf(value.data.masterid)<0)
		 {
		 $scope.allParents.push(value.data.masterid);
		 
		 }
	 if($scope.AlertComponentArray.indexOf(value.data.id) >-1 || $scope.AlertParentComponentArray.indexOf(value.data.component_code)>-1 )
     {
     
     $scope.data.nodes[key].data.alert='Y';
     
     }
		else{
			$scope.data.nodes[key].data.alert='N';
		}
		
		if(key==$scope.data.nodes.length-1)
			{
			
			 /*$scope.layout = {
				"name" : "preset"
			};*/
  			updateStyle($rootScope.selectedtrackarraylabels_neo)
			}
		
		
	});
 
    
		
    
}
$scope.filterByTrack=function(trackname,selected)
{
	console.log(selected)
	var temp=$rootScope.selectedtrackarraylabels_neo
	var index = temp.indexOf(trackname);
	
		if(selected)
			{
			temp.push(trackname);
			}
		else if(index > -1)
			{
				temp.splice(index, 1);
			}
		$rootScope.selectedtrackarraylabels_neo=temp
		console.log($rootScope.selectedtrackarraylabels_neo)

}
$scope.filterByAlertmode=function(alertmode,selected)
{
	console.log(selected);
	console.log(alertmode);
	var temp=$scope.selectedalertmodelabels
	var index = temp.indexOf(alertmode);
	
		if(selected)
			{
			temp.push(alertmode);
			}
		else if(index > -1)
			{
				temp.splice(index, 1);
			}
		$scope.selectedalertmodelabels=temp
		console.log($scope.selectedalertmodelabels);

}
updateStyle=function(newval,nodef)
{
	console.log(newval)
	console.log($scope.data.nodes)
	var temp_styles = [ 
	                    {
			"selector" : "node[masterid !='null']",
			"css" : {
				
				'display':'none'
			}
		},{
			"selector" : "node",
			"css" : {
				"width" : "mapData(weight,1,3, 20, 60)",
				"height" : "mapData(weight,1,3, 20, 60)",
				'content' : 'data(component_name)',
				'text-valign' : 'top',
				'color' : 'white',
				'background-color': '#1bbc9b',
				'text-wrap' : 'wrap',
				'text-max-width' : 80,
				'text-outline-width' : 0,
				'text-outline-color' : '#888',
				'min-zoomed-font-size' : 8,
				'font-size' : "mapData(weight,1,3, 8, 16)"
			}
		}, {
			"selector" : "edge",
			"css" : {
				'min-zoomed-font-size' : 10,
				'font-size' : 8,
				'color' : '#fff',
				'line-color' : '#e1e5ec',
				'width' : 2,
				'curve-style' : 'haystack',
				'haystack-radius' : 0,
				'opacity' : 0.5,
				'mid-target-arrow-shape':'triangle',
				'mid-target-arrow-fill':'hollow'
				
			}
		}, {
			"selector" : ":selected",
			"css" : {
				'background-color' : '#f2f2f2',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		{
			"selector" : ".selected",
			"css" : {
				'background-color' : '#f2f2f2',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},{
			"selector" : ".faded",
			"css" : {
				'opacity' : 0.25,
				'text-opacity' : 0
			}
		}/*,
		{
			"selector" : "node[parent]",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#ffffff',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		}*/,
		{
			"selector" : ".selectedSecondLevelNode",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#2f353b',
				'line-color' : 'black',
				'source-arrow-color' : 'black',
				'border-width':'1px',
				'border-color':'white'
			}
		}
		,
		{
			"selector" : "node[parentNode = 'yes']",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#2f353b',
				/*'background-color' : 'red',*/
				'line-color' : 'black',
				'source-arrow-color' : 'black',
				'border-width':'1px',
				'border-color':'white'
			}
		},
		
		
		{
			"selector" : "node[alert = 'Y']",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#e43a45',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},{
				"selector" : ".incomer",
  				"css" : {
  					'background-color' : '#049fd9',
  					'line-color' : '#049fd9',
  					'source-arrow-color' : '#049fd9'

  				},
  				
  			},
			{
				"selector" : ".outgoer",
				"css" : {
					'background-color' : '#abc123',
					'line-color' : '#abc123',
					'source-arrow-color' : '#abc123'

				},
				
			},
		
			
		
		{
			"selector" : ".alerted",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#f1c40f',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		} ,
		{
			"selector" : ".searched",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'color':'red'

			},
			
		},/*{
			"selector" : "node[parent]",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#ffffff',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},*/
		{
			"selector" : ".selectedSecondLevelNode",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#2f353b',
				'line-color' : 'black',
				'source-arrow-color' : 'black',
				'border-width':'1px',
				'border-color':'white'
			}
		}];
	angular.forEach($scope.allParents,function(value,key){
		
			temp_styles.push(
					{
						"selector" : "node[component_code = '"+value+"']",
						"css" : {
							'min-zoomed-font-size' : 4,
							'font-size' : "mapData(weight,1,3, 12, 16)",
							'background-color' : '#4B77BE',
							'line-color' : 'black',
							'source-arrow-color' : 'black'
						}
					}		
			)
			});
	
	angular.forEach($scope.trackarray,function(value,key){
		if(newval.length==0 || newval.indexOf(value.label)>-1)
			{
			temp_styles.push(
					{
						"selector" : "node[track_name = '"+value.label+"']",
						"css" : {
							'min-zoomed-font-size' : 4,
							'font-size' : "mapData(weight,1,3, 12, 16)",
							
							'line-color' : 'black',
							'source-arrow-color' : 'black'
						}
					}		
			)
			}
		
		else
			{
			temp_styles.push(
					{
						"selector" : "node[track_name = '"+value.label+"']",
						"css" : {
							'min-zoomed-font-size' : 4,
							'font-size' : "mapData(weight,1,3, 12, 16)",
							
							'line-color' : 'black',
							'source-arrow-color' : 'black',
							'display':'none'
						}
					}		
			)
			}
		
		if(key==$scope.trackarray.length-1)
			{
			
			if(nodef!=null)
				{
				temp_styles.push({
					
						"selector" : "node[component_name = '"+nodef+"']",
						"css" : {
							'min-zoomed-font-size' : 4,
							'font-size' : "mapData(weight,1,3, 12, 16)",
							'background-color' : '#f1c40f',
							'line-color' : 'black',
							'source-arrow-color' : 'black'
						}
					})
					temp_styles.push({
				
					"selector" : "node[track_name = '"+nodef+"']",
					"css" : {
						'min-zoomed-font-size' : 4,
						'font-size' : "mapData(weight,1,3, 12, 16)",
						'background-color' : '#f1c40f',
						'line-color' : 'black',
						'source-arrow-color' : 'black'
					}
				})
				}
			
			
			
			$timeout(function(){
				$scope.styles=temp_styles
				if(initdatacaller==0)
				{
					initdatacaller=1
					console.log("settinglayout")
				$scope.layout = {
						"name" : "preset"
					};
				}
			
			})
			
			
			}
	})

}
$scope.$watch('selectedtrackarraylabels_neo',function(newval){
	console.log("track changed")
	$scope.alerttracks=newval;
	updateNodes(newval);
	updateAlerts(newval)
	updateStyle(newval)
},true)

$scope.$watch('selectedalertmodelabels',function(newval){
	console.log("alert mode changed")
	$scope.alertmode=newval;
	console.log($scope.alertmode);
	console.log(newval);
	//updateNodes(newval);
    updateAlertmode(newval);
},true)

updateAlertmode=function(newval,selected)
{
console.log(selected)
console.log(newval)
console.log($scope.alerts.length)
	if(newval.length!=0)
		{
		$scope.alerts_filtered=[]
		if($scope.alerttracks.length>0)
		{
		$.each($scope.alerts,function(key,value){
			if((newval.indexOf(value.alert_mode))>-1&&($scope.alerttracks.indexOf(value.track_name)>-1))
				{
				$scope.alerts_filtered.push(value)
				}
			
			if(key==$scope.alerts.length-1 && selected!=undefined)
				{
				$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
				console.log($scope.alerts_filtered)
				}
			
			
		})
		
		}
		else {
			$.each($scope.alerts,function(key,value){
				if(newval.indexOf(value.alert_mode)>-1)
					{
					$scope.alerts_filtered.push(value)
					}
				
				if(key==$scope.alerts.length-1 && selected!=undefined)
					{
					$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
					console.log($scope.alerts_filtered)
					}
				
				
			})
		}
			
		
		}
	else
		{
		
		if($scope.alerttracks.length>0)
		{ $scope.alerts_filtered=[]
		 $.each($scope.alerts,function(key,value){
			if($scope.alerttracks.indexOf(value.track_name)>-1)
				{
				$scope.alerts_filtered.push(value)
				}
			
			if(key==$scope.alerts.length-1 && selected!=undefined)
				{
				$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
				console.log($scope.alerts_filtered)
				}
			
			
		})
		}
		else $scope.alerts_filtered=$scope.alerts
		if(selected!=undefined)
		{
			$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
			console.log($scope.alerts_filtered)
			}
		}
	 
}

updateNodes=function(newval,selected)
{
	
	console.log(selected)
	
	console.log(newval)
	console.log($scope.data)
	//console.log("inside Update nodes" +$scope.data.nodes[0].data.track_name);
	//console.log($scope.alerts.length)
		if(newval.length!=0)
			{
			$scope.data.selectedNodes=[];
			
			$.each($scope.data.nodes,function(key,value){
				//console.log(value.data.track_name);
				if(newval.indexOf(value.data.track_name)>-1)
					{
					$scope.data.selectedNodes.push(value)
					console.log($scope.data.selectedNodes);
					}
				
				if(key==$scope.data.nodes-1 && selected!=undefined)
					{
					$scope.data.selectedNodes=$filter('filter')($scope.data.selectedNodes,selected)
					console.log($scope.data.selectedNodes)
					}
				
				
			})
			}
		else
			{
			
			$scope.data.selectedNodes=$scope.data.nodes;
			if(selected!=undefined)
			{
				$scope.data.selectedNodes=$filter('filter')($scope.data.selectedNodes,selected)
				//console.log($scope.alerts_filtered)
				}
			}

}

updateAlerts=function(newval,selected)
{
console.log(selected)
console.log(newval)
console.log($scope.alerts.length)
	if(newval.length!=0)
		{
			$scope.alerts_filtered=[]
			if($scope.alertmode.length>0)
		{
				console.log("alertmode not empty");
		$.each($scope.alerts,function(key,value){
			if((newval.indexOf(value.track_name)>-1)&&($scope.alertmode.indexOf(value.alert_mode)>-1))
				{
				$scope.alerts_filtered.push(value)
				}
			
			if(key==$scope.alerts.length-1 && selected!=undefined)
				{
				$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
				console.log($scope.alerts_filtered)
				}
			
			
		})
		}
		else
			{
			console.log("alertmode is empty");
			$.each($scope.alerts,function(key,value){
				if(newval.indexOf(value.track_name)>-1)
					{
					$scope.alerts_filtered.push(value)
					}
				
				if(key==$scope.alerts.length-1 && selected!=undefined)
					{
					$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
					console.log($scope.alerts_filtered)
					}
				
				
			})
			}
		
		}
	else
		{
		
		if($scope.alertmode.length>0)
		{ $scope.alerts_filtered=[]
		 $.each($scope.alerts,function(key,value){
			if($scope.alertmode.indexOf(value.alert_mode)>-1)
				{
				$scope.alerts_filtered.push(value)
				}
			
			if(key==$scope.alerts.length-1 && selected!=undefined)
				{
				$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
				console.log($scope.alerts_filtered)
				}
			
			
		})
		}
		else $scope.alerts_filtered=$scope.alerts
		if(selected!=undefined)
		{
			$scope.alerts_filtered=$filter('filter')($scope.alerts_filtered,selected)
			console.log($scope.alerts_filtered)
			}
		}
	 
}

$scope.showdetails=function(nodeID)
{
	console.log("insidefunction");
	console.log(nodeID)
	
	var flag=1;
	//console.log($scope.data.nodes);
	$.each($scope.data.nodes,function(key,value){
		if(flag)
	  {
		if(nodeID.indexOf(value.data.id)>-1)
			{
			$timeout(function(){$scope.selected_node=value.data.component_name});
			$scope.selected_component_code=value.data.component_code;
			console.log($scope.selected_component_code);
			console.log($scope.selected_node);
			flag=0;
			//break;
			}
	  }
	});
	console.log($scope.selected_component_code)
	neo4jService.getNeoData($scope.selected_component_code).success( function(data)
			{
		       console.log(data);
		       $timeout(function(){$scope.jobdetails=data;});
		       
			});
}
$scope.selectedValue = function (selected) {
	console.log("in selected value fun")
	console.log($scope.data.selectedNodes)
	  if(selected!=undefined)
		  {
		  $scope.searchTerm= selected.title;
		  $scope.data.selectedNodes=$filter('filter')($scope.data.nodes,selected.title)
		  updateAlerts($rootScope.selectedtrackarraylabels_neo,selected.title)
		    var nodef =  $scope.searchTerm;
		    updateStyle($rootScope.selectedtrackarraylabels_neo,nodef)
		  }
	  else
		  {
		  $scope.data.selectedNodes=$scope.data.nodes
		  updateAlerts($rootScope.selectedtrackarraylabels_neo)
		  }


   /* $scope.styles = [ {
			"selector" : "node",
			"css" : {
				"width" : "mapData(weight,1,3, 20, 60)",
				"height" : "mapData(weight,1,3, 20, 60)",
				'content' : 'data(component_name)',
				'text-valign' : 'top',
				'color' : 'white',
				'background-color': '#1bbc9b',
				'text-wrap' : 'wrap',
				'text-max-width' : 80,
				'text-outline-width' : 0,
				'text-outline-color' : '#888',
				'min-zoomed-font-size' : 8,
				'font-size' : "mapData(weight,1,3, 8, 16)"
			}
		}, {
			"selector" : "edge",
			"css" : {
				'min-zoomed-font-size' : 10,
				'font-size' : 8,
				'color' : '#fff',
				'line-color' : '#e1e5ec',
				'width' : 2,
				'curve-style' : 'haystack',
				'haystack-radius' : 0,
				'opacity' : 0.5,
				'mid-target-arrow-shape':'triangle',
				'mid-target-arrow-fill':'hollow'
				
			}
		}, {
			"selector" : ":selected",
			"css" : {
				'background-color' : '#f2f2f2',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		{
			"selector" : ".selected",
			"css" : {
				'background-color' : '#f2f2f2',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},{
			"selector" : ".faded",
			"css" : {
				'opacity' : 0.25,
				'text-opacity' : 0
			}
		},
		
		
		{
			"selector" : "node[alert = 'Y']",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#e43a45',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		{
			"selector" : "node[alert = 'N']",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#1bbc9b',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		{
			"selector" : "node[component_name = '"+nodef+"']",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#f1c40f',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		{
			"selector" : "node[track_name = '"+nodef+"']",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#f1c40f',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		
		{
			"selector" : ".alerted",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#f1c40f',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		},
		{
			"selector" : ".highlighted",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 8, 16)"

			}
		} ,
		{
			"selector" : ".searched",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'color':'red'

			},
			
		}];
		*/
    /* $scope.layout = {
				"name" : "preset"
			};*/
   
     
};

 $('#searchTerm').keypress(function(e){ //when key is pressed on query text box

	
		if(e.which == 13){            //if the pressed key is enter
		
		}
}) 
initdata();
			$scope.$on('$viewContentLoaded', function() {
				// initialize core components
				App.initAjax();
			});
			 function uniq(a, param){
			        return a.filter(function(item, pos, array){
			            return array.map(function(mapItem){ return mapItem[param]; }).indexOf(item[param]) === pos;
			        })
			    }
			
			 
			 
			 $scope.reloadData= function(){
				  
				 
					 
						promise= $interval(function(){
							  
							 $rootScope.countvar_neo=1;
							 /*if($rootScope.countvar_neo_neo>1)
								 {
								 $interval.cancel(promise);
								 }*/
							 
							 if($state.current.name=="neoflow" || $state.current.name=="flow")
								 {
								
								 $scope.alerts=[];
								 $scope.alerts_filtered=[];
								 $scope.AlertComponentArray=[];
								 $scope.AlertParentComponentArray=[];
								 //$state.go($state.current, {}, {reload: true});
								dataservices.alertsdataaction($scope.alertCollection).success(
			                             function(data) {
			                            	 console.log("data");
			                            	 console.log(data);
			                            	 $scope.AlertComponentArray=[];
			                            	 $scope.AlertParentComponentArray=[];
			                            	 if(data.length==0)
			                                 {
			                                 $scope.updateNodes();
			                                 }
			                             	angular.forEach(data, function(value, key) {
			                             		
			                 	          		if(value!="No data available"){
			                 	          			value.alert_create_date_unx = moment.unix(value.alert_create_date_unx).fromNow();
			                 	          			/*if(value.spark_room_name==null)
			                 	          				{
			                 	          				dataservices.fetchRoomDetails(value.track_id).success(
			           			                             function(res) {
			           			                            	value.spark_room_name= res[0].spark_room_name;
			           			                            	value.spark_room_id= res[0].spark_room_id;
			           			                             });
			                 	          				}*/
			                 	          		console.log(value);
			                 	          		$scope.alerts.push(value);	
			                 	          		
			                 	          		$scope.AlertComponentArray.push(value.id);
			                 	          		if(value.masterid!=null)
		                 	          			{
		                 	          			$scope.AlertParentComponentArray.push(value.masterid)
		                 	          			}
			                 	          		$scope.historyarray="0";
			                 	          		$scope.alertsarray="1";
			                 	          		$scope.historyshow={value:true};
			                 	          		$scope.alertshow={value:true};
			                 	          		if(key==data.length-1)
		                 	          			{
		                 	          			console.log("calling update nodes from reload inif")
		                 	          			updateAlerts($rootScope.selectedtrackarraylabels_neo)
				                             	$scope.updateNodes();
		                 	          			}
			                 	          		}
			                 	          		else
			                 	          			{
			                 	          			console.log("calling update nodes from reload inelse")
			                 	          			updateAlerts($rootScope.selectedtrackarraylabels_neo)
					                             	$scope.updateNodes();
			                 	          			}
			                 	          		
			                             	});
			                             	
			                             	
			                            
			                 	});
								 
								
								 
								 
								 }
						 }, $rootScope.refreshFrequency, 0, false);
					
					 }
			 if($rootScope.countvar_neo==0){
				 $scope.reloadData()
			 }
			 
			 $scope.setRefreshFrequency=function(frequency)
			 {
				 $rootScope.refreshFrequency=frequency*60*1000
				 $interval.cancel(promise)
				 //$rootScope.countvar_neo=0;
				 $scope.reloadData()
				 
			 }
			 function initdata(){
			$scope.searchTerm = "";
			
            /* neo4jService.getscmdata().success(
                          function(data) {
                                
                                $scope.jobdetails=data
                                console.log(data);
                               
                               });
			 console.log($scope.jobdetails); */
			var resu = dataservices.flowdiagram($scope.flowCollection).success(
                    function(data) {
                    	
                    	console.log("node data");              
                    	console.log(data);     	
                    	$scope.data={"edges":[],"nodes":[],"selectedNodes":[]}
                    	
                    	angular.forEach(data[0].edges, function(value, key) {
                    		if(value.data.edge_fixed==true)
                			{
                    			$scope.data.edges.push(value)
                			}
                    	})
                    	angular.forEach(data[0].nodes, function(value, key) {
                    		if(value.node_fixed==true)
                    			{
                    			
                    			$scope.data.nodes.push(value)
                    			$scope.data.selectedNodes.push(value)
                    			var xx={'label':value.data.component_name,'category':'node'}
                        		var yy={'label':value.data.track_name,'category':'track'}
                        		 availableTags.push(xx)
                        		 availableTags.push(yy)
                        		 $scope.availableTags.push(xx);
                        		$scope.availableTags.push(yy);
                        		if($scope.trackarray.map(function(e){ return e.label}).indexOf(yy.label)<0)
                        			{
                        			if($rootScope.selectedtrackarraylabels_neo.length>0 && $rootScope.selectedtrackarraylabels_neo.indexOf(yy.label)>-1)
                    				{
                    				$scope.trackarray.push({'label':yy.label,selected:true});
                    				}
                    			else
                    				{
                    				$scope.trackarray.push({'label':yy.label});
                    				}
                        			}
                        		
                        		$scope.availableTags=uniq( $scope.availableTags, 'label');
                        		  availableTags=uniq( availableTags, 'label');
                        		    if(key==data[0].length-1){
                        		    	$("#searchTerm").catcomplete({
                        			        delay: 0,
                        			source: function(request, response) {
                        			var results = $.ui.autocomplete.filter(availableTags, request.term);

                        			response(results.slice(0, 10));
                        			
                        			}
                        			});
                        			

                    				}
                    			}
               
                    	
                    	});
                    	
                    	console.log("nodes data from value");
                    	console.log($scope.data.nodes);
                    	var resu = dataservices.alertsdataaction($scope.alertCollection).success(
                                function(data) {
                                	 $scope.alerts_filtered=[];
                                	 $scope.alerts=[];
                                	 if(data.length==0)
                                     {
                                     $scope.updateNodes();
                                     }
                                	angular.forEach(data, function(value, key) {
                                		
                    	          		if(value!="No data available"){
                    	          			value.alert_create_date_unx = moment.unix(value.alert_create_date_unx).fromNow();
                    	          			/*if(value.spark_room_name==null)
                 	          				{
                 	          				dataservices.fetchRoomDetails(value.track_id).success(
           			                             function(res) {
           			                            	value.spark_room_name= res[0].spark_room_name;
           			                            	value.spark_room_id= res[0].spark_room_id;
           			                             });
                 	          				}*/
                    	          			
                    	          		$scope.alerts.push(value);
                    	          		$scope.searchStr = "Invalid";
                    	          		console.log($scope.searchStr);
                    	          		$scope.$watch('searchStr',function(newval){
                    	          			console.log("inside watch")
                    	          			console.log(newval);
                    	          		})
                    	          		$scope.AlertComponentArray.push(value.id);
                    	          		if(value.masterid!=null)
                 	          			{
                 	          			$scope.AlertParentComponentArray.push(value.masterid)
                 	          			}
                    	          		$scope.historyarray="0";
                    	          		$scope.alertsarray="1";
                    	          		$scope.historyshow={value:true};
                    	          		$scope.alertshow={value:true};
                    	          		if(key==data.length-1){
                	          				console.log("calling update nodes onload inif")
                	          				updateAlerts($rootScope.selectedtrackarraylabels_neo)
			                             	$scope.updateNodes();
                	          			}
                    	          		}
                    	          		else
                    	          			{
                    	          			console.log("calling update nodes onload inelse")
                    	          			updateAlerts($rootScope.selectedtrackarraylabels_neo)
			                             	$scope.updateNodes();
                    	          			}
                    	          		//console.log("alertarray");
                    	          		//console.log($scope.AlertComponentArray);
                    	          		
                                	});
                                	
                                
                    	
                    	});
                    	
                    	 $scope.refresh=function()
                        	{
                    		 
                    		 $scope.refreshgraph=!$scope.refreshgraph;
                    		
                        	}	
                    		   
                    	
          	
                    	 $scope.highlightnode=function(componentID)
                       	{
                    		 $scope.hiddenBox=componentID;
                    		 
                    		
                       	}
                    	
                    	 $scope.dehighlightnode=function()
                       	{
                    		 $scope.hiddenBox="";
                    		 
                    		
                       	}
                         
                        /* $scope.assigntome = function(assignedto,alertID){
                         	if(assignedto=="" && $scope.historyshow.value==true){
                         		var resu = alertDetailsService.assignsaveaction(alertID).success(
                         	             function(data) {
                         	            	 angular.forEach($scope.alerts_filtered, function(value, key) {
                              	          		if(value.alert_id==alertID){
                              	          			
                              	          			$scope.alerts_filtered[key].alert_assignee=data;
                              	          		}
                                          	});
                         	             	
                         	             });
                         		
                         	}
                         }*/
                    	 
                    	 $scope.assigntome = function(assignedto,alertID){
                    		 console.log("inside assigntome");
                    		 $scope.required_alertid=alertID;
                    		 
                    		 angular.forEach($scope.alerts_filtered, function(value, key) {
                	          		if(value.alert_id==alertID){
                	          			
                	          			$scope.assigned_by=$scope.alerts_filtered[key].assigned_by;
                	          			$scope.assigned_to=$scope.alerts_filtered[key].alert_assignee;
                	          			console.log($scope.assigned_by);
                	          		}
                          	
                          		
                          	});
                    		 
                    		 $("#insertid").hide();
                         	$("#invalidalert").hide();
                         	//alert($scope.alert.alert_assignee.length);
                         	console.log("Value");
                         	console.log($scope.assigned_by);
                         	console.log(assignedto);
                         	
                         	if ($scope.assigned_by==undefined||$scope.assigned_by=="")
                         		{
                         		 $("#unassignbtn").hide();
                         		}
                         	else if($scope.assigned_by!=$rootScope.user.userId)
                         		{
                         		$("#unassignbtn").hide();
                         		}
                         
                        
                         	else if (assignedto=="")
                         		{
                         			//alert("inside if");
                         			$("#unassignbtn").hide();
                         			
                         			//console.log($rootScope.user);
                         		}
                         	
                         	else 
                         		$("#unassignbtn").show();
                         	$('#insertAssignee').modal("show");
                    		 
                    		 
                          }
                    	 
                    	 $scope.showinputbox=function()
                         {
                         	$("#insertid").show();
                         	
                        
                         }
                         
                         $scope.clickedyes = function(alertID,flag){
                        	 console.log("clicked yes")
                        	 console.log(alertID);
                         	if(flag=="0")
                         		{
                         		 $scope.newassignee="loggeduser";
                         		}
                         	var resu = alertDetailsService.assignsaveaction(alertID,$scope.newassignee).success(
                    	             function(data) {
                    	            	if(data!="No data available"){
                    	            		angular.forEach($scope.alerts_filtered, function(value, key) {
                               	          		if(value.alert_id==alertID){
                               	          			
                               	          			var res=data.split(" ");
                               	          			$scope.alerts_filtered[key].alert_assignee=res[0];
                               	          			$scope.alerts_filtered[key].assigned_by=res[1];
                               	          			
                               	          		}
                                           	});
                    	            	$('#insertAssignee').modal("hide");
                    	            	}
                    	             });
                         }
                         
                         $scope.addassignee= function(alertID,newassignee)
                         {
                         	//alert(newassignee);
                         	$("#invalidalert").hide();
                         	//$('#insertAssignee').modal("hide");
                         	
                         	if (newassignee=="")
                         		{
                         		$("#invalidalert").show();
                         		}
                         	var resu = alertDetailsService.verifyuser(newassignee).success(
                       	             function(data) {
                         	            	var ret_val=data;
                         	            	if(ret_val=="true")
                         	        		{
                         	        		  $scope.newassignee=newassignee;
                         	        		  $scope.clickedyes(alertID,1);
                         	        		  $("#insertAssignee").modal("hide");
                         	        		}
                         	        	if(ret_val=="false")
                         	        		{
                         	        		 $("#invalidalert").show();
                         	        		}
                         	             });
                         	
                         	
                         }
                         
                        
                         $scope.unassign=function (alertID)
                         {
                         	$scope.newassignee="unassigned";
                         	$scope.clickedyes(alertID,1);
                         	
                         }
                        
                         $scope.clickedno = function(){
                         	$('#insertAssignee').modal("hide")
                         }
                    	 
                    	 
                        
                        /* $scope.layout = {
                   				"name" : "preset"
                   			};
                         $scope.styles = [ {
                 			"selector" : "node[masterid !='null']",
                			"css" : {
                				
                				'display':'none'
                			}
                		},{
               				"selector" : "node",
               				"css" : {
               					"width" : "mapData(weight,1,3, 20, 60)",
               					"height" : "mapData(weight,1,3, 20, 60)",
               					'content' : 'data(component_name)',
               					'text-valign' : 'top',
               					'color' : 'white',
               					'background-color': '#1bbc9b',
               					'text-wrap' : 'wrap',
               					'text-max-width' : 80,
               					'text-outline-width' : 0,
               					'text-outline-color' : '#888',
               					'min-zoomed-font-size' : 8,
               					'font-size' : "mapData(weight,1,3, 8, 16)"
               				}
               			}, {
               				"selector" : "edge",
               				"css" : {
               					'min-zoomed-font-size' : 10,
               					'font-size' : 8,
               					'color' : '#fff',
               					'line-color' : '#e1e5ec',
               					'width' : 2,
               					'curve-style' : 'haystack',
               					'haystack-radius' : 0,
               					'opacity' : 0.5,
               					'mid-target-arrow-shape':'triangle',
               					'mid-target-arrow-fill':'hollow'
               					
               				}
               			}, {
               				"selector" : ":selected",
               				"css" : {
               					'background-color' : '#f2f2f2',
               					'line-color' : 'black',
               					'source-arrow-color' : 'black'
               				}
               			}, 
               		 {
               				"selector" : ".selected",
               				"css" : {
               					'background-color' : '#f2f2f2',
               					'line-color' : 'black',
               					'source-arrow-color' : 'black'
               				}
               			}, {
               				"selector" : ".faded",
               				"css" : {
               					'opacity' : 0.25,
               					'text-opacity' : 0
               				}
               			},
               			{
               				"selector" : "node[alert = 'N']",
               				"css" : {
               					'min-zoomed-font-size' : 4,
               					'font-size' : "mapData(weight,1,3, 12, 16)",
               					'background-color' : '#1bbc9b',
               					'line-color' : 'black',
               					'source-arrow-color' : 'black'
               				}
               			},
               			
               			{
               				"selector" : "node[alert = 'Y']",
               				"css" : {
               					'min-zoomed-font-size' : 4,
               					'font-size' : "mapData(weight,1,3, 12, 16)",
               					'background-color' : '#e43a45',
               					'line-color' : 'black',
               					'source-arrow-color' : 'black'
               				}
               			},
               			{
               				"selector" : ".alerted",
               				"css" : {
               					'min-zoomed-font-size' : 4,
               					'font-size' : "mapData(weight,1,3, 12, 16)",
               					'background-color' : '#f1c40f',
               					'line-color' : 'black',
               					'source-arrow-color' : 'black'
               				}
               			},
               			{
               				"selector" : ".highlighted",
               				"css" : {
               					'min-zoomed-font-size' : 4,
               					'font-size' : "mapData(weight,1,3, 8, 16)"

               				}
               			} ,
               			{
               				"selector" : ".searched",
               				"css" : {
               					'min-zoomed-font-size' : 4,
               					'font-size' : "mapData(weight,1,3, 12, 16)",
               					'color':'red'

               				},
               				
               			},
              			{
              				"selector" : ".incomer",
              				"css" : {
              					'background-color' : '#049fd9',
              					'line-color' : '#049fd9',
              					'source-arrow-color' : '#049fd9'

              				},
              				
              			},
              			{
              				"selector" : ".outgoer",
              				"css" : {
              					'background-color' : '#abc123',
              					'line-color' : '#abc123',
              					'source-arrow-color' : '#abc123'

              				},
              				
              			},{
              				"selector" : "node[parent]",
              				"css" : {
              					'min-zoomed-font-size' : 4,
              					'font-size' : "mapData(weight,1,3, 12, 16)",
              					'background-color' : '#ffffff',
              					'line-color' : 'black',
              					'source-arrow-color' : 'black'
              				}
              			}];*/
               			
                       
                  		
                    	
                    });}
			/* $scope.styles = [ {
  				"selector" : "node",
  				"css" : {
  					"width" : "mapData(weight,1,3, 20, 60)",
  					"height" : "mapData(weight,1,3, 20, 60)",
  					'content' : 'data(component_name)',
  					'text-valign' : 'top',
  					'color' : 'white',
  					'background-color': '#1bbc9b',
  					'text-wrap' : 'wrap',
  					'text-max-width' : 80,
  					'text-outline-width' : 0,
  					'text-outline-color' : '#888',
  					'min-zoomed-font-size' : 8,
  					'font-size' : "mapData(weight,1,3, 8, 16)"
  				}
  			}, {
  				"selector" : "edge",
  				"css" : {
  					'min-zoomed-font-size' : 10,
  					'font-size' : 8,
  					'color' : '#fff',
  					'line-color' : '#e1e5ec',
  					'width' : 2,
  					'curve-style' : 'haystack',
  					'haystack-radius' : 0,
  					'opacity' : 0.5,
  					'mid-target-arrow-shape':'triangle',
  					'mid-target-arrow-fill':'hollow'
  					
  				}
  			}, {
  				"selector" : ":selected",
  				"css" : {
  					'background-color' : '#f2f2f2',
  					'line-color' : 'black',
  					'source-arrow-color' : 'black'
  				}
  			},
  			 {
  				"selector" : ".selected",
  				"css" : {
  					'background-color' : '#f2f2f2',
  					'line-color' : 'black',
  					'source-arrow-color' : 'black'
  				}
  			},{
  				"selector" : ".faded",
  				"css" : {
  					'opacity' : 0.25,
  					'text-opacity' : 0
  				}
  			},
  			
  			{
  				"selector" : "node[alert = 'Y']",
  				"css" : {
  					'min-zoomed-font-size' : 4,
  					'font-size' : "mapData(weight,1,3, 12, 16)",
  					'background-color' : '#e43a45',
  					'line-color' : 'black',
  					'source-arrow-color' : 'black'
  				}
  			},
  			{
   				"selector" : "node[alert = 'N']",
   				"css" : {
   					'min-zoomed-font-size' : 4,
   					'font-size' : "mapData(weight,1,3, 12, 16)",
   					'background-color' : '#1bbc9b',
   					'line-color' : 'black',
   					'source-arrow-color' : 'black'
   				}
   			},
  			{
  				"selector" : ".alerted",
  				"css" : {
  					'min-zoomed-font-size' : 4,
  					'font-size' : "mapData(weight,1,3, 12, 16)",
  					'background-color' : '#f1c40f',
  					'line-color' : 'black',
  					'source-arrow-color' : 'black'
  				}
  			},{
  				"selector" : ".highlighted",
  				"css" : {
  					'min-zoomed-font-size' : 4,
  					'font-size' : "mapData(weight,1,3, 8, 16)"

  				}
  			} ,
  			{
  				"selector" : ".searched",
  				"css" : {
  					'min-zoomed-font-size' : 4,
  					'font-size' : "mapData(weight,1,3, 12, 16)",
  					'color':'red'

  				},
  				
  			},
  			{
  				"selector" : ".incomer",
  				"css" : {
  					'background-color' : '#049fd9',
  					'line-color' : '#049fd9',
  					'source-arrow-color' : '#049fd9'

  				},
  				
  			},
  			{
  				"selector" : ".outgoer",
  				"css" : {
  					'background-color' : '#abc123',
  					'line-color' : '#abc123',
  					'source-arrow-color' : '#abc123'

  				},
  				
  			}];
  			*/
  			
			/*$scope.layout = {
     				"name" : "preset"
     			};*/
     		
			$scope.poorFunction = function(e) {
				
				
			};
			
			
			$scope.alertfn= function(){
				
				$scope.$apply()
			}
			$scope.showalerts=function(element) {
				//alert(element);
				$("#homeclickneo").click();
				$("#menuclickneo").mouseout();
            	console.log(element);
            	var componentID = element;
            	$scope.history=[]
         		var resu = alertDetailsService.alerthistoryaction(componentID).success(
        	             function(data) {
        	            	 angular.forEach(data, function(value, key) {
                         		
             	          		if(value!="No data available"){
             	          			value.alert_create_date_unx = moment.unix(value.alert_create_date_unx).fromNow();
             	          		$scope.history.push(value);	
             	          		$scope.historyarray="1";
             	          		$scope.alertsarray="0";
             	          		$scope.historyshow={value:false};
             	          		$scope.alertshow={value:true};
             	          		}
             	          		
                         	});
                        	
        	             });
            	
            }
			
			$scope.showupstream=function(element) {
				
				//alert(element);
           	 /*$(".highlightable").removeClass("selected");
           	 $(".highlightable").removeClass("outgoer");
           	 $(".highlightable").removeClass("incomer");*/
           	// element.removeClass('highlighted')
           	element.addClass('selected')
           	
           	element.predecessors().addClass(
				'incomer');
           	updateStyle($rootScope.selectedtrackarraylabels_neo)
           	//element.addClass('highlightable')
           	/*element.predecessors().addClass(
				'highlightable');*/
           }
			
			$scope.showdownstream= function(element) {
            	/* $(".highlightable").removeClass("selected");
           	 $(".highlightable").removeClass("outgoer");
           	 $(".highlightable").removeClass("incomer");*/
           	 //element.removeClass('highlighted')
           	element.addClass('selected')
           	element.successors().addClass(
				'outgoer');
           	updateStyle($rootScope.selectedtrackarraylabels_neo)
           	//element.addClass('highlightable')
           	/*element.successors().addClass(
				'highlightable');*/
           }
			
		   $scope.kpidetails= function(element) {
			   $scope.selectedNodeCode= element.data().component_name;
			    
	         	  $scope.selected_component_code=element.data().component_code;
	         	 if($scope.selected_component_code!=null)
	         		   var comp_code=$scope.selected_component_code.toString();
	         	 neo4jService.getNeoData(comp_code).success( function(data)
	         			{
	         		       console.log(data);
	         		       $timeout(function(){$scope.jobdetails=data;});
	         		       
	         			});
        	  /*console.log(element.data())
        	  $timeout(function(){
        		  $scope.selectedNodeCode= element.data().component_name;
        		  $scope.kpi_details_for_node=element.data().kpi_data
        	  })*/
        	  $scope.kpi_details_for_node=[]
        	  console.log(element.data())
        	 KPIDetails.getKPIdetails({track_name: element.data().track_name,collection: $scope.flowCollection,"node_id":element.data().id}).then(function(res){
        		 console.log(res.data[0].kpi_data)
        		 $scope.kpi_details_for_node=res.data[0].kpi_data
        			//$scope.components=res.data;
        			
        			
        		},function(){
        			console.log("getting request failed");
        		});
        	  console.log($scope.kpi_details_for_node)
        	 /* neo4jService.getjobdata(element.data().component_name).success(
                         function(data) {
                        	 console.log(data[0])
                        	 $scope.jobdetails=data[0];
                        	 
                        	 $scope.jobdetails.concurrent_program = $scope.jobdetails.concurrent_program.filter( onlyUnique );
                        	 
                         	});*/
        	  
        	 /*  $scope.jobdetails={}
        	  var doc ={"it_service_owner":"Marryat Des",
        		  		"support_manager":"Broughton, Mark",
        		  		"service_exceutive":"Sesh Tirumala",
        		  		"service_hier":"Supply Chain Management",
        		  		"support_director":"Liem, Stephen A"
        		  		}
        	  $scope.jobdetails=doc*/
        	 /* $scope.jobdetails={}
        	  neo4jService.getscmdata().success(
                         function(data) {
                        	 
                        	 $scope.jobdetails=data
                        	
                         	});*/
          	$('#detailModal').modal('show');
          }
            $scope.contextMenu = [{
                content: 'Show Alert History',
                select: function(element) {
                	//console.log(element.id())
                	var componentID = element.id()
                	$scope.history=[]
             		var resu = alertDetailsService.alerthistoryaction(componentID).success(
            	             function(data) {
            	            	 angular.forEach(data, function(value, key) {
                             		
                 	          		if(value!="No data available"){
                 	          			value.alert_create_date_unx = moment.unix(value.alert_create_date_unx).fromNow();
                 	          		$scope.history.push(value);	
                 	          		$scope.historyarray="1";
                 	          		$scope.alertsarray="0";
                 	          		$scope.historyshow={value:false};
                 	          		$scope.alertshow={value:true};
                 	          		}
                 	          		
                             	});
                            	
            	             });
                	
                },
              }, {
                content: 'Show Downstream',
                select: function(element) {
                	/* $(".highlightable").removeClass("selected");
                	 $(".highlightable").removeClass("outgoer");
                	 $(".highlightable").removeClass("incomer");*/
                	 //element.removeClass('highlighted')
                	element.addClass('selected')
                	element.successors().addClass(
					'outgoer');
                	updateStyle($rootScope.selectedtrackarraylabels_neo)
                	//element.addClass('highlightable')
                	/*element.successors().addClass(
					'highlightable');*/
                },
              },{
                content: 'Show Upstream',
                select: function(element) {
                	 /*$(".highlightable").removeClass("selected");
                	 $(".highlightable").removeClass("outgoer");
                	 $(".highlightable").removeClass("incomer");*/
                	// element.removeClass('highlighted')
                	element.addClass('selected')
                	element.predecessors().addClass(
					'incomer');
                	updateStyle($rootScope.selectedtrackarraylabels_neo)
                	//element.addClass('highlightable')
                	/*element.predecessors().addClass(
					'highlightable');*/
                },
              },{
                  content: 'KPI Details',
                  select: function(element) {
                	  $scope.selectedNodeCode= element.data().component_name;
                	  /*console.log(element.data())
                	  $timeout(function(){
                		  $scope.selectedNodeCode= element.data().component_name;
                		  $scope.kpi_details_for_node=element.data().kpi_data
                	  })*/
                	  $scope.kpi_details_for_node=[]
                	  console.log(element.data())
                	 KPIDetails.getKPIdetails({track_name: element.data().track_name,collection: $scope.flowCollection,"node_id":element.data().id}).then(function(res){
                		 console.log(res.data[0].kpi_data)
                		 $scope.kpi_details_for_node=res.data[0].kpi_data
                			//$scope.components=res.data;
                			
                			
                		},function(){
                			console.log("getting request failed");
                		});
                	  console.log($scope.kpi_details_for_node)
                	  /*neo4jService.getjobdata(element.data().component_name).success(
	                             function(data) {
	                            	 console.log(data[0])
	                            	 $scope.jobdetails=data[0];
	                            	 
	                            	 $scope.jobdetails.concurrent_program = $scope.jobdetails.concurrent_program.filter( onlyUnique );
	                            	 
	                             	});
	                             
                	  */
                	  
                	  
                  	$('#detailModal').modal('show');
                  },
                }];
            
           
            
		} ]);