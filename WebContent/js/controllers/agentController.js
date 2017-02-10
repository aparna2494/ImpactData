angular.module('BCCapp').filter('startFrom', function() {
    return function(input, start) {
    	
        start = +start; //parse to int
        if(input!=null)
        	{
        	
        return input.slice(start);
        	}
    }
});
angular.module('BCCapp').controller('agentController', function($rootScope, $scope, $http, $timeout,autocomplete,cpedetails,openincidentdetails,closedincidentdetails,
																ops_service,sme_portfolio_list,sme_application_list,sme_list) {
    $scope.$on('$viewContentLoaded', function() {   
        // initialize core components
        App.initAjax();
    });

    // set sidebar closed and body solid layout mode
    $rootScope.settings.layout.pageContentWhite = true;
    $rootScope.settings.layout.pageBodySolid = false;
    $rootScope.settings.layout.pageSidebarClosed = false;
    
    
    var availableTags=[]
    autocomplete.get_method().success(function(data){
    	
    	angular.forEach(data, function(value, key) {
    		var xx={'label':value.customername,'category':'customer'}
    		var yy={'label':value.incidentNumber,'category':'incident_number'}
    		 availableTags.push(xx)
    		    availableTags.push(yy)
    		    if(key==data.length-1){
    		    	$("#inc_num").catcomplete({
    			        delay: 0,
    			source: function(request, response) {
    			var results = $.ui.autocomplete.filter(availableTags, request.term);

    			response(results.slice(0, 10));
    			}
    			});
    			

				}
    		   
    	})
    	
		
    })
    

    
    $scope.selected_portfolio="Portfolio"
    	$scope.selected_application="Application"
    		$scope.smes_list=[]
    var portfolios=sme_portfolio_list.get_method().success(function(data){
    	$scope.sme_portfolios=[]
    	angular.forEach(data, function(value, key) {
    		$scope.sme_portfolios.push(value.portfolio);	
    		
    	});
		
			}) ;
    $scope.select_portfolio=function($index)
    {
    	$scope.selected_portfolio=$scope.sme_portfolios[$index];
    	$scope.selected_application="Application";
    	$scope.smes_list=[]
    	$('#application_menu_div').show();
      	//console.log($scope.sme_portfolios[$index]);
      	var applications=sme_application_list.get_method($scope.sme_portfolios[$index]).success(function(data){
          	$scope.sme_applications=[]
          	angular.forEach(data, function(value, key) {
          		$scope.sme_applications.push(value.application);	
          		
          	});
      		
      			}) ;
    }
    
    $scope.select_application=function($index)
    {
    	$scope.selected_application=$scope.sme_applications[$index];
    	$scope.smes_list=[];
    	$('#sme_jabber_table').show();
    	var applications=sme_list.get_method($scope.selected_portfolio,$scope.selected_application).success(function(data){
          	
          	angular.forEach(data, function(value, key) {
          		//$scope.sme_applications.push(value.application);	
          		//console.log(value);
          		value.full_name=value.full_name.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
          		$scope.smes_list.push(value);
          		
          		if (key==data.length-1)
          			{
          			//alert('ff');
          			$('#sme_jabber_table').getjabberstatus({id:'#sme_jabber_table',values:$scope.smes_list});
          			}
          		
          	});
      		
      			}) ;
    	
    }
    
  $scope.past_incidents=function($index)
  {
	 
	  $('.details').removeClass('hidden'); 
	  
	  incident=$scope.closed_incidents[$index]
  	incident_number=incident.incidentNumber
  	
  	$scope.selected_incident="http://case/"+incident.incidentNumber
  	$scope.selected_incident_notes=incident.incidentNotes
	  
  }
  $scope.get_incident_details=function($index)
  {
	  	$('.details').removeClass('hidden');
    	//console.log($scope.open_incidents[$index]);
    	incident=$scope.open_incidents[$index]
    	incident_number=incident.incidentNumber
    	$scope.selected_incident="http://case/"+incident.incidentNumber
    	$scope.selected_incident_notes=incident.incidentNotes
    	
    	$scope.incident_ops_dollar_impacted='-'
			$scope.incident_ops_issue='-'
			$scope.incident_ops_customers='-'
    	var x=ops_service.get_method(incident_number).success(function(data){
			
			if(data.length==0)
				{
				$scope.incident_ops_dollar_impacted='-'
				$scope.incident_ops_issue='-'
				$scope.incident_ops_customers='-'
				
				}
			else
				{
				$scope.incident_ops_dollar_impacted=data[0].amount
				$scope.incident_ops_issue=data[0].issue
				$scope.incident_ops_customers=data[0].customers
			
			
				}
				}) ;
  }
  
    var main_function=function(){
    	
		$('#incident_details').show();
		$scope.open_incidents=[];
    	$scope.closed_incidents=[];
    	
    	
		var result=$.grep(availableTags, function(e){ return e.label ==$('#inc_num').val() });
		
		//console.log(result[0].label);
		
		cpedetails.get_method(result[0].label,result[0].category).success(function(data){
			
			$scope.cpe_details=data[0];
			openincidentdetails.get_method(data[0].customername).success(function(data2){
				
				$('#inc_num').val("");
				//console.log(data2);
				angular.forEach(data2, function(value, key) {
					var temp=parseInt(value.cet_value)
					if (temp==0)
						{
						value.cet_color="green";
						}
					else if(temp==1)
						{
						value.cet_color="red";
						}
					$scope.open_incidents.push(value)
				})
					
				 	$scope.currentPage_open = 1;
				    $scope.pageSize_open = 2;
				    $scope.maxSize=3;
				  
				    $scope.totalItems_open = data2.length;
				   
				
			});
			
			
				closedincidentdetails.get_method(data[0].customername).success(function(data3){
								
					
								angular.forEach(data3, function(value, key) {
									var temp=parseInt(value.cet_value)
									if (temp==0)
										{
										value.cet_color="green";
										}
									else if(temp==1)
										{
										value.cet_color="red";
										}
									$scope.closed_incidents.push(value)
								})
								 $scope.currentPage_closed = 1;
								    $scope.pageSize_closed = 2;
								   
								  
								    $scope.totalItems_closed = data3.length;
								    
								
							});
		});
		
		
	}
	$('#inc_num').keypress(function(e){ //when key is pressed on query text box

		$('.details').addClass('hidden');
		if(e.which == 13){            //if the pressed key is enter
		
			
          main_function();    //trigger the button click event
          $(".ui-menu-item").hide();
          $(".ui-autocomplete-category").hide();
          
		}
}) 

	
	
	

});

$(document).ready(function(){
	
	
	 $(document).delegate('.message_class','keypress',function(e){
		 
         if(e.which == 13){
        	 
         message_id=$(this).attr('id')
         //alert(message_id);
         chat_id=$(this).parent().find('.chat_class').attr('id')
         user_id=chat_id.substring(4,chat_id.length)
         if($('#'+message_id)!="") 
         {
				$('#'+message_id).send_message('#'+chat_id,user_id) ; 
             }
              
			}
         })	
         
     $(document).delegate('.extra_class','hidden.bs.modal', function () {
    // do something…
    
				$('#chat_queue').css('background-color','#ffffff');
				$('#chat_queue').html("<center><b><font size=4 color=#000000 >Chat Queue</font></b></center>")
})


$(document).delegate('#signin_button','click',function(e){
	//alert('in sign in');
	sessionStorage.username_index=$('#email').val()
	sessionStorage.password_index=$('#passwd').val()
	if($('#email').val() !="" & $('#passwd').val()!="")
	{
	$(document).connectToJabber($('#email').val(),$('#passwd').val());
	}
	else
	{
	$('#missing_jabber').show();
	}

	$('#email , #passwd').on('click',function()
	{
	$('#missing_jabber').hide();
	$('#error_jabber').hide();
	})
	})
})



