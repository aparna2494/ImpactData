BCCapp.controller('buildNetworkController', [ '$scope', 'flowGraph','dataservices',
		function($scope, flowGraph,dataservices) {
		
			$scope.$on('$viewContentLoaded', function() {
				// initialize core components
				App.initAjax();
			});

	
			$scope.data = {
				nodes : [ {
					data : {
						id : 'SQ1',
						name : 'Invalid',
						weight : 1,
						track : 'Service Quotes'
					},
					position : {
						x : 0,
						y : 0
					}
				}, {
					data : {
						id : 'SQ2',
						name : 'Invalid Submitted',
						weight : 1,
						track : 'Service Quotes'
					},
					position : {
						x : 100,
						y : 0
					}
				}, {
					data : {
						id : 'SBB2',
						name : 'Service Orders Stuck in B2B',
						weight : 1,
						track : 'B2B'
					},
					position : {
						x : 100,
						y : 200
					}
				} , {
					data : {
						id : 'SQ3',
						name : 'Invalid Submitted',
						weight : 1,
						track : 'Service Quotes'
					},
					position : {
						x : 100,
						y : 0
					}
				}, {
					data : {
						id : 'SQ4',
						name : 'Invalid Submitted',
						weight : 1,
						track : 'Service Quotes'
					},
					position : {
						x : 100,
						y : 0
					}
				}],
				edges : [ {
					data : {
						source : 'SQ1',
						target : 'SQ2',
						id : 'SQ1TSQ2'
					}
				}, {
					data : {
						source : 'SQ1',
						target : 'SQ3',
						id : 'SQ1TSQ3'
					}
				}, {
					data : {
						source : 'SQ2',
						target : 'SQ4',
						id : 'SQ2TSQ4'
					}
				}]
			};
			$scope.layout = {
				"name" : "preset"
			};
			$scope.styles = [ {
				"selector" : "node",
				"css" : {
					"width" : "mapData(weight,1,3, 20, 60)",
					"height" : "mapData(weight,1,3, 20, 60)",
					'content' : 'data(name)',
					'text-valign' : 'top',
					'color' : 'white',
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
					'line-color' : 'green',
					'width' : 4,
					'curve-style' : 'haystack',
					'haystack-radius' : 0,
					'opacity' : 0.5
				}
			}, {
				"selector" : ":selected",
				"css" : {
					'background-color' : 'black',
					'line-color' : 'black',
					'source-arrow-color' : 'black'
				}
			}, {
				"selector" : ".faded",
				"css" : {
					'opacity' : 0.25,
					'text-opacity' : 0
				}
			}, {
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

				}
			}];

			$scope.poorFunction = function(e) {
				
			};

            $scope.contextMenu = [{
                content: 'Edit',
                disabled: true
              }, {
                content: 'Mark',
                disabled: true
              }, {
                content: 'Remove',
                disabled: true
              }, {
                content: 'Add issue',
                select: function(element) {
                 
                },
              }];
  
            
		} ]);