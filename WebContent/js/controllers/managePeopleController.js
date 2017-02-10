
BCCapp.controller('managePeopleController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','$window','$location','manageRoster',function($rootScope, $scope, $http, $timeout, $compile,$filter,$window,$location,manageRoster) { 
    $scope.app={"id":"610","label":"Tax Finance Invoicing"};
	$scope.selected= manageRoster.track.getData();
    $scope.selectedGroup= { AG_name: '' };
    $scope.groups=[];

    $scope.selection=[];
    $scope.userchoise={way: ''};
     

  $scope.$on('$viewContentLoaded', 
			function(event){
	   	
	        if($scope.selected){
	        	
	        $scope.groups=$filter('filter')($scope.examples, {applications:{id: $scope.app.id}},true);
	     
	        
	        console.log("Get data")
	        ;}
			else{ 
			$location.url('roster/manageTrack');
			};
	        var selectedGroup=manageRoster.chosenAG.getData();
	        if(selectedGroup){
	    		    	$scope.selectedGroup=selectedGroup;
		     }	
		},function(){
		console.log("Failed");
		});
		

 
	 
	   $scope.loadMembers = function() {
		   if(Object.keys($scope.selection).length!=0){
			  manageRoster.chosenAG.sendData($scope.selectedGroup);
			  manageRoster.chosenMembers.sendData($scope.selection);
		      
			  $location.url('roster/manageShift');
			   }
		 
		  else{ 
			     alert("Pleasse, select users using mailer or AD");	
		        };
			  
	   };
	
	
	   $scope.back=function(){
		   $location.url('roster/manageTrack');
		   console.log("Load Track Page");
		   
		},function(){
			console.log("Fail");
		} ;	 
		
/*	 	manageRoster.clientList.getGroupDetails('OPS').then(function(data){
  			  {
  		  console.log(data);
  		  $scope.test=data;
  		//  $scope.groupMembers=data.groupMember;
  			  }});*/
		 $scope.groupMembers=[];
		$scope.fetchMembers =function(){
				 	  var temp={};
					  if($scope.userchoise.way=="maileralias")
						  {
						  temp['userId']=$scope.userchoise.way;
						  temp['memberOf']=$scope.MailerGroupName;
						  $scope.showDataLoader=true;
						  manageRoster.application.get_method(temp).success(function(data){
							  $scope.groupMembers=data;
							  $scope.showDataLoader=false;
							  console.log(data);
						  })
						  }
					  else
						  {
						  temp['userId']=$scope.userchoise.way;
						  temp['memberOf']=$scope.ADGroupName;
						  $scope.showDataLoader=true;
						  manageRoster.application.get_method(temp).success(function(data){
							  $scope.groupMembers=data;
							  $scope.showDataLoader=false;
						  })
						  }
					
					  }		
		
		  $scope.selectedMembers = function selectedMembers() {
			    return filterFilter($scope.groupMembers, { conflict: true });
			  };

			  // watch names for changes
			  $scope.$watch('groupMembers|filter:{conflict:true}', function (nv) {
			    $scope.selection = nv.map(function (groupMember) {
			      return groupMember;
			    });
			  }, true);
		  

		
}]);