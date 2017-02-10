angular.module('BCCapp').controller('customHeaderController',['$scope','$state','autocomplete', function($scope,$state,autocomplete) {
		
	
	autocomplete.getSearchIndex().success(function(data){

		$scope.availableTags=[]
		angular.forEach(data, function(value, key) {
				
		if(value.category=="user")
			{

			value.pic="assets/global/img/user.png"
			}
		else if(value.category=="country"){
			
			value.pic="assets/global/img/globe1.png"
		}
		$scope.availableTags.push(value)
		});
		
    	
		
    })
    $scope.selectedValueFunction =function(selected)
    {
		
		console.log(selected)
		console.log(selected && typeof selected.originalObject=="object")
		var check_incident=selected.originalObject
		var flag_incident=false
		
		if(typeof selected.originalObject=="string" && check_incident.substring(0, 3).toUpperCase()=="INC" && check_incident.length>=10)
			{
			
			flag_incident=true
			}
		   if(selected){
		if (!selected.title)
        	{
			if(flag_incident)
			{
			 $state.go('incidentdetails', {'incidentNumber':selected.originalObject});
			}
			else
				{
        	autocomplete.verifyUser(selected.originalObject).success(function(data)
        	{
        		
        		if(data.status==true)
        			{
        			$state.go('userdetails', {'userID':selected.originalObject});
        			}
        		else if(flag_incident)
        			{
        			 $state.go('incidentdetails', {'incidentNumber':selected.originalObject});
        			}
        	})
        	}
        	}
        else
        	{
			if(selected.description.category=="country")
		   {
           $state.go('graphOps',{'countryCode':selected.description.type});
           
		   }
       else if(selected.description.category=="user")
		{
    	   $state.go('userdetails', {'userID':selected.description.type});
    	  
		}
       else if(flag_incident)
    	   {
    	   $state.go('incidentdetails', {'incidentNumber':selected.title});
    	   }
         }
}
    }
	
	
  
}])
	