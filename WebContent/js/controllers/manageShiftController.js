BCCapp.controller('manageShiftController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','$location','manageRoster',function($rootScope, $scope, $http, $timeout, $compile,$filter,$location,manageRoster) { 
	 $scope.members=[];
     $scope.AG=[];
	 $scope.selectedDate = {date: new Date()};
	 $scope.today=new Date();
	 $scope.time=new Date();
	 $scope.selectedStart=[];
	 $scope.selectedEnd=[];
     $scope.members=[];
     $scope.finalMembers={};
     $scope.trackArray=manageRoster.track.getData();

     $scope.selectedUsers= manageRoster.chosenMembers.getData();
	 $scope.$on('$viewContentLoaded', 
				function(event){
		          if(!$scope.trackArray&&!$scope.selectedUsers) $location.url('roster/manageTrack');
		          else{
		            $scope.AG=manageRoster.track.getData();
		        
			       angular.forEach($scope.selectedUsers,function(user,key) 
							  {  
						$scope.members.push(
							{
								    userID : user.userID,
								    userName: user.userName,
								    title: user.title,
						            shiftStartTime: '',
						            shiftEndTime: '',
							}
								
							);  
						});  }
			},function(){
			console.log("Failed");
		});
       
	   $scope.selectedData=[];
	   $scope.validation= false; 
	   $scope.submit=function(){
		   $scope.selectedData = '';
		   $scope.selectedDate.date.setMonth( $scope.selectedDate.date.getMonth( ) + 1 );

		   
	   angular.forEach($scope.members,function(user,key){
			   if(user.shiftStartTime&&user.shiftStartTime){
				   return $scope.validation=true;
			   }
			   else{return $scope.validation=false; alert("Please, fill all fields!");}
		   });
		   if($scope.validation){
			   var usersArray=[];
			 
           angular.forEach($scope.members,function(user,key){
        	   usersArray.push(
        				    {
        						   name: user.userID,
        						   shiftStartTime: $scope.convert(user.shiftStartTime),
						           shiftEndTime: $scope.convert(user.shiftEndTime),
        					   }
        				   )});
                   $scope.trackArray[Object.keys( $scope.trackArray)[0]].users = usersArray;
                   $scope.finalMembers =  $scope.trackArray;
                   $scope.selectedData = $scope.finalMembers;
                   $scope.selectedData.date = $scope.selectedDate.date;
            //       manageRoster.application.send_method($scope.selectedData);
		   }
		   $scope.result=true;
	   }; 
            
	       $scope.convert=function(time){
	    	      var splitDate = time.split(":");
	    	      var minutes = parseInt(splitDate[0])*60+parseInt(splitDate[1]);
		          return minutes;
		         
	       };
	 
		   $scope.today = function() {
		    $scope.selectedDate.date = new Date();
		    };
		  
		  $scope.selectedTime = new Date();

		  $scope.hstep = 1;
		  $scope.mstep = 10;

		  $scope.ismeridian = true;
		  $scope.toggleMode = function() {
		    $scope.ismeridian = ! $scope.ismeridian;
		  };
		  
		  $scope.clear = function() {
			    $scope.mytime = null;
			  };
		
		 $scope.back=function(){
			   $location.url('roster/managePeople');
			   console.log("Load People Page");
			   
			},function(){
				console.log("Fail");
			};	 
			
			

	  $scope.dateOptions = {
			    formatYear: 'yyyy',
			    minDate: new Date(),
			    startingDay: 1
			  };
	  $scope.open2 = function() {
		    $scope.popup2.opened = true;
		  };
    
	  $scope.popup2 = {
				    opened: false
				  };
	  var tomorrow = new Date();
	  tomorrow.setDate(tomorrow.getDate() + 1);
	  var afterTomorrow = new Date();
	  afterTomorrow.setDate(tomorrow.getDate() + 1);
	  $scope.events = [
	    {
	      date: tomorrow,
	      status: 'full'
	    },
	    {
	      date: afterTomorrow,
	      status: 'partially'
	    }
	  ];
	  
	  $scope.newRoster= function(){
		  $location.url('roster/manageTrack');
	  };
	  
	  
	  
	  
	  
	  
	  
					
			

}]);