BCCapp.controller('dataEntryController', [ '$scope','KPIDetails','flowGraph','dataservices','alertDetailsService','$state','$timeout','dataEntryService','neo4jService',
                                 		function($scope, KPIDetails, flowGraph,dataservices,alertDetailsService,$state,$timeout,dataEntryService,neo4jService) {
	$scope.fetchTrackflowCounter=0
	$scope.activeTracks=[]
	$scope.data={nodes:[],edges:[]}
	$scope.allParents=[]
	$scope.savePubClicked=0;
	KPIDetails.DBnames({}).then(function(data){
		$scope.DBnames=data;
	});
	$scope.fetchTrackflow=function(track_name){
		console.log(track_name)
		
		var indextrack=$scope.activeTracks.indexOf(track_name)
		if(indextrack>-1)
			{
			
			$scope.activeTracks.splice(indextrack, 1);
			var dataTemp=[]
			angular.forEach($scope.data.nodes,function(value_node,key_node){
				 
				if(value_node.data.track_name!=track_name ||value_node.data.neo_node_on_ui==undefined)
					{
					dataTemp.push(value_node)
					}
				
				else {
					$scope.fetchTrackflowCounter--
				}
				if(key_node==$scope.data.nodes.length-1)
					{
					$scope.data.nodes=dataTemp
					//$scope.updateStyle("")
					$scope.layout = {
		      				"name" : "preset"
		      			};
					
					}
			
			})
			
			return
			}
		else
			{
			
			$scope.activeTracks.push(track_name)
			
			}
	
		neo4jService.getTrackflow(track_name,$scope.fetchTrackflowCounter).success(function(data){
			console.log(data)
			if(data.error){
				alert(data.error)
			}
			else if(data.nodes.length==0)
				{
				alert("No Data Available")
				}
			
			console.log($scope.fetchTrackflowCounter)
			angular.forEach(data.nodes,function(value_node,key_node){
				
				$scope.data.nodes.push(value_node)	
				
				$scope.fetchTrackflowCounter++;
				if(key_node==data.nodes.length-1)
					{
					angular.forEach(data.edges,function(value_edge,key_edge){
						$scope.data.edges.push(value_edge)
						if(key_edge==data.edges.length-1)
							{
							$scope.updateStyle("")
							$scope.layout = {
				      				"name" : "preset"
				      			};
							}
					})
					}
			})
			
			
			
		})
		
	}
	
	
	$scope.updateStyle=function(selectedTrack)
	{
		
		console.log("in update style")
		
		var temp_styles = [{
			"selector" : "node[masterid !='null']",
			"css" : {
				
				'display':'none'
			}
		}, {
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
			}, /*{
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
			},*/{
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
				
			},{
				"selector" : "node[parent]",
				"css" : {
					'min-zoomed-font-size' : 4,
					'font-size' : "mapData(weight,1,3, 12, 16)",
					'background-color' : '#ffffff',
					'line-color' : 'black',
					'source-arrow-color' : 'black'
				}
			},{
				"selector" : ".selectedSecondLevelNode",
				"css" : {
					'min-zoomed-font-size' : 4,
					'font-size' : "mapData(weight,1,3, 12, 16)",
					'background-color' : '#E87E04',
					'line-color' : 'black',
					'source-arrow-color' : 'black',
					'border-width':'1px',
					'border-color':'white'
				}
			},
			{
				"selector" : "node[parentNode = 'yes']",
				"css" : {
					'min-zoomed-font-size' : 4,
					'font-size' : "mapData(weight,1,3, 12, 16)",
					'background-color' : '#2f353b',
					//'background-color' : 'red',
					'line-color' : 'black',
					'source-arrow-color' : 'black',
					'border-width':'1px',
					'border-color':'white'
				}
			}
			];
		
	
		
		
	
		if(selectedTrack!="")
			{
			temp_styles.push(
					{
						"selector" : "node[track_name != '"+selectedTrack+"']",
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
		
		$timeout(function(){
			console.log(temp_styles)
			$scope.styles=temp_styles
		})
		

	}
	$scope.count = 0;
	$scope.maxid=0;
	$scope.selectednode=null;
	$scope.selectednodeX=0;
	$scope.selectednodeY=0;
	$scope.starttime=[]
	$scope.nodeids=[];
	$scope.saved="yes";
	$scope.starttime[0]=new Date()
	$scope.obj={opened:[false]}
    $scope.openCalendar = function(e, index) {
     e.preventDefault();
         e.stopPropagation();

         $scope.obj.opened[index] = true;
         
    };
    neo4jService.fetchTracks().success(function(data){
    	$scope.neo_tracks=data
    	
    	
    })
	dataEntryService.fetchMaxNodeID().success( function(data){
		$scope.maxid=data;
		
			/*var num2=parseInt(data)+1
			var num2=parseInt(data)+1
			num2=num2.toString()
			var leading=(4-num2.length)
			str=Array(leading+1).join("0")+num2*/

	})
	kpi_data=[];
	NodeModel={};
	PositionModel={};
	Node={};
	$scope.newNodes=[];
	$scope.source= null;
	$scope.changed=0;
	clearAll=function(){
		for(i=0;i<=$scope.count;i++)
		{
		$scope.criticalThreshold=[];
		$scope.mediumThreshold=[];
		$scope.highThreshold=[];
		$scope.kpiName=[];
		$scope.kpiStatus=[];
		$scope.skmsId=[];
		$scope.query=[];
		$scope.database=[];
		$scope.frequency=[];
		$scope.starttime=[];
		
		}
		$scope.weight=null;$scope.trackName= null;
		
	}
	 $scope.saveNodeData= function(){
		for(i=0;i<=$scope.count;i++)
			{
			threshold={Critical:$scope.criticalThreshold[i],Medium:$scope.mediumThreshold[i],High:$scope.highThreshold[i]};
			kpi_data.push({kpi_name:$scope.kpiName[i],kpi_status:$scope.kpiStatus[i],skms_id:$scope.skmsId[i],threshold:threshold,query:$scope.query[i],database:$scope.database[i]})
			}
     	//userdetails.savecalldetail(calldetails);
		
		NodeModel={component_name:$scope.componentName,weight:$scope.weight,track_name:$scope.trackName,kpi_data:kpi_data,id:$scope.selectednode}
		PositionModel ={x:$scope.selectednodeX,y:$scope.selectednodeY}
		Node={data:NodeModel,position:PositionModel};
		$scope.data.nodes.push(Node)
		var resu= dataEntryService.savenodedetails(Node).success(
                function(data) {
                	kpi_data=[];
                	NodeModel={};
                	PositionModel={};
                	Node={};
                	$scope.count=0;
                	$('#space-for-buttons-node').empty();
                	$('#nodeModal').modal('hide');
                	$scope.changed=$scope.componentName;
                	clearAll();
                });
	 }
	 $scope.saveKPIData= function(){
			for(i=0;i<=$scope.count;i++)
				{
				console.log(i)
				console.log($scope.count)
				threshold={Critical:$scope.criticalThreshold[i],Medium:$scope.mediumThreshold[i],High:$scope.highThreshold[i]};
				kpi_data.push({kpi_name:$scope.kpiName[i],kpi_status:$scope.kpiStatus[i],skms_id:$scope.skmsId[i],threshold:threshold,query:$scope.query[i],database:$scope.database[i],nextRunTime:$scope.starttime[i].getTime(),frequency:$scope.frequency[i]})
				if(i==$scope.count)
					{
					NodeModel={kpi_data:kpi_data,id:$scope.selectednode}
					
					var resu= dataEntryService.savekpidetails(NodeModel).success(
			                function(data) {
			                	kpi_data=[];
			                	NodeModel={};
			                	$scope.count=0;
			                	$('#space-for-buttons-kpi').empty();
			                	$('#kpiModal').modal('hide');
			                	clearAll();
			                });
					console.log(NodeModel)
					}
				
				}
	     	//userdetails.savecalldetail(calldetails);
			
			
		 }
                                 $scope.alerts=[];
                                 $scope.AlertComponentArray=[];
                                 $scope.data=[];
                                 console.log("a")
                                 $scope.refreshgraph=false;
                                 var availableTags=[];
                                 $scope.availableTags=[];
                                 $scope.alertshow={value:false};
                                 			$scope.$on('$viewContentLoaded', function() {
                                 				// initialize core components
                                 				App.initAjax();
                                 			});
                                 			 function uniq(a, param){
                                 			        return a.filter(function(item, pos, array){
                                 			            return array.map(function(mapItem){ return mapItem[param]; }).indexOf(item[param]) === pos;
                                 			        })
                                 			    }

                                 			    
                                 			$scope.searchTerm = "";
                                 			$scope.flowDiagram_function=function(data)
                                 			{
                                 				
                                 				
                                 					$scope.backup_data = data[0];
                                 				$scope.data=data[0];
                                 				console.log($scope.data)
                                             	angular.forEach($scope.data.nodes, function(value, key) {
                                             		console.log(value.data.masterid)
                                    				if((value.data.masterid!="null") && $scope.allParents.indexOf(value.data.masterid)<0)
                                    				 {
                                    				 $scope.allParents.push(value.data.masterid);
                                    				 console.log("hey")
                                    				 console.log(value.data.masterid)
                                    				 
                                    				 }
                                             		var xx={'label':value.data.component_name,'category':'node'}
                                             		var yy={'label':value.data.track_name,'category':'track'}
                                             		 availableTags.push(xx)
                                             		 availableTags.push(yy)
                                             		 $scope.availableTags.push(xx);
                                             		$scope.availableTags.push(yy);
                                             		$scope.availableTags=uniq( $scope.availableTags, 'label');
                                             		  availableTags=uniq( availableTags, 'label');
                                             		    if(key==$scope.data.nodes.length-1){
                                             		    	$("#searchTerm").catcomplete({
                                             			        delay: 0,
                                             			source: function(request, response) {
                                             			var results = $.ui.autocomplete.filter(availableTags, request.term);

                                             			response(results.slice(0, 10));
                                             			
                                             			}
                                             			});
                                             			

                                         				}
                                             	
                                             	});
                                             	angular.forEach($scope.backup_data.nodes, function(value, key) {
                                                  	$scope.nodeids.push(value.data.id)
                                              	 });
                                             	
                                             	$scope.selectedValue = function (selected) {
                                             	  
                                             	    $scope.searchTerm= selected.title;
                                                     var nodef =  $scope.searchTerm;
                                                    
                                                     $scope.updateStyle("")
                                            			
                                                      $scope.layout = {
                                               				"name" : "preset"
                                               			};
                                                    
                                                      
                                             	};

                                             	 $('#searchTerm').keypress(function(e){ //when key is pressed on query text box

                                             		
                                               		if(e.which == 13){            //if the pressed key is enter
                                               		
                                               			
                                                        /* $scope.searchTerm= $('#searchTerm').val();
                                                        var nodef =  $scope.searchTerm;
                                                        $scope.styles = [ {
                                               				"selector" : "node",
                                               				"css" : {
                                               					"width" : "mapData(weight,1,3, 20, 60)",
                                               					"height" : "mapData(weight,1,3, 20, 60)",
                                               					'content' : 'data(name)',
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
                                               				"selector" : "node[name = '"+nodef+"']",
                                               				"css" : {
                                               					'min-zoomed-font-size' : 4,
                                               					'font-size' : "mapData(weight,1,3, 12, 16)",
                                               					'background-color' : '#f1c40f',
                                               					'line-color' : 'black',
                                               					'source-arrow-color' : 'black'
                                               				}
                                               			},
                                               			{
                                               				"selector" : "node[track = '"+nodef+"']",
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
                                               			
                                                         $scope.layout = {
                                                  				"name" : "preset"
                                                  			};
                                                       

                                                         $(".ui-menu-item").hide();
                                                         $(".ui-autocomplete-category").hide();
                                                         */
                                               		}
                                               }) 
                                             	
                                             	 $scope.refresh=function()
                                                 	{
                                             		 
                                             		 $scope.refreshgraph=!$scope.refreshgraph;
                                             		var resu = dataservices.flowdiagram("bp_flow_neo_data_stage").success(
                                                            function(data) {
                                                            	if(data!=""){
                                                           	 $scope.flowDiagram_function(data);
                                                            	}
                                                            });
                                                 	}	
                                             	 $scope.saveUpdates = function()
                                             	 {
                                             		 console.log("inside save");
                                             		
                                             		$scope.savePubClicked=$scope.savePubClicked+1
	                                             	 for(var i = 0; i < $scope.data.nodes.length; i++) {
																    var obj = $scope.data.nodes[i];
																   
																    if(obj.data.parentNode=="yes") {
																    	$scope.data.nodes.splice(i, 1);
																    }
																    if(obj.data.secondary=="yes") {
																    	$scope.data.nodes.splice(i, 1);
																    }
																}
	                                             		 
                                             		
                                             		dataEntryService.saveFlow({"node":$scope.data.nodes,"edge":$scope.data.edges}).success(function(data){
                                             			console.log("updated flow")
                                             			// $scope.successsave=true;
                                             			
                                                 		 
                                             			var resu = dataservices.flowdiagram("bp_flow_neo_data_stage").success(
                                                                function(data) {                                              
                                                                $('#alertSaveModal').modal('show');
                                                               	 $scope.flowDiagram_function(data);
                                                                });
                                             			
                                             		}).error(function(error)
                                             		{
                                             			$('#alertSaveFailedModal').modal('show');	
                                             		});
                                             		
                                             		
                                             	 }
                                             	 $scope.publishChanges = function()
                                             	 {
                                             		
                                             		 console.log("inside publish");
                                             		$scope.savePubClicked=$scope.savePubClicked+1
                                             		for(var i = 0; i < $scope.data.nodes.length; i++) {
													    var obj = $scope.data.nodes[i];
													   
													    if(obj.data.parentNode=="yes") {
													    	$scope.data.nodes.splice(i, 1);
													    }
													}
                                             		dataEntryService.publishFlow({"node":$scope.data.nodes,"edge":$scope.data.edges}).success(function(data){
                                             			console.log("published flow")
                                             			//  $scope.successpublish=true;
                                             			var resu = dataservices.flowdiagram("bp_flow_neo_data_stage").success(
                                                                function(data) {
                                                                  
                                                                $('#alertPublishModal').modal('show');
                                                               	 $scope.flowDiagram_function(data);
                                                                });
                                             			
                                             		}).error(function(error)
                                             		{
                                             			$('#alertPublishFailedModal').modal('show');	
                                             		});
                                             		
                                             		 
                                             	 }
                                             		   
                                             	
                                   	
                                             	 $scope.highlightnode=function(componentID)
                                                	{
                                             		 $scope.hiddenBox=componentID;
                                             		 
                                             		
                                                	}
                                             	
                                             	 $scope.dehighlightnode=function()
                                                	{
                                             		 $scope.hiddenBox="";
                                             		 
                                             		
                                                	}
                                                  
                                              
                                             	$scope.updateStyle("")
                                        			
                                                  $scope.layout = {
                                           				"name" : "preset"
                                           			};
                                           		
                                 			
                                             }
                                 			var resu = dataservices.flowdiagram("bp_flow_neo_data_stage").success(
                                                     function(data) {
                                                    	 if(data!=""){
                                                    	 $scope.flowDiagram_function(data);
                                                    	 }
                                                     });
                                 			$scope.updateStyle("")
                                   			
                                   			
                                 			$scope.layout = {
                                      				"name" : "preset"
                                      			};
                                      		
                                 			$scope.poorFunction = function(e) {
                                 				
                                 				
                                 			};
                                 			
                                 			
                                 			$scope.alertfn= function(){
                                 				
                                 				$scope.$apply()
                                 			}
                                 			$scope.addnodedetails=function(element) {
                                			 	 $scope.count=0;
                                            	$('#nodeModal').modal('show');
                                            	$scope.selectednode=element.id();
                                            	$scope.selectednodeX=element._private.position.x;
                                            	$scope.selectednodeY=element._private.position.y;
                                            	
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
                                           	
                                           }
                               			
                               			$scope.addnewkpi=function(element) {
                               				
                               				 $scope.count=0;
                                           	 $scope.selectednode=element.id();
                                           	 $('#kpiModal').modal('show');
                                          }
                               			
                               			$scope.connectto= function(element) {
                               				
                               				 console.log(element.id())
                                             	$scope.source=element.id();
                                         
                                          }
                               		

                                             $scope.contextMenu = [
                                                                   {
                                                 content: 'Add Node Details',
                                                 select: function(element) {
                                                	 $scope.count=0;
                                                	$('#nodeModal').modal('show');
                                                	$scope.selectednode=element.id();
                                                	$scope.selectednodeX=element._private.position.x;
                                                	$scope.selectednodeY=element._private.position.y;
                                                	
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
                                               }, 
                                                                   {
                                                 content: 'Add New KPI',
                                                
                                                 select: function(element) {
                                                	 $scope.count=0;
                                                	 $scope.selectednode=element.id();
                                                	 
                                                	 
                                                	// if $scope.selectednode in $scope.nodeids
                                                	 if ($scope.nodeids.indexOf($scope.selectednode) > -1)
                                                	 { $scope.saved ="yes";
                                                	 console.log("yes")
                                                	 } 
                                                		 else
                                                		 { $scope.saved ="no";
                                                    	 console.log("no")
                                                    	 } 
                                                	 $timeout(function(){$('#kpiModal').modal('show');}) 
                                                 },
                                               },{
                                                 content: 'Connect to',
                                                 select: function(element) {
                                                	 console.log(element.id())
                                                 	$scope.source=element.id();
                                                 	
                                                 },
                                               }];
                                             
                                            
                                             
                                 		} ]);
