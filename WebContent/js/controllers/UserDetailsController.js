angular.module('BCCapp').controller('UserDetailsController', function($scope,$stateParams,$state,userdetails,userIncidentdetails) {
	$scope.incidentexist="yes";
	$scope.obj={opened:false,endopened:false}
    $scope.starttime=new Date();
    $scope.openCalendar = function(e, date) {
     e.preventDefault();
         e.stopPropagation();

         $scope.obj.opened = true;
         
    };
    $scope.openCalendarend = function(e, date) {
        e.preventDefault();
            e.stopPropagation();

            $scope.obj.endopened = true;
       };
		incidentvaluechange= function(){
		var incidentexists=angular.element('#incidentexistence');
    	if(incidentexists.is(':checked')==true)
    		$scope.incidentexist="yes";
    	else
    		$scope.incidentexist="no";
    	$scope.$apply();
    	
		}
        $scope.UserID = $stateParams.userID;
        $scope.verifynumber=function(){
        	userdetails.verify_incident__number($scope.incidentnumber).success(function(data,response) {  

                // this is only run after getData() resolves
               
               $scope.verifynum="no-error";
        	})
        	.error(function(data,response) {  

                // this is only run after getData() resolves
              
                if(response!="202"){
                	//$("#incidentNumber").addClass('has-error');
                	$scope.verifynum="has-error";
                }
        	});
        }
        clearCallDetails= function(){
        	$scope.affecteduserID="";
           	$scope.incidentnumber="";
           	$scope.callReason="";
           	$scope.starttime="";
           	$scope.endtime="";
            $scope.verifynum="no-error";
        }
        registerCallDetails= function(){
        	userdetails.verify_incident__number($scope.incidentnumber).success(function(data,response) {  

              
               $scope.verifynum="no-error";
               var res=angular.element('#resolved');
           	/*$scope.resolutionvalue=res.is(':checked');*/
           	if(res.is(':checked')==true)
           		$scope.resolutionvalue="yes";
           	else
           		$scope.resolutionvalue="no";
           	
           	var calldetails={affectedUserID:$scope.affecteduserID,callUserID: $scope.UserID,incidentExist:$scope.incidentexist,incidentNumber:$scope.incidentnumber,reason:$scope.callReason,resolution:$scope.resolutionvalue,startTime:String($scope.starttime),endTime:String($scope.endtime)}
           	userdetails.savecalldetail(calldetails);
           	$scope.affecteduserID="";
           	$scope.incidentnumber="";
           	$scope.callReason="";
           	$scope.starttime="";
           	$scope.endtime="";
           	
        	})
        	.error(function(data,response) {  

                if(response!="202"){
                
                	$scope.verifynum="has-error";
                }
        	});
        	
        	
        }
        userdetails.get_method($scope.UserID).success(function(data){
         console.log(data)
         console.log($scope.UserID)
         $scope.caseCountSummary=data;
         $('#timeline_anchor').click(); 
         
        })
        
      
});