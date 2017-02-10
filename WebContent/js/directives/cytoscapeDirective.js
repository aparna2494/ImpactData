BCCapp
		.directive(
				'cytoscape',
				function($timeout,secondLevelNodes) {
					return {
						restrict : 'E',
						template : '<div id="cy"></div>',
						replace : true,
						scope : {
							elements : '=',
							styles : '=',
							layout : '=',
							 /*highlightByName: '=',*/
							 highlightById: '=',
							historyShow:'=',
							 /*alertDataNode:'=',*/
							 sampleFunction:'&',
							 refreshGraph:'=',
							// navigatorContainerId: '@',
							contextMenuCommands : '=',
							filterArray:'=',
							showSelectedNodeDetails:'=',
							showAlertFunction:'&',
							showUpstreamFunction:'&',
							showDownstreamFunction:'&',
							kpiDetailsFunction:'&',
							trackarray:'='
							  
						
						},
						link : function(scope, element, attributes, controller,highlightElementById,historyShow,sampleFunction,refreshGraph,filterArray,showSelectedNodeDetails) {
							var cy_dir;
							
							var selected_nodes=[]
							var second_level_nodes=[]
							
							showalertfn=function(node)
							 {
								//alert(node);
								 scope.showAlertFunction({arg1:node});
							 }
							 showupstreamfn=function()
							 {
								 console.log(scope.hoveredNode)
						    
							 scope.showUpstreamFunction({arg1:scope.hoveredNode});
							 }
							showdownstreamfn=function()
							{
							 scope.showDownstreamFunction({arg1:scope.hoveredNode});
							}
							kpidetailsfn=function()
							{
								scope.kpiDetailsFunction({arg1:scope.hoveredNode});
							}
							
							scope.$watch('styles',function(newval,oldval){
								console.log("in watch style directive")
								
								if(cy_dir!=undefined)
									{
									
									cy_dir.style(newval);
									
									}
								
								
							},true)
							/*scope.$watch('filterArray',function(newval,oldval){
								
								console.log(newval)
								
								if(cy_dir!=undefined)
									{
									cy_dir.elements().filter(function(){ return newval.indexOf(this.track_name) >= 0; }).select()
									
									}
								
								
							},true)*/
							scope
									.$watchGroup(
											[ 'layout' ],
											function(newValues, oldValues,
													scope) {
												console.log("in watch layout directive")
												var safe = true;
												for ( var i in newValues)
													{
													
													if (!newValues[i])
														safe = false;
													}
											
												if (safe) {
													var elements = scope.elements;
													console.log("layout watch")
													var styles = scope.styles;
													var layout = newValues[0];
													scope.levelCounter=0
													cy_dir=cytoscape({
														container : element[0],
														elements : elements,
														style : styles,
														layout : layout,
														fit : true,
														boxSelectionEnabled : false,
														motionBlur : false,
														autoungrabify : true,
														wheelSensitivity : 0.2,
														ready : function() {
															var cy = this;
															// Run layout after
															// add new elements
															console.log("in ready")
															var runLayout = function(
																	addedElements) {
																if (addedElements) {
																	layout.maxSimulationTime = 10;
																	layout.fit = false;
																	var addLayout = addedElements
																			.makeLayout(layout);
																	addLayout
																			.pon(
																					'layoutstop')
																			.then(
																					function(
																							event) {
																						layout.maxSimulationTime = 2000;
																						cy
																								.elements()
																								.makeLayout(
																										layout)
																								.run();
																					});
																	addLayout
																			.run();
																}
															};
															// Tap
					/*										cy
																	.on(
																			'mouseover',
																			function(
																					event) {
																				$timeout(
																						function() {
																							scope.selectedElements = cy
																									.$(':selected');
																							if(event.cyTarget.data!=undefined){
																							highlightElement(event.cyTarget.data().id);
																							console.log(event.cyTarget.data().kpi_data);
																				}
																								
																						},
																						10);

																			});*/
															// Mouseout
															cy
																	.on(
																			'mouseout',
																			function() {
																				cy
																						.elements()
																						.removeClass(
																								'mouseover');
																				cy
																						.elements()
																						.removeClass(
																								'edge-related');
																			});
															
															//qtip
															var qtipfn=function(node){
																console.log("qtip function");
																node.qtip({
																	content:{
																		title:'Show Details',
																		
																		text:[																						     
																	      '<div></div><div class="menustyle"><a ng-href="" onClick="showalertfn(\''+node.id()+'\')"  class="anchorstyle"  class="font-grey-mint">Alert Details</a>',
																	         '</div><div class="menustyle"><a ng-href="" onClick="showupstreamfn();"  class="anchorstyle"  class="font-grey-mint">Upstream</a>',
																	         '</div><div class="menustyle"><a ng-href="" onClick="showdownstreamfn();"  class="anchorstyle" class="font-grey-mint">Downstream</a>',
																	         '</div><div class="menustyle"><a ng-href="" onClick="kpidetailsfn();" class="anchorstyle" class="font-grey-mint">KPI Details</a></div><div></div>'
																
																	         
		                                                
																	         ].join('<br />\n')
																	},
																	
									
																	 show: {
																		        delay:200,
																		        event:'directtap' ,
																		         ready: true
																		   },
																     hide: {
																    	     event:'mouseout unfocus',
																    	     fixed: true,
																    	    
																		   },

																position: {
																	       my: 'top center',
																	       at: 'bottom center'
																	      },
															      style: {
																	
															    	 classes: 'qtipstyle',
																	       tip: {
																	               width: 3,
																	               height: 3
																	             }
																	      
																	     }


																		

																		
																	},event);
															}
															//click

															var tappedBefore;
															var tappedTimeout;
															cy.on('click','node', function(event) {
																console.log("st")
															  var tappedNow = event.cyTarget;
															  if (tappedTimeout && tappedBefore) {
															    clearTimeout(tappedTimeout);
															  }
															  if(tappedBefore === tappedNow) {
															    tappedNow.trigger('doubleClick');
															    tappedBefore = null;
															  } else {
															    tappedTimeout = setTimeout(function(){ tappedBefore = null; }, 300);
															    tappedBefore = tappedNow;
															    /*	$timeout(
																function() {
																	scope.selectedElements = cy
																			.$(':selected');
																	if(event.cyTarget.data!=undefined){
																	highlightElement(event.cyTarget.data().id);
														}
																		
																},
																10);*/
														
															    var node=event.cyTarget;
															       scope.hoveredNode=node;
															        //var nodeid=node.id();
																	 var eventIsDirect = event.cyTarget === this;

																     if( eventIsDirect ){
																    	console.log("direct tap");
																    	// console.log(event.cyTarget.id());
																	     qtipfn(node);
																	     
														              }
															    
															    
															 
															  }
															});
															
															cy
															.on(
																'doubleClick',
																'node',
																function(
																		event) {
																	
																	
																	//showing node details
																	
																	var target = event.cyTarget;
																	if (event.cyTarget.data!=undefined)
														        	  {
														        	  console.log("onclick"+event.cyTarget.data().id);
														        	  //showNodeDetails(event.cyTarget.data().id);
														        	  $("#menuclick").click();
														        	  $("#homeclick").mouseout();
														        	  $("#nodetab").show();
														        	  scope.showSelectedNodeDetails(event.cyTarget.data().id);
														        	  }
														          
																	
																	
																	
																	//showing sub process
		var nodeComponentCode=event.cyTarget._private.data.component_code
		scope.selectedTrack=event.cyTarget._private.data.track_name
		
		if(nodeComponentCode==null)
							{
							console.log("inside nodeComponentCode null ")
							
							return
							}

		else if(cy.elements("node[masterid='"+nodeComponentCode+"']").length==0)
			{
			console.log("node[masterid] is null")
			
			return
			}
		else
			{
			if(scope.filterArray.indexOf(scope.selectedTrack)<0)
			{
			scope.filterArray.push(scope.selectedTrack)
			}
			}

		event.cyTarget.addClass('selectedSecondLevelNode')





		//
		if(scope.levelCounter==0)
			{
			cy.fit(cy.elements("node[track_name = '"+scope.selectedTrack+"']"))
			cy.zoom({
				level: 0.5, // the zoom level
				renderedPosition: { x: 100, y: 0 }
						
				});
			}
		else{
			//cy.fit()
			cy.panBy({
				  x: 0,
				  y: -200-(50*cy.zoom())
				});

		}

		scope.levelCounter+=10
		var nodeid=event.cyTarget._private.data.id
		var nodes_second=[]
		console.log("parent"+scope.levelCounter+scope.fetchTrackflowCounter)

		nodes_second.push({
		    "locked" : false,
		    "selected" : false,
		    "group" : "nodes",
		    "classes" : "",
		    "node_fixed" : true,
		    "grabbable" : true,
		    "renderedPosition" : {
		        "y" : 300,
		        "x" : 300
		    },
		    "selectable" : true,
		    "removed" : false,
		    "data" : {
		        "kpi_data" : [ 
		            
		        ],
		        "weight" : 1,
		        "track_id" : 1,
		        "track_name" : scope.selectedTrack,
		        "id" : "parent"+scope.levelCounter+scope.fetchTrackflowCounter,
		        "component_name" : event.cyTarget._private.data.component_name,
		        "masterid":"null",
		        "parentNode":"yes",
		        	"secondary":"yes"
		    }
		})
		//adding parent node
		cy.add(nodes_second)
		 var edges=[]
		edges.push({
		    "data" : {
		        "source" : nodeid,
		        "edge_fixed" : true,
		        "target" : "parent"+scope.levelCounter+scope.fetchTrackflowCounter,
		        "id" : nodeid+"1"+"R"+nodeid+2
		    }
		})
		cy.add(edges)






		cy.elements("node[masterid='"+nodeComponentCode+"']").each(function(i,ele){
			
			//cy.remove("node[id='"+ele._private.data.id+"']")
			nodes_second=[]
		nodes_second.push({
		    "locked" : false,
		    "selected" : false,
		    "group" : "nodes",
		    "classes" : "",
		    "node_fixed" : true,
		    "grabbable" : true,
		    "position" : ele._private.position,
		    "selectable" : true,
		    "removed" : false,
		    "data" : {
		        "kpi_data" : [ 
		            
		        ],
		        "alert":ele._private.data.alert,
		        "weight" : 1,
		        "track_id" : 1,
		        "track_name" : scope.selectedTrack,
		        "id" : "Du"+ele._private.data.id,
		        "component_name" : ele._private.data.component_name,
		        "parent":"parent"+scope.levelCounter+scope.fetchTrackflowCounter	,
		        "secondary":"yes",
		        "masterid":"null"
		    }
		})



			
			




		//adding 1st child node
		cy.add(nodes_second)

		cy.elements("edge[source='"+ele._private.data.id+"']").each(function(i,edg){
				console.log(edg)
				var edges=[]
				edges.push({
				    "data" : {
				        "source" : "Du"+ele._private.data.id,
				        "edge_fixed" : true,
				        "target" : "Du"+edg._private.data.target,
				        "id" : "Du"+ele._private.data.id+"R"+"Du"+edg._private.data.target
				    }
				})
				cy.add(edges)
			})
			
			
			cy.elements("edge[target='"+ele._private.data.id+"']").each(function(i,edg){
				console.log(edg)
				var edges=[]
				edges.push({
				    "data" : {
				        "source" : "Du"+edg._private.data.source,
				        "edge_fixed" : true,
				        "target" : "Du"+ele._private.data.id,
				        "id" : "Du"+edg._private.data.source+"R"+"Du"+ele._private.data.id
				    }
				})
				cy.add(edges)
			})


		})

		console.log("updating style frm addnode")


		
																});

															
															
															
														/*	cy
															.on(
																'click',
																'node',
																function(
																		event) {
																	
																	scope.styles.push({
			"selector" : "node[parent]",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#ffffff',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		})
		console.log(event.cyTarget._private.data.track_name)
		if(scope.filterArray.indexOf(event.cyTarget._private.data.track_name)<0)
			{
			scope.filterArray.push(event.cyTarget._private.data.track_name)
			}
		filterArray.push(event.cyTarget._private.data.track_name)
		console.log(scope.filterArray)
		event.cyTarget.addClass('selectedSecondLevelNode')
		scope.styles.push({
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
		})
		console.log(scope.styles)
		var position_x=cy.$('#'+event.cyTarget._private.data.id).position().x
		var position_y=cy.$('#'+event.cyTarget._private.data.id).position().y
		var position={'x':position_x,'y':position_y}
		
		var doc={'id':event.cyTarget._private.data.id,'position':position}
		
		selected_nodes.push(doc)
		
		
		secondLevelNodes.getNodes(event.cyTarget._private.data.id,position_x,position_y).success(function(data){
			
																	console.log(data)
																	angular.forEach(data.node,function(value,key)
																			{
																		
																		var addedElements = cy.add(value);
																	
																		//cy.center()
																		//scope.onChange(cy,null);
																			})
																			angular.forEach(data.edge,function(value,key)
																			{
																		var addedElements = cy.add(value);
																		//cy.center()
																		//scope.onChange(cy,null);
																			})
																			
																	//highlight(target);
																	//showNodeInfo(target);
																})
		
																
																			
																	//highlight(target);
																	//showNodeInfo(target);
																});*/

															// Mouseover
															// Mouseover
															cy
																	.on(
																			'tap',
																			function(
																					event) {
																				
																				console.log('tap event')
																				
																					scope.historyShow.value=true;
																					
																				scope.sampleFunction()
																				var target = event.cyTarget;
																				if (target != event.cy) {
																					/*target
																							.addClass('mouseover');
																					target
																							.neighborhood()
																							.edges()
																							.addClass(
																									'edge-related');*/
																					
																					
																				}
																				else
																					{
																					console.log("in else tap")
																					clear();
																					$('#homeclick').click()
																					$("#menuclick").mouseout();
																					$('#home').show()
																					cy.layout()
																					scope.levelCounter=0
																					var index = scope.filterArray.indexOf(scope.selectedTrack);
																					
																					var trackarrayindex=-1
																					
																					angular.forEach(scope.trackarray,function(value,key){
																						console.log(value)
																						if(value.label==scope.selectedTrack && value.selected==true)
																							{
																							trackarrayindex=key
																							}
																						if(key==scope.trackarray.length-1)
																							{
																							
																							if(index > -1 &&trackarrayindex<0)
																							{
																								
																								$timeout(function(){
																									scope.filterArray.splice(index, 1);
																								})
																							}
																							
																							}
																					})
																					
																					var removed=(cy.remove(cy.elements("node[secondary= 'yes']")))
																					if(removed.length!=0)
																						{
																						
																						angular.forEach(selected_nodes,function(value,key){
																							
																							cy.$('#'+value.id).position(value.position);
																							
																							console.log(cy.$('#'+value.id).position())
																							if(key==selected_nodes.length-1)
																								{
																							
																							selected_nodes=[]
																								}
																							
																						})
																						
																						}
																					
																					//scope.onChange(cy,null);
																					
																					//scope.onChange(cy,null);
																					}
																				
																			//
																				
																			});

														/*	cy
																	.on(
																		'mouseover', 'node',
																		function(
																				event) {
																			scope.alertDataNode = true;
																			var alertDetailstarget = "alertDetails";
																			var target = event.cyTarget;
																		//	highlight(target);
																			highlightAlertNodeInfo(alertDetailstarget);
																		});
															cy
																	.on(
																	'mouseout', 'node',
																	function(
																			event) {
																		scope.alertDataNode = false;
																		var alertDetailstarget = "alertDetails";
																		var target = event.cyTarget;
																	//	clear();
																		fadeAlertNodeInfo(alertDetailstarget);
																	});*/
															cy
																	.on(
																		'select',
																		'node',
																		function(
																				event) {
	
																			
																			//highlightselection(target);
																			//highlight(target);
																			//showNodeInfo(target);
																		});
														/*	cy
															.on(
																'click',
																'node',
																function(
																		event) {
																	
																	scope.styles.push({
			"selector" : "node[parent]",
			"css" : {
				'min-zoomed-font-size' : 4,
				'font-size' : "mapData(weight,1,3, 12, 16)",
				'background-color' : '#ffffff',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}
		})
		console.log(event.cyTarget._private.data.track_name)
		if(scope.filterArray.indexOf(event.cyTarget._private.data.track_name)<0)
			{
			scope.filterArray.push(event.cyTarget._private.data.track_name)
			}
		filterArray.push(event.cyTarget._private.data.track_name)
		console.log(scope.filterArray)
		event.cyTarget.addClass('selectedSecondLevelNode')
		scope.styles.push({
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
		})
		console.log(scope.styles)
		var position_x=cy.$('#'+event.cyTarget._private.data.id).position().x
		var position_y=cy.$('#'+event.cyTarget._private.data.id).position().y
		var position={'x':position_x,'y':position_y}
		
		var doc={'id':event.cyTarget._private.data.id,'position':position}
		
		selected_nodes.push(doc)
		
		
		secondLevelNodes.getNodes(event.cyTarget._private.data.id,position_x,position_y).success(function(data){
			
																	console.log(data)
																	angular.forEach(data.node,function(value,key)
																			{
																		
																		var addedElements = cy.add(value);
																	
																		//cy.center()
																		//scope.onChange(cy,null);
																			})
																			angular.forEach(data.edge,function(value,key)
																			{
																		var addedElements = cy.add(value);
																		//cy.center()
																		//scope.onChange(cy,null);
																			})
																			
																	//highlight(target);
																	//showNodeInfo(target);
																})
		
																
																			
																	//highlight(target);
																	//showNodeInfo(target);
																});*/

															cy
																	.on(
																			'unselect',
																			'node',
																			function(
																					event) {
																				var target = event.cyTarget;
																				$("#nodetab").hide();
																				//$("#portlet"+event.cyTarget.data().id).css("background-color","white");

																				//clear();
																				hideNodeInfo();
																				
																			});
															/*cy
																	.on(
																		'cxttap',
																		function(
																				event) {
																				//var nodeLen = scope.data.length + 1;
																				var newnodeElemPosX = Math.floor((Math.random() * 800) + 1) ;
																				var newnodeElemPosY = Math.floor((Math.random() * 800) + 1) ;
																				var newnodeElemID = 'SQ' + newnodeElemPosX; 
																				var addedElements = cy.add({
																					group: "nodes",
																					data: { id: newnodeElemID },
																					position: { x: newnodeElemPosX, y: newnodeElemPosY }
																					
																				});
																				runLayout(addedElements);
																				scope.onChange(cy,data.forceApply);
																		});*/
															/* scope.$watch('scope.alertDataNode', function() {
															        if(scope.alertDataNode === true){
															        	$('#alertDetails').removeClass('faded');
															        } else if(scope.alertDataNode === false){
															        	$('#alertDetails').addClass('faded');
															        }
															    });
															*/
															 
															 function highlightselection(
																		node) {
																 cy
																	.elements()
																	.removeClass(
																			'outgoer'); 
																 cy
																	.elements()
																	.removeClass(
																			'incomer'); 
																 node.outgoers().addClass(
																	'outgoer');
																 node.incomers().addClass(
																	'incomer');
																 cy.center( node )
															 }

															function highlight(
																	node) {

																var nhood = node
																		.closedNeighborhood();
																var layoutPadding = 50;
																var layoutDuration = 500;

																cy
																		.batch(function() {
																			cy
																					.elements()
																					.not(
																							nhood)
																					.removeClass(
																							'highlighted')
																					.addClass(
																							'faded');
																			nhood
																					.removeClass(
																							'faded')
																					.addClass(
																							'highlighted');

																			var npos = node
																					.position();
																			var w = window.innerWidth;
																			var h = window.innerHeight;

																			cy
																					.stop()
																					.animate(
																							{
																								fit : {
																									eles : cy
																											.elements(),
																									padding : layoutPadding
																								}
																							},
																							{
																								duration : layoutDuration
																							})
																					.delay(
																							layoutDuration,
																							function() {
																								nhood
																										.layout({
																											name : 'concentric',
																											padding : layoutPadding,
																											animate : true,
																											animationDuration : layoutDuration,
																											boundingBox : {
																												x1 : npos.x
																														- w
																														/ 2,
																												x2 : npos.x
																														+ w
																														/ 2,
																												y1 : npos.y
																														- w
																														/ 2,
																												y2 : npos.y
																														+ w
																														/ 2
																											},
																											fit : true,
																											concentric : function(
																													n) {
																												if (node
																														.id() === n
																														.id()) {
																													return 2;
																												} else {
																													return 1;
																												}
																											},
																											levelWidth : function() {
																												return 1;
																											}
																										});
																							});

																		});
															}
															function clear() {
																
																cy
																		.batch(function() {
																			cy
																					.$(
																							'.highlighted')
																					.forEach(
																							function(
																									n) {
																								console.log(n.data('name') + ":" + n
																										.position());
																								n
																										.animate({
																											position : n
																													.position()
																										});
																							});

																			cy
																					.elements()
																					.removeClass(
																							'highlighted')
																					.removeClass(
																							'faded')
																								.removeClass(
																								'incomer')
																								.removeClass(
																							'outgoer').
																							removeClass('selectedSecondLevelNode');
																			
																		});
															}

															function showNodeInfo(
																	node) {
																$('#info')
																		.html(
																				infoTemplate(node
																						.data()))
																		.show();
															}
															
															function showNodeDetails(selectednode)
															{
																//$("#tablistbox").tabs("select","menu1");
																//highlightselection(selectednode);
															    highlightElement(selectednode)
																$("#menuclick").click();
															    $("#homeclick").mouseout();
																$("#appendhere").prepend("<div id=\"nodediv\"></div>");
																var $clonediv=$("#node"+selectednode).clone();
																$("#nodediv").prepend($clonediv);
																$("#portlet"+selectednode).css("background-color","#ffff99");
														    
																//$("#tryappend").append("<p>TEST</p>");
															}
															/*function highlightAlertNodeInfo(alertDetails) {
																$('#' + alertDetails)
																		.removeClass('faded');
																		
															}
															function fadeAlertNodeInfo(alertDetails) {
																$('#' + alertDetails)
																	.addClass('faded');
															}*/
															function hideNodeInfo() {
																$('#info')
																		.hide();
															}
															function highlightElement(nodeID){
																  $(".highlightable").removeClass("highlighted");
																$('#'+nodeID).addClass('highlighted');
															}
															// Add elements
															scope
																	.$on(
																			'cytoscapeAddElements',
																			function(
																					event,
																					data) {
																				var addElements = data.elements;
																				var addedElements = cy
																						.add(addElements);
																				runLayout(addedElements);
																				scope
																						.onChange(
																								cy,
																								data.forceApply);
																				console.log("added")
																			});
															// Delete elements
															scope
																	.$on(
																			'cytoscapeDeleteElements',
																			function(
																					event,
																					data) {
																				console.log("deleted")
																				var deleteElements = data.elements;
																				try {
																					cy
																							.remove(deleteElements);
																				} catch (exception) {
																					for ( var i in deleteElements) {
																						cy
																								.remove(cy
																										.$('#'
																												+ deleteElements[i].data.id));
																					}
																				}
																				scope
																						.onChange(
																								cy,
																								data.forceApply);
																			});
															
															/*// Filter nodes by name
															scope
																	.$watch(
																			'highlightByName',
																			function(
																					name) {
																				cy
																						.elements()
																						.removeClass(
																								'searched'); 
																				if (name
																						&& name.length > 0) {
																					var cleanName = name
																							.toLowerCase()
																							.trim();
																					var doHighlight = function(
																							i,
																							node) {
																						var currentName = node
																								.data().name
																								.toLowerCase()
																								.trim();
																						if (currentName
																								.indexOf(cleanName) > -1)
																							node
																									.addClass('searched');
																					};
																					cy
																							.nodes()
																							.each(
																									doHighlight);
																				} else {
																					cy
																							.elements()
																							.removeClass(
																									'searched');
																				}
																			});*/
															 scope.$watch('refreshGraph', function() {
																 
																 clear();
																	var removed=(cy.remove("node[parent]"))
																	if(removed.length!=0)
																		{
																		
																		angular.forEach(selected_nodes,function(value,key){
																			
																			cy.$('#'+value.id).position(value.position);
																			
																			console.log(cy.$('#'+value.id).position())
																			if(key==selected_nodes.length-1)
																				{
																			
																			selected_nodes=[]
																				}
																			
																		})
																		
																		}
																 
															    });
															scope
															.$watch(
																	'highlightById',
																	function(
																			id) {
																		cy
																		.elements()
																		.removeClass(
																				'alerted'); 
																if (id
																		&& id.length > 0) {
																	
																	var doHighlight = function(
																			i,
																			node) {
																		var currentid = node
																				.data().id;
																				
																		if (currentid
																				.indexOf(id) > -1)
																			node
																					.addClass('alerted');
																	};
																	cy
																			.nodes()
																			.each(
																					doHighlight);
																} else {
																	cy
																			.elements()
																			.removeClass(
																					'alerted');
																}
																		
																	});

															// Navigator
															cy
																	.panzoom({
																		//zoomOnly : true,
																	// options here...
																	});
															if (scope.navigatorContainerId) {
																cy
																		.navigator({
																			container : document
																					.getElementById(scope.navigatorContainerId)
																		});
															}
															// Context menu
															if (scope.contextMenuCommands) {
																/*cy
																		.cxtmenu({
																			menuRadius : 75,
																			indicatorSize : 17,
																			activePadding : 10,
																			selector : 'node',
																			commands : scope.contextMenuCommands
																		});*/
															}
															// On complete
															if (scope.onComplete) {
													
																scope
																		.onComplete({
																			graph : cy,
																			layout : layout
																		});
																
															}
														}
													});
												}
											});
						}
					}
				});