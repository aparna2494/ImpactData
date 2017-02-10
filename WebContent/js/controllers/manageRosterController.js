BCCapp.filter('capitalize', function() {
    return function(input) {
      return (!!input) ? input.charAt(0).toUpperCase() : '';
    }
});
BCCapp.controller('manageRosterController', ['$rootScope', '$scope', '$http', '$timeout', '$compile','$filter','$location','manageRoster',function($rootScope, $scope, $http, $timeout, $compile,$filter,$location,manageRoster) { 
	
}]);