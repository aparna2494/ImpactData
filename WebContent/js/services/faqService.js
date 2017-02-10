
'use strict';

BCCapp.service('faqService', ['$http','$q', 'commonUtils',function($http, $q, commonUtils) {
  this.getQuestions = function(params) {
	    var deferred = $q.defer();
		commonUtils
		.httpPOST("api/skms/questions",params)
		.success(function(data){
	         // With the data succesfully returned, we can resolve promise and we can access it in controller
	         deferred.resolve(data);
	     }).error(function(){
	          //let the function caller know the error
	          deferred.reject();
	     });
	     return deferred.promise;
	  };
	  
	  this.getQuestionAns = function(params) {
		    var deferred = $q.defer();
			commonUtils
			.httpPOST("api/skms/answer",params)
			.success(function(data){
		         // With the data succesfully returned, we can resolve promise and we can access it in controller
		         deferred.resolve(data);
		     }).error(function(){
		          //let the function caller know the error
		          deferred.reject();
		     });
		     return deferred.promise;
		  };	
		  this.getRKMData = function(keyword) {
			    var deferred = $q.defer();
				commonUtils
				.httpGET("api/incident/rkm/"+keyword)
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