/***
Metronic AngularJS App Main Script
***/

/* Metronic App */
var CaseimpactApp = angular.module("CaseimpactApp", [
    "ui.router", 
    "ui.bootstrap", 
    "oc.lazyLoad",  
    "ngSanitize",
    "ngAnimate",
    "ngResource",
    "datatables",
    "toaster",
    "ui.bootstrap.datetimepicker",
    "moment-picker"
    

/*    "ui.bootstrap.datetimepicker"*/
]); 

/* Configure ocLazyLoader(refer: https://github.com/ocombe/ocLazyLoad) */
CaseimpactApp.config(['$ocLazyLoadProvider','$httpProvider', function($ocLazyLoadProvider,$httpProvider) {
    $ocLazyLoadProvider.config({
        cssFilesInsertBefore: 'ng_load_plugins_before' // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
    });
    $httpProvider.defaults.headers.common = { 'TEST_USER' : 'ckoyi' };
}]);


/* Setup global settings */
CaseimpactApp.factory('settings', ['$rootScope', function($rootScope) {
    // supported languages
    var settings = {
        layout: {
            pageSidebarClosed: false, // sidebar state
            pageAutoScrollOnLoad: 1000 // auto scroll to top on page load
        },
        layoutImgPath: Metronic.getAssetsPath() + 'admin/layout/img/',
        layoutCssPath: Metronic.getAssetsPath() + 'admin/layout/css/'
    };

    $rootScope.settings = settings;

    return settings;
}]);

/* Setup App Main Controller */
CaseimpactApp.controller('AppController', ['$scope', '$rootScope', function($scope, $rootScope) {
    $scope.$on('$viewContentLoaded', function() {
        Metronic.initComponents(); // init core components
        //Layout.init(); //  Init entire layout(header, footer, sidebar, etc) on page load if the partials included in server side instead of loading with ng-include directive 
    });
}]);

/***
Layout Partials.
By default the partials are loaded through AngularJS ng-include directive. In case they loaded in server side(e.g: PHP include function) then below partial 
initialization can be disabled and Layout.init() should be called on page load complete as explained above.
***/

/* Setup Layout Part - Header */
CaseimpactApp.controller('HeaderController', ['$scope', function($scope) {
    $scope.$on('$includeContentLoaded', function() {
        Layout.initHeader(); // init header
    });
}]);

/* Setup Layout Part - Sidebar */
CaseimpactApp.controller('SidebarController', ['$scope', function($scope) {
    $scope.$on('$includeContentLoaded', function() {
        Layout.initSidebar(); // init sidebar
    });
}]);

/* Setup Layout Part - Sidebar */
CaseimpactApp.controller('PageHeadController', ['$scope', function($scope) {
    $scope.$on('$includeContentLoaded', function() {        
        Demo.init(); // init theme panel
    });
}]);

/* Setup Layout Part - Footer */
CaseimpactApp.controller('FooterController', ['$scope', function($scope) {
    $scope.$on('$includeContentLoaded', function() {
        Layout.initFooter(); // init footer
    });
}]);

/* Setup Rounting For All Pages */
CaseimpactApp.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

    // Redirect any unmatched url
    $urlRouterProvider.otherwise("/addcaseimpact.html");

    $stateProvider

        // Dashboard
        .state('dashboard', {
            url: "/dashboard.html",
            templateUrl: "views/dashboard.html",            
            data: {pageTitle: 'Dashboard', pageSubTitle: 'statistics & reports'},
            controller: "DashboardController",
            resolve: {
                deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'CaseimpactApp',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before '#ng_load_plugins_before'
                        files: [
                             'js/controllers/DashboardController.js'
                        ] 
                    });
                }]
            }
        })



        // Add New Case
        .state('addCaseImpact', {
            url: "/addcaseimpact.html",
            templateUrl: "views/addcaseimpact.html",
            data: {pageTitle: 'Add New Case Impact', pageSubTitle: 'Add transaction impact details'},
            controller: "AddCaseController",
            resolve: {
                deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([{
                        name: 'CaseimpactApp',
                        files: [
                            'js/services/caseFactory.js',
                            'js/services/remedyFactory.js',
                            'js/controllers/AddCaseController.js',
                            './assets/global/plugins/select2/select2.css',
                            './assets/global/plugins/select2/select2.min.js',
                            './assets/admin/pages/scripts/components-dropdowns.js',
                        ] 
                    }]);
                }] 
            }
        })

        // Update existing Case
        .state('updateCaseImpact', {
            url: "/updatecaseimpact.html",
            templateUrl: "views/updatecaseimpact.html",
            data: {pageTitle: 'Update Case Impact', pageSubTitle: 'Edit impact details for already entered cases'},
            controller: "UpdateCaseController",
            resolve: {
                deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([{
                        name: 'CaseimpactApp',
                        files: [
                                'js/services/caseFactory.js',
                                'js/controllers/UpdateCaseController.js'
                        ] 
                    }]);
                }] 
            }
        })
        // View case impact details
        .state('viewCaseImpact', {
            url: "/viewcaseimpact.html/{incidentNumber}",
            templateUrl: "views/viewcaseimpact.html",
            data: {pageTitle: 'View Case Impact', pageSubTitle: 'details'},
            controller: "ViewCaseController",
            resolve: {
                deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([{
                        name: 'CaseimpactApp',
                        files: [
                                './assets/admin/pages/css/profile.css',
                                'js/services/caseFactory.js',
                                'js/controllers/ViewCaseController.js'
                        ] 
                    }]);
                }] 
            }
        })   
                .state('submittedCases', {
            url: "/submittedCases.html",
            templateUrl: "views/submittedCases.html",
            data: {pageTitle: 'Submitted Cases', pageSubTitle: 'list of all cases submitted in case impact tool for this release '},
            controller: "SubmittedCasesController",
            resolve: {
                deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([{
                        name: 'CaseimpactApp',
                        files: [
                                'js/services/caseFactory.js',
                                'js/controllers/SubmittedCasesController.js'
                        ] 
                    }]);
                }] 
            }
        }) 
        // Todo
        .state('todo', {
            url: "/todo",
            templateUrl: "views/todo.html",
            data: {pageTitle: 'Todo', pageSubTitle: 'user todo & tasks sample'},
            controller: "TodoController",
            resolve: {
                deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load({ 
                        name: 'CaseimpactApp',  
                        insertBefore: '#ng_load_plugins_before', // load the above css files before '#ng_load_plugins_before'
                        files: [
                            './assets/global/plugins/bootstrap-datepicker/css/datepicker3.css',
                            './assets/global/plugins/select2/select2.css',
                            './assets/admin/pages/css/todo.css',
                            
                            './assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js',
                            './assets/global/plugins/select2/select2.min.js',

                            './assets/admin/pages/scripts/todo.js',

                            'js/controllers/TodoController.js'  
                        ]                    
                    });
                }]
            }
        })

}]);

/* Init global settings and run the app */
CaseimpactApp.run(["$rootScope", "settings", "$state", function($rootScope, settings, $state) {
    $rootScope.$state = $state; // state to be accessed from view
}]);