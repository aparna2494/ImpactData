BCCapp.factory('flowGraph', [ '$q', function($q) {
	var cy;
	  var layoutPadding = 50;
	  var layoutDuration = 500;
	var flowGraph = function(flowdata) {
		var deferred = $q.defer();

		// put people model in cy.js
		/*
		 * var eles = []; for( var i = 0; i < flowdata.nodes.length; i++ ){
		 * eles.push({ group: 'nodes', data: { id: flowdata.nodes[i].id, weight:
		 * flowdata.nodes[i].weight, name: flowdata.nodes[i].name } }); }
		 * 
		 * for( var i = 0; i < flowdata.edges.length; i++ ){ eles.push({ group:
		 * 'edges', data: { id: flowdata.edges[i].id, source:
		 * flowdata.edges[i].source, target: flowdata.edges[i].target } }); }
		 */
		$(function() { // on dom ready

			cy = cytoscape({
				container : $('#network')[0],

				boxSelectionEnabled : false,
				autounselectify : false,

				style : cytoscape.stylesheet().selector('core').css({
					'active-bg-color': '#fff',
					'active-bg-opacity': 0.333
			})
			.selector('node').css({
				"width":"mapData(weight,1,3, 20, 60)",
				"height":"mapData(weight,1,3, 20, 60)",
				'content' : 'data(name)',
				'text-valign' : 'top',
				'color' : 'white',
				'text-wrap': 'wrap',
                'text-max-width': 80,
				'text-outline-width' : 0,
				'text-outline-color' : '#888',
				'min-zoomed-font-size': 8,
				 'font-size': "mapData(weight,1,3, 8, 16)"
			}).selector('edge').css({
				'min-zoomed-font-size': 10,
			  'font-size': 8,
			  'color': '#fff',
			  'line-color': 'green',
			  'width': 4,
			  'curve-style': 'haystack',
			  'haystack-radius': 0,
			  'opacity': 0.5
			}).selector(':selected').css({
				'background-color' : 'black',
				'line-color' : 'black',
				'source-arrow-color' : 'black'
			}).selector('.faded').css({
				'opacity' : 0.25,
				'text-opacity' : 0
			})
			.selector('.nodeTrafficLow')
			  .css({
				'width': 25,
				'height': 25,
				'border-width': 1,
				'background-color': 'green',
				'line-color': 'green',
				'text-valign': 'top',
				'text-valign': 'center',
				'target-arrow-color': 'green',
				'source-arrow-color': 'green',
				'border-color': 'yellow',
				'opacity': 1
			  })
			  .selector('.nodeTrafficMed')
			  .css({
				'width': 40,
				'height': 40,
				'border-width': 2,
				'background-color': 'yellow',
				'line-color': 'yellow',
				'text-valign': 'top',
				'text-valign': 'center',
				'target-arrow-color': 'yellow',
				'source-arrow-color': 'yellow',
				'border-color': 'yellow',
				'opacity': 1
			  })
			  .selector('.nodeTrafficHigh')
			  .css({
				'width': 55,
				'height': 55,
				'border-width': 3,
				'background-color': 'orange',
				'line-color': 'orange',
				'text-valign': 'top',
				'text-valign': 'center',
				'target-arrow-color': 'orange',
				'source-arrow-color': 'orange',
				'border-color': 'yellow',
				'opacity': 1
			  })
			.selector('.edgeSelected')
			  .css({
				'width': 6,
				'line-color': 'red',
				'target-arrow-color': 'red',
				'source-arrow-color': 'red',
				'opacity': 1
			  })
			  .selector('.edgeTrafficLow')
			  .css({
				'width': 3,
				'line-color': 'green',
				'target-arrow-color': 'green',
				'source-arrow-color': 'green',
				'opacity': 1
			  })
			  .selector('.edgeTrafficMed')
			  .css({
				'width': 5,
				'line-color': 'yellow',
				'target-arrow-color': 'yellow',
				'source-arrow-color': 'yellow',
				'opacity': 1
			  })
			  .selector('.edgeTrafficHigh')
			  .css({
				'width': 7,
				'line-color': 'orange',
				'target-arrow-color': 'orange',
				'source-arrow-color': 'orange',
				'opacity': 1
			  }),

				layout : {
					name: 'preset',
					padding: layoutPadding
				},

				elements : {
					nodes : [ { data : { id : 'SQ1', name : 'Invalid' , weight: 1, track:'Service Quotes'}, position : { x: 0, y:0}},
					          { data : { id : 'SQ2', name : 'Invalid Submitted' , weight: 1, track:'Service Quotes'}, position : { x: 100, y:0}},
					          { data : { id : 'SQ3', name : 'Invalid Ordererable' , weight: 1, track:'Service Quotes'}, position : { x: 0, y:100}},
					          { data : { id : 'SQ4', name : 'Order In Progress' , weight: 2, track:'Service Quotes'}, position : { x: 200, y:0}},
					          { data : { id : 'SQ5', name : 'Orders Submitted' , weight: 1, track:'Service Quotes'}, position : { x: 300, y:0}},
					          { data : { id : 'SQ6', name : 'Orders Booked' , weight: 1, track:'Service Quotes'}, position : { x: 300, y:100}},
					          { data : { id : 'SQ7', name : 'Conversion In Progress' , weight: 2, track:'Service Quotes'}, position : { x: 500, y:0}},
					          { data : { id : 'SQ8', name : 'Conversion Failed' , weight: 1, track:'Service Quotes'}, position : { x: 400, y:100}},
					          { data : { id : 'SQ9', name : 'Conversion Ravlidated' , weight: 1, track:'Service Quotes'}, position : { x: 500, y:100}},
					          { data : { id : 'SQ10', name : 'Conversion Resubmitted' , weight: 1, track:'Service Quotes'}, position : { x: 600, y:100}},
					          { data : { id : 'SQ11', name : 'Entitiled' , weight: 1, track:'Service Quotes'}, position : { x: 700, y:100}},
					          { data : { id : 'SQ12', name : 'Orders Complete' , weight: 1, track:'Service Quotes'}, position : { x: 700, y:0}},
					          { data : { id : 'PQ1', name : 'Quotes Initiated' , weight: 1, track:'Product Quotes'}, position : { x: 0, y:300}},
					          { data : { id : 'PQ2', name : 'Quotes Submitted' , weight: 1, track:'Product Quotes'}, position : { x: 100, y:300}},
					          { data : { id : 'PQ3', name : 'Lost Quotes' , weight: 1, track:'Product Quotes'}, position : { x: 100, y:350}},
					          { data : { id : 'PQ4', name : 'Quotes AIP' , weight: 2, track:'Product Quotes'}, position : { x: 300, y:350}},
					          { data : { id : 'PQ5', name : 'Approved Quotes' , weight: 1, track:'Product Quotes'}, position : { x: 400, y:350}},
					          { data : { id : 'PQ6', name : 'Rejected Quotes' , weight: 1, track:'Product Quotes'}, position : { x: 400, y:400}},
					          { data : { id : 'PQ7', name : 'Expired Quotes' , weight: 1, track:'Product Quotes'}, position : { x: 500, y:350}},
					          { data : { id : 'PQ8', name : 'Quotes order Initiated' , weight: 1, track:'Product Quotes'}, position : { x: 300, y:275}},
					          { data : { id : 'PQ9', name : 'Order Initiated' , weight: 1, track:'Product Quotes'}, position : { x: 400, y:250}},
					          { data : { id : 'PQ10', name : 'Orders In Error' , weight: 2, track:'Product Quotes'}, position : { x: 500, y:300}},
					          { data : { id : 'PQ11', name : 'Orders Submitted' , weight: 1, track:'Product Quotes'}, position : { x: 600, y:300}},
					          { data : { id : 'PQ12', name : 'Order stuck for SO#' , weight: 2, track:'Product Quotes'}, position : { x: 700, y:300}},
					          { data : { id : 'PQ13', name : 'Orders Entered' , weight: 1, track:'Product Quotes'}, position : { x: 800, y:300}},
					          { data : { id : 'PBB1', name : 'Product Orders Passed B2B validation' , weight: 1, track:'B2B'}, position : { x: 600, y:200}},
					          { data : { id : 'PBB2', name : 'Product Orders Rejected in B2B' , weight: 1, track:'B2B'}, position : { x: 400, y:200}},
					          { data : { id : 'PBB3', name : 'Product Orders Stuck in B2B' , weight: 1, track:'B2B'}, position : { x: 500, y:200}},
					          { data : { id : 'SBB1', name : 'Service Orders Processed in B2B' , weight: 1, track:'B2B'}, position : { x: 200, y:200}},
					          { data : { id : 'SBB2', name : 'Service Orders Stuck in B2B' , weight: 1, track:'B2B'}, position : { x: 100, y:200}}],
					edges : [{ data : { source : 'SQ1', target : 'SQ2', id : 'SQ1TSQ2'} },
					         { data : { source : 'SQ1', target : 'SQ3', id : 'SQ1TSQ3'} },
					         { data : { source : 'SQ2', target : 'SQ4', id : 'SQ2TSQ4'} },
					         { data : { source : 'SQ3', target : 'SQ4', id : 'SQ3TSQ4'} },
					         { data : { source : 'SQ4', target : 'SQ5', id : 'SQ4TSQ5'} },
					         { data : { source : 'SQ5', target : 'SQ6', id : 'SQ5TSQ6'} },
					         { data : { source : 'SQ6', target : 'SQ7', id : 'SQ6TSQ7'} },
					         { data : { source : 'SQ7', target : 'SQ8', id : 'SQ7TSQ8'} },
					         { data : { source : 'SQ7', target : 'SQ11', id : 'SQ7TSQ11'} },
					         { data : { source : 'SQ8', target : 'SQ9', id : 'SQ8TSQ9'} },
					         { data : { source : 'SQ9', target : 'SQ10', id : 'SQ9TSQ10'} },
					         { data : { source : 'SQ10', target : 'SQ7', id : 'SQ10TSQ7'} },
					         { data : { source : 'SQ11', target : 'SQ12', id : 'SQ11TSQ12'} },
					         { data : { source : 'PQ1', target : 'PQ2', id : 'PQ1TPQ2'} },
					         { data : { source : 'PQ1', target : 'PQ3', id : 'PQ1TPQ3'} },
					         { data : { source : 'PQ2', target : 'PQ4', id : 'PQ2TPQ4'} },
					         { data : { source : 'PQ4', target : 'PQ5', id : 'PQ4TPQ5'} },
					         { data : { source : 'PQ4', target : 'PQ6', id : 'PQ4TPQ6'} },
					         { data : { source : 'PQ5', target : 'PQ7', id : 'PQ5TPQ7'} },
					         { data : { source : 'PQ2', target : 'PQ8', id : 'PQ2TPQ8'} },
					         { data : { source : 'PQ8', target : 'PQ9', id : 'PQ8TPQ9'} },
					         { data : { source : 'PQ9', target : 'PQ10', id : 'PQ9TPQ10'} },
					         { data : { source : 'PQ10', target : 'PQ11', id : 'PQ10TPQ11'} },
					         { data : { source : 'PQ11', target : 'PQ12', id : 'PQ11TPQ12'} },
					         { data : { source : 'PQ12', target : 'PQ13', id : 'PQ12TPQ13'} },
					         { data : { source : 'PQ9', target : 'PBB1', id : 'PQ9TPBB1'} },
					         { data : { source : 'PQ9', target : 'PBB2', id : 'PQ9TPBB2'} },
					         { data : { source : 'PQ9', target : 'PBB3', id : 'PQ9TPBB3'} },
					         { data : { source : 'SQ4', target : 'SBB1', id : 'SQ4TSBB1'} },
					         { data : { source : 'SQ4', target : 'SBB2', id : 'SQ4TSBB2'} },
					         { data : { source : 'PBB1', target : 'PQ11', id : 'PBB1TPQ11'} },
					         { data : { source : 'SBB1', target : 'SQ5', id : 'SBB1TSQ5'} }]
				},

				ready : function() {
					
					deferred.resolve(this);

				}
			});
			
		    cy.on('free', 'node', function( e ){
		        var n = e.cyTarget;
		        var p = n.position();
		        
		        n.data('orgPos', {
		          x: p.x,
		          y: p.y
		        });
		      });
		      
		      cy.on('tap', function(){
		        $('#search').blur();
		      });

		      cy.on('select', 'node', function(e){
		        var node = this;

		        highlight( node );
		        showNodeInfo( node );
		      });

		      cy.on('unselect', 'node', function(e){
		        var node = this;

		        clear();
		        hideNodeInfo();
		      });
		      
			  function highlight( node ){
				    var nhood = node.closedNeighborhood();

				    cy.batch(function(){
				      cy.elements().not( nhood ).removeClass('highlighted').addClass('faded');
				      nhood.removeClass('faded').addClass('highlighted');
				      
				      var npos = node.position();
				      var w = window.innerWidth;
				      var h = window.innerHeight;
				      
				      cy.stop().animate({
				        fit: {
				          eles: cy.elements(),
				          padding: layoutPadding
				        }
				      }, {
				        duration: layoutDuration
				      }).delay( layoutDuration, function(){
				        nhood.layout({
				          name: 'concentric',
				          padding: layoutPadding,
				          animate: true,
				          animationDuration: layoutDuration,
				          boundingBox: {
				            x1: npos.x - w/2,
				            x2: npos.x + w/2,
				            y1: npos.y - w/2,
				            y2: npos.y + w/2
				          },
				          fit: true,
				          concentric: function( n ){
				            if( node.id() === n.id() ){
				              return 2;
				            } else {
				              return 1;
				            }
				          },
				          levelWidth: function(){
				            return 1;
				          }
				        });
				      } );
				      
				    });
				  }
			  function clear(){
				    cy.batch(function(){
				      cy.$('.highlighted').forEach(function(n){
				        n.animate({
				          position: n.position()
				        });
				      });
				      
				      cy.elements().removeClass('highlighted').removeClass('faded');
				    });
				  }

				  function showNodeInfo( node ){
				    $('#info').html( infoTemplate( node.data() ) ).show();
				  }
				  
				  function hideNodeInfo(){
				    $('#info').hide();
				  }
				  

		}); // on dom ready

		return deferred.promise;
	};

	flowGraph.listeners = {};

	function fire(e, args) {
		var listeners = flowGraph.listeners[e];

		for (var i = 0; listeners && i < listeners.length; i++) {
			var fn = listeners[i];

			fn.apply(fn, args);
		}
	}

	function listen(e, fn) {
		var listeners = flowGraph.listeners[e] = flowGraph.listeners[e] || [];

		listeners.push(fn);
	}

	return flowGraph;

} ]);