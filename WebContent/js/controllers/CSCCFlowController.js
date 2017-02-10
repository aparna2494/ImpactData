
angular.module('BCCapp').controller('CSCCFlowController', function($rootScope, $scope, $http, $timeout,dataservices) {
    $scope.$on('$viewContentLoaded', function() {   
        // initialize core components
        App.initAjax();
        var res = dataservices.csccdataaction().success(
                function(data) {
                	
                	 	$scope.invalid=data.qcurrentinvalidfilter;
                	    $scope.invalidsubmitted=data.qinvalidsubmittedfilter;
                	    $scope.orderinprogress=data.qtooipfilter;
                	    $scope.orderssubmitted=data.qtoosubmitted;
                	    $scope.conversioninprogress=data.qconversionipfilter;
                	    $scope.orderscomplete=data.qordercompletedfilter;
                	    $scope.invalidorderable=data.qcurrentvalidfilter;
                	    $scope.orderbooked=data.qtoobookedfilter;
                	    $scope.conversionfailed=data.qconversionfailedfilter;
                	    $scope.conversionrevalidated=data.qconversionrevalfilter;
                	    $scope.conversionresubmitted=data.qconversionresubfilter;
                	    $scope.entitled=data.qentitledfilter;
                	    if(data.qcurrentinvalidfilterthreshold=="Critical" || data.qcurrentinvalidfilterthreshold=="High")
        				{
                	    	$scope.invalidthreshold="RED";
                	    	$scope.invalidicon="fa-caret-up";
            				$scope.invalidcommentdisplay="BLOCK";

        				}
        			else if(data.qcurrentinvalidfilterthreshold=="Green")
        			{
        				$scope.invalidthreshold="GREEN";
        				$scope.invalidicon="fa-caret-down";
        				$scope.invalidcommentdisplay="NONE";
        				
        					}
        			else{
        				
        				$scope.invalidthreshold="YELLOW";
        				$scope.invalidicon="fa-caret-down";
        				$scope.invalidcommentdisplay="NONE";
        			}
        			if(data.qtooipfilterthreshold=="Critical" || data.qtooipfilterthreshold=="High")
        			{
        				$scope.orderinprogressthreshold="RED";
        				$scope.orderinprogressicon="fa-caret-up";
        				$scope.orderinprogressthresholdcommentdisplay="BLOCK";
        			}
        		else if(data.qtooipfilterthreshold=="Green")
        		{
        			$scope.orderinprogressthreshold="GREEN";
        			$scope.orderinprogressicon="fa-caret-down";
        			$scope.orderinprogressthresholdcommentdisplay="NONE";
        				}
        		else{
        			$scope.orderinprogressthreshold="YELLOW";
        			$scope.orderinprogressicon="fa-caret-down";
        			$scope.orderinprogressthresholdcommentdisplay="NONE";
        		}
        			if(data.qtoosubmittedthreshold=="Critical" || data.qtoosubmittedthreshold=="High")
        			{
        				$scope.orderssubmittedthreshold="RED";
            			$scope.orderssubmittedicon="fa-caret-up";
        				$scope.orderssubmittedcommentdisplay="BLOCK";
        			}
        		else if(data.qtoosubmittedthreshold=="Green")
        		{
        			$scope.orderssubmittedthreshold="GREEN";
        			$scope.orderssubmittedicon="fa-caret-down";
        			$scope.orderssubmittedcommentdisplay="NONE";
        				}
        		else{
        			$scope.orderssubmittedthreshold="YELLOW";
        			$scope.orderssubmittedicon="fa-caret-down";
        			$scope.orderssubmittedcommentdisplay="NONE";
        		}
        			if(data.qconversionipfilterthreshold=="Critical" || data.qconversionipfilterthreshold=="High")
        			{
        				$scope.conversioninprogressthreshold="RED";
        				$scope.conversioninprogressicon="fa-caret-up";
        				$scope.conversioninprogresscommentdisplay="BLOCK";
        			}
        		else if(data.qconversionipfilterthreshold=="Green")
        		{
        			$scope.conversioninprogressthreshold="GREEN";
        			$scope.conversioninprogressicon="fa-caret-down";
        			$scope.conversioninprogresscommentdisplay="NONE";
        				}
        		else{
        			$scope.conversioninprogressthreshold="YELLOW";
        			$scope.conversioninprogressicon="fa-caret-down";
        			$scope.conversioninprogresscommentdisplay="NONE";
        		}
        			if(data.qordercompletedfilterthreshold=="Critical" || data.qordercompletedfilterthreshold=="High")
        			{
        				$scope.orderscompletethreshold="RED";
        				$scope.orderscompleteicon="fa-caret-up";
        				$scope.orderscompletecommentdisplay="BLOCK";
        			}
        		else if(data.qordercompletedfilterthreshold=="Green")
        		{
        			$scope.orderscompletethreshold="GREEN";
        			$scope.orderscompleteicon="fa-caret-down";
        			$scope.orderscompletecommentdisplay="NONE";
        				}
        		else{
        			$scope.orderscompletethreshold="YELLOW";
        			$scope.orderscompleteicon="fa-caret-down";
        			$scope.orderscompletecommentdisplay="NONE";
        		}
        			if(data.qcurrentvalidfilterthreshold=="Critical" || data.qcurrentvalidfilterthreshold=="High")
        			{
        				$scope.invalidorderablethreshold="RED";
        				$scope.invalidorderableicon="fa-caret-up";
        				$scope.invalidorderablecommentdisplay="BLOCK";
        			}
        		else if(data.qcurrentvalidfilterthreshold=="Green")
        		{
        			$scope.invalidorderablethreshold="GREEN";
        			$scope.invalidorderableicon="fa-caret-down";
        			$scope.invalidorderablecommentdisplay="NONE";
        				}
        		else{
        			$scope.invalidorderablethreshold="YELLOW";
        			$scope.invalidorderableicon="fa-caret-down";
        			$scope.invalidorderablecommentdisplay="NONE";
        		}
        			if(data.qinvalidsubmittedfilterthreshold=="Critical" || data.qinvalidsubmittedfilterthreshold=="High")
        			{
        				$scope.invalidsubmittedthreshold="RED";
        				$scope.invalidsubmittedicon="fa-caret-up";
        				$scope.invalidsubmittedcommentdisplay="BLOCK";

        			}
        		else if(data.qinvalidsubmittedfilterthreshold=="Green")
        		{
        			$scope.invalidsubmittedthreshold="GREEN";
        			$scope.invalidsubmittedicon="fa-caret-down";
    				$scope.invalidsubmittedcommentdisplay="NONE";

        				}
        		else{
        			$scope.invalidsubmittedthreshold="YELLOW";
        			$scope.invalidsubmittedicon="fa-caret-down";
    				$scope.invalidsubmittedcommentdisplay="NONE";

        		}
        			if(data.qtoobookedfilterthreshold=="Critical" || data.qtoobookedfilterthreshold=="High")
        			{
        				$scope.orderbookedthreshold="RED";
        				$scope.orderbookedicon="fa-caret-up";
            			$scope.orderbookedcommentdisplay="BLOCK";

        			}
        		else if(data.qtoobookedfilterthreshold=="Green")
        		{
        			$scope.orderbookedthreshold="GREEN";
        			$scope.orderbookedicon="fa-caret-down";
        			$scope.orderbookedcommentdisplay="NONE";
        				}
        		else{
        			$scope.orderbookedthreshold="YELLOW";
        			$scope.orderbookedicon="fa-caret-down";
        			$scope.orderbookedcommentdisplay="NONE";
        		}
        			if(data.qconversionfailedfilterthreshold=="Critical" || data.qconversionfailedfilterthreshold=="High")
        			{
        				$scope.conversionfailedthreshold="RED";
        				$scope.conversionfailedicon="fa-caret-up";
        				$scope.conversionfailedcommentdisplay="BLOCK";
        			}
        		else if(data.qconversionfailedfilterthreshold=="Green")
        		{
        			$scope.conversionfailedthreshold="GREEN";
        			$scope.conversionfailedicon="fa-caret-down";
        			$scope.conversionfailedcommentdisplay="NONE";
        				}
        		else{
        			$scope.conversionfailedthreshold="YELLOW";
        			$scope.conversionfailedicon="fa-caret-down";
        			$scope.conversionfailedcommentdisplay="NONE";

        		}
        			if(data.qentitledfilterthreshold=="Critical" || data.qentitledfilterthreshold=="High")
        			{
        				$scope.entitledthreshold="RED";
        				$scope.entitledicon="fa-caret-up";
            			$scope.entitledcommentdisplay="BLOCK";

        			}
        		else if(data.qentitledfilterthreshold=="Green")
        		{
        			$scope.entitledthreshold="GREEN";
        			$scope.entitledicon="fa-caret-down";
        			$scope.entitledcommentdisplay="NONE";

        				}
        		else{
        			$scope.entitledthreshold="YELLOW";
        			$scope.entitledicon="fa-caret-down";
        			$scope.entitledcommentdisplay="NONE";

        		}
        			if(data.qconversionrevalfilterthreshold=="Critical" || data.qconversionrevalfilterthreshold=="High")
        			{
        				$scope.conversionrevalidatedthreshold="RED";
        				$scope.conversionrevalidatedicon="fa-caret-up";
        				$scope.conversionrevalidatedcommentdisplay="BLOCK";
        			}
        		else if(data.qconversionrevalfilterthreshold=="Green")
        		{
        			$scope.conversionrevalidatedthreshold="GREEN";
        			$scope.conversionrevalidatedicon="fa-caret-down";
        			$scope.conversionrevalidatedcommentdisplay="NONE";
        				}
        		else{
        			$scope.conversionrevalidatedthreshold="YELLOW";
        			$scope.conversionrevalidatedicon="fa-caret-down";
        			$scope.conversionrevalidatedcommentdisplay="NONE";

        		}
        			if(data.qconversionresubfilterthreshold=="Critical" || data.qconversionresubfilterthreshold=="High")
        			{
        				$scope.conversionresubmittedthreshold="RED";
        				$scope.conversionresubmittedicon="fa-caret-up";
            			$scope.conversionresubmittedcommentdisplay="BLOCK";

        				
        			}
        		else if(data.qconversionresubfilterthreshold=="Green")
        		{
        			$scope.conversionresubmittedthreshold="GREEN";
        			$scope.conversionresubmittedicon="fa-caret-down";
        			$scope.conversionresubmittedcommentdisplay="NONE";

        				}
        		else{
        			$scope.conversionresubmittedthreshold="YELLOW";
        			$scope.conversionresubmittedicon="fa-caret-down";
        			$scope.conversionresubmittedcommentdisplay="NONE";

        		}
        			
                }).error(function(data) {
                	alert("error");
                               
        });
    });
   
    $scope.invalidcomment="";
    $scope.invalidauthor="";
    $scope.invalidsubmittedcomment="";
    $scope.invalidsubmittedauthor="";
    $scope.orderinprogresscomment="";
    $scope.orderinprogressauthor="";
    $scope.orderssubmittedcomment="";
    $scope.orderssubmittedauthor="";
    $scope.conversioninprogresscomment="";
    $scope.conversioninprogressauthor="";
    $scope.orderscompletecomment="";
    $scope.orderscompleteauthor="";
    $scope.invalidorderablecomment="";
    $scope.invalidorderableauthor="";
    $scope.orderbookedcomment="";
    $scope.orderbookedauthor="";
    $scope.conversionfailedcomment="";
    $scope.conversionfailedauthor="";
    $scope.conversionrevalidatedcomment="";
    $scope.conversionrevalidatedauthor="";
    $scope.orderssubmittedcomment="";
    $scope.orderssubmittedauthor="";
    $scope.conversionresubmittedcomment="";
    $scope.conversionresubmittedauthor="";
    $scope.entitledcomment="";
    $scope.entitledauthor="";
  
       

    var firstInstance;
	 
    $(window).resize(function(){
    	firstInstance = jsPlumb.getInstance();
		 /*jsPlumb.repaintEverything();*/
		 firstInstance.repaintEverything();
	 });
		jsPlumb.bind("ready", function() { 
			
			
	console.log("connecting boxes");
	/* jsPlumb.setContainer("#container"); */	
	
	firstInstance = jsPlumb.getInstance();
	 firstInstance.importDefaults({
		  Anchors: ["RightMiddle","Continuous"],
	      Endpoint : "Blank",
	      HoverPaintStyle : {strokeStyle: "#0277bd", lineWidth: 3 },
	      PaintStyle : { lineWidth : 1, strokeStyle : "#000" },
	      ConnectionOverlays : [
	          [ 
	              "Arrow", {
	                  location: 1,
	                  id: "arrow",
	                  width: 10,
	                  length: 10,
	                  foldback: 0.2
	              } 
	          ]
	      ]
	});
	firstInstance.connect({
        source: $("#element1"),
        target: $('#element2'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element2"),
        target: $('#element3'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element3"),
        target: $('#element4'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element4"),
        target: $('#element5'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element1"),
        target: $('#element6'),
        anchors: ["Bottom","Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element6"),
        target: $('#element7'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element7"),
        target: $('#element8'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element8"),
        target: $('#element9'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element2"),
        target: $('#element8'),
        anchors: ["Bottom","Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element3"),
        target: $('#element9'),
        anchors: ["Bottom","Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element4"),
        target: $('#element10'),
        anchors: ["Bottom","Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element5"),
        target: $('#element11'),
        anchors: ["Bottom","Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element6"),
        target: $('#element12'),
        anchors: ["Bottom","LeftMiddle"],
        connector:"Flowchart"
    });
	
	
	firstInstance.connect({
        source: $("#element14"),
        target: $('#element15'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element15"),
        target: $('#element16'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element12"),
        target: $('#element8'),
        anchors: ["RightMiddle","Bottom"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element9"),
        target: $('#element13'),
        anchors: ["Bottom","Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element10"),
        target: $('#element14'),
        anchors: [[0.25,1,0,1],"Top"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element16"),
        target: $('#element10'),
        anchors: ["Top",[0.75,1,0,1]],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element17"),
        target: $('#element11'),
        anchors: ["Top","Bottom"],
        connector:"Flowchart"
    });
		});
   /* var formData = new FormData();

    formData.append("username", "Groucho");
    var request = new XMLHttpRequest();
    request.open("POST", "http://foo.com/submitform.php");
    request.send(formData);
    $http.get('http://rest-service.guides.spring.io/greeting').
    success(function(data) {
        $scope.quotesinitiated = data.id;
    });*/
		
    /*$http({
        method: 'POST',
        url: 'http://vm-gse-analytics:8080/AnyQueryService/dataService',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        
        	
        crossDomain : true,
        transformRequest: function(obj) {
            var str = [];
            for(var p in obj)
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            return str.join("&");
        },
        data: {"targetColl": "bcc_ccw_quotes_tvs_data"}
    }).success(function (data) {console.log(data)});
*/
    // set sidebar closed and body solid layout mode
    $rootScope.settings.layout.pageContentWhite = true;
    $rootScope.settings.layout.pageBodySolid = false;
    $rootScope.settings.layout.pageSidebarClosed = false;
});