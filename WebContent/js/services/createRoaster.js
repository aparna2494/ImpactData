
'use strict';

BCCapp.service('trackService', ['$http','$q', 'commonUtils',function($http, $q,commonUtils) {
  this.getTracks = function(params) {
	    var deferred = $q.defer();
		commonUtils
		.httpGET("api/roster/tracks",params)
		.success(function(data){
	         // With the data succesfully returned, we can resolve promise and we can access it in controller
	         deferred.resolve(data);
	     }).error(function(){
	          //let the function caller know the error
	          deferred.reject();
	     });
	     return deferred.promise;
	  };
	  this.getEvents = function(params) {
		    var deferred = $q.defer();
			commonUtils
			.httpPOST("api/roster/ag",params)
			.success(function(data){
		         // With the data succesfully returned, we can resolve promise and we can access it in controller
		         deferred.resolve(data);
		     }).error(function(){
		          //let the function caller know the error
		          deferred.reject();
		     });
		     return deferred.promise;
		  };
	  this.getShifts = function(params) {
		    var deferred = $q.defer();
			commonUtils
			.httpGET("api/roster/shift ",params)
			.success(function(data){
		         // With the data succesfully returned, we can resolve promise and we can access it in controller
		         deferred.resolve(data);
		     }).error(function(){
		          //let the function caller know the error
		          deferred.reject();
		     });
		     return deferred.promise;
		  };
		  this.updateRoaster = function(params) {
			    var deferred = $q.defer();
				commonUtils
				.httpPOST("api/roster/update",params)
				.success(function(data){
			         // With the data succesfully returned, we can resolve promise and we can access it in controller
			         deferred.resolve(data);
			     }).error(function(){
			          //let the function caller know the error
			          deferred.reject();
			     });
			     return deferred.promise;
			  };
			  this.removeRoaster = function(params) {
				    var deferred = $q.defer();
					commonUtils
					.httpPOST("api/roster/remove",params)
					.success(function(data){
				         // With the data succesfully returned, we can resolve promise and we can access it in controller
				         deferred.resolve(data);
				     }).error(function(){
				          //let the function caller know the error
				          deferred.reject();
				     });
				     return deferred.promise;
				  };
}]);