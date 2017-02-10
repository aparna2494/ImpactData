
'use strict';

BCCapp.service('trackService', ['$http','$q', 'commonUtils',function($http, $q,commonUtils) {
 	  
	  this.getAGs = function(params) {
		    var deferred = $q.defer();
			commonUtils
			.httpGET("api/roster/agList",params)
			.success(function(data){
		         // With the data succesfully returned, we can resolve promise and we can access it in controller
		         deferred.resolve(data);
		     }).error(function(){
		          //let the function caller know the error
		          deferred.reject();
		     });
		     return deferred.promise;
		  };
		 
		  
		  this.getApplication = function(params) {
			    var deferred = $q.defer();
				commonUtils
				.httpGET("api/roster/applicationList",params)
				.success(function(data){
			         // With the data succesfully returned, we can resolve promise and we can access it in controller
			         deferred.resolve(data);
			     }).error(function(){
			          //let the function caller know the error
			          deferred.reject();
			     });
			     return deferred.promise;
			  };
			  
	 this.getRoster = function(trackName) {
				  return $http({

				      method: 'POST', url: '/api/roster/getRoster',

				      data: JSON.stringify(trackName),
				      dataType: 'json',
				      contentType: "application/json"
				  });}
	this.getDates = function(trackName){
		 return $http({
			 method: 'POST', url: '/api/roster/dateList',
			 data: JSON.stringify(trackName),
		      dataType: 'json',
		      contentType: "application/json"
		 });		
	}
	
	this.getRosterDate = function(trackName){
		 return $http({
			 method: 'POST', url: '/api/roster/getRosterDate',
			 data: JSON.stringify(trackName),
		      dataType: 'json',
		      contentType: "application/json"
		 });		
	}


}]);