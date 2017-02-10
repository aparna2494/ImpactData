
'use strict';

BCCapp.service('caseService', ['$http','$q', 'commonUtils',function($http, $q, commonUtils) {
  
	 this.createCase = function(params) {
		    var deferred = $q.defer();
			commonUtils
			.httpPOST("api/incident/create",params)
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