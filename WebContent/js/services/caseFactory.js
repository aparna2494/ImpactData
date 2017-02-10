'use strict';

/* Case Factory */

// Get list of cases
CaseimpactApp.factory('CasesFactory', function($resource) {

    var data;
    var resource = $resource('/api/incident/',  {}, {
        query: {
            method: 'GET',
            params:{},
            isArray: true,
            headers:{'TEST_USER':'ckoyi'}
        },
        save: {
        	method: 'POST',
            params:{},
            headers:{'TEST_USER':'ckoyi'}
        }
    });

    var cases = function(param, successcall,errorcall) {
        resource.query(param).$promise.then(
                //success
                function( value ){successcall(value)},
                //error
                function( error ){errorcall(error)/*Do something with error*/}
              );
        
    }
    var addcase = function(param, data,successcall,errorcall) {
        resource.save(param,data).$promise.then(
                //success
                function( value ){successcall(value)},
                //error
                function( error ){errorcall(error)/*Do something with error*/}
              );
    }


    return {
        getCases: function(param,callback) {            
            return cases(param,callback); 
        },
        addCase:function(param,data,successcall,errorcall){
        	return addcase(param,data,successcall,errorcall);        	
        }
    };
  });

CaseimpactApp.factory('CaseDetailFactory', function ($resource) {
    return $resource('/api/incident/:incidentNumber', {incidentNumber:'@num'}, {
        query: {
            method: 'GET',
            params:{},
            isArray: false,
            headers:{'TEST_USER':'ckoyi'}
        },
        update: {
        	method:'PUT',
        	params:{},
        	headers:{'TEST_USER':'ckoyi'}
        }
    })
});