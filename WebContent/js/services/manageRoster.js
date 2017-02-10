BCCapp.factory('manageRoster',['$rootScope','$http','$q', 'commonUtils',function($rootScope,$http,$q,commonUtils){
	var factory={};
	factory.getapplicationlist=function()
	{
		return $http.get('/api/applicationlist/details');
	}
	
	var factory1={};
	
	factory1.getAssignedGroup = function() {
		
		return $http.get('/api/AGList/aggroups');
		};
   
		
	//SHARED VARIABLES
	
   	 var service = {};
	  service.data = false;
	  service.sendData = function(data){
	      this.data = data;
	      $rootScope.$broadcast('data_shared');
	  };
	  service.getData = function(){
	    return this.data;
	  };
	  // VIEW STATES FOR MANAGE TRACK/PEOPLE/SHIFTS
	  var view = {};
	  view.data = false;
	  view.sendData = function(data){
	      this.data = data;
	      $rootScope.$broadcast('data_shared');
	  };
	  view.getData = function(){
	    return this.data;
	  };
	  var factory2 = {};
	  //GET GROUP DETAILS FROM DB
	  factory.get_method=function(temp)
		{
			return $http({

			      method: 'POST', url: '/api/ldap/details/',

			      data: JSON.stringify(temp),
			      dataType: 'json',
			      contentType: "application/json"

			    })
			
		};
		
	  factory.send_method=function(roster)
		{
			return $http({

			      method: 'POST', url: '/api/roster/receiveRoster',

			      data: JSON.stringify(roster),
			      dataType: 'json',
			      contentType: "application/json"

			    })
			
		};

			
	 	
	     //SELECTED AG
		var AG = {};
		AG.data = false;
		  AG.sendData = function(data){
		      this.data = data;
		      $rootScope.$broadcast('data_shared');
		  };
		  AG.getData = function(){
		    return this.data;
		  };
		
		 // SELECTED MEMBERS
		var members = {};
			members.data = false;
			  members.sendData = function(data){
			      this.data = data;
			      $rootScope.$broadcast('data_shared');
			  };
			  members.getData = function(){
			    return this.data;
			  };
	
	  	  
	  return {application: factory,ag: factory1, track: service, viewState: view, chosenAG: AG, chosenMembers: members}; 
}])