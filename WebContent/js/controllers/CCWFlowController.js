angular.module('BCCapp').controller('CCWFlowController', function($rootScope, $scope, $http, $timeout,dataservices) {
    
	
	$scope.$on('$viewContentLoaded', function() {   
    	 // initialize core components
        App.initAjax();
        
      
        var res = dataservices.ccwdataaction().success(
                function(data) {
                	 	$scope.quotesinitiated=data.qinitiated;
                	    $scope.quotessubmitted=data.qsubmitted;
                	    $scope.lostquotes=data.qlost;
                	    $scope.quotesAIP=data.qaip;
                	    $scope.approvedquotes=data.qapproved;
                	    $scope.rejectedquotes=data.qrejected;
                	    $scope.expiredquotes=data.qexpired;
                	    $scope.quoteswhereorderinitiated=data.qtooinitiated;
                	    $scope.orderinitiated=data.oinitiated;
                	    $scope.ordersinerror=data.oinerror;
                	    $scope.orderssubmitted=data.osubmitted;
                	    $scope.ordersstuck=data.ostuckso;
                	    $scope.ordersentered=data.oentered;
                	    if(data.qinitiatedthreshold=="Critical" || data.qinitiatedthreshold=="High")
        				{
                	    	$scope.quotesinitiatedthreshold="RED";
                	    	$scope.quotesinitiatedicon="fa-caret-up";
            				$scope.quotesinitiatedcommentdisplay="BLOCK";

        				}
        			else if(data.qinitiatedthreshold=="Green")
        			{
        				$scope.quotesinitiatedthreshold="GREEN";
        				$scope.quotesinitiatedicon=" fa-caret-down";
        				$scope.quotesinitiatedcommentdisplay="NONE";
        				
        					}
        			else{
        				
        				$scope.quotesinitiatedthreshold="YELLOW";
        				$scope.quotesinitiatedicon=" fa-caret-down";
        				$scope.quotesinitiatedcommentdisplay="NONE";
        			}
        			if(data.qsubmittedthreshold=="Critical" || data.qsubmittedthreshold=="High")
        			{
        				$scope.quotessubmittedthreshold="RED";
        				$scope.quotessubmittedicon="fa-caret-up";
        				$scope.quotessubmittedcommentdisplay="BLOCK";
        			}
        		else if(data.qsubmittedthreshold=="Green")
        		{
        			$scope.quotessubmittedthreshold="GREEN";
        			$scope.quotessubmittedicon="fa-caret-down";
        			$scope.quotessubmittedcommentdisplay="NONE";
        				}
        		else{
        			$scope.quotessubmittedthreshold="YELLOW";
        			$scope.quotessubmittedicon="fa-caret-down";
        			$scope.quotessubmittedcommentdisplay="NONE";
        		}
        			if(data.qlostthreshold=="Critical" || data.qlostthreshold=="High")
        			{
        				$scope.lostquotesthreshold="RED";
        				$scope.lostquotesicon="fa-caret-up";
        				$scope.lostquotescommentdisplay="BLOCK";
        			}
        		else if(data.qlostthreshold=="Green")
        		{
        			$scope.lostquotesthreshold="GREEN";
        			$scope.lostquotesicon="fa-caret-down";
        			$scope.lostquotescommentdisplay="NONE";
        				}
        		else{
        			$scope.lostquotesthreshold="YELLOW";
        			$scope.lostquotesicon="fa-caret-down";
        			$scope.lostquotescommentdisplay="NONE";
        		}
        			if(data.qaipthreshold=="Critical" || data.qaipthreshold=="High")
        			{
        				$scope.quotesAIPthreshold="RED";
        				$scope.quotesAIPicon="fa-caret-up";
        				$scope.quotesAIPcommentdisplay="BLOCK";
        			}
        		else if(data.qaipthreshold=="Green")
        		{
        			$scope.quotesAIPthreshold="GREEN";
        			$scope.quotesAIPicon="fa-caret-down";
        			$scope.quotesAIPcommentdisplay="NONE";
        				}
        		else{
        			$scope.quotesAIPthreshold="YELLOW";
        			$scope.quotesAIPicon="fa-caret-down";
        			$scope.quotesAIPcommentdisplay="NONE";
        		}
        			if(data.qapprovedthreshold=="Critical" || data.qapprovedthreshold=="High")
        			{
        				$scope.approvedquotesthreshold="RED";
        				$scope.approvedquotesicon="fa-caret-up";
        				$scope.approvedquotescommentdisplay="BLOCK";
        			}
        		else if(data.qapprovedthreshold=="Green")
        		{
        			$scope.approvedquotesthreshold="GREEN";
        			$scope.approvedquotesicon="fa-caret-down";
        			$scope.approvedquotescommentdisplay="NONE";
        				}
        		else{
        			$scope.approvedquotesthreshold="YELLOW";
        			$scope.approvedquotesicon="fa-caret-down";
        			$scope.approvedquotescommentdisplay="NONE";
        		}
        			if(data.qrejectedthreshold=="Critical" || data.qrejectedthreshold=="High")
        			{
        				$scope.rejectedquotesthreshold="RED";
        				$scope.rejectedquotesicon="fa-caret-up";
        				$scope.rejectedquotescommentdisplay="BLOCK";
        			}
        		else if(data.qrejectedthreshold=="Green")
        		{
        			$scope.rejectedquotesthreshold="GREEN";
        			$scope.rejectedquotesicon="fa-caret-down";
        			$scope.rejectedquotescommentdisplay="NONE";
        				}
        		else{
        			$scope.rejectedquotesthreshold="YELLOW";
        			$scope.rejectedquotesicon="fa-caret-down";
        			$scope.rejectedquotescommentdisplay="NONE";
        		}
        			if(data.qexpiredthreshold=="Critical" || data.qexpiredthreshold=="High")
        			{
        				$scope.expiredquotesthreshold="RED";
        				$scope.expiredquotesicon="fa-caret-up";
        				$scope.expiredquotescommentdisplay="BLOCK";

        			}
        		else if(data.qexpiredthreshold=="Green")
        		{
        			$scope.expiredquotesthreshold="GREEN";
        			$scope.expiredquotesicon="fa-caret-down";
    				$scope.expiredquotescommentdisplay="NONE";

        				}
        		else{
        			$scope.expiredquotesthreshold="YELLOW";
        			$scope.expiredquotesicon="fa-caret-down";
    				$scope.expiredquotescommentdisplay="NONE";

        		}
        			if(data.qtooinitiatedthreshold=="Critical" || data.qtooinitiatedthreshold=="High")
        			{
        				$scope.quoteswhereorderinitiatedthreshold="RED";
        				$scope.quoteswhereorderinitiatedicon="fa-caret-up";
            			$scope.quoteswhereorderinitiatedcommentdisplay="BLOCK";

        			}
        		else if(data.qtooinitiatedthreshold=="Green")
        		{
        			$scope.quoteswhereorderinitiatedthreshold="GREEN";
        			$scope.quoteswhereorderinitiatedicon="fa-caret-down";
        			$scope.quoteswhereorderinitiatedcommentdisplay="NONE";
        				}
        		else{
        			$scope.quoteswhereorderinitiatedthreshold="YELLOW";
        			$scope.quoteswhereorderinitiatedicon="fa-caret-down";
        			$scope.quoteswhereorderinitiatedcommentdisplay="NONE";
        		}
        			if(data.oinitiatedthreshold=="Critical" || data.oinitiatedthreshold=="High")
        			{
        				$scope.orderinitiatedthreshold="RED";
        				$scope.orderinitiatedicon="fa-caret-up";
        				$scope.orderinitiatedcommentdisplay="BLOCK";
        			}
        		else if(data.oinitiatedthreshold=="Green")
        		{
        			$scope.orderinitiatedthreshold="GREEN";
        			$scope.orderinitiatedicon="fa-caret-down";
        			$scope.orderinitiatedcommentdisplay="NONE";
        				}
        		else{
        			$scope.orderinitiatedthreshold="YELLOW";
        			$scope.orderinitiatedicon="fa-caret-down";
        			$scope.orderinitiatedcommentdisplay="NONE";

        		}
        			if(data.oinerrorthreshold=="Critical" || data.oinerrorthreshold=="High")
        			{
        				$scope.ordersinerrorthreshold="RED";
        				$scope.ordersinerroricon="fa-caret-up";
            			$scope.ordersinerrorcommentdisplay="BLOCK";

        			}
        		else if(data.oinerrorthreshold=="Green")
        		{
        			$scope.ordersinerrorthreshold="GREEN";
        			$scope.ordersinerroricon="fa-caret-down";
        			$scope.ordersinerrorcommentdisplay="NONE";

        				}
        		else{
        			$scope.ordersinerrorthreshold="YELLOW";
        			$scope.ordersinerroricon="fa-caret-down";
        			$scope.ordersinerrorcommentdisplay="NONE";

        		}
        			if(data.osubmittedthreshold=="Critical" || data.osubmittedthreshold=="High")
        			{
        				$scope.orderssubmittedthreshold="RED";
        				$scope.orderssubmittedicon="fa-caret-up";
        				$scope.orderssubmittedcommentdisplay="BLOCK";
        			}
        		else if(data.osubmittedthreshold=="Green")
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
        			if(data.ostucksothreshold=="Critical" || data.ostucksothreshold=="High")
        			{
        				$scope.ordersstuckthreshold="RED";
        				$scope.ordersstuckicon="fa-caret-up";
            			$scope.ordersstuckcommentdisplay="BLOCK";

        				
        			}
        		else if(data.ostucksothreshold=="Green")
        		{
        			$scope.ordersstuckthreshold="GREEN";
        			$scope.ordersstuckicon="fa-caret-down";
        			$scope.ordersstuckcommentdisplay="NONE";

        				}
        		else{
        			$scope.ordersstuckthreshold="YELLOW";
        			$scope.ordersstuckicon="fa-caret-down";
        			$scope.ordersstuckcommentdisplay="NONE";

        		}
        		if(data.oenteredthreshold=="Critical" || data.oenteredthreshold=="High")
    			{
    				$scope.ordersenteredthreshold="RED";
    				$scope.ordersenteredicon="fa-caret-up";
        			$scope.ordersenteredcommentdisplay="BLOCK";

    				
    			}
    		else if(data.oenteredthreshold=="Green")
    		{
    			$scope.ordersenteredthreshold="GREEN";
				$scope.ordersenteredicon="fa-caret-down";
    			$scope.ordersenteredcommentdisplay="NONE";

    				}
    		else{
    			$scope.ordersenteredthreshold="YELLOW";
				$scope.ordersenteredicon="fa-caret-down";
    			$scope.ordersenteredcommentdisplay="NONE";

    		}
        			
                }).error(function(data) {
                	alert("error");
                               
        });
    });
    
    $scope.quotesinitiatedcomment="";
    $scope.quotesinitiatedauthor="";
    $scope.quotessubmittedcomment="";
    $scope.quotessubmittedauthor="";
    $scope.quotesAIPcomment="";
    $scope.quotesAIPauthor="";
    $scope.approvedquotescomment="";
    $scope.approvedquotesauthor="";
    $scope.expiredquotescomment="";
    $scope.expiredquotesauthor="";
    $scope.lostquotescomment="";
    $scope.lostquotesauthor="";
    $scope.rejectedquotescomment="";
    $scope.rejectedquotesauthor="";
    $scope.quoteswhereorderinitiatedcomment="";
    $scope.quoteswhereorderinitiatedauthor="";
    $scope.orderinitiatedcomment="";
    $scope.orderinitiatedauthor="";
    $scope.ordersinerrorcomment="";
    $scope.ordersinerrorauthor="";
    $scope.orderssubmittedcomment="";
    $scope.orderssubmittedauthor="";
    $scope.ordersstuckcomment="";
    $scope.ordersstuckauthor="";
    $scope.ordersenteredcomment="";
    $scope.ordersenteredauthor="";
    
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
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element3"),
        target: $('#element7'),
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
        source: $("#element9"),
        target: $('#element10'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element10"),
        target: $('#element11'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element11"),
        target: $('#element12'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element13"),
        target: $('#element14'),
        anchors: ["RightMiddle","LeftMiddle"],
        connector:"Flowchart"
    });
	firstInstance.connect({
        source: $("#element14"),
        target: $('#element15'),
        anchors: ["RightMiddle","LeftMiddle"],
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
   /* $http({
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
        data: {"targetColl": "bcc_alerts"}
    }).success(function () {alert("data");});*/

    // set sidebar closed and body solid layout mode
    $rootScope.settings.layout.pageContentWhite = true;
    $rootScope.settings.layout.pageBodySolid = false;
    $rootScope.settings.layout.pageSidebarClosed = false;
    $scope.submit = function()
    {
   	console.log("click");
   }
    
});