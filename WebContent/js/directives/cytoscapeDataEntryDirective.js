BCCapp
		.directive(
				'cytoscaped',
				function($timeout,dataEntryService) {
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
							newNodes :'=',
							source :'=',
							maxid:'=',
							changed:'=',
							selectednode:'=',
							updateStyle:'&',
							fetchTrackflowCounter:'=',
							addNodeDetailsFunction:'&',
							addNewKpiFunction:'&',
							connectToFunction:'&',
							savePubClicked:'='
						},
						
						link : function(scope, element, attributes, controller,highlightElementById,historyShow,sampleFunction,refreshGraph) {
							var cy_dir;
							scope.initialLoad=0
							addnodedetailsfn=function()
							{
							 scope.addNodeDetailsFunction({arg1:scope.hoveredNode});
							}
						
						 addnewkpifn=function()
						 {
							 //console.log(scope.hoveredNode)
					    
						 scope.addNewKpiFunction({arg1:scope.hoveredNode});
						 }
						
						connecttofn=function()
						{
							scope.connectToFunction({arg1:scope.hoveredNode});
						}
						
scope.$watch('savePubClicked',function(newval,oldval){
	if(cy_dir!=undefined)
		{
		console.log("savePubClicked")
		var removed=(cy_dir.remove(cy_dir.elements("node[secondary= 'yes']")))
		console.log(removed)
		removed.each(function(i,ele){
			
			
			if(ele._private.group=="nodes")
				{
				console.log(ele._private.data.id)
				if(ele._private.data.id.includes("Du")){
					var id=ele._private.data.id.replace('Du','').trim();
					console.log(ele._private.position)
					cy_dir.elements("node[id='"+id+"']").position(ele._private.position)
				}
				}
		})
		}
	
})
scope.$watch('styles',function(newval,oldval){
								
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
												console.log("direcctive watch")
												var safe = true;
												for ( var i in newValues)
													{
													
													if (!newValues[i])
														safe = false;
													}
											
												if (safe) {
													var elements = scope.elements;
													scope.levelCounter=0
													console.log(elements)
													var styles = scope.styles;
													var layout = newValues[0];
													cy_dir=cytoscape({
														container : element[0],
														elements : elements,
														style : styles,
														layout : layout,
														fit : true,
														boxSelectionEnabled : false,
														motionBlur : false,
														zoom:1,
														
														autounselectify: false,
														wheelSensitivity : 0.2,
														ready : function() {
															var cy = this;
															// Run layout after
															// add new elements
															
															/**/
															cy.fit()
															cy.zoom({
																level: 1, // the zoom level
																renderedPosition: { x: 100, y: 0 }
																
																});
																		
																		/*else
																			{
																			console.log("in else")
																			cy.zoom({
																				level: 1, // the zoom level
																				
																				renderedPosition: { x: 100, y: -100 }
																				
																				});
																			}*/
															var runLayout = function(
																	addedElements) {
																if (addedElements) {
																	console.log("in runlayout")
																	layout.maxSimulationTime = 10;
																	layout.fit = true;
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
															/*cy
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
																				}
																								
																						},
																						10);
																				
																				
																				

																			});
															*/
															//qtip insterted
															cy
																	.on(
																			'mouseover','node',
																			function(
																					event) {
																			
																			});
														//	cy.on('directtap','node',function(event){
															//	var node=event.cyTarget;
																//scope.hoveredNode=node;
														var qtipfn=function(node){
															console.log("qtip function");
															node.qtip({
																	
																	content:{
																		title:'Add Data',
																		
																		text:[																						     
																	      '<div></div><div class="menustyle"><a ng-href="" onClick="addnodedetailsfn();"  class="anchorstyle"  class="font-grey-mint">Add Node Details</a>',
																	         '</div><div class="menustyle"><a ng-href="" onClick="addnewkpifn();"  class="anchorstyle"  class="font-grey-mint">Add new KPI </a>',
																	         '</div><div class="menustyle"><a ng-href="" onClick="connecttofn();"  class="anchorstyle" class="font-grey-mint">Connect to</a><div></div>'
																	         
																
																	         
		                                                
																	         ].join('<br />\n')
																	},
																	
									
																	 show: {
																		        delay:200,
																		     //    event:'click' ,
																		        event: 'directtap',
																		         ready: true
																		   },
																     hide: {
																    	     event:'mouseout unfocus',
																    	    fixed: true
																    	     
																    	    
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

														
															
															cy
															.on(
																'select',
																'node',
																function(
																		event) {

																	var target = event.cyTarget;
																	
																	//highlightselection(target);
																	//highlight(target);
																	//showNodeInfo(target);
																});
															
															
															var tappedBefore;
															var tappedTimeout;
															cy.on('click','node', function(event) {
																//console.log("st")
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
												    	// console.log("direct tap");
												    	// console.log(event.cyTarget.id());
													     qtipfn(node);
													     
										              }
														//var node = event.cyTarget;
												      
														
													//	scope.id="anchorstyle";
														/*scope.styles.push({
															
															"selector" : ".qtipstyle",
															"css" : {
																'background': '#BADA55',
															   'border': '3px solid #f8f8ff',
															   'border-radius': '10px'
																
														        
															}
														});
													  console.log(node)
													    console.log()*/

														
                                                         
													 }
															  
													});
													cy
													.on(
														'doubleClick',
														'node',
														function(
																event) {
															
															

var nodeComponentCode=event.cyTarget._private.data.component_code

if(nodeComponentCode==null)
					{
					console.log("inside nodeComponentCode null ")
					//scope.updateStyle({data: scope.selectedTrack})
					return
					}

if(cy.elements("node[masterid='"+nodeComponentCode+"']").length==0)
	{
	console.log("node[masterid] is null")
	//scope.updateStyle({data: scope.selectedTrack})
	return
	}
scope.selectedTrack=event.cyTarget._private.data.track_name
event.cyTarget.addClass('selectedSecondLevelNode')





//
if(scope.levelCounter==0)
	{
	//cy.fit(cy.elements("node[track_name = '"+scope.selectedTrack+"']"))
	cy.zoom({
		//level: 0.5, // the zoom level
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
    position:ele._private.position,
    /*"renderedPosition" : {
        "y" : 300,
        "x" : 250+i*50*cy.zoom()
    },*/
    "selectable" : true,
    "removed" : false,
    "data" : {
        "kpi_data" : [ 
            
        ],
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


scope.updateStyle({data: scope.selectedTrack})
														});
													
													
													
															cy.on('mouseover', 'node', function(event) {
																//console.log('mouseover')
															   
															});
															// Mouseout
															cy
																	.on(
																			'mouseout',
																			function() {
																				clear()
																				cy
																						.elements()
																						.removeClass(
																								'mouseover');
																				cy
																						.elements()
																						.removeClass(
																								'edge-related');
																			});
															// Mouseover
															cy
															.on(
																	'tap',
																	function(
																			event) {
																		
																		var target = event.cyTarget;
																		if (target != event.cy) {
																			target
																					.addClass('mouseover');
																			target
																					.neighborhood()
																					.edges()
																					.addClass(
																							'edge-related');
																			
																			
																		}
																		else
																			{
																			clear();
																			
																			cy.layout()

																			scope.levelCounter=0
																			
																			var removed=(cy.remove(cy.elements("node[secondary= 'yes']")))
																			console.log(removed)
																			removed.each(function(i,ele){
																				
																				
																				if(ele._private.group=="nodes")
																					{
																					console.log(ele._private.data.id)
																					if(ele._private.data.id.includes("Du")){
																						var id=ele._private.data.id.replace('Du','').trim();
																						console.log(ele._private.position)
																						cy.elements("node[id='"+id+"']").position(ele._private.position)
																					}
																					}
																			})
																			
																			scope.selectedTrack=""
																			scope.updateStyle({data: scope.selectedTrack})
																			
																			//scope.onChange(cy,null);
																			}
																		if(scope.historyShow!=undefined)
																			{
																			scope.historyShow.value=true;
																			}
																		
																		
																		
																	//
																		cy.zoom({
																			level: 1, // the zoom level
																			renderedPosition: { x: 100, y: 0 }
																			
																			});
																	});
															cy
															.on(
																	'drag',
																	function(
																			event) {
																		
																		
																	
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
															/*cy
																	.on(
																		'select',
																		'node',
																		function(
																				event) {
																			
																			var target = event.cyTarget;
																			var targetid= target.id();
																			console.log(scope.source)
																			console.log(targetid)
																			console.log(scope.elements.edges)
																			//if(scope.source!=null && (scope.newNodes.indexOf(targetid)>=0||scope.newNodes.indexOf(scope.source)>=0)){
																			if(scope.source!=null ){	console.log("again"+scope.source)
																			var addedElements = cy.add({
																					group: "edges",
																					data: { id: scope.source+"R"+targetid, source: scope.source, target:targetid }
																					
																				});
																				var edge= {id:scope.source+"R"+targetid, source: scope.source, target:targetid}
																				dataEntryService.saveedgedetails(edge).success( function(data){
																					//console.log(data);
																				
																				});
																				
																				scope.elements.edges.push({data:{"edge_fixed":true,id: scope.source+"R"+targetid, source: scope.source, target:targetid }})
																				scope.source=null;
																				runLayout(addedElements);
																			}
																			//highlightselection(target);
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

																				clear();
																				hideNodeInfo();
																				
																			});
														/*	cy
															.on(
																'cxttap',
																function(
																		event) {
																		//var nodeLen = scope.data.length + 1;
																	scope.maxid=parseInt(scope.maxid)+1
																	console.log('in ctxtap')
																	scope.maxid=scope.maxid.toString()
																	var leading=(4-scope.maxid.length)
																	scope.maxid=Array(leading+1).join("0")+scope.maxid
																	
																		var newnodeElemPosX = Math.floor((Math.random() * 800) + 1) ;
																		var newnodeElemPosY = Math.floor((Math.random() * 800) + 1) ;
																		var xPosition = event.cyPosition.x;
																	    var yPosition = event.cyPosition.y;
																	    console.log(xPosition)
																	    console.log(yPosition)
																	   var newnodeElemID =scope.maxid; 
																		var addedElements = cy.add({
																			group: "nodes",
																			data: { id: newnodeElemID },
																			position: { x: xPosition, y: yPosition },
																			removed : false,
																            selected : true,
																            selectable : true,
																            locked: false,
																            grabbable : true,
																            classes : ""
																			
																		});
																		
																		runLayout(addedElements);
																		scope.elements.nodes.push({
																			group: "nodes",
																			data: { id: newnodeElemID },
																			position: { x: xPosition, y: yPosition },
																			removed : false,
																            selected : true,
																            selectable : true,
																            locked: false,
																            grabbable : true,
																            classes : ""
																			
																		})
																		scope.newNodes.push( newnodeElemID);
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
															 scope.$watch('changed', function() {

cy.$('#'+scope.selectednode).data("component_name",scope.changed)

});
															
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
																							'outgoer')
																							.removeClass('selectedSecondLevelNode');;
																			
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
																			});
															// Delete elements
															scope
																	.$on(
																			'cytoscapeDeleteElements',
																			function(
																					event,
																					data) {
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
																 
																 cy
																	.elements()
																	.removeClass(
																			'highlighted'); 
																 cy
																	.elements()
																	.removeClass(
																			'outgoer'); 
																 cy
																	.elements()
																	.removeClass(
																			'incomer'); 
																 cy
																	.elements()
																	.removeClass(
																			'selected'); 
																 
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
															/*	cy
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





				