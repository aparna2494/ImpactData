//angular.module('BCCapp',['ui.calendar'])
BCCapp.filter('capitalize', function() {
    return function(input) {
      return (!!input) ? input.charAt(0).toUpperCase() : '';
    }
});
BCCapp.controller('displayRosterController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','trackService',function($rootScope, $scope, $http, $timeout, $compile,$filter,trackService) { 
	$scope.tracks=[];
	$scope.rosters=[];
	$scope.events = [];
	$scope.AGs=[];
	$scope.calEventsExt=[];
	$scope.trackUsers=[];
	$scope.trackSelected="";
	$scope.agSelected="";
	$scope.application=[];
	 $scope.eventSources=[];

	
	trackService.getAGs({}).then(function(data){
		$scope.AGs=data;
	});
	
	trackService.getApplication({}).then(function(data){
		$scope.tracks=data;
	});
	
 
	
	
   $scope.getTrack=function(){		
				$scope.trackSelected=$scope.tracks.name;
				console.log($scope.trackSelected);	 // put selected Track from unique list to agSelected - not array
			};
		 
		 
     $scope.getAG=function(){		
				$scope.agSelected=$scope.AGs.name;
				console.log($scope.agSelected);	 // put selected Track from unique list to agSelected - not array
			};
	
	 $scope.$watch('trackSelected', function(newValue, oldValue) {
             if(newValue){
            $scope.eventSources.length = 0; // if new value clear array of events 
            $scope.agSelected="";
            trackService.getRoster({application: $scope.trackSelected, fromDate: $scope.fromDate, toDate: $scope.toDate}).then(function(data){
            	$scope.trackUsers = data.data;
            	$scope.calEvents();
        	});
            
      //   	$scope.calEvents();
             console.log("I see a data change!");}   // watch if agSelected is changed or not
     });
	 
	 $scope.$watch('agSelected', function(newValue, oldValue) {
            if(newValue){
         	$scope.eventSources.length = 0; // if new value clear array of events 
         	$scope.trackSelected="";
         	 	trackService.getRoster({AG: $scope.agSelected, fromDate: $scope.fromDate, toDate: $scope.toDate}).then(function(data){
            	$scope.trackUsers = data.data;
            	$scope.calEvents();
        	}); 
         	
      //  	$scope.calEvents();
          console.log("I see a data change!");}   // watch if agSelected is changed or not
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
		 
		 
		 console.log(hours);
		 
	 };
	 $scope.getRandomInt=function() {
		    return Math.floor(Math.random() * (10 - 0 + 1)) + 0; // random function 
		};
	 
	
	 var eventStatusColors = {	
				"0" : "#049FD9",
				"1" : "#64BBE3", 
				"2" : "#ABC233", 
				"3" : "#B6B9BB", 
				"4" : "#C6C7CA", 
				"5" : "#DFDFDF", 
				"6" : "#F2F2F2", 
				"7" : "#14A792",		
				"8" : "#FFCC00",

				}; // array of colors
		
	 	$scope.calEvents=function(){
	 		var tempArray= [];
	 	 		
	 		 angular.forEach($scope.trackUsers,function(dane){ // for each date from DB do
					var day=dane.date; // create new var and write date
					var Z='-0700'; // define california time zone -0700 GMT - PDT
				    var users=dane.users; // get user

				    angular.forEach(users,function(user,key) // for each user under application do
					  {
			            		       	  
			      var startTime= $scope.convertDate(user.shiftStartTime); // convert start time minutes to hours
			      var endTime= $scope.convertDate(user.shiftEndTime); // convert end time
			 	  
			      tempArray.push({
					        
						        title: user.name, 
						        start: moment(day+'T'+startTime+Z), // create an event for uiCalendar
								end: moment(day+'T'+endTime+Z), // use moment to define time with timezone
						        allDay: false,
						        backgroundColor:eventStatusColors[$scope.getRandomInt()],
						        url: "http://wwwin-tools.cisco.com/dir/reports/"+user.name
						    						        
						        
				  })});
				 }); 	

	          $scope.eventSources.push({events:tempArray});
 	
	 	};
	 
	 	
	  
	    $scope.uiConfig = {
	    	      calendar:{
	    	        height: 550,
	    	        editable: false,
	    	        droppable: false,
	    	        header:{
	    	          left: 'title',
	    	          center: 'month,agendaWeek,agendaDay',
	    	          right: 'today prev,next'
	    	        },
	    	        viewRender: function(view, element) {
	    	            $scope.fromDate= moment(view.start._d).format("YYYY-MM-DD");
	    	     	    $scope.toDate= moment(view.end._d).format("YYYY-MM-DD");
	    	        },
	    	      
	    	              }
	    	    };

	  
	    /* event sources array*/


	   // $scope.eventSources = $scope.calEventsExt; // array of events
	    

    
    
}]);