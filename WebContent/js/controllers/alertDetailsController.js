angular.module('BCCapp').controller('alertDetailsController', function($scope,$stateParams,$rootScope,alertDetailsService,dataservices,faqService,caseService) {
		$('#chat_portlet').removeClass('hide')
		//alert(1)
		$scope.chatNotification=function(string)
		{
			
			
			toastr.options = {
					  "closeButton": true,
					  "debug": false,
					  "positionClass": "toast-top-right",
					  "onclick": null,
					  "showDuration": "1000",
					  "hideDuration": "1000",
					  "timeOut": "5000",
					  "extendedTimeOut": "1000",
					  "showEasing": "swing",
					  "hideEasing": "linear",
					  "showMethod": "fadeIn",
					  "hideMethod": "fadeOut"
					} 
			toastr['error'](string, "Chat Notifications")
		}
        $scope.alertID = $stateParams.alertID 
        $scope.mainquerypresent= false;
        $scope.newassignee="loggeduser";
        $scope.newuserid="";
        $scope.alertComments=[];
        var rkmdata=[];
        
        $('#popover').popover();
        $scope.customStyle = {};
       $scope.smeList=[];
       var timestamp;  
       $scope.searchText="";
   	$scope.showLoad=false;
   	$scope.queries=[];
    $scope.answer={};
	 $scope.showAnsLoad=false;
     $scope.open=true ;
$scope.rkmqueries=[];
        var resu = alertDetailsService.alertdetailsaction( $scope.alertID).success(
                function(data) {
                	
                	if(data.result!="No data available"){
                		data[0].alert_create_date_unx=moment.unix(data[0].alert_create_date_unx).fromNow()
                	$scope.alert=data[0];
                		$scope.kpi_id= data[0].kpi_id;
                		$scope.kpi_name= data[0].kpi_name;
                	
                	$rootScope.track_id=data[0].track_id;	
                	$scope.track_id= data[0].track_id;
                	$scope.alertComponent=data[0].alert_component;
                	$scope.flowComponentId=data[0].id;
                	$scope.assigned_by=data[0].assigned_by;
                	$scope.getQuestionsOnLoad();
                	
                	//$scope.alertComments=data[0].ALERT_ISSUE;
                	angular.forEach(data[0].alert_comments, function(value, key) {
                		
                		timestamp = moment.unix(value.TIME).fromNow()
                		//data[0].ALERT_ISSUE[key].TIME= timestamp.format('MMMM Do YYYY, h:mm:ss a');
                		data[0].alert_comments[key].TIME= timestamp;
                		});
                	$scope.alertComments=data[0].alert_comments;
                	if($scope.alert.alert_status=="closed")
            		{
            		$scope.alert.alert_mode="Closed";
            		$scope.open=false;
            		 $scope.customStyle.style = {"color":"green"};
            	       
            		}
            	else
            		{
           		 $scope.customStyle.style = {"color":"#e43a45"};

            		}
                	alertDetailsService.smelocatoraction($scope.track_id).success(
             	             function(data) {
             	            	
             	            	 if(data!="")
             	            		 {
             	            	$scope.smeList=data[0].userIds;
             	            		//$scope.smeList=data;
             	            		
             	            	getjabberstatus({id:'#sme_jabber_table',values:$scope.smeList,createRoom:true,room_id:$scope.alertID});
             	            		 }
             	            	
             	             });
                	
                	var result = alertDetailsService.alerthistoryaction( $scope.flowComponentId).success(
                    		function(data) {
                    			var arrayData=[];
                    			//$scope.details=data;
                    			
                    			for(var i=0; i<data.length; i++){
                    				
                     			 	//var theTime = data[i].ALERT_AS_OF.split(":");
                     				//var theDate = data[i].ALERT_DATE.split("-");
                     				//var trendDate = new Date(theDate[2],theDate[1]-1,theDate[0],theTime[0],theTime[1].substr(0,2));
                     				//pushArray = [Date.UTC(parseFloat(trendDate.getFullYear()),parseFloat(trendDate.getMonth()),parseFloat(trendDate.getDate()),parseFloat(trendDate.getHours()),parseFloat(trendDate.getMinutes())),parseFloat(data[i].CURRENT_THRESHOLD)];
                     				//console.log("pushArray--"+pushArray);
                            		//pushArray=[(data[i].ALERT_CREATE_DATE_UNX)*1000,parseFloat(data[i].CURRENT_THRESHOLD)];
                    				pushedArray={id:data[i].alert_id,x:(data[i].alert_create_date_unx)*1000,y:parseFloat(data[i].current_value)}
                     				arrayData.push(pushedArray);
                     				options.series[0].data = arrayData;
                     				
                     				//console.log("arrData--"+arrData);
                     			 }
                            	$scope.alertTrend=arrData;
                            	$('#containerY').highcharts(options); 
                    			
                    		});

                	} });
        var resu = dataservices.alertsdataaction().success(
                function(data) {
                	if(data.result!="No data available"){
                		angular.forEach(data, function(value, key) {
value.alert_create_date_unx=moment.unix(value.alert_create_date_unx).fromNow();
                	$scope.dropdowns.push(value);
                	
                	
                	});}
                		$scope.alertCount=$scope.dropdowns.length;
                });
        $scope.assigntome = function(assignedto,alertID){
        	
        	/*if(assignedto==""){
        		
        		var resu = alertDetailsService.assignsaveaction(alertID).success(
        	             function(data) {
        	            	 if(data!="No data available"){
        	            	 $scope.alert.alert_assignee=data;
        	            	 }
        	             });
        		
        		
        	}
        	else{
        		$('#confirmation').show();
        		
        	}*/
        	//alert($scope.assigned_by);
        	$("#insertid").hide();
        	$("#invalidalert").hide();
        	//alert($scope.alert.alert_assignee.length);
        	
        	 console.log("check values");
        	 console.log($scope.alert.assigned_by);
        	 
        	if ($scope.alert.assigned_by==undefined||$scope.alert.assigned_by=="")
        		{
        		 $("#unassignbtn").hide();
        		}
        	else if($scope.alert.assigned_by!=$rootScope.user.userId)
        		{
        		$("#unassignbtn").hide();
        		}
        
       
        	else if ($scope.alert.alert_assignee=="")
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
        	if(flag=="0")
        		{
        		 $scope.newassignee="loggeduser";
        		}
        	var resu = alertDetailsService.assignsaveaction(alertID,$scope.newassignee).success(
   	             function(data) {
   	            	if(data!="No data available"){
   	            	var res=data.split(" ")
   	            	$scope.alert.alert_assignee=res[0];
   	            	$scope.alert.assigned_by=res[1];
   	            	console.log(res[0]);
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
        $scope.getQuestionAnss= function(){	
        	dataservices.getnid($scope.kpi_id,$scope.flowComponentId).success(
                    function(data) {
                    	//console.log(data)
                    	 $scope.answer={};
                   		 $scope.showAnsLoad=true;
                   		 if(data!="0"){
                   			 
                   		 
                   			var params={"nid" : data};
                   			
                   			faqService
                   			.getQuestionAns(params).then(function(res){
                   				if(res!=null){
                   				 		$scope.mainquery= res[0];
                   				 		$scope.mainquerypresent= true;
                   				 		
                   				}
                   				else{
                   					$scope.mainquerypresent= false;
                   				}
                   				 		$scope.showAnsLoad=false;
                   						}, function(){
                   						 // console.log('getting data failed');
                   						  $scope.showAnsLoad=false;
                   						});  
                   		 }});
   		
   		 }; 
$scope.getQuestionsOnLoad= function(){
    		
    		
    		$scope.showLoad=false;
    		$scope.getQuestionAnss();
    		 
    		var params={"searchFilter" : $scope.alertComponent};
    		$scope.showLoad=true;	
    		faqService
    		.getQuestions(params).then(function(data){
    					   //console.log('queries '+data);
    					   $scope.queries= data;
    					   $scope.showLoad=false;
    					}, function(){
    					  //console.log('getting data failed');
    					   $scope.showLoad=false;
    					});  
    		faqService
    		.getRKMData( $scope.alertComponent).then(function(data){
    					  // rkmdata=[];
    					   $scope.rkmqueries=[];
    			angular.forEach(data, function(value, key) {
    				
    				if(value!="No entry was found for the given criteria" && value!="The request does not contain any parameters added"){
    					//rkmdata.push(value);
    					value.linkID = value.Article_ID.split("|")[1];
    					  $scope.rkmqueries.push(value);
    					   $scope.showLoad=false;
    			}
    			});
    			//$scope.rkmqueries=rkmdata;
    					}, function(){
    					  //console.log('getting data failed');
    					   $scope.showLoad=false;
    					});  
    		
    	 };
    	 
        $scope.getQuestions= function(){
    		
    		if($scope.searchText == ""){
    			$scope.queries=[];
    			$scope.rkmqueries=[];
    			return;
    		}
    		$scope.showLoad=false;
    		var params={"searchFilter" : $scope.searchText};
    		$scope.showLoad=true;	
    		
    	
    	 
    	
    		faqService
    		.getQuestions(params).then(function(data){
    					   //console.log('queries '+data);
    					   $scope.queries= data;
    					   $scope.showLoad=false;
    					}, function(){
    					  //console.log('getting data failed');
    					   $scope.showLoad=false;
    					}); 
    		faqService
    		.getRKMData( $scope.searchText).then(function(data){
    				//rkmdata=[];	
    			$scope.rkmqueries=[];
    			angular.forEach(data, function(value, key) {
    				
    				if(value!="No entry was found for the given criteria" && value!="The request does not contain any parameters added"){
    				//rkmdata.push(value)
    					value.linkID = value.Article_ID.split("|")[1];
    					
    					   $scope.rkmqueries.push(value);
    					   $scope.showLoad=false;
    			}
    			});
    			
    			//$scope.rkmqueries=rkmdata
    		}, function(){
    					  //console.log('getting data failed');
    					   $scope.showLoad=false;
    					});  
    		
    	 };
    	 
    	 
    	
    	 $scope.getQuestionAns= function(questionId){	
    		 $scope.answer={};
    		 $scope.showAnsLoad=true;
    			var params={"nid" : questionId};				
    			faqService
    			.getQuestionAns(params).then(function(data){
    				
    				 		$scope.answer= data[0];
    				 		//console.log($scope.answer['ke #']);
    				 		$scope.showAnsLoad=false;
    						}, function(){
    						 // console.log('getting data failed');
    						  $scope.showAnsLoad=false;
    						});  
    		 }; 
    	 
    	
        $scope.locatesme = function(trackID){
        	
        	alertDetailsService.smelocatoraction(trackID).success(
      	             function(data) {
      	            	
      	            	 if(data!="")
      	            		 {
      	            	$scope.smeList=data[0].userIds;
      	            		//$scope.smeList=data;
      	            		
      	            	getjabberstatus({id:'#sme_jabber_table',values:$scope.smeList,createRoom:true,room_id:$scope.alertID});
      	            		 }
      	            	
      	             });
        }
        
        var pushArray = [];
        var arrData = [];
        $scope.dropdowns=[];
        Highcharts.theme = {
        		   colors: ["#7798BF", "#90ee7e", "#f45b5b", "#aaeeee", "#ff0066", "#eeaaee",
        		      "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
        		   chart: {
        		      backgroundColor: {
        		         linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },
        		         stops: [
        		            [0, '#fff'],
        		            [1, '#fff']
        		         ]
        		      },
        		      style: {
        		         fontFamily: "'Unica One', sans-serif"
        		      },
        		      plotBorderColor: '#606063'
        		   },
        		   title: {
        		      style: {
        		         color: '#E0E0E3',
        		         textTransform: 'uppercase',
        		         fontSize: '14px'
        		      }
        		   },
        		   subtitle: {
        		      style: {
        		         color: '#E0E0E3',
        		         textTransform: 'uppercase'
        		      }
        		   },
        		   xAxis: {
        		      gridLineColor: '#707073',
        		      labels: {
        		         style: {
        		            color: '#E0E0E3'
        		         }
        		      },
        		      lineColor: '#707073',
        		      minorGridLineColor: '#505053',
        		      tickColor: '#707073',
        		      
        		      title: {
        	                enabled: true,
        	                text: 'Time',
        	                style: {
        	                    fontWeight: 'normal',
        	                    color: '#A0A0A3'
        	                }
        		   }
        		   },
        		   yAxis: {
        		      gridLineColor: '#707073',
        		      labels: {
        		         style: {
        		            color: '#E0E0E3'
        		         }
        		      },
        		      lineColor: '#707073',
        		      minorGridLineColor: '#505053',
        		      tickColor: '#707073',
        		      tickWidth: 1,
        		      title: {
        		         style: {
        		            color: '#A0A0A3'
        		         }
        		      }
        		   },
        		   tooltip: {
        		      backgroundColor: 'rgba(0, 0, 0, 0.85)',
        		      style: {
        		         color: '#F0F0F0'
        		      }
        		   },
        		   plotOptions: {
        		      series: {
        		         dataLabels: {
        		            color: '#B0B0B3'
        		         },
        		         marker: {
        		            lineColor: '#333'
        		         },
        		         point: {
                             events: {
                                 click: function (e) {
                                	
                                    var pastID=e.point.id;
                                     
                                      var resu = alertDetailsService.alertdetailsaction( pastID).success(
                function(data) {
                	
                	if(data.result!="No data available"){
                		
                		angular.forEach(data[0].alert_comments, function(value, key) {
                    		
                    		timestamp = moment.unix(value.TIME).fromNow()
                    		//data[0].ALERT_ISSUE[key].TIME= timestamp.format('MMMM Do YYYY, h:mm:ss a');
                    		data[0].alert_comments[key].TIME= timestamp;
                    		});
                    	$scope.pastComments=data[0].alert_comments;
                	}
                	
                                 });}
                             }
                         }
        		      },
        		      boxplot: {
        		         fillColor: '#505053'
        		      },
        		      candlestick: {
        		         lineColor: 'white'
        		      },
        		      errorbar: {
        		         color: 'white'
        		      }
        		   },
        		   legend: {
        		      itemStyle: {
        		         color: '#E0E0E3'
        		      },
        		      itemHoverStyle: {
        		         color: '#FFF'
        		      },
        		      itemHiddenStyle: {
        		         color: '#606063'
        		      }
        		   },
        		   credits: {
        		      style: {
        		         color: '#666'
        		      }
        		   },
        		   labels: {
        		      style: {
        		         color: '#707073'
        		      }
        		   },

        		   drilldown: {
        		      activeAxisLabelStyle: {
        		         color: '#F0F0F3'
        		      },
        		      activeDataLabelStyle: {
        		         color: '#F0F0F3'
        		      }
        		   },

        		   navigation: {
        		      buttonOptions: {
        		         symbolStroke: '#DDDDDD',
        		         theme: {
        		            fill: '#505053'
        		         }
        		      }
        		   },

        		   // scroll charts
        		   rangeSelector: {
        		      buttonTheme: {
        		         fill: '#505053',
        		         stroke: '#000000',
        		         style: {
        		            color: '#CCC'
        		         },
        		         states: {
        		            hover: {
        		               fill: '#707073',
        		               stroke: '#000000',
        		               style: {
        		                  color: 'white'
        		               }
        		            },
        		            select: {
        		               fill: '#000003',
        		               stroke: '#000000',
        		               style: {
        		                  color: 'white'
        		               }
        		            }
        		         }
        		      },
        		      inputBoxBorderColor: '#505053',
        		      inputStyle: {
        		         backgroundColor: '#333',
        		         color: 'silver'
        		      },
        		      labelStyle: {
        		         color: 'silver'
        		      }
        		   },

        		   navigator: {
        		      handles: {
        		         backgroundColor: '#666',
        		         borderColor: '#AAA'
        		      },
        		      outlineColor: '#CCC',
        		      maskFill: 'rgba(255,255,255,0.1)',
        		      series: {
        		         color: '#7798BF',
        		         lineColor: '#A6C7ED'
        		      },
        		      xAxis: {
        		         gridLineColor: '#505053'
        		      }
        		   },

        		   scrollbar: {
        		      barBackgroundColor: '#808083',
        		      barBorderColor: '#808083',
        		      buttonArrowColor: '#CCC',
        		      buttonBackgroundColor: '#606063',
        		      buttonBorderColor: '#606063',
        		      rifleColor: '#FFF',
        		      trackBackgroundColor: '#404043',
        		      trackBorderColor: '#404043'
        		   },
        		   global: {
        	            //timezoneOffset: 7 * 60
        			   useUTC: false
        	        },
        		   // special colors for some of the
        		   legendBackgroundColor: 'rgba(0, 0, 0, 0.5)',
        		   background2: '#505053',
        		   dataLabelsColor: '#B0B0B3',
        		   textColor: '#C0C0C0',
        		   contrastTextColor: '#F0F0F3',
        		   maskColor: 'rgba(255,255,255,0.3)'
        		};

        		// Apply the theme
        		Highcharts.setOptions(Highcharts.theme);
        		
        		
        		
        		

        		
        		//var chart = new Highcharts.Chart({
        		    
        	     var options= {   
        	    	 chart: {
        	            //renderTo: 'containerZ',
        	    		 height: 200,
        	            zoomType: 'xy'
        				
        	        },
        			title: {
        					text: 'Trending'
        				},
        	        xAxis: {
        	            type: 'datetime',
        				dateTimeLabelFormats : {
        					/*hour: '%H:%M',
        					minute: '%I:%M'*/
        					 hour: '%I %p',
        				        minute: '%I:%M %p'
        				}
        	        }/* ,
        	        plotOptions: {
        	            spline: {
        	                
        	                marker: {
        	                    enabled: true
        	                },
        	                pointInterval: 3600000, // one hour
        	            }
        	        } */,
        	        series: [{
        				name:"Trend Value",
        	            data: [] //arrData
        	        }]
        	     };
                var resu = alertDetailsService.alerttrendsaction( $scope.alertID).success(
                        function(data) {
                        	var arrData=[];
                        	if(data.result!="No data available"){
                        	for(var i=0; i<data[0].trend_data.length; i++){
                 			 	//var theTime = data[0].TREND_DATA[i].TIME_STAMP.split(":");
                 				//var theDate = data[0].TREND_DATA[i].DATE_STAMP.split("-");
                 				//var trendDate = new Date(theDate[2],theDate[1]-1,theDate[0],theTime[0],theTime[1].substr(0,2));
                 				//pushArray = [Date.UTC(parseFloat(trendDate.getFullYear()),parseFloat(trendDate.getMonth()),parseFloat(trendDate.getDate()),parseFloat(trendDate.getHours()),parseFloat(trendDate.getMinutes())),parseFloat(data[0].TREND_DATA[i].DATUM)];
                 				//console.log("pushArray--"+pushArray);
                        		pushArray=[(data[0].trend_data[i].trend_create_date_unx)*1000,parseFloat(data[0].trend_data[i].current_value)];
                 				arrData.push(pushArray);
                 				options.series[0].data = arrData;
                 				//console.log("arrData--"+arrData);
                 			 }
                        	$scope.alertTrend=arrData;
                        	$('#containerZ').highcharts(options);
                        	}
                        });
                
	  
	  

 $scope.save_comment = function() {
	 
	 var resu = alertDetailsService.commentssaveaction($scope.comment,$scope.alertID).success(
             function(data) {
            	 
            	 if($scope.alertComments.length>0){
            		 timestamp = moment.unix(data[0].TIME).fromNow();
             		//data[0].TIME= timestamp.format('MMMM Do YYYY, h:mm:ss a');
            		 data[0].TIME= timestamp;
            	 $scope.alertComments.push(data[0]);
            	 }
            	 else
            		 {
            		 timestamp = moment.unix(data[0].TIME).fromNow();
              		//data[0].TIME= timestamp.format('MMMM Do YYYY, h:mm:ss a');
            		 data[0].TIME= timestamp;
            		 $scope.alertComments=data;
            		 }
             	
             });
 };
 /*$scope.fetchpastcomments = function(pastalertID) {
	 alertDetailsService.alertdetailsaction(pastalertID).success(
             function(data) {
             	
             	if(data.result!="No data available"){
             	
             	angular.forEach(data[0].ALERT_ISSUE, function(value, key) {
             		
             		timestamp = moment.unix(value.TIME).fromNow()
             		//data[0].ALERT_ISSUE[key].TIME= timestamp.format('MMMM Do YYYY, h:mm:ss a');
             		data[0].ALERT_ISSUE[key].TIME= timestamp;
             		});
             	$scope.alertComments=data[0].ALERT_ISSUE;
             	
                 		
             	}});
 };*/
$scope.closealert = function(alertID) {
	
	var re = alertDetailsService.closealertaction(alertID).success(
            function(data) {
           	
            });
 };
 
 
 //createcasecontroller
// scope.remedyQueue = [];
	$scope.problemDescription="";
	$scope.successMessage="";
	$scope.caseAddLoader=false;
	/*scope.getRemedyQueue=function(){
		caseService
		.getRemedyQueue().then(function(data){
					  // console.log('remedies '+data);
					   scope.remedyQueue= data;
					}, function(){
					  console.log('getting data failed');
					}); 
	};
	*/
	$scope.createCase=function(){
		//console.log("creating case");
		$scope.caseAddLoader=true;
		var params={"assignedGroupId":"GSE-BCC",
 			"description":$scope.problemDescription};
		//console.log($scope.problemDescription)
		caseService
		.createCase(params).then(function(data){
						//console.log(data)
						$scope.problemDescription="";
						$scope.htmlSnippet="Case created sucessfully, Id is : <a target='_blank' href='https://remedy.cloudapps.cisco.com/fetchIncidentInfo.do?case="+data.Message+"'>"+data.Message+"</a>";
						$scope.caseAddLoader=false;
					}, function(){
						$scope.caseAddLoader=false;
						$scope.successMessage="Network error!";						
						//console.log('getting data failed');
					});		
	};
	
	$scope.openCaseClick=function(){
	$scope.successMessage="";
		$scope.selected = {}
		$scope.problemDescription="";
		$('#case_modal').modal('show');
	};
	/*scope.getRemedyQueue();*/

})
.controller('faqController',['$scope','faqService',function(scope,faqService){
	
	scope.searchText="";
	scope.showLoad=false;
	scope.queries=[
					"How to check order status?",
					"Where is order# 103456789 stuck?",
					"Why is order# 245356798 on hold?",
					"How to get access to DISE?",
					"Why am I not able to access OTS reports?",
					"When is next IT freeze?"
	];
	scope.getQuestions= function(){
	//	alert(scope.alertComponent)
		if(scope.searchText == ""){
			scope.queries=[];
			return;
		}
		scope.showLoad=false;
		var params={"searchFilter" : scope.searchText};
		scope.showLoad=true;	
		faqService
		.getQuestions(params).then(function(data){
					   //console.log('queries '+data);
					   scope.queries= data;
					   scope.showLoad=false;
					}, function(){
					  //console.log('getting data failed');
					   scope.showLoad=false;
					});  
	 };
	 
	 scope.answer={};
	 scope.showAnsLoad=false;
	 scope.getQuestionAns= function(questionId){	
		 scope.answer={};
		 scope.showAnsLoad=true;
			var params={"nid" : questionId};				
			faqService
			.getQuestionAns(params).then(function(data){
				 		scope.answer= data[0];
				 		//console.log(scope.answer['ke #']);
				 		 scope.showAnsLoad=false;
						}, function(){
						 // console.log('getting data failed');
						  scope.showAnsLoad=false;
						});  
		 }; 
	 
	
	
}]);