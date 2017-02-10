angular.module('BCCapp').controller('UserIncidentDetailsController', function($scope,$stateParams,$state,$filter,userdetails,userIncidentdetails) {
	
        $scope.UserID = $stateParams.userID;
        
        
        $scope.dataloaded=false
        userIncidentdetails.get_method($scope.UserID).success(function(data){
        	$scope.AllIncidentdetails=[];
        	$scope.Incidentdetails=[];
        	if(data.length==0){
        		$scope.dataloaded=true
        	}
        	angular.forEach(data,function(value,key){
        		if(value.status=="Pending" | value.status=="Assigned" | value.status=="In Progress")
        			{
        			value.status_class="label-warning";
        			}
        		else
        			{
        			value.status_class="label-success";
        			}
        		if(value.cet_value=="1" )
    			{
    			value.cet_class="badge-warning";
    			value.cet_value="Escalated";
    			}
    		else
    			{
    			value.cet_class="label-success";
    			value.cet_value="Not Escalated";
    			}
        		$scope.Incidentdetails.push(value);
        		$scope.AllIncidentdetails.push(value);
        		if(key==data.length-1)
        			{
        			$scope.dataloaded=true
        			}
        	})
        	
        })
        
        $scope.filtertable=function(event)
        {
        	
        	if(!angular.element(event.currentTarget).find('p').hasClass('selected_filter'))
        		{
        		
        	$('.filterClass').each(function(i, obj) {
   
        		$(obj).find('p').removeClass('selected_filter')
        		});
        	angular.element(event.currentTarget).find('p').addClass('selected_filter')
        	$scope.Incidentdetails=$filter('filter')($scope.AllIncidentdetails,function(value, index, array){
        		
        		if(angular.element(event.currentTarget).find('p').data('id').toUpperCase()=="OPEN CASES")
        			{
        		if(value.status=="Pending" | value.status=="Assigned" | value.status=="In Progress")
    			{
    			return true
    			}
        		else
        			{
        			return false
        			}
        	}
        		else if(angular.element(event.currentTarget).find('p').data('id').toUpperCase()=="OPEN ESCALATIONS")
    			{
    		if((value.status=="Pending" | value.status=="Assigned" | value.status=="In Progress") & value.cet_value=="Escalated" )
			{
			return true
			}
    		else
    			{
    			return false
    			}
    	}
        		else if(angular.element(event.currentTarget).find('p').data('id').toUpperCase()=="TOTAL CASES")
    			{
    		return true
    	}
        		else if(angular.element(event.currentTarget).find('p').data('id').toUpperCase()=="TOTAL ESCALATIONS")
    			{
    		if(value.cet_value=="Escalated" )
			{
    			
			return true
			}
    		else
    			{
    			return false
    			}
    	}
        	})
        		}
        	else
        		{
        		$scope.Incidentdetails=$scope.AllIncidentdetails
        		angular.element(event.currentTarget).find('p').removeClass('selected_filter')
        		}
        }
});