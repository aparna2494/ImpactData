BCCapp.controller('graphOpsController',['$scope','$stateParams','$filter','graphOps_service','graphOps_region_service','graphOps_summary_service',function($scope,$stateParams,$filter,graphOps_service,graphOps_region_service,graphOps_summary_service){
	//console.log("in controllerss");
	$scope.countryCode = $stateParams.countryCode 
	var main_function=function(data)
	{
		$scope.items=[];
		var cust_array=[];
		$scope.color_threshold_high=50000000;
		$scope.color_threshold_medium=10000000;
angular.forEach(data, function(value, key) {
			
			
			var index=cust_array.indexOf(value.customer_name)
			
			if ( index== -1)
				{
				
				cust_array.push(value.customer_name);
				var item={};
				item.customer=value.customer_name;
				item.total_dollar_impact=value.total_amount;
				
				item['OM']="#26c281";
				item['Q2O']="#26c281";
				item['SCM']="#26c281";
					//item[value['function']]="#e43a45";
					var sum=0;
					var max=0;
					angular.forEach(value.incident_list, function(value_il, key) {
						
						sum+=value.total_amount;
						
					})
					if(max<=value.total_amount)
							{ 
							
							max=value.total_amount;
							if(max>=$scope.color_threshold_high)
							{
								
							item[value['function']]="#e43a45";
							}
							else if(max>=$scope.color_threshold_medium)
							{
								
							item[value['function']]="#e87e04";
							}
						else if(value.incident_list.length>0)
							{
							
							item[value['function']]="#F7ca18";
							}
							}
						
					item['incident_list_'+value['function']]=value.incident_list;
					
				
				$scope.items.push(item);
				}
			else
				{
				
				$scope.items[index].total_dollar_impact+=value.total_amount
				item=$scope.items[index]
				var sum=0;
				var max=0;
				angular.forEach(value.incident_list, function(value_il, key) {
					
					sum+=value.total_amount;
					
				})
				
				if(max<=value.total_amount)
					{
					max=value.total_amount;
					if(max>=$scope.color_threshold_high)
					{
						
					item[value['function']]="#e43a45";
					}
					else if(max>=$scope.color_threshold_medium)
					{
						
					item[value['function']]="#e87e04";
					}
					else if(value.incident_list.length>0)
					{
					
					item[value['function']]="#F7ca18";
					}
					}
				//$scope.items[index][value['function']]=1;
				$scope.items[index]['incident_list_'+value['function']]=value.incident_list;
				
			
				
				}
			//item.customer=value.customer_name
			//$scope.items.push(item);
			
		})
	}
	$scope.global_function=function(){
		
		
		graphOps_service.get_method().success(function(data){
		
		main_function(data);
		
	})
	}
	$scope.country_function=function(country_code){
		
		
		graphOps_region_service.get_method(country_code).success(function(data){
		
		
		main_function(data);
	})
	}
	
	$scope.test=function(incident_list)
	{
		$scope.incident_list=[];
		angular.forEach(incident_list, function(value, key) {
			
			if(value.cet_value==1)
				{
				value.cet_value="red";
				}
			else
				{
				value.cet_value="green";
				}
			if(value.cep_value==0)
			{
			value.cep_value="green";
			}
			else if(value.cep_value==1)
			{
			value.cep_value="orange";
			}
		else
			{
			value.cep_value="red";
			}
			$scope.incident_list.push(value);
		});
		$scope.incident_list=$filter('orderBy')($scope.incident_list, '-total_incident_amount');
		if ($scope.incident_list.length!=0)
			{
			$('#modal_open').click();
			}
		
		
		
	}
	
	
	
	var summary_function=function (){
		
		graphOps_summary_service.get_method().success(function(data){
		
		
		
		$scope.graphOps_summary=data;
		var cc_latlong = (country_code);
		
		var myCenter=new google.maps.LatLng(cc_latlong['pl'][0],cc_latlong['pl'][1]);
		
		var max_zoom_size=5;
		var min_zoom_size=2;
		function initialize() {
			
		  var mapProp = {
		    center:myCenter,
		    zoom:min_zoom_size,
		    maxZoom:max_zoom_size,
		    mapTypeId:google.maps.MapTypeId.ROADMAP
		  };
		  var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
		
		 
		 angular.forEach($scope.graphOps_summary, function(value, key) {
			 
			 
			 var markers = [];
	      for (var i = 0; i < value.incident_count; i++) {
	    	 
	       var geo=value.region_code.toLowerCase();
	        
	       if(cc_latlong[geo])
	    	   {
	    	   
	    	  
	        var latLng = new google.maps.LatLng(cc_latlong[geo][0],cc_latlong[geo][1]);
	        var marker = new google.maps.Marker({
	          position: latLng,
	          country_code:geo,
	        });
	        google.maps.event.addListener(marker, 'click', function() {
				
				
				var cc=this.country_code;
				$scope.country_function(cc);
				 map.setCenter(new google.maps.LatLng(cc_latlong[cc][0],cc_latlong[cc][1]));
				 map.setZoom(5)
				// map.setCenter(new google.maps.LatLng(cc_latlong[cc][0],cc_latlong[cc][1]));
				});
	        markers.push(marker);
	    	   } 
	      }
		
	      
	      var markerCluster = new MarkerClusterer(map, markers,{
	    	   
	    	     zoomOnClick: true
	    	   });
	      google.maps.event.addListener(markerCluster, 'clusterclick', function(cluster) {
			   //placeMarker(event.latLng);
	    	  
	    	 
	    	  var cc=(cluster.markerClusterer_.markers_[0].country_code)
	    	 
		  
	    	  	
				$scope.country_function(cc);
			//map.setZoom(2);
			
		
	    	  
			 map.setCenter(new google.maps.LatLng(cc_latlong[cc][0],cc_latlong[cc][1]));
			});
	      
	      google.maps.event.addListener(map, 'zoom_changed', function() {
			
			if(map.getZoom()<=2)
			{
				$scope.country_function('global');
			
			}
		
			});
	      
	      if($scope.countryCode)
			{
			
			$scope.country_function($scope.countryCode.toLowerCase()); 
			map.setCenter(new google.maps.LatLng(cc_latlong[$scope.countryCode.toLowerCase()][0],cc_latlong[$scope.countryCode.toLowerCase()][1]));
			map.setZoom(5)
			}
	      else
			{
			
			$scope.country_function('global'); 
			}
	      
	     
		});
		}
		
		initialize();
		
	});
	}

	summary_function();
	angular.element(document).ready(function () { 

		  $('.modal_open').attr('data-target','#add_quest_modal');
		  $('.modal_open').attr('data-toggle','modal');
	})
	
}])