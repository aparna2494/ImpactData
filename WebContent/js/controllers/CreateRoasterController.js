//angular.module('BCCapp',['ui.calendar'])
BCCapp.filter('capitalize', function() {
    return function(input) {
      return (!!input) ? input.charAt(0).toUpperCase() : '';
    }
});
BCCapp.controller('CreateRoasterController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','trackService',function($rootScope, $scope, $http, $timeout, $compile,$filter,trackService) { 
	$scope.shifts=[];
	$scope.tracks=[];
	$scope.events = [];
	$scope.AGS=[];
	$scope.agUsers=[]
	$scope.trackSelected="";
	$scope.selectedAG="";
	$scope.selectedShift={};
	$scope.eventsDate="";
	
	trackService.getTracks({}).then(function(data){
		$scope.tracks=data;		
	},function(){
		console.log("getting tracks request failed");
	});
	
	trackService.getShifts({}).then(function(data){
		$scope.shifts=data;		
		if($scope.shifts.length > 0)
			$scope.selectedShift=JSON.stringify($scope.shifts[0]);
	},function(){
		console.log("getting shifts request failed");
	});
	
	$scope.trackChange=function(){		
		$scope.trackSelected=$scope.tracks[0].trackId;
		$scope.loadAGS();
	};
	
	$scope.agChange=function(){
		$scope.loadEvents();
		$scope.loadUsers();
	};
	
	$scope.getShiftObject=function(){
		try{
        	var shift=JSON.parse(""+$scope.selectedShift);
    	}catch(error){
    		alert("please select shift");
    		return {startTime : "00:00" ,endTime : "023:59"};
    	}
    	var startTime=$scope.minutesToHours(shift.shiftStartTime);
		var endTime=$scope.minutesToHours(shift.shiftEndTime);
		
		return {startTime : startTime ,endTime : endTime};
	}
	$scope.shiftChange= function(){
		var date=$scope.getShiftObject();
		angular.forEach($scope.eventsToDrop, function(eachEvt, key){
			eachEvt.start= new Date(y, m, d,date.startTime.split(":")[0],date.startTime.split(":")[1],0,0);
			eachEvt.end= new Date(y, m, d,date.endTime.split(":")[0],date.endTime.split(":")[1],0,0);			
		});	
	};
	
	$scope.loadAGS=function(){		
		$scope.AGS = $filter('filter')($scope.tracks, { trackId: parseInt($scope.trackSelected) }, true)[0].assignedGroups;		
	};
	$scope.getRandomInt=function() {
	    return Math.floor(Math.random() * (10 - 0 + 1)) + 0;
	};
	$scope.minutesToHours=function(minutes){
		 var hours = Math.floor( minutes / 60);          
		 var minutes =minutes % 60;
		 return hours + ":" + minutes;
	};
	$scope.loadUsers=function(){
		$scope.eventsToDrop=[];
		if($scope.AGS.length > 0)
		var users=$filter('filter')($scope.AGS, { id: parseInt($scope.selectedAG) }, true)[0].users;//$scope.selectedAG
		angular.forEach(users,function(user,index){
			$scope.eventsToDrop.push({
				id: user.userId,
				title: user.userId,
				start: new Date(),
				end: new Date(),
				backgroundColor: eventStatusColors[$scope.getRandomInt()]
			});
		});		
		
	};
	
	$scope.loadEvents=function(){
		//$scope.events=[];
		var params={month:4,agid:$scope.selectedAG};
		trackService.getEvents(params).then(function(data){		
			$scope.eventsDate=data[0].date;
			var date =data[0].date.split("-");
				var d =date[2];
			    var m = date[1]-1;
			    var y = date[0];
			    var events=[];
				angular.forEach(data[0].users,function(obj,i){
					var startTime=$scope.minutesToHours(obj.shiftStartTime);
					var endTime=$scope.minutesToHours(obj.shiftEndTime);
					$scope.events.push({
						id: obj.name,
						title: obj.name+" "+startTime +" "+ endTime,
						start: new Date(y, m, d,startTime.split(":")[0],startTime.split(":")[1],0,0),
						end: new Date(y, m, d,endTime.split(":")[0],endTime.split(":")[1],0,0),
						backgroundColor: eventStatusColors[$scope.getRandomInt()]
					});
				});
				$scope.eventSources=[events]; 
			   
		},function(){
			console.log("getting events request failed");
		});
		
	};
	
	$scope.eventsToDrop = [];
	var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    $scope.dropEvtRepeatFinished = function(){
		$(".draggable_li").draggable({
			revert:true
		});		
    };
    
    $scope.externalEventDropped = function(date, jsEvent, ui, resourceId ){
    	try{
        	var shift=JSON.parse(""+$scope.selectedShift);
    	}catch(error){
    		alert("please select shift");
    		return;
    	}
    	var dateString=date._d.getFullYear()+"-"+(date._d.getMonth() + 1)+ "-" +(date._d.getDate()+1);
    	var userId="";
    	var droppedEvtObject = $(this).data("event");
    	angular.forEach($scope.eventsToDrop, function(eachEvt, key){
    		if(eachEvt.id == droppedEvtObject.id){
    			console.log($scope.eventsToDrop[key]);
    			userId=$scope.eventsToDrop[key].id;
    			$scope.eventsToDrop.splice(key,1); 
    			trackService.updateRoaster({"shiftId":shift.shiftId,"userId":userId,"startShiftTime":shift.shiftStartTime,"endShiftTime":shift.shiftEndTime,"agid":$scope.selectedAG,"date":dateString})
    			.then(function(data){
    				console.log("reponse from "+data);	
    			},function(){
    				console.log("adding roaster request failed");
    			});
    			
    			console.log("evnt dropped");
    		}
    	});    	
    };
	var eventStatusColors = {	
			"0" : "#FF6B6B",
			"1" : "#52E3D2", 
			"2" : "#00BCD5", 
			"3" : "red", 
			"4" : "#D8437D", 
			"5" : "#DADD06", 
			"6" : "#D82205", 
			"7" : "#F49612",		
			"8" : "#8E4497",
			"9" : "#1B39A8",
			"10" : "#01A896",
			"11" : "#2196F3",
			"12" : "#0097A7"};
	
	/*for(var i=1; i<=10; i++){
		$scope.events.push({
			id: 'EVT'+i,
			title: 'My event'+i,
			start: new Date(y, m, i),
			end: new Date(y, m, i+1),
			backgroundColor: eventStatusColors[i]
		});
	}*/
	
	/*for(var i=1; i<=10; i++){
		$scope.eventsToDrop.push({
			id: 'DRP'+i,
			title: 'Drag event'+i,
			start: new Date(y, m, i),
			end: new Date(y, m, i+1),
			backgroundColor: eventStatusColors[i]
		});
	}*/
	
	/*
	
    
    //$scope.changeTo = 'Hungarian';
    /* event source that pulls from google.com 
    $scope.eventSource = {
            url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",
            className: 'gcal-event',           // an option!
            currentTimezone: 'America/Chicago' // an option!
    };
    */
    /* event source that contains custom events on the scope */
    /*
    $scope.events = [
      {title: 'All Day Event',start: new Date(y, m, 1)},
      {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
      {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
      {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
      {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
      {title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
    ];
    */
    /* event source that calls a function on every view switch 
    $scope.eventsF = function (start, end, timezone, callback) {
      var s = new Date(start).getTime() / 1000;
      var e = new Date(end).getTime() / 1000;
      var m = new Date(start).getMonth();
      var events = [{title: 'Feed Me ' + m,start: s + (50000),end: s + (100000),allDay: false, className: ['customFeed']}];
      callback(events);
    };

    $scope.calEventsExt = {
       color: '#f00',
       textColor: 'yellow',
       events: [ 
          {type:'party',title: 'Lunch',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
          {type:'party',title: 'Lunch 2',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
          {type:'party',title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
        ]
    };
    */
    /* alert on eventClick */
    $scope.alertOnEventClick = function( date, jsEvent, view){
        $scope.alertMessage = (date.title + ' was clicked ');
    };
    /* alert on Drop */
     $scope.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view){
       $scope.alertMessage = ('Event Droped to make dayDelta ' + delta);
    };
    /* alert on Resize */
    $scope.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view ){
       $scope.alertMessage = ('Event Resized to make dayDelta ' + delta);
    };
    /* add and removes an event source of choice */
    $scope.addRemoveEventSource = function(sources,source) {
    	alert("hi");
      var canAdd = 0;
      angular.forEach(sources,function(value, key){
        if(sources[key] === source){
          sources.splice(key,1);
          canAdd = 1;
        }
      });
      if(canAdd === 0){
        sources.push(source);
      }
    };
    /* add custom event*/
   /* $scope.addEvent = function() {
      $scope.events.push({
        title: 'Open Sesame',
        start: new Date(y, m, 28),
        end: new Date(y, m, 29),
        className: ['openSesame']
      });
    };*/
    /* remove event */
    /*$scope.remove = function(index) {
    	alert(""+index);
      $scope.events.splice(index,1);
    };*/
    /* Change View */
    $scope.changeView = function(view,calendar) {
      uiCalendarConfig.calendars[calendar].fullCalendar('changeView',view);
    };
    /* Change View */
    $scope.renderCalender = function(calendar) {
      if(uiCalendarConfig.calendars[calendar]){
        uiCalendarConfig.calendars[calendar].fullCalendar('render');
      }
    };
     /* Render Tooltip */
    $scope.eventRender = function( event, element, view ) { 
        element.attr({'tooltip': event.title,
                     'tooltip-append-to-body': true});        
        element.append( "<span class='closeEvnt'>X</span>" );
        element.find(".closeEvnt").click(function() {
        	$('#myCalendar').fullCalendar('removeEvents',event._id);
        	alert("hello::"+event._id);
        	var eventData=$scope.events[parseInt(""+event._id)-1];
        	trackService.removeRoaster({"userId":eventData.id,"agid":$scope.selectedAG,"date":$scope.eventsDate})
			.then(function(data){
				console.log("reponse from "+data);	
			},function(){
				console.log("remove roaster request failed");
			});
        	
        	$('.tooltip').remove();
        });
        $compile(element)($scope);
    };

    /* config object */
    $scope.uiConfig = {
      calendar:{
        height: 550,
        editable: true,
        droppable: true,
        header:{
          left: 'title',
          center: 'month,agendaWeek,agendaDay',
          right: 'today prev,next'
        },
        drop: $scope.externalEventDropped,
        eventClick: $scope.alertOnEventClick,
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize,
        eventRender: $scope.eventRender
      }
    };
    
    /* event sources array, can be comma separated multiple events array objects*/
    $scope.eventSources = [$scope.events];
    
    //$scope.eventSources = [$scope.events, $scope.eventSource, $scope.eventsF];
    //$scope.eventSources2 = [$scope.calEventsExt, $scope.eventsF, $scope.events];
    
}]);