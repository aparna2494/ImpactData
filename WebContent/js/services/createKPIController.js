BCCapp.controller('createKPIController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','KPIDetails',function($rootScope, $scope, $http, $timeout, $compile,$filter,KPIDetails) { 
	$scope.collection="bp_flow_data";
	$scope.collectionStage="bp_flow_data_stage";
	$scope.selectedTrack='';
	$scope.selectedKPI='';
	$scope.selectedComponent='';
	$scope.selectedDB='';
	$scope.querySelect='';
	$scope.queryFrom='';
	$scope.queryWhere='';
	$scope.selectedFrequencyHour='';
	$scope.selectedFrequencyMinutes='';
	$scope.kpi_data={threshold:
		                 {Critical:'',High:'',Medium:''}
		                ,
		                   kpi_status: ''};
	$scope.loadspiner=false;
	
	
/*	KPIDetails.kpiMapping({}).then(function(data){	 	 
		$scope.KPInames=data;
	});*/
	
	KPIDetails.getTracks({collection: $scope.collection}).then(function(data){	 	 
		$scope.KPItracks=data.data;
	});
	
	KPIDetails.DBnames({}).then(function(data){
		$scope.DBnames=data;
	});
	
	
	$scope.load = function(){
		KPIDetails.getComponents({collection: $scope.collection, track_name: $scope.selectedTrack.track_name}).then(function(data){	 	 
			$scope.KPIcomponents=data.data;
		});
	};
	

	
	  $scope.filterValue = function($event){
	        if(isNaN(String.fromCharCode($event.keyCode))){
	            $event.preventDefault();
	        }
	};
	$scope.test={
			validation: "[a-zA-Z0-9\-\ ]*"
	};
	
	
	$scope.save = function(form){
		$scope.result=false;
		$scope.loadspiner=true;
		$scope.frequency=($scope.selectedFrequencyHour*3600+$scope.selectedFrequencyMinutes*60);
		$scope.query='SELECT '+$scope.querySelect+' FROM '+$scope.queryFrom+' WHERE '+$scope.queryWhere;
		if(form){
			KPIDetails.saveKPI({collection_name: $scope.collection, track_name: $scope.selectedTrack.track_name, component_name: $scope.selectedComponent.component_name,
				critical: $scope.kpi_data.threshold.Critical,
				high: $scope.kpi_data.threshold.High,
				medium: $scope.kpi_data.threshold.Medium,
				kpi_status: $scope.kpi_data.kpi_status,
				kpi_name: $scope.selectedKPI,
				DB_NAME: $scope.selectedDB.DB_NAME,
				query: $scope.query,
				frequency: $scope.frequency,
				jobupdate: true});
			KPIDetails.saveKPI({collection_name: $scope.collectionStage, track_name: $scope.selectedTrack.track_name, component_name: $scope.selectedComponent.component_name,
				critical: $scope.kpi_data.threshold.Critical,
				high: $scope.kpi_data.threshold.High,
				medium: $scope.kpi_data.threshold.Medium,
				kpi_status: $scope.kpi_data.kpi_status,
				kpi_name: $scope.selectedKPI,
				jobupdate: false});
			    $scope.loadspiner=false;
			    $scope.result=true;
		}
		else{
			$scope.loadspiner=false;
			$scope.error=true;
			console.log("error");
		}
	};
	$scope.isComplited = function() {
	    if($scope.kpi_data.kpi_status && $scope.selectedKPI){
	    	return false;
	    }
	    else return true;
	};
	
	
	

}])