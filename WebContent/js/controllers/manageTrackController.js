
BCCapp.controller('manageTrackController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','$location','manageRoster',function($rootScope, $scope, $http, $timeout, $compile,$filter,$location,manageRoster) { 
   
   $scope.applications=[];
   $scope.selectedAG='';
   $scope.selectedApp='';   
   $scope.userchoise={application: false, AG:false};
   $scope.AGs=[];
   
   manageRoster.application.getapplicationlist().then(function(data){
	    	 
		$scope.applications=data.data;
		 var selectedApp= manageRoster.track.getData();
		    if(selectedApp){
		    	$scope.selectedApp=selectedApp;
		    }	
	},function(){
		console.log("getting applications request failed");
	});
   
   manageRoster.ag.getAssignedGroup().then(function(data){
	   $scope.AGs=data.data;
   });
  
   $scope.selectedIndex = '';

   $scope.select= function(index) {
      $scope.selectedIndex = index; 
   };
   
  /* $scope.getApplication=function(){		
		$scope.selectedApp=$scope.applications;
		console.log($scope.selectedApp);//get id of selected application by user
	};*/
  
   $scope.send=function(){
	   if($scope.userchoise.AG)
		       {
	            manageRoster.track.sendData({AG:{name:$scope.selectedAG.assignedGroup}});
	            }
	     
	   else if($scope.userchoise.application)
	          {
		          manageRoster.track.sendData({application:{name: $scope.selectedApp.label}});
	           }
	   manageRoster.viewState.sendData(true);
	   $location.url('roster/managePeople');

	},function(){
		console.log("Fail");
	} ;	 
	
	$scope.changeAG=function(){
				$scope.userchoise.AG = false;
				$scope.selectedAG = '';
	};
	
	$scope.changeApp=function(){
	         	$scope.userchoise.application = false;
	         	$scope.selectedApp = '';
	};
	
   
}]);