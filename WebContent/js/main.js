/***
Metronic AngularJS App Main Script
***/

/* Metronic App */

var BCCapp = angular.module("BCCapp", [ "ngCookies", "ui.router",

                                    		"ui.bootstrap", "oc.lazyLoad", "ngSanitize","angucomplete-alt","ui.calendar","ui.bootstrap.datetimepicker","moment-picker","angular-carousel"]);

/* Configure ocLazyLoader(refer: https://github.com/ocombe/ocLazyLoad) */
BCCapp.config(['$ocLazyLoadProvider', function($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        // global configs go here
    });
}]);

BCCapp.filter('thousandSuffix', function() {
	return function(input, decimals) {
	var exp, rounded,
	suffixes = ['K', 'M', 'G', 'T', 'P', 'E'];

	if (window.isNaN(input)) {
	return null;
	}

	if (input < 1000) {
	return input;
	}

	exp = Math.floor(Math.log(input) / Math.log(1000));

	rounded = (input / Math.pow(1000, exp)).toFixed(decimals) + suffixes[exp - 1];
	return rounded;

	};
	});

/********************************************
 BEGIN: BREAKING CHANGE in AngularJS v1.3.x:
*********************************************/
/**
`$controller` will no longer look for controllers on `window`.
The old behavior of looking on `window` for controllers was originally intended
for use in examples, demos, and toy apps. We found that allowing global controller
functions encouraged poor practices, so we resolved to disable this behavior by
default.

To migrate, register your controllers with modules rather than exposing them
as globals:

Before:

```javascript
function MyController() {
  // ...
}
```

After:

```javascript
angular.module('myApp', []).controller('MyController', [function() {
  // ...
}]);

Although it's not recommended, you can re-enable the old behavior like this:

```javascript
angular.module('myModule').config(['$controllerProvider', function($controllerProvider) {
  // this option might be handy for migrating old apps, but please don't use it
  // in new ones!
  $controllerProvider.allowGlobals();
}]);
**/

//AngularJS v1.3.x workaround for old style controller declarition in HTML
BCCapp.config(['$controllerProvider', function($controllerProvider) {
  // this option might be handy for migrating old apps, but please don't use it
  // in new ones!
  $controllerProvider.allowGlobals();
}]);

/********************************************
 END: BREAKING CHANGE in AngularJS v1.3.x:
*********************************************/

/* Setup global settings */
BCCapp.factory('settings', ['$rootScope', function($rootScope) {
    // supported languages
    var settings = {
        layout: {
            pageSidebarClosed: false, // sidebar menu state
            pageContentWhite: true, // set page content layout
            pageBodySolid: false, // solid body color state
            pageAutoScrollOnLoad: 1000 // auto scroll to top on page load
        },
        assetsPath: 'assets',
        globalPath: 'assets/global',
        layoutPath: 'assets/layouts',
    };

    $rootScope.settings = settings;
$rootScope.countvar=0;
$rootScope.countvar_neo=0;
$rootScope.selectedtrackarraylabels_neo=[]
$rootScope.selectedtrackarraylabels=[]
    return settings;
}]);

/* Setup App Main Controller */
BCCapp.controller('AppController', ['$scope', '$rootScope', function($scope, $rootScope) {
    $scope.$on('$viewContentLoaded', function() {
        App.initComponents(); // init core components
        //Layout.init(); //  Init entire layout(header, footer, sidebar, etc) on page load if the partials included in server side instead of loading with ng-include directive 
    });
}]);

/***
Layout Partials.
By default the partials are loaded through AngularJS ng-include directive. In case they loaded in server side(e.g: PHP include function) then below partial 
initialization can be disabled and Layout.init() should be called on page load complete as explained above.
***/
/* Setup Layout Part - Header */
BCCapp.controller('HeaderController', [ '$scope', '$rootScope', 'Auth','$state',
                                  		function($scope, $rootScope, Auth, $state) {
                                  			$scope.$on('$includeContentLoaded', function() {
                                  				Layout.initHeader(); // init header
                                  			});
                                  			$rootScope.user = {
                                  				userId : null,
                                  				userName : ''
                                  			};
                                  			$rootScope.refreshFrequency=60000;
                                  			$rootScope.refreshFrequency_neo=60000;
                                  			Auth.login(function(res) {
                                  				console.log(res);
                                  				$rootScope.user.userId = res.data.userId;
                                  				$rootScope.user.userName = res.data.fullName;
                                  				//$location.path('dashboard');
                                  			}, function(data) {
                                  			//	console.log(data.data);
                                  				if (data.data.code == 'NORESOURCE') {
                                  					$rootScope.user.userId = data.data.code;
                                  					$rootScope.user.userName='';
                                  					$state.go('noaccess') 
                                  					//$location.path('noaccess');
                                  				}
                                  			});
                                  		} 
]);


/* Setup Layout Part - Sidebar */
BCCapp.controller('SidebarController', ['$scope', function($scope) {
    $scope.$on('$includeContentLoaded', function() {
        Layout.initSidebar(); // init sidebar
    });
}]);

/* Setup Layout Part - Sidebar */
BCCapp.controller('PageHeadController', ['$scope', function($scope) {
	
    $scope.$on('$includeContentLoaded', function() {        
        Demo.init(); // init theme panel
        /*$('#inc_num').keypress(function(e){ //when key is pressed on query text box

     		$('.details').addClass('hidden');
     		if(e.which == 13){            //if the pressed key is enter
     		
     			
               //trigger the button click event
               $(".ui-menu-item").hide();
               $(".ui-autocomplete-category").hide();
               
     		}
     }) */
    });
}]);

/* Setup Layout Part - Quick Sidebar */
BCCapp.controller('QuickSidebarController', ['$scope', function($scope) {    
    $scope.$on('$includeContentLoaded', function() {
       setTimeout(function(){
            QuickSidebar.init(); // init quick sidebar        
        }, 2000)
    });
}]);

/* Setup Layout Part - Theme Panel */
BCCapp.controller('ThemePanelController', ['$scope', function($scope) {    
    $scope.$on('$includeContentLoaded', function() {
        Demo.init(); // init theme panel
    });
}]);

/* Setup Layout Part - Footer */
BCCapp.controller('FooterController', ['$scope', function($scope) {
    $scope.$on('$includeContentLoaded', function() {
        Layout.initFooter(); // init footer
    });
}]);

BCCapp.controller('ChatController', ['$scope', function($scope) {
   /* $("#chat_modal").modal('show');
    $(".modal-backdrop").hide();
    $scope.activeUser = {};
    $scope.groupInfo = [];
    $scope.userGroupMembers = [];
    $scope.activeUserGroupMembers = [];
    $scope.chatHistory = [];
    $scope.count = 0;
    $scope.newGroupContact = ""
    $scope.chatContacts = [
                       {id: 0, name: 'Ned Stark', email: 'ned@winterfell.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Winter is coming.'},
                       {id: 1, name: 'Theon Greyjoy', email: 'tgreyjoy@winterfell.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Reluctant to pay iron price.'},
                       {id: 2, name: 'Samwell Tarly', email: 'starly@castleblack.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Loyal brother of the watch.'},
                       {id: 3, name: 'Jon Snow', email: 'jsnow@castleblack.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Knows nothing.'},
                       {id: 4, name: 'Arya Stark', email: 'waterdancer@winterfell.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Has a list of names.'},
                       {id: 5, name: 'Jora Mormont', email: 'khaleesifan100@gmail.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Lost in the friend-zone.'},
                       {id: 6, name: 'Tyrion Lannister', email: 'tyrion@lannister.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Currently drunk.'},
                       {id: 7, name: 'Stannis Baratheon', email: 'onetrueking@dragonstone.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Nobody expects the Stannish inquisition.'},
                       {id: 8, name: 'Hodor', email: 'hodor@hodor.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Hodor? Hodor... Hodor!'},
                       {id: 9, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 10, name: 'Brienne of Tarth', email: 'oathkeeper@gmail.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Do not cross her.'},
                       {id: 11, name: 'Petyr Baelish', email: 'petyr@baelishindustries.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Do not trust anyone.'},
					   {id: 12, name: 'Ned Stark', email: 'ned@winterfell.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Winter is coming.'},
                       {id: 13, name: 'Theon Greyjoy', email: 'tgreyjoy@winterfell.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Reluctant to pay iron price.'},
                       {id: 14, name: 'Samwell Tarly', email: 'starly@castleblack.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Loyal brother of the watch.'},
                       {id: 15, name: 'Jon Snow', email: 'jsnow@castleblack.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Knows nothing.'},
                       {id: 16, name: 'Arya Stark', email: 'waterdancer@winterfell.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Has a list of names.'},
                       {id: 17, name: 'Jora Mormont', email: 'khaleesifan100@gmail.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Lost in the friend-zone.'},
                       {id: 18, name: 'Tyrion Lannister', email: 'tyrion@lannister.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Currently drunk.'},
                       {id: 19, name: 'Stannis Baratheon', email: 'onetrueking@dragonstone.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Nobody expects the Stannish inquisition.'},
                       {id: 20, name: 'Hodor', email: 'hodor@hodor.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Hodor? Hodor... Hodor!'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 21, name: 'Margaery Tyrell', email: 'mtyrell@highgarden.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Keeper of kings.'},
                       {id: 22, name: 'Brienne of Tarth', email: 'oathkeeper@gmail.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Do not cross her.'},
                       {id: 23, name: 'Petyr Baelish', email: 'petyr@baelishindustries.com', phone: '123-456-7890', url: 'www.google.com', notes: 'Do not trust anyone.'}
                     ]; 
    $scope.chatMessages = [
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 0?'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 0'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 1?'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 1 ?'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 2?'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good  2'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 3?'},
    	{id: 0, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 3'},
    	
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 4?'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 4'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 5?'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 5'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 6?'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 6'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'Hello How are you 6?'},
    	{id: 1, name: 'Ned Stark', dateTime : '10-10-2010 10:11', message : 'I am Good 6'},
    ];
    $("button.minus").click(function(){
    	event.stopPropagation();
    	if ($('.modal-content').hasClass("full-screen")) {
    		$('.modal-content').removeClass("full-screen");
    	}
    	$(this).hide();
    	$(this).parent().find('button.full').hide();
    	$(this).parents('.modal-content').find('.modal-body').hide('slow');
    });
    $(".modal-header button.full").click(function(){
    	event.stopPropagation();
    	$(this).parents('.modal-content').toggleClass("full-screen",500);
    	//$('.modal-middle').css({'height': (($(window).height())-50)+'px'});
    });
    
    $(".modal-header").click(function(){
    	event.stopPropagation();
    	if($(this).parent('.modal-content').find('.modal-body').css('display') == 'none'){
    		$(this).parent('.modal-content').find('.modal-body').show('slow');
    		$(this).parent().find('button.full').show();
    		$(this).parent().find('button.minus').show();
    	}
    });
    $scope.showHistory = function($event, contact){
    	$scope.activeUser.id = contact.id;
        $scope.activeUser.name = contact.name;
    	angular.element($event.target).parent().find('li').removeClass('selected');
    	angular.element($event.target).addClass('selected');
    	$scope.chatHistory = $.grep($scope.chatMessages, function(e){ return e.id == contact.id; });
    	$scope.activeUserGroupMembers = $scope.userGroupMembers[contact.id];
    	console.log("Hello");
    };
    var members = [];
    $scope.addUsertoGroupChat = function(){    	
    	var members = [];
    	if(!$scope.newGroupContact || !$scope.activeUser.id)
    		return;
    	
    	if($scope.userGroupMembers[$scope.activeUser.id]){
    		var existUser = $.grep($scope.userGroupMembers[$scope.activeUser.id], function(e, indx){
    			return e.name == $scope.newGroupContact;
    			});
    		if(existUser.length == 0 )
    		$scope.userGroupMembers[$scope.activeUser.id].push({id: $scope.count++, name: $scope.newGroupContact});
    	}else {
    		members.push({id: $scope.activeUser.id, name: $scope.activeUser.name},
        			{id: $scope.count++, name: $scope.newGroupContact});
    		$scope.userGroupMembers[$scope.activeUser.id]= members;
    	}
    	$scope.activeUserGroupMembers = $scope.userGroupMembers[$scope.activeUser.id];
    };
    
    
    $scope.addSMEToChat=function()
		{
		alert('in addsme')
		getjabberstatus({id:'#sme_jabber_table',values:$scope.sme_track_list});
		}
			*/
  }]);

/* Setup Rounting For All Pages */
BCCapp.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    // Redirect any unmatched url
    $urlRouterProvider.otherwise("/neoflow");  
    
    $stateProvider

       
									.state(
									'neoflow',
									{
										url : "/neoflow",
										templateUrl : "views/datacollector.html",
										data : {
											pageTitle : 'Flow View',
											alertCollection:"bcc_alerts_sec_neo",
											flowCollection:"bp_flow_neo_data"
										},
										controller : "NeoNetworkController",
										resolve : {
											deps : [
													'$ocLazyLoad',
													function($ocLazyLoad) {
														return $ocLazyLoad
																.load({
																	name : 'BCCapp',
																	insertBefore : '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
																	files : [
																	         'js/angucomplete-alt.js',
																	         'js/services/secondLevelNodes.js',
																	         'js/directives/KPIDirective.js',
																			'js/directives/cytoscapeNeoDirective.js',
																			'assets/global/scripts/agent_custom.js',
																			/*'assets/global/css/structure.css',*/
																			'assets/global/css/angucomplete-alt.css',
																			'js/controllers/neoNetworkController.js',
																			'js/services/networkFactory.js',
																			'js/services/dataservices.js',
																			 'assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css',
																				'assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css',
																				'assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js',
																				'assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js'
																			
																			/*'js/services/alertDetailsService.js',*/
																			]
																});
													} ]
										}
									}) 
									.state(
									'flow',
									{
										url : "/flow",
										templateUrl : "views/neonetwork.html",
										data : {
											pageTitle : 'Flow View',
											alertCollection:"bcc_alerts_sec",
											flowCollection:"bp_flow_data"
										},
										controller : "NeoNetworkController",
										resolve : {
											deps : [
													'$ocLazyLoad',
													function($ocLazyLoad) {
														return $ocLazyLoad
																.load({
																	name : 'BCCapp',
																	insertBefore : '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
																	files : [
																	         'js/angucomplete-alt.js',
																	         'js/services/secondLevelNodes.js',
																			'js/directives/cytoscapeNeoDirective.js',
																			'assets/global/scripts/agent_custom.js',
																			/*'assets/global/css/structure.css',*/
																			'assets/global/css/angucomplete-alt.css',
																			'js/controllers/neoNetworkController.js',
																			'js/services/networkFactory.js',
																			'js/services/dataservices.js',
																			'js/services/dataEntryService.js',
																			 'assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css',
																				'assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css',
																				'assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js',
																				'assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js'
																			
																			/*'js/services/alertDetailsService.js',*/
																			]
																});
													} ]
										}
									});
													
       
}]);

/* Init global settings and run the app */
BCCapp.run(["$rootScope", "settings", "$state", function($rootScope, settings, $state) {
    $rootScope.$state = $state; // state to be accessed from view
    $rootScope.$settings = settings; // state to be accessed from view
}]);