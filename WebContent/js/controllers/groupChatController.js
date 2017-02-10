BCCapp.controller('groupChatController', ['$scope','alertDetailsService','$filter','$rootScope', function($scope,alertDetailsService,$filter,$rootScope) {
	
	alertDetailsService.smelocatoraction(5).success(function(data){
		
	//$scope.sme_list=[{'track':'productCommerce','jid':'parma'},{'track':'serviceCommerce','jid':'apgeorge'},{'track':'serviceCommerce','jid':'ckoyi'}]
	$scope.sme_list=data[0].userIds
	$scope.selectedTrack=data[0].appName
	
	
	 
          			
	})
	
	
	 $("#chat_modal").modal('show');
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
    	//event.stopPropagation();
    	if ($('.modal-content').hasClass("full-screen")) {
    		$('.modal-content').removeClass("full-screen");
    	}
    	$(this).hide();
    	$(this).parent().find('button.full').hide();
    	$(this).parents('.modal-content').find('.modal-body').hide('slow');
    });
    $(".modal-header button.full").click(function(){
    	//event.stopPropagation();
    	$(this).parents('.modal-content').toggleClass("full-screen",500);
    	//$('.modal-middle').css({'height': (($(window).height())-50)+'px'});
    });
    
    $(".modal-header").click(function(){
    	//event.stopPropagation();
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
    	//console.log("Hello");
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
    	/*$scope.sme_list=[{'track':'productCommerce','jid':'parma'},{'track':'serviceCommerce','jid':'apgeorge'},{'track':'serviceCommerce','jid':'ckoyi'}]
    	$scope.selectedTrack='productCommerce'
    	$scope.sme_track_list=$filter('filter')($scope.sme_list, $scope.selectedTrack, true)
    	console.log($scope.sme_track_list)*/
    	
		getjabberstatus({id:'#sme_jabber_table',values:$scope.sme_track_list[0].userIds,createRoom:false});
		}
    $scope.select_track=function($index)
    {
    	console.log([$scope.sme_list[$index]])
    	getjabberstatus({id:'#sme_jabber_table',values:[$scope.sme_list[$index]],createRoom:false});
    	
    }
  
	
}]);