BCCapp.controller('displayDetailsKPIController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','KPIDetails',function($rootScope, $scope, $http, $timeout, $compile,$filter,KPIDetails) { 

	$scope.tracks=[];
	$scope.components=[];
	$scope.selectedTrack={"track_name": ""};
	$scope.mainCollection="bp_flow_data";
	$scope.stageCollection="bp_flow_data_stage";
	$scope.collection="bp_flow_data_stage";
	$scope.kpi={"Critical": "", "Hight": "", "Medium" : "" };
	$scope.loadsearch=true;
	$scope.error=false;             
		
		
	KPIDetails.getTracks({collection: $scope.mainCollection}).then(function(data){
	    	 
		$scope.tracks=data.data;
		
	},function(){
		console.log("getting request failed");
	});
	

	
	$scope.loadKPI=function(){
		$scope.load=true;
		 $scope.loadsearch=true;
		KPIDetails.getKPIdetails({track_name: $scope.selectedTrack.track_name,collection: $scope.mainCollection}).then(function(data){
	 	 
		$scope.components=data.data;
		$scope.resetData = angular.copy($scope.components);
		$scope.load=false;
		 $scope.loadsearch=false;
	});
		
	}
	
	$scope.isEmpty = function(value) {
	    return !value;
	};
    
	 $scope.update = function(track,component,kpi) {
		     $scope.load2=true;
		     $scope.success=false;
		     
	        KPIDetails.updateKPIdetails({collection_name: $scope.mainCollection , track_name: track , 
	      	component_name: component ,
	     	kpi_name: kpi.kpi_name ,
	        kpi_status: kpi.kpi_status, critical: kpi.threshold.Critical , 
	        high: kpi.threshold.High, medium: kpi.threshold.Medium}).then(function(data){
	        	$scope.success=data.data;
	        	$scope.load2=false;
		        },function(){
	        	$scope.error=true;
	        	$scope.load2=false;
	        	console.log("getting request failed");
	    	});
	        
	        KPIDetails.updateKPIdetails({collection_name: $scope.stageCollection , track_name: track , 
		      	component_name: component ,
		     	kpi_name: kpi.kpi_name ,
		        high: kpi.threshold.High, medium: kpi.threshold.Medium}).then(function(data){
		        	$scope.success=data.data;
		        	$scope.load2=false;
		        },function(){
		        	$scope.error=true;
		        	$scope.load2=false;
		        	console.log("getting request failed");
		    	});
	           		KPIDetails.updateQuery({kpi_name: kpi.kpi_name ,component_name: component,track_name: $scope.selectedTrack.track_name ,query: kpi.details.query_string }).then(function(data){
	        			$scope.queryUpdate=data;
	        		});
   
	 };

	  $scope.filterValue = function($event){
	        if(isNaN(String.fromCharCode($event.keyCode))){
	            $event.preventDefault();
	        }
	};
	

	

}]);
