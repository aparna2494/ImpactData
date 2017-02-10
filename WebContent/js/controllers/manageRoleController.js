
BCCapp.controller('manageRoleController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','$window','$location','manageRoster',function($rootScope,$scope,$http, $timeout, $compile,$filter,$window,$location,manageRoster) { 

	$scope.fetchMembers =function(){
		  $scope.groupMembers=[];
			  var temp={};
				
			  if($scope.userchoise.way=="maileralias")
				  {
				  temp['userId']=$scope.userchoise.way;
				  temp['memberOf']=$scope.MailerGroupName;
				  manageRoster.application.get_method(temp).success(function(data){
					  $scope.groupMembers=data;
					  console.log(temp);
					  console.log(data);
				  })
				  }
			  else
				  {
				  temp['userId']=$scope.userchoise.way;
				  temp['memberOf']=$scope.ADGroupName;
				  manageRoster.application.get_method(temp).success(function(data){
					  $scope.groupMembers=data;
					  console.log(temp);
					  console.log(data);
				  })
				  }
			
			  }			  


}]);