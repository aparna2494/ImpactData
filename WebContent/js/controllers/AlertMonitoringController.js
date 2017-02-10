angular.module('BCCapp').controller('AlertMonitoringController', function($rootScope, $scope, $http, $timeout,dataservices) {
    $scope.$on('$viewContentLoaded', function() {   
        // initialize core components
        App.initAjax();
    });

    // set sidebar closed and body solid layout mode
    $rootScope.settings.layout.pageContentWhite = true;
    $rootScope.settings.layout.pageBodySolid = false;
    $rootScope.settings.layout.pageSidebarClosed = false;
    $scope.subunits=[];
    $scope.alerts=[]
  	
    var res = dataservices.alertsmonitoringaction().success(
            function(data) {
            	$scope.subunits=(data[0].SUB_UNITS);
            	/*angular.forEach(data, function(value, key) {
	          		$scope.subunits=(value.SUB_UNITS);	
	          		
	          	});*/
            	
            	
            });
   
    var resu = dataservices.alertsdataaction().success(
            function(data) {
            	//console.log(data)
            	angular.forEach(data, function(value, key) {
	          		$scope.alerts.push(value);	
	          		
	          	});
            	
            });
    
  
		$.ajax({
			url : "http://vm-gsqs-opsplus.cisco.com:8080/BCC_service/rest/open_incidents",
			type : "GET",
			dataType : "json",
			traditional : true,
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				//	alert(result.length);
					var escObj = '';
					
					if(result.length == 0){
						escObj = escObj+ '<div class="item1" ><label><br>No escalations</label></div>';
					} else {
						for(var i =0 ; i < result.length;i++){
					
							var dateStr = result[i].start_time;
							if(dateStr != ""){
								dateStr = dateStr.split(" ");
								var escDate = dateStr[0].split("-");
								var escTime = dateStr[1].split(":");
								var escDateStr = new Date(escDate[0],escDate[1]-1,escDate[2],escTime[0],escTime[1],escTime[2]);
								var todayDate =  new Date();
								var perDay = 1000 * 60 * 60 * 24;
								var diffDay = Math.ceil((todayDate - escDateStr) / perDay);
								//escObj = escObj+ '<div class="item1" ><label style="font-weight:normal;padding-top:8px;font-size:14px;"><a href="http://case/'+result[i].incident_number+'" target="_blank">'+result[i].incident_number+'</a></label><br><label style="font-weight:normal;font-size:12px;">'+result[i].AG+'</label><br><label style="font-weight:normal;font-size:12px;"><b>'+result[i].agent+'</b>&nbsp; is currently working on it</label><br><label style="font-weight:normal;font-size:12px;">BCC Escalated since <label style="color:red;"><b>'+diffDay+'</b></label> day(s)</label></div><hr style="margin-top:10px;margin-bottom:10px;">';
							}
						}
					}
					$('#escalations').append(escObj);
					/*if($('.item1').length > 1){
						$('#escalations').css("height","230px");
						$('#escalations').css("overflow","auto");
					}*/

			},
			error : function() {
				//alert("An error has occured!!!");
			}
		});
	
});