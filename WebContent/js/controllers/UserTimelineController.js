angular.module('BCCapp').controller('UserTimelineController', function($scope,$stateParams,$state,usertimeline,$filter) {
	
        $scope.UserID = $stateParams.userID;
        $scope.dataloaded=false
        usertimeline.get_method($scope.UserID).success(function(data){
        	
        	$scope.timelineSummary=[];
        	if(data.length==0){
        		$scope.dataloaded=true
        	}
        	//console.log($scope.timelineSummary[0].activitytime)
        	//var d = new Date($scope.timelineSummary[0].activitytime);
        	//console.log(Date.parse($scope.timelineSummary[0].activitytime))
        	angular.forEach(data,function(value,key){
        		//console.log(value) 
        		value.epoche=Date.parse(value.activitytime)
        		$scope.timelineSummary.push(value)
        		if(key==data.length-1)
        			{
        			$scope.timelineSummary=$filter('orderBy')($scope.timelineSummary, 'activitytime')
        			console.log($scope.timelineSummary)
        			$scope.dataloaded=true
        			}
        		
        	})
        	$scope.timelineSummary=$filter('orderBy')($scope.timelineSummary, '-epoche')
        	
        	
        })
        
        /*userIncidentdetails.get_method($scope.UserID).success(function(data){
        	
        	$scope.Incidentdetails=[];
        	angular.forEach(data,function(value,key){
        		if(value.status=="Pending" | value.status=="Assigned" | value.status=="In Progress")
        			{
        			value.status_class="label-warning";
        			}
        		else
        			{
        			value.status_class="label-success";
        			}
        		$scope.Incidentdetails.push(value);
        		
        	})
        	
        })*/
});