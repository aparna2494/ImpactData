
'use strict';

BCCapp.service('KPIDetails', ['$http','$q', 'commonUtils',function($http, $q,commonUtils) {
	 this.getTracks = function(collectionName) {
		  return $http({

		      method: 'POST', url: '/api/KPI/tracks',

		      data: JSON.stringify(collectionName),
		      dataType: 'json',
		      contentType: "application/json"
		  });}
	 
	 
	 this.saveKPI = function(kpidata) {
		  return $http({

		      method: 'POST', url: '/api/KPI/createKPI',

		      data: JSON.stringify(kpidata),
		      dataType: 'json',
		      contentType: "application/json"
		  });}
	  
	 
	 this.getComponents = function(collectionName) {
		  return $http({

		      method: 'POST', url: '/api/KPI/components',

		      data: JSON.stringify(collectionName),
		      dataType: 'json',
		      contentType: "application/json"
		  });}
	 
	 this.updateQuery = function(data) {
		  return $http({

		      method: 'POST', url: '/api/KPI/updateQuery',

		      data: JSON.stringify(data),
		      dataType: 'json',
		      contentType: "application/json"
		  });}

	 this.getKPIs= function(kpiData) {
		  return $http({

		      method: 'POST', url: '/api/KPI/getUniqueKPIs',

		      data: JSON.stringify(kpiData),
		      dataType: 'json',
		      contentType: "application/json"
		  });}
	  
	  this.getKPIdetails = function(trackName) {
		  return $http({

		      method: 'POST', url: '/api/KPI/getKPIdetails',

		      data: JSON.stringify(trackName),
		      dataType: 'json',
		      contentType: "application/json"
		  });}
	  
	  	  
	  this.updateKPIdetails = function(newKPIs) {
		  return $http({

		      method: 'POST', url: '/api/KPI/updateKPIdetails',

		      data: JSON.stringify(newKPIs),
		      dataType: 'json',
		      contentType: "application/json"
		  });}
	  this.DBnames = function(params) {
			    var deferred = $q.defer();
				commonUtils
				.httpGET("api/KPI/DBnames",params)
				.success(function(data){
			         // With the data succesfully returned, we can resolve promise and we can access it in controller
			         deferred.resolve(data);
			     }).error(function(){
			          //let the function caller know the error
			          deferred.reject();
			     });
			     return deferred.promise;
			  };
			  
			  this.kpiMapping = function(params) {
				    var deferred = $q.defer();
					commonUtils
					.httpGET("api/KPI/KPImapping",params)
					.success(function(data){
				         // With the data succesfully returned, we can resolve promise and we can access it in controller
				         deferred.resolve(data);
				     }).error(function(){
				          //let the function caller know the error
				          deferred.reject();
				     });
				     return deferred.promise;
				  }; 
			  
			  this.trackMapping = function(params) {
				    var deferred = $q.defer();
					commonUtils
					.httpGET("api/KPI/trackMapping",params)
					.success(function(data){
				         // With the data succesfully returned, we can resolve promise and we can access it in controller
				         deferred.resolve(data);
				     }).error(function(){
				          //let the function caller know the error
				          deferred.reject();
				     });
				     return deferred.promise;
				  };

					  this.testQuerySave = function(data) {
						  return $http({

						      method: 'POST', url: '/api/KPI/testquery',

						      data: JSON.stringify(data),
						      dataType: 'json',
						      contentType: "application/json"
						  });}		



	

}]);
