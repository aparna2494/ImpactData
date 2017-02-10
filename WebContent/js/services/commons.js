BCCapp.factory('commonUtils',['$http', function($http) {
     var baseURL="/";
	 var buildApiUrl=function(){
			  return baseURL;
		  }	; 
	 return {
			httpGET: function(URL, params){				
				return	$http({
						 method: 'GET',
						 url: buildApiUrl() + URL,
						 params : params  
						});
              },
			httpPOST: function(URL, params){				
				return $http({
						 method: 'POST',
						 url: buildApiUrl() + URL,
						 headers: {
							   'Content-Type': 'application/json'
							 },
						 data: params  
						})
              },
              httpPUT: function(URL, params){				
  				return $http({
  						 method: 'PUT',
  						 url: buildApiUrl() + URL,
  						 headers: {
  							   'Content-Type': 'application/json'
  							 },
  						 data: params  
  						})
                }	
  			  
	      };
}]).factory('httpRequestInterceptor', function () {
  return {
    request: function (config) {
    
	     // any header need to send to for every request	 
	  
       config.headers['TEST_USER'] = 'ckoyi';
      return config; 
    }
  };
}).config(function ($httpProvider) {
  $httpProvider.interceptors.push('httpRequestInterceptor');
});
;