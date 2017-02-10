BCCapp.controller('updateRosterController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','$location','trackService',function($rootScope, $scope, $http, $timeout, $compile,$filter,$location,trackService) { 
    
	$scope.AGs=[];
	$scope.tracks=[];
	$scope.dates=[];
	$scope.users=[];
	 $scope.agSelected="";
	 $scope.trackSelected="";
	 $scope.dateSelected="";
	
	trackService.getAGs({}).then(function(data){
		$scope.AGs=data;
	});
	
	trackService.getApplication({}).then(function(data){
		$scope.tracks=data;
	});
	trackService.getDates({}).then(function(data){
		$scope.dates=data.data;
	});
	
	$scope.$watch('trackSelected', function(newValue, oldValue) {
        if(newValue&&!$scope.dateSelected){
       $scope.agSelected="";
       trackService.getDates({application: $scope.trackSelected, }).then(function(data){
   		$scope.dates=data.data;
   	});
        }
        else if(newValue&&$scope.dateSelected){
     	   $scope.agSelected="";
        	trackService.getDates({application: $scope.trackSelected}).then(function(data){
           		$scope.dates=data.data;
           	});
     	   $scope.users="";
        	trackService.getRosterDate({application:$scope.trackSelected, Date: $scope.dateSelected}).then(function(data){
        		var usersArray=[]
        		usersArray=data.data;
        		for(i=0;i<usersArray.length;i++){
        			usersArray[i].shiftStartTime=$scope.convertDate(usersArray[i].shiftStartTime);
        			usersArray[i].shiftEndTime=$scope.convertDate(usersArray[i].shiftEndTime);
        		}
        		$scope.users=usersArray;
        	}); 
        }
        
	});

    $scope.$watch('agSelected', function(newValue, oldValue) {
       if(newValue&&!$scope.dateSelected){
    	$scope.trackSelected="";
    	trackService.getDates({AG: $scope.agSelected}).then(function(data){
       		$scope.dates=data.data;
       	});
       }
       else if(newValue&&$scope.dateSelected){
    	   $scope.trackSelected="";
       	trackService.getDates({AG: $scope.agSelected}).then(function(data){
          		$scope.dates=data.data;
          	});
    	   $scope.users="";
       	trackService.getRosterDate({AG:$scope.agSelected, Date: $scope.dateSelected}).then(function(data){
       		var usersArray=[]
       		usersArray=data.data;
       		for(i=0;i<usersArray.length;i++){
       			usersArray[i].shiftStartTime=$scope.convertDate(usersArray[i].shiftStartTime);
       			usersArray[i].shiftEndTime=$scope.convertDate(usersArray[i].shiftEndTime);
       		}
       		$scope.users=usersArray;
       	}); 
       }
   });
     $scope.$watch('dateSelected', function(newValue, oldValue) {
      if(newValue&&$scope.trackSelected){
    	  $scope.users="";
    	trackService.getRosterDate({application:$scope.trackSelected, Date: $scope.dateSelected}).then(function(data){
    		var usersArray=[]
    		usersArray=data.data;
    		for(i=0;i<usersArray.length;i++){
    			usersArray[i].shiftStartTime=$scope.convertDate(usersArray[i].shiftStartTime);
    			usersArray[i].shiftEndTime=$scope.convertDate(usersArray[i].shiftEndTime);
    		}
    		$scope.users=usersArray;
    	});
      }
      else if(newValue&&$scope.agSelected){
    	  $scope.users="";
    	  trackService.getRosterDate({AG: $scope.agSelected, Date: $scope.dateSelected}).then(function(data){
    		var usersArray=[]
    		usersArray=data.data;
    		for(i=0;i<usersArray.length;i++){
    			usersArray[i].shiftStartTime=$scope.convertDate(usersArray[i].shiftStartTime);
    			usersArray[i].shiftEndTime=$scope.convertDate(usersArray[i].shiftEndTime);
    		}
    		$scope.users=usersArray;
      	});
      }
      
});
     
     $scope.convertDate=function(date){  // convert minutes to hours
		 
 		
		 var hours;
		 if(date==1440){
			return '23:59';
		 }
		 else if(date==0){
			 return '00:00';
		 }
		 else if(date<600){
			 hours = Math.trunc(date/60);
			 return "0"+hours+':00';
		 }
	     else{
		 hours = Math.trunc(date/60);
		 return hours+':00';
		 }

		 
	 };

}])
