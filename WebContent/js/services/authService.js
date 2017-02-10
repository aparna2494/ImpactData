'use strict';

BCCapp.factory('Auth',['$http','$cookieStore','$rootScope', function($http, $cookieStore,$rootScope){

    var accessLevels = routingConfig.accessLevels
        , userRoles = routingConfig.userRoles
        , currentUser = $cookieStore.get('user') || { username: '', role: userRoles.public,fullname:''};

        
    $cookieStore.remove('user');

    function changeUser(user) {
    	angular.extend(currentUser,user);
        $cookieStore.remove('user');
        $cookieStore.put('user',user)
    };

    return {
        authorize: function(accessLevel, role) {

            if(role === undefined)
                role = currentUser.role;
            return accessLevel.bitMask & role.bitMask;
        },
        isLoggedIn: function(user) {
        	
            if(user === undefined)
                user = currentUser;
            return user.role.title == userRoles.user.title || user.role.title == userRoles.admin.title;
        },
        login: function(success, error) {
        	var req = {
        			 method: 'GET',
        			 url: '/api/userinfo/login',
        			 headers: {
        			   'TEST_USER': 'ckoyi'
        			 },
        			 data: { }
        			}
            $http(req).then(function(user){
            	changeUser({
                    username: user.userId,
                    role: userRoles.user,
                    fullname:user.fullName,
                });
                success(user);
            },function(err){
            	error(err);
            });
        },
        accessLevels: accessLevels,
        userRoles: userRoles,
        user: currentUser
    };
}]);